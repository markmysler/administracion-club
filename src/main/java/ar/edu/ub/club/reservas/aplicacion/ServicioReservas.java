package ar.edu.ub.club.reservas.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.reservas.dominio.OrigenSolicitud;
import ar.edu.ub.club.reservas.dominio.PublicadorEventosReserva;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.reservas.dominio.ReservaCanceladaEvento;
import ar.edu.ub.club.reservas.dominio.ReservaConfirmadaEvento;
import ar.edu.ub.club.reservas.dominio.ReservaModificadaEvento;
import ar.edu.ub.club.reservas.dominio.SolicitudReserva;
import ar.edu.ub.club.reservas.dominio.TipoOperacion;
import ar.edu.ub.club.reservas.persistencia.ReservasCumplidas;
import ar.edu.ub.club.reservas.persistencia.ReservasPorFiltro;
import ar.edu.ub.club.socios.aplicacion.SocioActual;
import ar.edu.ub.club.socios.dominio.Socio;
import ar.edu.ub.club.socios.persistencia.SocioParaReserva;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Fachada del módulo reservas y Creator de {@code Reserva}. Si al guardar una reserva la base rechaza la
 * superposición (restricción de exclusión), {@code BrokerBaseDatos.insertar}/{@code actualizar} traducen la
 * violación a {@code ErrorNegocio("El turno ya fue reservado; elija otro turno.")}, que el presentador muestra
 * como cualquier regla incumplida. Toda alta o modificación arma una {@code SolicitudReserva} y la pasa por la
 * cadena de validadores construida por {@code FabricaCadenaValidacion} (Chain of Responsibility: cada {@code
 * ValidadorReservaEncadenado.validar} es un método plantilla que llama a su {@code verificar} y luego al
 * siguiente), que obtiene los datos con {@code FachadaPersistencia} y crea los validadores de dominio, entre
 * ellos {@code ValidadorDuracionTurno(regla, disponibilidad)}, que verifica la duración del turno y su
 * alineación con la grilla medida desde la hora de apertura del club; las transiciones de estado las resuelve el
 * patrón State; al confirmar, modificar o cancelar publica el evento correspondiente en {@code
 * PublicadorEventosReserva} (Observer). La base revalida con la restricción de exclusión.
 *
 * <p>Clase anotada con {@code @Service} y {@code @Transactional}: cada método público es una transacción (las
 * consultas con {@code readOnly = true}); la primera instrucción de cada método con permiso es {@code
 * controlAcceso.verificar(usuario, permiso)}.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33; tareas programadas: disparo de tareas programadas, finalización de reservas cumplidas
 */
@Service
@Transactional
public class ServicioReservas {

    /** Bitácora técnica (finalización de reservas que no pudieron cerrarse). */
    private static final Logger BITACORA = LoggerFactory.getLogger(ServicioReservas.class);

    /** Motivo que se registra cuando el socio cancela su propia reserva (CU-30 paso 7). */
    private static final String MOTIVO_CANCELACION_SOCIO = "Cancelada por el socio";

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code ReservasPorFiltro}, {@code SocioParaReserva} (FOR UPDATE), {@code ReservasCumplidas}; reglas por {@code get(ReglaReserva.ID_UNICO, ReglaReserva)}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Arma la cadena de validadores. */
    private final FabricaCadenaValidacion fabricaCadena;

    /** Sujeto del patrón Observer. */
    private final PublicadorEventosReserva publicador;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión en los métodos que no la reciben por parámetro. */
    private final UsuarioActual usuarioActual;

    /** Socio de la sesión, para verificar la pertenencia en {@code consultarReserva} (RN-20). */
    private final SocioActual socioActual;

    /** Fecha y hora actual. */
    private final Reloj reloj;

    /** Abre una transacción nueva por reserva ({@code PROPAGATION_REQUIRES_NEW}) en {@code finalizarReservasCumplidas}. */
    private final TransactionTemplate plantillaTransaccion;

    /**
     * Crea la fachada (bean de Spring; dependencias por constructor). La plantilla de transacción recibida (la de
     * Spring Boot) se copia con {@code PROPAGATION_REQUIRES_NEW} para no modificar el bean compartido.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33; tareas programadas: finalización de
     * reservas cumplidas
     * @param fachadaPersistencia fachada de persistencia
     * @param fabricaCadena fábrica de la cadena de validadores
     * @param publicador sujeto del patrón Observer
     * @param controlAcceso verificación de permisos
     * @param usuarioActual cuenta de la sesión
     * @param socioActual socio de la sesión
     * @param reloj fecha y hora actual
     * @param plantillaTransaccion plantilla de transacción de Spring
     */
    public ServicioReservas(FachadaPersistencia fachadaPersistencia, FabricaCadenaValidacion fabricaCadena,
                            PublicadorEventosReserva publicador, ControlAcceso controlAcceso,
                            UsuarioActual usuarioActual, SocioActual socioActual, Reloj reloj,
                            TransactionTemplate plantillaTransaccion) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.fabricaCadena = fabricaCadena;
        this.publicador = publicador;
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
        this.socioActual = socioActual;
        this.reloj = reloj;
        TransactionTemplate nueva = new TransactionTemplate(plantillaTransaccion.getTransactionManager());
        nueva.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        this.plantillaTransaccion = nueva;
    }

    /**
     * Bloquea la fila del socio ({@code fachadaPersistencia.buscarUno(SocioParaReserva(id))}, SELECT ... FOR UPDATE)
     * para que dos altas simultáneas del mismo socio no superen el límite de reservas vigentes (RN-13); arma la
     * solicitud con {@code tipoOperacion = ALTA} y {@code origen = OrigenSolicitud.SOCIO}, ejecuta la cadena de
     * validadores, crea la {@code Reserva} con {@code registradaPor = socio}, la confirma ({@code confirmar()}), la
     * guarda y publica {@code ReservaConfirmadaEvento} (sec_cu27_p2). El algoritmo común está en {@code
     * registrarAlta}.
     *
     * <p>Permiso: {@code RESERVAR_ONLINE}.
     *
     * <p>Casos de uso: CU-27
     * @param socio Socio de la sesión.
     * @param idCancha Cancha.
     * @param turno Fecha, hora de inicio y fin.
     * @return {@code Reserva} confirmada.
     * @throws ErrorNegocio si un validador rechaza la solicitud o el turno se ocupó entretanto
     */
    public Reserva reservarOnline(Socio socio, Long idCancha, Turno turno) {
        controlAcceso.verificar(socio, Permiso.RESERVAR_ONLINE);
        return registrarAlta(socio, idCancha, turno, socio, OrigenSolicitud.SOCIO);
    }

    /**
     * Como {@code reservarOnline}, para el socio indicado, con {@code origen = ADMINISTRADOR} y {@code registradaPor
     * = admin}. La cuota vencida del grupo también impide esta alta (sec_cu28_p3).
     *
     * <p>Permiso: {@code GESTIONAR_RESERVAS_CLUB}.
     *
     * <p>Casos de uso: CU-28
     * @param admin Cuenta del administrador.
     * @param idSocio Socio titular de la reserva.
     * @param idCancha Cancha.
     * @param turno Turno.
     * @return {@code Reserva} confirmada.
     * @throws RecursoNoEncontradoException si el socio no existe
     */
    public Reserva registrarReservaManual(Administrador admin, Long idSocio, Long idCancha, Turno turno) {
        controlAcceso.verificar(admin, Permiso.GESTIONAR_RESERVAS_CLUB);
        Socio socio = fachadaPersistencia.get(idSocio, Socio.class);
        return registrarAlta(socio, idCancha, turno, admin, OrigenSolicitud.ADMINISTRADOR);
    }

    /**
     * Usa {@code FiltroReservas.paraSocio(socio.getId())} con las fechas y el estado del filtro recibido (el alcance
     * lo determina la sesión, RN-20) y busca con {@code fachadaPersistencia.buscar(ReservasPorFiltro(filtro))}
     * (sec_cu29).
     *
     * <p>Permiso: {@code GESTIONAR_RESERVAS_PROPIAS}.
     *
     * <p>Casos de uso: CU-29
     * @param socio Socio de la sesión.
     * @param filtro Rango y estado.
     * @return Lista de {@code Reserva}.
     * @throws ErrorNegocio si el rango de fechas es inválido
     */
    @Transactional(readOnly = true)
    public List<Reserva> consultarReservasPropias(Socio socio, FiltroReservas filtro) {
        controlAcceso.verificar(socio, Permiso.GESTIONAR_RESERVAS_PROPIAS);
        FiltroReservas delSocio = FiltroReservas.paraSocio(socio.getId());
        if (filtro != null) {
            filtro.validar();
            delSocio.setDesde(filtro.getDesde());
            delSocio.setHasta(filtro.getHasta());
            delSocio.setEstado(filtro.getEstado());
        }
        return fachadaPersistencia.buscar(new ReservasPorFiltro(delSocio));
    }

    /**
     * Verifica {@code perteneceA(socio)} (RN-20) y {@code esVigente(ahora)}; {@code regla.admiteCancelacion(turno,
     * ahora)} (RN-14); {@code cancelar(motivo, socio, ahora)} delega en el estado; publica {@code
     * ReservaCanceladaEvento} (sec_cu30_p2). Una reserva inexistente o ajena produce {@code
     * AccesoDenegadoException}, para no revelar si existe.
     *
     * <p>Permiso: {@code GESTIONAR_RESERVAS_PROPIAS}.
     *
     * <p>Casos de uso: CU-30
     * @param socio Socio de la sesión.
     * @param idReserva Reserva.
     * @throws AccesoDenegadoException si la reserva no existe o es de otro socio
     * @throws ErrorNegocio si no está vigente o venció el plazo de cancelación
     */
    public void cancelarReservaPropia(Socio socio, Long idReserva) {
        controlAcceso.verificar(socio, Permiso.GESTIONAR_RESERVAS_PROPIAS);
        LocalDateTime ahora = reloj.ahora();
        Reserva r;
        try {
            r = buscarReserva(idReserva);
        } catch (RecursoNoEncontradoException e) {
            throw new AccesoDenegadoException();
        }
        if (!r.perteneceA(socio)) {
            throw new AccesoDenegadoException();
        }
        if (!r.esVigente(ahora)) {
            throw new ErrorNegocio("La reserva ya no está vigente y no puede cancelarse.");
        }
        ReglaReserva regla = fachadaPersistencia.get(ReglaReserva.ID_UNICO, ReglaReserva.class);
        if (!regla.admiteCancelacion(r.getTurno(), ahora)) {
            throw new ErrorNegocio("Ya no puede cancelarse: faltan menos de "
                    + regla.getAnticipacionMinimaCancelacionHoras()
                    + " horas. Solicite la cancelación a la administración.");
        }
        r.cancelar(MOTIVO_CANCELACION_SOCIO, socio, ahora);
        fachadaPersistencia.put(r);
        publicador.publicar(new ReservaCanceladaEvento(r, socio, ahora));
    }

    /**
     * Devuelve las reservas del club que cumplen el filtro (sec_cu31_p1). Como no recibe la cuenta, la obtiene con
     * {@code usuarioActual}.
     *
     * <p>Permiso: {@code GESTIONAR_RESERVAS_CLUB}.
     *
     * <p>Casos de uso: CU-31
     * @param filtro Rango, deporte, cancha, socio y estado.
     * @return Lista de {@code Reserva}.
     * @throws ErrorNegocio si el rango de fechas es inválido
     */
    @Transactional(readOnly = true)
    public List<Reserva> consultarReservasClub(FiltroReservas filtro) {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.GESTIONAR_RESERVAS_CLUB);
        FiltroReservas criterio = filtro == null ? new FiltroReservas() : filtro;
        criterio.validar();
        return fachadaPersistencia.buscar(new ReservasPorFiltro(criterio));
    }

    /**
     * Verifica que la {@code version} recibida coincida con la de la reserva (si no, {@code ConcurrenciaException};
     * además {@code BrokerReserva} actualiza con {@code WHERE version = ?}) y que siga vigente; verifica la
     * anticipación mínima de modificación sobre la reserva original con el método privado {@code
     * verificarPlazo(reserva: Reserva, horasMinimas: Integer, ahora: DateTime, mensaje: String): void} ({@code
     * horasMinimas = regla.anticipacionMinimaModificacionHoras}; si faltan menos horas, {@code
     * ErrorNegocio(mensaje)}; RN-15, CU-32 paso 3). Bloquea la fila del socio ({@code SocioParaReserva}). Arma la
     * solicitud con {@code tipoOperacion = MODIFICACION} y {@code origen = OrigenSolicitud.ADMINISTRADOR}: {@code
     * ValidadorSocioHabilitado} omite el control de cuota vencida (exige socio activo), el validador de anticipación
     * usa la de modificación y el de límite excluye la reserva original. Luego {@code modificar(nuevoTurno,
     * nuevaCancha, ahora)} y publica {@code ReservaModificadaEvento} (sec_cu32_p3).
     *
     * <p>Permiso: {@code GESTIONAR_RESERVAS_CLUB}.
     *
     * <p>Casos de uso: CU-32
     * @param admin Cuenta del administrador.
     * @param idReserva Reserva.
     * @param nuevoTurno Turno nuevo.
     * @param idNuevaCancha Cancha nueva.
     * @param version Versión leída por P-23 (control optimista).
     * @return {@code Reserva} modificada.
     * @throws ConcurrenciaException si la reserva cambió desde que se leyó
     * @throws ErrorNegocio si no está vigente, venció el plazo o un validador rechaza
     */
    public Reserva modificarReserva(Administrador admin, Long idReserva, Turno nuevoTurno, Long idNuevaCancha, Integer version) {
        controlAcceso.verificar(admin, Permiso.GESTIONAR_RESERVAS_CLUB);
        LocalDateTime ahora = reloj.ahora();
        Reserva r = buscarReserva(idReserva);
        if (!Objects.equals(r.getVersion(), version)) {
            throw new ConcurrenciaException("Reserva", idReserva);
        }
        if (!r.esVigente(ahora)) {
            throw new ErrorNegocio("La reserva no puede modificarse: ya no está vigente.");
        }
        ReglaReserva regla = fachadaPersistencia.get(ReglaReserva.ID_UNICO, ReglaReserva.class);
        verificarPlazo(r, regla.getAnticipacionMinimaModificacionHoras(), ahora,
                "La reserva ya no puede modificarse: faltan menos de "
                        + regla.getAnticipacionMinimaModificacionHoras()
                        + " horas para el turno. Puede cancelarla indicando el motivo.");
        fachadaPersistencia.buscarUno(new SocioParaReserva(r.getSocio().getId()));
        Cancha nuevaCancha = buscarCancha(idNuevaCancha);
        SolicitudReserva s = new SolicitudReserva(r.getSocio(), nuevaCancha, nuevoTurno, r,
                TipoOperacion.MODIFICACION, OrigenSolicitud.ADMINISTRADOR, ahora);
        fabricaCadena.crearCadena(s).validar(s);
        Turno turnoAnterior = r.getTurno();
        Cancha canchaAnterior = r.getCancha();
        r.modificar(nuevoTurno, nuevaCancha, ahora);
        fachadaPersistencia.put(r);
        publicador.publicar(new ReservaModificadaEvento(r, admin, ahora, turnoAnterior, canchaAnterior));
        return r;
    }

    /**
     * Exige motivo; no está limitada por la anticipación mínima de cancelación (RN-14). Verifica que la reserva siga
     * vigente, {@code cancelar(motivo, admin, ahora)} y publica {@code ReservaCanceladaEvento} (sec_cu33_p2).
     *
     * <p>Permiso: {@code GESTIONAR_RESERVAS_CLUB}.
     *
     * <p>Casos de uso: CU-33
     * @param admin Cuenta del administrador.
     * @param idReserva Reserva.
     * @param motivo Motivo obligatorio.
     * @throws ErrorNegocio si falta el motivo o la reserva no está vigente
     */
    public void cancelarReservaAdministrativa(Administrador admin, Long idReserva, String motivo) {
        controlAcceso.verificar(admin, Permiso.GESTIONAR_RESERVAS_CLUB);
        if (motivo == null || motivo.isBlank()) {
            throw new ErrorNegocio("motivo", "Indique el motivo de la cancelación.");
        }
        LocalDateTime ahora = reloj.ahora();
        Reserva r = buscarReserva(idReserva);
        if (!r.esVigente(ahora)) {
            throw new ErrorNegocio("La reserva ya no está vigente y no puede cancelarse.");
        }
        r.cancelar(motivo.trim(), admin, ahora);
        fachadaPersistencia.put(r);
        publicador.publicar(new ReservaCanceladaEvento(r, admin, ahora));
    }

    /**
     * Obtiene las reservas confirmadas cuyo turno terminó ({@code
     * fachadaPersistencia.buscar(ReservasCumplidas(ahora))}) y pasa cada una a FINALIZADA en su propia transacción
     * ({@code plantillaTransaccion}, configurada con {@code PROPAGATION_REQUIRES_NEW} para no unirse a la
     * transacción de lectura del lote), de modo que el fallo de una no revierte las demás
     * (sec_tmp_finalizar_reservas). No hay usuario de sesión ni permiso (evento temporal).
     *
     * <p>Casos de uso: tareas programadas: disparo de tareas programadas, finalización de reservas cumplidas
     * @return Cantidad de reservas finalizadas.
     */
    @Transactional(readOnly = true)
    public int finalizarReservasCumplidas() {
        LocalDateTime ahora = reloj.ahora();
        List<Reserva> reservas = fachadaPersistencia.buscar(new ReservasCumplidas(ahora));
        int finalizadas = 0;
        for (Reserva r : reservas) {
            Long id = r.getId();
            try {
                Boolean ok = plantillaTransaccion.execute(estado -> finalizarEnTransaccion(id));
                if (Boolean.TRUE.equals(ok)) {
                    finalizadas++;
                }
            } catch (RuntimeException e) {
                BITACORA.error("No se pudo finalizar la reserva {}", id, e);
            }
        }
        return finalizadas;
    }

    /**
     * Algoritmo común de {@code reservarOnline} y {@code registrarReservaManual}: bloquea la fila del socio con
     * {@code buscarUno(new SocioParaReserva(socio.id))}, obtiene la cancha, arma la {@code SolicitudReserva} de tipo
     * ALTA, la valida con la cadena, crea y confirma la {@code Reserva} (Creator), la guarda con {@code put}
     * (inserción inmediata; la restricción de exclusión revalida) y publica {@code ReservaConfirmadaEvento}.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param socio socio a cuyo nombre se registra
     * @param idCancha cancha pedida
     * @param turno fecha y horario pedidos
     * @param registradaPor cuenta que registra: el socio (CU-27) o el administrador (CU-28)
     * @param origen origen de la solicitud
     * @return reserva CONFIRMADA y persistida
     */
    private Reserva registrarAlta(Socio socio, Long idCancha, Turno turno, Usuario registradaPor, OrigenSolicitud origen) {
        LocalDateTime ahora = reloj.ahora();
        Socio titular = fachadaPersistencia.buscarUno(new SocioParaReserva(socio.getId()))
                .orElseThrow(() -> new RecursoNoEncontradoException(Socio.class, socio.getId()));
        Cancha cancha = buscarCancha(idCancha);
        SolicitudReserva s = new SolicitudReserva(titular, cancha, turno, null, TipoOperacion.ALTA, origen, ahora);
        fabricaCadena.crearCadena(s).validar(s);
        Reserva r = new Reserva(titular, cancha, turno, registradaPor, ahora);
        r.confirmar();
        fachadaPersistencia.put(r);
        publicador.publicar(new ReservaConfirmadaEvento(r, registradaPor, ahora));
        return r;
    }

    /**
     * Obtiene la reserva con {@code fachadaPersistencia.get(idReserva, Reserva)}; si no existe la fachada lanza
     * {@code RecursoNoEncontradoException}, que el método propaga.
     *
     * <p>Casos de uso: CU-29, CU-30, CU-31, CU-32, CU-33
     * @param idReserva identificador
     * @return la reserva
     * @throws RecursoNoEncontradoException si no existe
     */
    private Reserva buscarReserva(Long idReserva) {
        return fachadaPersistencia.get(idReserva, Reserva.class);
    }

    /**
     * Obtiene la cancha con {@code fachadaPersistencia.get(idCancha, Cancha)}; que esté activa lo decide {@code
     * ValidadorRecursoActivo}.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param idCancha identificador
     * @return la cancha
     * @throws RecursoNoEncontradoException si no existe
     */
    private Cancha buscarCancha(Long idCancha) {
        return fachadaPersistencia.get(idCancha, Cancha.class);
    }

    /**
     * Calcula los minutos que faltan entre {@code ahora} y {@code reserva.turno.inicio()}; si son menos que {@code
     * horasMinimas} horas lanza {@code ErrorNegocio(mensaje)} (RN-15, CU-32 paso 3).
     *
     * <p>Casos de uso: CU-32
     * @param reserva reserva a modificar
     * @param horasMinimas anticipación mínima exigida
     * @param ahora momento de la operación
     * @param mensaje mensaje para el usuario
     * @throws ErrorNegocio si no se cumple la anticipación
     */
    private void verificarPlazo(Reserva reserva, Integer horasMinimas, LocalDateTime ahora, String mensaje) {
        long minutos = Duration.between(ahora, reserva.getTurno().inicio()).toMinutes();
        if (minutos < horasMinimas * 60L) {
            throw new ErrorNegocio(mensaje);
        }
    }

    /**
     * Se ejecuta dentro de {@code plantillaTransaccion.execute(...)}, en una transacción nueva: obtiene la reserva,
     * invoca {@code finalizar()} (que delega en el estado) y la guarda. Si la versión cambió ({@code
     * ConcurrenciaException}) o la transición no es válida (la reserva se canceló entretanto), lo registra en la
     * bitácora y devuelve false sin escribir.
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     * @param idReserva reserva a finalizar
     * @return true si quedó FINALIZADA
     */
    private Boolean finalizarEnTransaccion(Long idReserva) {
        try {
            Reserva r = fachadaPersistencia.get(idReserva, Reserva.class);
            r.finalizar();
            fachadaPersistencia.put(r);
            return Boolean.TRUE;
        } catch (ConcurrenciaException | TransicionInvalidaException | RecursoNoEncontradoException e) {
            BITACORA.warn("La reserva {} no se finalizó: {}", idReserva, e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * Devuelve una reserva. Obtiene la cuenta de la sesión con {@code usuarioActual}; si tiene
     * GESTIONAR_RESERVAS_CLUB la devuelve; si no, verifica GESTIONAR_RESERVAS_PROPIAS y exige {@code
     * perteneceA(socio)} con el socio de {@code socioActual} (RN-20). La decisión depende de los permisos, no de la
     * clase de la cuenta.
     *
     * <p>Permiso: {@code GESTIONAR_RESERVAS_CLUB} o {@code GESTIONAR_RESERVAS_PROPIAS}.
     *
     * <p>Casos de uso: CU-29, CU-31, CU-32
     * @param idReserva Reserva.
     * @return {@code Reserva}.
     * @throws AccesoDenegadoException si la reserva es de otro socio
     */
    @Transactional(readOnly = true)
    public Reserva consultarReserva(Long idReserva) {
        Usuario usuario = usuarioActual.obtener();
        if (controlAcceso.tienePermiso(usuario, Permiso.GESTIONAR_RESERVAS_CLUB)) {
            return buscarReserva(idReserva);
        }
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_RESERVAS_PROPIAS);
        Reserva r = buscarReserva(idReserva);
        if (!r.perteneceA(socioActual.obtener())) {
            throw new AccesoDenegadoException();
        }
        return r;
    }
}

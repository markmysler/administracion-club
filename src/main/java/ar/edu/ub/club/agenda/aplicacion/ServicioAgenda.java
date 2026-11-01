package ar.edu.ub.club.agenda.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.CalendarioTurnos;
import ar.edu.ub.club.agenda.dominio.ConsultaTurnosOcupados;
import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import ar.edu.ub.club.agenda.dominio.Feriado;
import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.agenda.dominio.TurnoDisponible;
import ar.edu.ub.club.agenda.persistencia.FeriadosEntre;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.ConsultaReservasFuturas;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.excepciones.ConfirmacionRequeridaException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.recursos.persistencia.CanchasDeDeporte;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada del módulo agenda: disponibilidad del club, feriados, reglas de reserva y cálculo de turnos
 * disponibles con el servicio de dominio {@code CalendarioTurnos}. Obtiene los turnos ocupados a través de la
 * interfaz {@code ConsultaTurnosOcupados} (agenda.dominio), que implementa {@code
 * ConsultaTurnosOcupadosReservas} (reservas.persistencia); así agenda no depende de reservas.
 *
 * <p>Patrones: GoF Facade y GRASP Controller; las validaciones las delega en los Information Expert
 * {@code DisponibilidadClub} y {@code ReglaReserva}; las reservas afectadas por un cambio se obtienen con la
 * consulta invertida {@code ConsultaReservasFuturas} (protocolo de confirmación con
 * {@code ConfirmacionRequeridaException}; las reservas se conservan, RN-16 no aplica a la agenda). Cada método
 * público es una transacción ({@code @Transactional}); las consultas son de solo lectura.
 *
 * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
 */
@Service
@Transactional
public class ServicioAgenda {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code FeriadosEntre}, {@code CanchasDeDeporte}; disponibilidad y reglas por {@code get(ID_UNICO, …)}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Turnos ocupados por cancha y fecha. */
    private final ConsultaTurnosOcupados consultaTurnos;

    /** Reservas futuras afectadas por un cambio de disponibilidad o un feriado. */
    private final ConsultaReservasFuturas consultaReservasFuturas;

    /** Servicio de dominio que genera los turnos. */
    private final CalendarioTurnos calendario;

    /** Auditoría de cambios de disponibilidad, feriados y reglas. */
    private final ServicioAuditoria servicioAuditoria;

    /** Fecha y hora actual. */
    private final Reloj reloj;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea la fachada con sus colaboradores (inyección por constructor de Spring).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @param fachadaPersistencia acceso a la persistencia
     * @param consultaTurnos consulta invertida de turnos ocupados (la implementa reservas)
     * @param consultaReservasFuturas consulta invertida de reservas futuras (la implementa reservas)
     * @param calendario servicio de dominio que genera la grilla
     * @param servicioAuditoria auditoría (RNF-12)
     * @param reloj fecha y hora actual
     * @param controlAcceso verificación de permisos (RNF-04)
     * @param usuarioActual cuenta de la sesión
     */
    public ServicioAgenda(FachadaPersistencia fachadaPersistencia, ConsultaTurnosOcupados consultaTurnos,
                          ConsultaReservasFuturas consultaReservasFuturas, CalendarioTurnos calendario,
                          ServicioAuditoria servicioAuditoria, Reloj reloj, ControlAcceso controlAcceso,
                          UsuarioActual usuarioActual) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.consultaTurnos = consultaTurnos;
        this.consultaReservasFuturas = consultaReservasFuturas;
        this.calendario = calendario;
        this.servicioAuditoria = servicioAuditoria;
        this.reloj = reloj;
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Devuelve la disponibilidad con sus feriados ({@code get(DisponibilidadClub.ID_UNICO, DisponibilidadClub)}).
     *
     * <p>Permiso: {@code CONFIGURAR_AGENDA}.
     *
     * <p>Casos de uso: CU-24
     * @return {@code DisponibilidadClub}.
     */
    @Transactional(readOnly = true)
    public DisponibilidadClub consultarDisponibilidadClub() {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.CONFIGURAR_AGENDA);
        return disponibilidad();
    }

    /**
     * {@code disponibilidad.modificar(dias, apertura, cierre, duracionTurnoMinutos)} valida al menos un día, cierre
     * posterior a apertura y lugar para un turno. Obtiene las reservas futuras con {@code buscarFuturasDesde(ahora)}
     * y filtra las que la nueva disponibilidad dejaría fuera ({@code atiende}, {@code dentroDelHorario}); si hay y
     * no se confirmó lanza {@code ConfirmacionRequeridaException}. Las reservas se conservan. Audita.
     *
     * <p>Permiso: {@code CONFIGURAR_AGENDA}.
     *
     * <p>Casos de uso: CU-24
     * @param datos Días, horario y {@code confirmar}.
     * @return {@code DisponibilidadClub}.
     * @throws ErrorNegocio si los valores son inválidos (FA3, FA4)
     * @throws ConfirmacionRequeridaException si hay reservas fuera de la nueva disponibilidad y no se confirmó (FA5)
     */
    public DisponibilidadClub configurarDisponibilidad(DatosDisponibilidad datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.CONFIGURAR_AGENDA);
        ReglaReserva regla = regla();
        DisponibilidadClub disponibilidad = disponibilidad();
        disponibilidad.modificar(datos.getDiasAtencion(), datos.getHoraApertura(), datos.getHoraCierre(),
                regla.getDuracionTurnoMinutos());
        List<ResumenReserva> fuera = new ArrayList<>();
        for (ResumenReserva reserva : consultaReservasFuturas.buscarFuturasDesde(reloj.ahora())) {
            Turno turno = new Turno(reserva.fecha(), reserva.horaInicio(), reserva.horaFin());
            if (!disponibilidad.atiende(turno.getFecha()) || !disponibilidad.dentroDelHorario(turno)) {
                fuera.add(reserva);
            }
        }
        if (!fuera.isEmpty() && !datos.getConfirmar()) {
            throw new ConfirmacionRequeridaException(fuera);
        }
        fachadaPersistencia.put(disponibilidad);
        String dias = disponibilidad.getDiasAtencion().stream().map(d -> String.valueOf(d.numeroIso()))
                .collect(Collectors.joining(","));
        servicioAuditoria.registrar(usuario, "MODIFICACION", "DisponibilidadClub", disponibilidad.getId(),
                "{\"dias\":[" + dias + "],\"horaApertura\":\"" + disponibilidad.getHoraApertura()
                        + "\",\"horaCierre\":\"" + disponibilidad.getHoraCierre()
                        + "\",\"reservasFuera\":" + fuera.size() + "}");
        return disponibilidad;
    }

    /**
     * Agrega un feriado de fecha única. Si {@code buscarDeFecha(fecha)} devuelve reservas y no se confirmó, lanza
     * {@code ConfirmacionRequeridaException}; las reservas se conservan. Audita (sec_cu24_p4).
     *
     * <p>Permiso: {@code CONFIGURAR_AGENDA}.
     *
     * <p>Casos de uso: CU-24
     * @param datos Fecha, descripción y {@code confirmar}.
     * @return {@code Feriado}.
     * @throws ErrorNegocio con campo {@code fecha} si no es futura o ya es feriado (3.2), o {@code descripcion}
     * @throws ConfirmacionRequeridaException si hay reservas confirmadas en esa fecha y no se confirmó (3.3)
     */
    public Feriado registrarFeriado(DatosFeriado datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.CONFIGURAR_AGENDA);
        LocalDate fecha = datos.getFecha();
        if (fecha == null) {
            throw new ErrorNegocio("fecha", "Ingrese la fecha del feriado.");
        }
        if (!fecha.isAfter(reloj.hoy())) {
            throw new ErrorNegocio("fecha", "La fecha debe ser futura.");
        }
        if (fachadaPersistencia.contar(new FeriadosEntre(fecha, fecha)) > 0) {
            throw new ErrorNegocio("fecha", "La fecha ya es feriado.");
        }
        List<ResumenReserva> reservas = consultaReservasFuturas.buscarDeFecha(fecha);
        if (!reservas.isEmpty() && !datos.getConfirmar()) {
            throw new ConfirmacionRequeridaException(reservas);
        }
        Feriado feriado = new Feriado(fecha, datos.getDescripcion());
        fachadaPersistencia.put(feriado);
        servicioAuditoria.registrar(usuario, "ALTA", "Feriado", feriado.getId(),
                "{\"fecha\":\"" + fecha + "\",\"reservasEnLaFecha\":" + reservas.size() + "}");
        return feriado;
    }

    /**
     * Elimina un feriado (CU-24 FA2, sec_cu24_p5): deja de bloquear turnos. Audita.
     *
     * <p>Permiso: {@code CONFIGURAR_AGENDA}.
     *
     * <p>Casos de uso: CU-24
     * @param id Feriado.
     */
    public void eliminarFeriado(Long id) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.CONFIGURAR_AGENDA);
        Feriado feriado = fachadaPersistencia.get(id, Feriado.class);
        fachadaPersistencia.eliminar(feriado);
        servicioAuditoria.registrar(usuario, "BAJA", "Feriado", id, "{\"fecha\":\"" + feriado.getFecha() + "\"}");
    }

    /**
     * Devuelve las reglas vigentes ({@code get(ReglaReserva.ID_UNICO, ReglaReserva)}). Es una lectura sin permiso
     * específico (solo exige una sesión autenticada): la usan P-18 (administrador, CU-25) y también las pantallas del
     * socio P-19 (acotar el calendario, CU-26) y P-21 (plazos de cancelación, CU-29), que no tienen
     * {@code CONFIGURAR_AGENDA}. Modificar las reglas ({@code configurarReglas}) sigue exigiendo ese permiso.
     *
     * <p>Permiso: ninguno específico (cuenta autenticada).
     *
     * <p>Casos de uso: CU-25, CU-26, CU-29
     * @return {@code ReglaReserva}.
     */
    @Transactional(readOnly = true)
    public ReglaReserva consultarReglas() {
        usuarioActual.obtener();
        return regla();
    }

    /**
     * {@code regla.modificar(valores, disponibilidad)} valida los seis parámetros (enteros positivos y duración
     * compatible con el horario). Audita (sec_cu25_p2).
     *
     * <p>Permiso: {@code CONFIGURAR_AGENDA}.
     *
     * <p>Casos de uso: CU-25
     * @param datos Parámetros.
     * @return {@code ReglaReserva}.
     * @throws ErrorNegocio con el campo a corregir (FA2, FA3)
     */
    public ReglaReserva configurarReglas(DatosReglaReserva datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.CONFIGURAR_AGENDA);
        ReglaReserva regla = regla();
        DisponibilidadClub disponibilidad = disponibilidad();
        regla.modificar(datos, disponibilidad);
        fachadaPersistencia.put(regla);
        servicioAuditoria.registrar(usuario, "MODIFICACION", "ReglaReserva", regla.getId(),
                "{\"anticipacionMinimaHoras\":" + regla.getAnticipacionMinimaHoras()
                        + ",\"anticipacionMaximaDias\":" + regla.getAnticipacionMaximaDias()
                        + ",\"duracionTurnoMinutos\":" + regla.getDuracionTurnoMinutos()
                        + ",\"limiteReservasVigentesPorSocio\":" + regla.getLimiteReservasVigentesPorSocio()
                        + ",\"anticipacionMinimaCancelacionHoras\":" + regla.getAnticipacionMinimaCancelacionHoras()
                        + ",\"anticipacionMinimaModificacionHoras\":" + regla.getAnticipacionMinimaModificacionHoras()
                        + "}");
        return regla;
    }

    /**
     * Para cada cancha activa del filtro obtiene los turnos ocupados ({@code consultaTurnos.turnosOcupados}) y llama
     * a {@code CalendarioTurnos.generarTurnos(cancha, fecha, disponibilidad, regla, ocupados)}, que aplica días de
     * atención, horario, feriados y duración de turno (sec_cu26_p2): verifica la fecha con
     * {@code regla.fechaConsultable} (paso 4, FA2) y {@code disponibilidad.atiende} (paso 5, FA3), busca
     * {@code CanchasDeDeporte(idDeporte)} y conserva las que cumplen {@code estaDisponibleParaReservar()} y, si se
     * eligió, la cancha del filtro (paso 6, FA4).
     *
     * <p>Permiso: {@code CONSULTAR_DISPONIBILIDAD}.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param filtro Fecha, deporte y cancha.
     * @return Lista de {@code TurnoDisponible}.
     * @throws ErrorNegocio si falta la fecha, está fuera de rango, el club no atiende o no hay canchas activas
     */
    @Transactional(readOnly = true)
    public List<TurnoDisponible> consultarDisponibilidad(FiltroDisponibilidad filtro) {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.CONSULTAR_DISPONIBILIDAD);
        LocalDate fecha = filtro.getFecha();
        if (fecha == null) {
            throw new ErrorNegocio("fecha", "Elija la fecha a consultar.");
        }
        ReglaReserva regla = regla();
        LocalDate hoy = reloj.hoy();
        if (!regla.fechaConsultable(fecha, hoy)) {
            throw new ErrorNegocio("fecha", "Solo se pueden consultar fechas entre el " + FormatoUI.fecha(hoy)
                    + " y el " + FormatoUI.fecha(hoy.plusDays(regla.getAnticipacionMaximaDias())) + ".");
        }
        DisponibilidadClub disponibilidad = disponibilidad();
        if (!disponibilidad.atiende(fecha)) {
            throw new ErrorNegocio("fecha", "El club no atiende en esa fecha.");
        }
        List<Cancha> canchas = new ArrayList<>();
        for (Cancha cancha : fachadaPersistencia.buscar(new CanchasDeDeporte(filtro.getIdDeporte()))) {
            if (cancha.estaDisponibleParaReservar()
                    && (filtro.getIdCancha() == null || filtro.getIdCancha().equals(cancha.getId()))) {
                canchas.add(cancha);
            }
        }
        if (canchas.isEmpty()) {
            throw new ErrorNegocio("No hay canchas activas para la selección.");
        }
        List<TurnoDisponible> turnos = new ArrayList<>();
        for (Cancha cancha : canchas) {
            List<Turno> ocupados = consultaTurnos.turnosOcupados(cancha.getId(), fecha);
            turnos.addAll(calendario.generarTurnos(cancha, fecha, disponibilidad, regla, ocupados));
        }
        return turnos;
    }

    /**
     * Fila única de disponibilidad del club con sus días y feriados.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return disponibilidad
     */
    private DisponibilidadClub disponibilidad() {
        return fachadaPersistencia.get(DisponibilidadClub.ID_UNICO, DisponibilidadClub.class);
    }

    /**
     * Fila única de reglas de reserva.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return reglas vigentes
     */
    private ReglaReserva regla() {
        return fachadaPersistencia.get(ReglaReserva.ID_UNICO, ReglaReserva.class);
    }
}

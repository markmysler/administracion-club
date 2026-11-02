package ar.edu.ub.club.recursos.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.ConsultaReservasFuturas;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.dominio.ResultadoBaja;
import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.excepciones.ConfirmacionRequeridaException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.recursos.dominio.CanchaDesactivadaEvento;
import ar.edu.ub.club.recursos.dominio.Deporte;
import ar.edu.ub.club.recursos.dominio.DeporteDesactivadoEvento;
import ar.edu.ub.club.recursos.persistencia.CanchaPorNombre;
import ar.edu.ub.club.recursos.persistencia.CanchasDeDeporte;
import ar.edu.ub.club.recursos.persistencia.DeportePorNombre;
import ar.edu.ub.club.recursos.persistencia.Deportes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada del módulo recursos (deportes y canchas). Las bajas obtienen las reservas futuras afectadas con la
 * consulta invertida {@code ConsultaReservasFuturas}, las informan antes de confirmar y, al confirmar, publican
 * un evento de dominio que hace cancelar esas reservas (RN-16). Así recursos no depende de reservas.
 *
 * <p>Patrones: GoF Facade y GRASP Controller (controlador de casos de uso de recursos); publica eventos del
 * patrón Observer ({@code DeporteDesactivadoEvento}, {@code CanchaDesactivadaEvento}) que atiende
 * {@code CanceladorReservasPorBaja} en la misma transacción. Cada método público es una transacción
 * ({@code @Transactional}); las consultas son de solo lectura.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-26, CU-32, CU-38
 */
@Service
@Transactional
public class ServicioRecursos {

    /** Nombre de entidad para la auditoría de deportes. */
    private static final String ENTIDAD_DEPORTE = "Deporte";

    /** Nombre de entidad para la auditoría de canchas. */
    private static final String ENTIDAD_CANCHA = "Cancha";

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code Deportes}, {@code DeportePorNombre}, {@code CanchasDeDeporte}, {@code CanchaPorNombre}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Consulta invertida de reservas futuras por cancha o deporte. */
    private final ConsultaReservasFuturas consultaReservasFuturas;

    /** Publica {@code CanchaDesactivadaEvento} y {@code DeporteDesactivadoEvento}. */
    private final ApplicationEventPublisher publicadorEventos;

    /** Auditoría de altas, bajas y modificaciones de deportes y canchas. */
    private final ServicioAuditoria servicioAuditoria;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea la fachada con sus colaboradores (inyección por constructor de Spring).
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-26, CU-32, CU-38
     * @param fachadaPersistencia acceso a la persistencia
     * @param consultaReservasFuturas consulta invertida de reservas futuras (la implementa reservas)
     * @param publicadorEventos publicador de eventos de Spring
     * @param servicioAuditoria auditoría (RNF-12)
     * @param controlAcceso verificación de permisos (RNF-04)
     * @param usuarioActual cuenta de la sesión
     */
    public ServicioRecursos(FachadaPersistencia fachadaPersistencia, ConsultaReservasFuturas consultaReservasFuturas,
                            ApplicationEventPublisher publicadorEventos, ServicioAuditoria servicioAuditoria,
                            ControlAcceso controlAcceso, UsuarioActual usuarioActual) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.consultaReservasFuturas = consultaReservasFuturas;
        this.publicadorEventos = publicadorEventos;
        this.servicioAuditoria = servicioAuditoria;
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Devuelve los deportes (activos e inactivos) ordenados por nombre con {@code buscar(Deportes())}. Sin
     * permiso propio: la usan P-15, P-16 y P-19 (socios incluidos).
     *
     * <p>Casos de uso: CU-18, CU-21, CU-26, CU-38
     * @return Lista de {@code Deporte}.
     */
    @Transactional(readOnly = true)
    public List<Deporte> listarDeportes() {
        return fachadaPersistencia.buscar(new Deportes());
    }

    /**
     * Crea un deporte activo con nombre único (sec_cu18): verifica el permiso, {@code contar(DeportePorNombre(nombre,
     * null))} (paso 5; si hay otro, FA3), crea el deporte (paso 6), lo guarda y audita (paso 7).
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-18
     * @param datos Nombre y descripción.
     * @return {@code Deporte}.
     * @throws ErrorNegocio con campo {@code nombre} si el nombre falta, es largo o ya existe (FA2, FA3)
     */
    public Deporte registrarDeporte(DatosDeporte datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_RECURSOS);
        if (fachadaPersistencia.contar(new DeportePorNombre(datos.getNombre(), null)) > 0) {
            throw new ErrorNegocio("nombre", "Ya existe un deporte con ese nombre.");
        }
        Deporte deporte = new Deporte(datos.getNombre(), datos.getDescripcion());
        fachadaPersistencia.put(deporte);
        servicioAuditoria.registrar(usuario, "ALTA", ENTIDAD_DEPORTE, deporte.getId(), detalleDeporte(deporte));
        return deporte;
    }

    /**
     * Verifica {@code fachadaPersistencia.contar(DeportePorNombre(nombre, id)) &gt; 0}; {@code
     * deporte.modificar(nombre, descripcion)}; con {@code activo = true} invoca {@code activar()}. Audita.
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-19
     * @param id Deporte.
     * @param datos Datos modificados.
     * @return {@code Deporte}.
     * @throws ErrorNegocio con campo {@code nombre} si el nombre es inválido o lo usa otro deporte (FA3, FA4)
     */
    public Deporte modificarDeporte(Long id, DatosDeporte datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_RECURSOS);
        Deporte deporte = fachadaPersistencia.get(id, Deporte.class);
        if (fachadaPersistencia.contar(new DeportePorNombre(datos.getNombre(), id)) > 0) {
            throw new ErrorNegocio("nombre", "Ya existe otro deporte con ese nombre.");
        }
        deporte.modificar(datos.getNombre(), datos.getDescripcion());
        if (datos.getActivo() && !deporte.estaActivo()) {
            deporte.activar();
        }
        fachadaPersistencia.put(deporte);
        servicioAuditoria.registrar(usuario, "MODIFICACION", ENTIDAD_DEPORTE, id, detalleDeporte(deporte));
        return deporte;
    }

    /**
     * Obtiene las reservas afectadas con {@code buscarFuturasDeDeporte(id)}; si no se confirmó devuelve {@code
     * ResultadoBaja(false, reservasAfectadas)} sin cambios (aunque la lista esté vacía: FA2 pide confirmación sin
     * lista, sec_cu20_p3). Si se confirma, {@code desactivar()}, lo guarda y publica {@code
     * DeporteDesactivadoEvento}; {@code CanceladorReservasPorBaja} cancela las reservas futuras de sus canchas en
     * la misma transacción. Audita.
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-20
     * @param id Deporte.
     * @param confirmar Confirmación tras la advertencia.
     * @return {@code ResultadoBaja}.
     * @throws ErrorNegocio si el deporte ya está inactivo (FA1)
     */
    public ResultadoBaja desactivarDeporte(Long id, boolean confirmar) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_RECURSOS);
        Deporte deporte = fachadaPersistencia.get(id, Deporte.class);
        if (!deporte.estaActivo()) {
            throw new ErrorNegocio("El deporte ya está inactivo.");
        }
        List<ResumenReserva> afectadas = consultaReservasFuturas.buscarFuturasDeDeporte(id);
        if (!confirmar) {
            return ResultadoBaja.pendiente(afectadas);
        }
        deporte.desactivar();
        fachadaPersistencia.put(deporte);
        publicadorEventos.publishEvent(new DeporteDesactivadoEvento(id, usuario, ahora()));
        servicioAuditoria.registrar(usuario, "BAJA", ENTIDAD_DEPORTE, id,
                "{\"nombre\":" + json(deporte.getNombre()) + ",\"reservasCanceladas\":" + afectadas.size() + "}");
        return ResultadoBaja.realizada(afectadas);
    }

    /**
     * Devuelve las canchas, opcionalmente de un deporte, con {@code buscar(CanchasDeDeporte(idDeporte))}. Sin
     * permiso propio (la usan P-15, P-16 y P-19).
     *
     * <p>Casos de uso: CU-18, CU-21, CU-26, CU-32
     * @param idDeporte Deporte (opcional).
     * @return Lista de {@code Cancha}.
     */
    @Transactional(readOnly = true)
    public List<Cancha> listarCanchas(Long idDeporte) {
        return fachadaPersistencia.buscar(new CanchasDeDeporte(idDeporte));
    }

    /**
     * Crea una cancha activa de un deporte activo, con nombre único (sec_cu21_p2): verifica el permiso,
     * {@code contar(CanchaPorNombre(nombre, null))} (paso 5, FA4), obtiene el deporte y verifica que esté activo
     * (paso 6, FA5), crea la cancha (paso 7), la guarda y audita (paso 8).
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-21
     * @param datos Nombre, deporte, ubicación y características.
     * @return {@code Cancha}.
     * @throws ErrorNegocio con campo {@code nombre} o {@code idDeporte} (FA3, FA4, FA5)
     */
    public Cancha registrarCancha(DatosCancha datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_RECURSOS);
        if (fachadaPersistencia.contar(new CanchaPorNombre(datos.getNombre(), null)) > 0) {
            throw new ErrorNegocio("nombre", "Ya existe una cancha con ese nombre.");
        }
        Deporte deporte = deporteActivo(datos.getIdDeporte());
        Cancha cancha = new Cancha(datos.getNombre(), deporte, datos.getUbicacion(), datos.getCaracteristicas());
        fachadaPersistencia.put(cancha);
        servicioAuditoria.registrar(usuario, "ALTA", ENTIDAD_CANCHA, cancha.getId(), detalleCancha(cancha));
        return cancha;
    }

    /**
     * Verifica {@code fachadaPersistencia.contar(CanchaPorNombre(nombre, id)) &gt; 0}; {@code
     * cancha.modificar(nombre, deporte, ubicacion, caracteristicas)}; con {@code activa = true} invoca {@code
     * activar()}, que exige el deporte activo. Si cambia el deporte y {@code buscarFuturasDeCancha(id)} devuelve
     * reservas, sin {@code datos.confirmar} lanza {@code ConfirmacionRequeridaException}; las reservas se conservan.
     * Audita.
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-22
     * @param id Cancha.
     * @param datos Datos modificados.
     * @return {@code Cancha}.
     * @throws ErrorNegocio con campo {@code nombre} o {@code idDeporte} (FA3, FA4, FA5) o sin campo si se pide
     *     reactivar con el deporte inactivo (FA1)
     * @throws ConfirmacionRequeridaException si cambia el deporte, hay reservas futuras y no se confirmó (FA6)
     */
    public Cancha modificarCancha(Long id, DatosCancha datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_RECURSOS);
        Cancha cancha = fachadaPersistencia.get(id, Cancha.class);
        if (fachadaPersistencia.contar(new CanchaPorNombre(datos.getNombre(), id)) > 0) {
            throw new ErrorNegocio("nombre", "Ya existe otra cancha con ese nombre.");
        }
        Deporte deporte = cancha.getDeporte();
        if (datos.getIdDeporte() == null) {
            throw new ErrorNegocio("idDeporte", "Elija el deporte de la cancha.");
        }
        if (!Objects.equals(datos.getIdDeporte(), deporte.getId())) {
            deporte = deporteActivo(datos.getIdDeporte());
            List<ResumenReserva> reservas = consultaReservasFuturas.buscarFuturasDeCancha(id);
            if (!reservas.isEmpty() && !datos.getConfirmar()) {
                throw new ConfirmacionRequeridaException(reservas);
            }
        }
        cancha.modificar(datos.getNombre(), deporte, datos.getUbicacion(), datos.getCaracteristicas());
        if (datos.getActiva() && !cancha.estaActivo()) {
            cancha.activar();
        }
        fachadaPersistencia.put(cancha);
        servicioAuditoria.registrar(usuario, "MODIFICACION", ENTIDAD_CANCHA, id, detalleCancha(cancha));
        return cancha;
    }

    /**
     * Como {@code desactivarDeporte}, con {@code buscarFuturasDeCancha(id)} y {@code CanchaDesactivadaEvento}
     * (sec_cu23_p3).
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-23
     * @param id Cancha.
     * @param confirmar Confirmación tras la advertencia.
     * @return {@code ResultadoBaja}.
     * @throws ErrorNegocio si la cancha ya está inactiva (FA1)
     */
    public ResultadoBaja desactivarCancha(Long id, boolean confirmar) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_RECURSOS);
        Cancha cancha = fachadaPersistencia.get(id, Cancha.class);
        if (!cancha.estaActivo()) {
            throw new ErrorNegocio("La cancha ya está inactiva.");
        }
        List<ResumenReserva> afectadas = consultaReservasFuturas.buscarFuturasDeCancha(id);
        if (!confirmar) {
            return ResultadoBaja.pendiente(afectadas);
        }
        cancha.desactivar();
        fachadaPersistencia.put(cancha);
        publicadorEventos.publishEvent(new CanchaDesactivadaEvento(id, usuario, ahora()));
        servicioAuditoria.registrar(usuario, "BAJA", ENTIDAD_CANCHA, id,
                "{\"nombre\":" + json(cancha.getNombre()) + ",\"reservasCanceladas\":" + afectadas.size() + "}");
        return ResultadoBaja.realizada(afectadas);
    }

    /**
     * Obtiene el deporte elegido y verifica que esté activo (CU-21 paso 6 / FA5, CU-22 paso 6 / FA5).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param idDeporte deporte elegido
     * @return el deporte activo
     * @throws ErrorNegocio con campo {@code idDeporte} si no se eligió o está inactivo
     */
    private Deporte deporteActivo(Long idDeporte) {
        if (idDeporte == null) {
            throw new ErrorNegocio("idDeporte", "Elija el deporte de la cancha.");
        }
        Deporte deporte = fachadaPersistencia.get(idDeporte, Deporte.class);
        if (!deporte.estaActivo()) {
            throw new ErrorNegocio("idDeporte", "El deporte no está activo.");
        }
        return deporte;
    }

    /**
     * Momento de la baja en la hora local del club (la fachada no tiene {@code Reloj} en el diseño; ver notas).
     *
     * <p>Casos de uso: CU-20, CU-23
     * @return fecha y hora actual
     */
    private static LocalDateTime ahora() {
        return LocalDateTime.now(RelojSistema.ZONA_CLUB);
    }

    /**
     * Detalle JSON de auditoría de un deporte (sin datos sensibles, RNF-12).
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param deporte deporte
     * @return texto JSON
     */
    private static String detalleDeporte(Deporte deporte) {
        return "{\"nombre\":" + json(deporte.getNombre()) + ",\"descripcion\":" + json(deporte.getDescripcion())
                + ",\"activo\":" + deporte.estaActivo() + "}";
    }

    /**
     * Detalle JSON de auditoría de una cancha (RNF-12).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param cancha cancha
     * @return texto JSON
     */
    private static String detalleCancha(Cancha cancha) {
        return "{\"nombre\":" + json(cancha.getNombre()) + ",\"idDeporte\":" + cancha.getDeporte().getId()
                + ",\"ubicacion\":" + json(cancha.getUbicacion()) + ",\"caracteristicas\":"
                + json(cancha.getCaracteristicas()) + ",\"activa\":" + cancha.estaActivo() + "}";
    }

    /**
     * Convierte un texto en literal JSON (comillas y barras escapadas; null → {@code null}).
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-23
     * @param texto texto o null
     * @return literal JSON
     */
    private static String json(String texto) {
        if (texto == null) {
            return "null";
        }
        return "\"" + texto.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
}

package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.reservas.dominio.EstadoReserva;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Intermediario de base de datos de {@code Reserva} (tabla {@code reserva}). Broker concreto del patrón Database
 * Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos} (sus métodos
 * {@code materializar}, {@code insertar} y {@code actualizar} son finales). {@code consultaBase()} hace JOIN con
 * {@code usuario}/{@code socio} (columnas con alias {@code s_}) y {@code cancha} (alias {@code c_}) para leer la
 * reserva con su socio y su cancha en una sola consulta; {@code registradaPor} y {@code canceladaPor} se resuelven
 * con {@code get(id, Usuario)}. Control optimista: como {@code Reserva} tiene versión no nula, el {@code
 * actualizar} heredado agrega {@code version = version + 1 ... AND version = ?} y lanza {@code
 * ConcurrenciaException} si no cambió ninguna fila. La violación de la restricción de exclusión (SQLState 23P01)
 * la traduce {@code BrokerBaseDatos} a {@code ErrorNegocio}. Nadie fuera de {@code compartido.persistencia} lo
 * invoca: se usa {@code FachadaPersistencia}.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33; tareas programadas:
 * finalización de reservas cumplidas
 */
@Component
public class BrokerReserva extends BrokerBaseDatos<Reserva> {

    /** Columnas de usuario y socio expuestas con prefijo {@code s_} (las que lee {@code BrokerSocio}). */
    private static final String COLUMNAS_SOCIO = String.join(", ",
            "usuario.id AS s_id", "usuario.username AS s_username", "usuario.email AS s_email",
            "usuario.password_hash AS s_password_hash", "usuario.estado AS s_estado",
            "usuario.intentos_fallidos AS s_intentos_fallidos", "usuario.bloqueado_hasta AS s_bloqueado_hasta",
            "usuario.fecha_creacion AS s_fecha_creacion", "socio.dni AS s_dni", "socio.nombre AS s_nombre",
            "socio.apellido AS s_apellido", "socio.fecha_nacimiento AS s_fecha_nacimiento",
            "socio.telefono AS s_telefono", "socio.direccion AS s_direccion", "socio.fecha_alta AS s_fecha_alta",
            "socio.categoria_id AS s_categoria_id", "socio.grupo_familiar_id AS s_grupo_familiar_id");

    /** Columnas de cancha expuestas con prefijo {@code c_} (las que lee {@code BrokerCancha}). */
    private static final String COLUMNAS_CANCHA = String.join(", ",
            "cancha.id AS c_id", "cancha.nombre AS c_nombre", "cancha.deporte_id AS c_deporte_id",
            "cancha.ubicacion AS c_ubicacion", "cancha.caracteristicas AS c_caracteristicas",
            "cancha.activa AS c_activa");

    /**
     * Asociaciones leídas en {@code crearObjeto} que se entregan en {@code cargarAsociaciones} (el broker es único
     * para toda la aplicación, por eso el mapa es sincronizado y por identidad del objeto).
     */
    private final Map<Reserva, AsociacionesPendientes> pendientes = Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerReserva(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase que materializa; la fachada registra el broker con ella.
     *
     * <p>Casos de uso: todos
     * @return {@code Reserva}
     */
    @Override
    public Class<Reserva> claseObjeto() {
        return Reserva.class;
    }

    /**
     * Nombre de la tabla.
     *
     * <p>Casos de uso: todos
     * @return «reserva»
     */
    @Override
    protected String tabla() {
        return "reserva";
    }

    /**
     * SELECT de {@code reserva} con JOIN a {@code usuario}/{@code socio} (alias {@code s_}) y a {@code cancha}
     * (alias {@code c_}). Las tablas no llevan alias, así los objetos consulta escriben {@code reserva.fecha},
     * {@code socio.apellido} o {@code cancha.deporte_id} en sus condiciones.
     *
     * <p>Casos de uso: todos
     * @return SELECT sin WHERE
     */
    @Override
    protected String consultaBase() {
        return "SELECT reserva.*, " + COLUMNAS_SOCIO + ", " + COLUMNAS_CANCHA
                + " FROM reserva"
                + " JOIN usuario ON usuario.id = reserva.socio_id"
                + " JOIN socio ON socio.id = reserva.socio_id"
                + " JOIN cancha ON cancha.id = reserva.cancha_id";
    }

    /**
     * Rehidrata el estado con {@code EstadoReserva.desdeCodigo(fila.texto("estado"))}, arma el {@code Turno} con
     * las columnas {@code fecha}, {@code hora_inicio} y {@code hora_fin} e invoca {@code Reserva.reconstituir}; el
     * socio y la cancha se materializan desde las columnas con prefijo ({@code
     * fachada().brokerDe(Socio.class).materializarDesdeFila(fila.conPrefijo("s_"))}, ídem cancha con {@code c_}).
     *
     * <p>Casos de uso: todos
     * @param fila fila de {@code consultaBase()}
     * @return reserva reconstituida (las asociaciones se entregan en {@code cargarAsociaciones})
     */
    @Override
    protected Reserva crearObjeto(FilaResultado fila) {
        Turno turno = new Turno(fila.fecha("fecha"), fila.hora("hora_inicio"), fila.hora("hora_fin"));
        Reserva r = Reserva.reconstituir(fila.largo("id"), fila.entero("version"), turno,
                EstadoReserva.desdeCodigo(fila.texto("estado")), fila.fechaHora("fecha_creacion"),
                fila.texto("motivo_cancelacion"));
        Socio socio = fachada().brokerDe(Socio.class).materializarDesdeFila(fila.conPrefijo("s_"));
        Cancha cancha = fachada().brokerDe(Cancha.class).materializarDesdeFila(fila.conPrefijo("c_"));
        pendientes.put(r, new AsociacionesPendientes(socio, cancha, fila.largo("registrada_por_id"),
                fila.largo("cancelada_por_id")));
        return r;
    }

    /**
     * Columnas de la reserva nueva con sus valores; el estado se guarda con {@code estado.codigo()} y las entidades
     * con su id (conversión de {@code aParametro}).
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param r reserva nueva
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(Reserva r) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("socio_id", r.getSocio());
        valores.put("cancha_id", r.getCancha());
        valores.put("fecha", r.getTurno().getFecha());
        valores.put("hora_inicio", r.getTurno().getHoraInicio());
        valores.put("hora_fin", r.getTurno().getHoraFin());
        valores.put("estado", r.getEstado());
        valores.put("fecha_creacion", r.getFechaCreacion());
        valores.put("registrada_por_id", r.getRegistradaPor());
        valores.put("cancelada_por_id", r.getCanceladaPor());
        valores.put("motivo_cancelacion", r.getMotivoCancelacion());
        valores.put("version", r.getVersion() == null ? 0 : r.getVersion());
        return valores;
    }

    /**
     * Columnas que cambian en una actualización (turno y cancha en CU-32, estado y cancelación en CU-30, CU-33 y
     * las bajas, estado en la finalización); la versión la agrega la plantilla.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-32, CU-33; tareas programadas: finalización de reservas
     * cumplidas
     * @param r reserva persistida
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Reserva r) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("cancha_id", r.getCancha());
        valores.put("fecha", r.getTurno().getFecha());
        valores.put("hora_inicio", r.getTurno().getHoraInicio());
        valores.put("hora_fin", r.getTurno().getHoraFin());
        valores.put("estado", r.getEstado());
        valores.put("cancelada_por_id", r.getCanceladaPor());
        valores.put("motivo_cancelacion", r.getMotivoCancelacion());
        return valores;
    }

    /**
     * Obtiene {@code registradaPor} y {@code canceladaPor} con {@code fachada().get(id, Usuario)} y entrega todas
     * las asociaciones con {@code r.restaurarAsociaciones(...)}.
     *
     * <p>Casos de uso: todos
     * @param r reserva recién creada
     */
    @Override
    protected void cargarAsociaciones(Reserva r) {
        AsociacionesPendientes a = pendientes.remove(r);
        if (a == null) {
            return;
        }
        Usuario registradaPor = a.idRegistradaPor() == null ? null : fachada().get(a.idRegistradaPor(), Usuario.class);
        Usuario canceladaPor = a.idCanceladaPor() == null ? null : fachada().get(a.idCanceladaPor(), Usuario.class);
        r.restaurarAsociaciones(a.socio(), a.cancha(), registradaPor, canceladaPor);
    }

    /**
     * Datos de una fila que se resuelven después de registrar la reserva en el mapa de identidad.
     *
     * <p>Casos de uso: todos
     * @param socio socio materializado por JOIN
     * @param cancha cancha materializada por JOIN
     * @param idRegistradaPor cuenta que registró
     * @param idCanceladaPor cuenta que canceló o {@code null}
     */
    private record AsociacionesPendientes(Socio socio, Cancha cancha, Long idRegistradaPor, Long idCanceladaPor) {
    }
}

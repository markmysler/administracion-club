package ar.edu.ub.club.notificaciones.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.notificaciones.dominio.EstadoEnvio;
import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import ar.edu.ub.club.notificaciones.dominio.TipoNotificacion;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Intermediario de base de datos de {@code Notificacion} (tabla {@code notificacion}). Broker concreto del patrón
 * Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}. Los
 * enumerados {@code TipoNotificacion} y {@code EstadoEnvio} se guardan por nombre; el socio y la reserva se
 * resuelven con {@code get} en {@code cargarAsociaciones}. No usa control optimista.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
 */
@Component
public class BrokerNotificacion extends BrokerBaseDatos<Notificacion> {

    /** Claves foráneas leídas en {@code crearObjeto} que se resuelven en {@code cargarAsociaciones}. */
    private final Map<Notificacion, Long[]> pendientes = Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerNotificacion(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Resuelve el socio destinatario y la reserva con {@code fachada().get(id, Clase)} y los entrega con {@code
     * restaurarAsociaciones}.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param n notificación recién creada
     */
    @Override
    protected void cargarAsociaciones(Notificacion n) {
        Long[] ids = pendientes.remove(n);
        if (ids == null) {
            return;
        }
        Socio socio = fachada().get(ids[0], Socio.class);
        Reserva reserva = fachada().get(ids[1], Reserva.class);
        n.restaurarAsociaciones(socio, reserva);
    }

    /**
     * Clase que materializa.
     *
     * <p>Casos de uso: todos
     * @return {@code Notificacion}
     */
    @Override
    public Class<Notificacion> claseObjeto() {
        return Notificacion.class;
    }

    /**
     * Nombre de la tabla.
     *
     * <p>Casos de uso: todos
     * @return «notificacion»
     */
    @Override
    protected String tabla() {
        return "notificacion";
    }

    /**
     * Reconstituye la notificación con {@code Notificacion.reconstituir(...)} y guarda las claves del socio y de
     * la reserva para {@code cargarAsociaciones}.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param fila fila de {@code notificacion}
     * @return notificación sin asociaciones
     */
    @Override
    protected Notificacion crearObjeto(FilaResultado fila) {
        Notificacion n = Notificacion.reconstituir(fila.largo("id"), fila.fechaHora("fecha"),
                TipoNotificacion.valueOf(fila.texto("tipo")), fila.texto("mensaje"),
                EstadoEnvio.valueOf(fila.texto("estado_envio")), fila.texto("detalle_error"));
        pendientes.put(n, new Long[] {fila.largo("socio_id"), fila.largo("reserva_id")});
        return n;
    }

    /**
     * Columnas del INSERT.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param objeto notificación nueva
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(Notificacion objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("socio_id", objeto.getSocio());
        valores.put("reserva_id", objeto.getReserva());
        valores.put("tipo", objeto.getTipo());
        valores.put("mensaje", objeto.getMensaje());
        valores.put("fecha", objeto.getFecha());
        valores.put("estado_envio", objeto.getEstadoEnvio());
        valores.put("detalle_error", objeto.getDetalleError());
        return valores;
    }

    /**
     * Columnas del UPDATE: solo cambia el resultado del envío.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param objeto notificación persistida
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Notificacion objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("estado_envio", objeto.getEstadoEnvio());
        valores.put("detalle_error", objeto.getDetalleError());
        return valores;
    }
}

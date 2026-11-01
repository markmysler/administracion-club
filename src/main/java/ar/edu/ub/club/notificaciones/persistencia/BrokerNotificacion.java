package ar.edu.ub.club.notificaciones.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import java.util.Map;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class BrokerNotificacion extends BrokerBaseDatos<Notificacion> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: PENDIENTE
     * @param proveedor proveedor de conexiones
     */
    public BrokerNotificacion(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param n valor de {@code n}
     */
    protected void cargarAsociaciones(Notificacion n) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    public Class<Notificacion> claseObjeto() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    protected String tabla() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param fila valor de {@code fila}
     * @return el resultado
     */
    @Override
    protected Notificacion crearObjeto(FilaResultado fila) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param objeto valor de {@code objeto}
     * @return el resultado
     */
    @Override
    protected Map<String, Object> valoresInsercion(Notificacion objeto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param objeto valor de {@code objeto}
     * @return el resultado
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Notificacion objeto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

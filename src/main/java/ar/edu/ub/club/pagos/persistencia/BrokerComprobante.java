package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import java.util.Map;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class BrokerComprobante extends BrokerBaseDatos<Comprobante> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: PENDIENTE
     * @param proveedor proveedor de conexiones
     */
    public BrokerComprobante(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param c valor de {@code c}
     */
    protected void cargarAsociaciones(Comprobante c) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    public Class<Comprobante> claseObjeto() {
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
    protected Comprobante crearObjeto(FilaResultado fila) {
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
    protected Map<String, Object> valoresInsercion(Comprobante objeto) {
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
    protected Map<String, Object> valoresActualizacion(Comprobante objeto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

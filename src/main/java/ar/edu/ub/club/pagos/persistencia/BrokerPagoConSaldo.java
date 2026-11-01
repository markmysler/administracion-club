package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.pagos.dominio.PagoConSaldo;
import java.util.Map;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class BrokerPagoConSaldo extends BrokerPago<PagoConSaldo> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: PENDIENTE
     * @param proveedor proveedor de conexiones
     */
    public BrokerPagoConSaldo(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    protected String tablaHija() {
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
    protected Map<String, Object> valoresHija(PagoConSaldo objeto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    public Class<PagoConSaldo> claseObjeto() {
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
    protected PagoConSaldo crearObjeto(FilaResultado fila) {
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
    protected Map<String, Object> valoresInsercion(PagoConSaldo objeto) {
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
    protected Map<String, Object> valoresActualizacion(PagoConSaldo objeto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

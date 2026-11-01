package ar.edu.ub.club.agenda.persistencia;

import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import java.util.Map;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class BrokerReglaReserva extends BrokerBaseDatos<ReglaReserva> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: PENDIENTE
     * @param proveedor proveedor de conexiones
     */
    public BrokerReglaReserva(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    public Class<ReglaReserva> claseObjeto() {
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
    protected ReglaReserva crearObjeto(FilaResultado fila) {
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
    protected Map<String, Object> valoresInsercion(ReglaReserva objeto) {
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
    protected Map<String, Object> valoresActualizacion(ReglaReserva objeto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

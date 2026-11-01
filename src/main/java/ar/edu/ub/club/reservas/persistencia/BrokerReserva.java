package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.reservas.dominio.Reserva;
import java.util.Map;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: todos
 */
public class BrokerReserva extends BrokerBaseDatos<Reserva> {

    /** Atributo proveedor. */
    private ProveedorConexiones proveedor;

    /** Atributo fachada. */
    private FachadaPersistencia fachada;

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
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    public Class<Reserva> claseObjeto() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    protected String tabla() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    protected String consultaBase() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param fila valor de {@code fila}
     * @return el resultado
     */
    protected Reserva crearObjeto(FilaResultado fila) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param r valor de {@code r}
     * @return el resultado
     */
    protected Map<String, Object> valoresInsercion(Reserva r) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param r valor de {@code r}
     * @return el resultado
     */
    protected Map<String, Object> valoresActualizacion(Reserva r) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param r valor de {@code r}
     */
    protected void cargarAsociaciones(Reserva r) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

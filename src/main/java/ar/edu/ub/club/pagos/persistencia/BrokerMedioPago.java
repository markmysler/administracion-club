package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code MedioPago} sobre {@code medio_pago} (nombre UNIQUE; baja lógica con {@code habilitado}).
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-34, CU-36, CU-37
 */
@Component
public class BrokerMedioPago extends BrokerBaseDatos<MedioPago> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerMedioPago(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @return MedioPago.class
     */
    @Override
    public Class<MedioPago> claseObjeto() {
        return MedioPago.class;
    }

    /**
     * Tabla principal.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @return "medio_pago"
     */
    @Override
    protected String tabla() {
        return "medio_pago";
    }

    /**
     * Reconstituye el medio.
     *
     * <p>Casos de uso: CU-34, CU-36, CU-37
     * @param fila fila de {@code medio_pago}
     * @return el medio
     */
    @Override
    protected MedioPago crearObjeto(FilaResultado fila) {
        return MedioPago.reconstituir(fila.largo("id"), fila.texto("nombre"), fila.booleano("habilitado"));
    }

    /**
     * Columnas del INSERT (alta de medio, CU-34 FA1).
     *
     * <p>Casos de uso: CU-34
     * @param objeto medio nuevo
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(MedioPago objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("nombre", objeto.getNombre());
        v.put("habilitado", objeto.getHabilitado());
        return v;
    }

    /**
     * Columnas del UPDATE: solo la habilitación.
     *
     * <p>Casos de uso: CU-34
     * @param objeto medio persistido
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(MedioPago objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("habilitado", objeto.getHabilitado());
        return v;
    }
}

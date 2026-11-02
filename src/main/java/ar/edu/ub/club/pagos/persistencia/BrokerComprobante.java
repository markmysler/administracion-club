package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code Comprobante} sobre {@code comprobante} (UNIQUE {@code pago_id} y {@code numero}). El pago se
 * resuelve con {@code get(id, Pago)} polimórfico; si se llegó desde el pago, ya está en el mapa de identidad.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-35, CU-36, CU-37
 */
@Component
public class BrokerComprobante extends BrokerBaseDatos<Comprobante> {

    /** Pago leído en {@code crearObjeto} hasta {@code cargarAsociaciones}. */
    private final Map<Comprobante, Long> pendientes = Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerComprobante(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Resuelve el pago con {@code get(id, Pago)} (polimórfico) y lo entrega.
     *
     * <p>Casos de uso: CU-37
     * @param c comprobante recién creado
     */
    @Override
    protected void cargarAsociaciones(Comprobante c) {
        Long idPago = pendientes.remove(c);
        if (idPago != null) {
            c.restaurarAsociaciones(fachada().get(idPago, Pago.class));
        }
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return Comprobante.class
     */
    @Override
    public Class<Comprobante> claseObjeto() {
        return Comprobante.class;
    }

    /**
     * Tabla principal.
     *
     * <p>Casos de uso: todos
     * @return "comprobante"
     */
    @Override
    protected String tabla() {
        return "comprobante";
    }

    /**
     * Reconstituye el comprobante y recuerda su pago.
     *
     * <p>Casos de uso: CU-37
     * @param fila fila de {@code comprobante}
     * @return el comprobante
     */
    @Override
    protected Comprobante crearObjeto(FilaResultado fila) {
        Comprobante c = Comprobante.reconstituir(fila.largo("id"), fila.texto("numero"), fila.fechaHora("fecha_emision"),
                fila.dinero("importe"));
        pendientes.put(c, fila.largo("pago_id"));
        return c;
    }

    /**
     * Columnas del INSERT.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param objeto comprobante nuevo
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(Comprobante objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("pago_id", objeto.getPago());
        v.put("numero", objeto.getNumero());
        v.put("fecha_emision", objeto.getFechaEmision());
        v.put("importe", objeto.getImporte());
        return v;
    }

    /**
     * El comprobante es inmutable: no hay columnas que actualizar.
     *
     * <p>Casos de uso: ninguno (el comprobante no cambia)
     * @param objeto comprobante
     * @return mapa vacío
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Comprobante objeto) {
        return new LinkedHashMap<>();
    }
}

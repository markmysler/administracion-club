package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.compartido.persistencia.ProxyListaVirtual;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.EstadoCuota;
import ar.edu.ub.club.cuotas.dominio.ItemCuota;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code Cuota} sobre la tabla {@code cuota}; en el alta inserta también las filas {@code item_cuota}
 * de la composición. Los ítems de una cuota leída se cargan con un {@code ProxyListaVirtual} ({@code
 * ItemsDeCuota}) solo si alguien los recorre. El estado se guarda como su código (patrón State).
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-05, CU-35, CU-36, CU-37; tareas programadas: emisión mensual de cuotas, vencimiento de cuotas
 */
@Component
public class BrokerCuota extends BrokerBaseDatos<Cuota> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerCuota(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Reconstituye la cuota con {@code Cuota.reconstituir}; el estado con {@code EstadoCuota.desdeCodigo} y los
     * ítems como {@code ProxyListaVirtual} (sin tocar la base).
     *
     * <p>Casos de uso: todos
     * @param fila fila de {@code cuota}
     * @return la cuota
     */
    @Override
    protected Cuota crearObjeto(FilaResultado fila) {
        Long id = fila.largo("id");
        List<ItemCuota> items = new ProxyListaVirtual<>(fachada(), new ItemsDeCuota(id));
        return Cuota.reconstituir(id, fila.largo("grupo_familiar_id"), fila.texto("periodo").trim(),
                fila.dinero("importe"), fila.dinero("credito_aplicado"), fila.fecha("fecha_emision"),
                fila.fecha("fecha_vencimiento"), EstadoCuota.desdeCodigo(fila.texto("estado")), items);
    }

    /**
     * En el alta inserta una fila {@code item_cuota} por ítem (composición); los ítems no cambian después, así que
     * en la actualización no hace nada.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param c cuota guardada
     * @param alta true si se acaba de insertar
     */
    @Override
    protected void guardarPartes(Cuota c, boolean alta) {
        if (!alta) {
            return;
        }
        for (ItemCuota item : c.getItems()) {
            ejecutarActualizacion("INSERT INTO item_cuota (cuota_id, socio_id, categoria_id, importe) VALUES (?, ?, ?, ?)",
                    List.of(c.getId(), item.getSocio().getId(), item.getCategoria().getId(), item.getImporte()));
        }
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return Cuota.class
     */
    @Override
    public Class<Cuota> claseObjeto() {
        return Cuota.class;
    }

    /**
     * Tabla principal.
     *
     * <p>Casos de uso: todos
     * @return "cuota"
     */
    @Override
    protected String tabla() {
        return "cuota";
    }

    /**
     * Columnas del INSERT de la cuota emitida.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param objeto cuota nueva
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(Cuota objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("grupo_familiar_id", objeto.getIdGrupo());
        v.put("periodo", objeto.getPeriodo());
        v.put("importe", objeto.getImporte());
        v.put("credito_aplicado", objeto.getCreditoAplicado());
        v.put("fecha_emision", objeto.getFechaEmision());
        v.put("fecha_vencimiento", objeto.getFechaVencimiento());
        v.put("estado", objeto.getEstado());
        return v;
    }

    /**
     * Columnas del UPDATE: solo cambia el estado (Pendiente → Vencida | Pagada).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: vencimiento de cuotas
     * @param objeto cuota persistida
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Cuota objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("estado", objeto.getEstado());
        return v;
    }
}

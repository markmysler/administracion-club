package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.cuotas.dominio.ItemCuota;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code ItemCuota} sobre {@code item_cuota}, de solo lectura en la práctica: las filas las inserta
 * {@code BrokerCuota.guardarPartes} con la cuota. Resuelve el socio y la categoría con {@code fachada().get}.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-05, CU-35, CU-36
 */
@Component
public class BrokerItemCuota extends BrokerBaseDatos<ItemCuota> {

    /** Claves foráneas leídas en {@code crearObjeto} ([socio_id, categoria_id]) hasta {@code cargarAsociaciones}. */
    private final Map<ItemCuota, Long[]> pendientes = Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerItemCuota(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Resuelve socio y categoría por {@code get} y los entrega con {@code restaurarAsociaciones}.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @param i ítem recién creado
     */
    @Override
    protected void cargarAsociaciones(ItemCuota i) {
        Long[] claves = pendientes.remove(i);
        if (claves != null) {
            i.restaurarAsociaciones(fachada().get(claves[0], Socio.class), fachada().get(claves[1], CategoriaSocio.class));
        }
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @return ItemCuota.class
     */
    @Override
    public Class<ItemCuota> claseObjeto() {
        return ItemCuota.class;
    }

    /**
     * Tabla principal.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @return "item_cuota"
     */
    @Override
    protected String tabla() {
        return "item_cuota";
    }

    /**
     * Reconstituye el ítem y guarda las claves foráneas para {@code cargarAsociaciones}.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @param fila fila de {@code item_cuota}
     * @return el ítem
     */
    @Override
    protected ItemCuota crearObjeto(FilaResultado fila) {
        ItemCuota item = ItemCuota.reconstituir(fila.largo("id"), fila.dinero("importe"));
        pendientes.put(item, new Long[] {fila.largo("socio_id"), fila.largo("categoria_id")});
        return item;
    }

    /**
     * Columnas del INSERT. El ítem no conoce su cuota, por eso no se inserta con {@code put}: lo hace {@code
     * BrokerCuota.guardarPartes}; este método describe solo las columnas propias.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param objeto ítem
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(ItemCuota objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("socio_id", objeto.getSocio());
        v.put("categoria_id", objeto.getCategoria());
        v.put("importe", objeto.getImporte());
        return v;
    }

    /**
     * Columnas del UPDATE: el ítem es inmutable, no hay columnas que actualizar.
     *
     * <p>Casos de uso: ninguno (el ítem no cambia)
     * @param objeto ítem
     * @return mapa vacío
     */
    @Override
    protected Map<String, Object> valoresActualizacion(ItemCuota objeto) {
        return new LinkedHashMap<>();
    }
}

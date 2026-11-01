package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code CategoriaSocio} sobre la tabla {@code categoria_socio}: sin asociaciones ni partes.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de
 * {@code BrokerBaseDatos}. Bean {@code @Component}; se usa a través de {@code FachadaPersistencia}.
 *
 * <p>Casos de uso: todos
 */
@Component
public class BrokerCategoriaSocio extends BrokerBaseDatos<CategoriaSocio> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerCategoriaSocio(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return {@code CategoriaSocio.class}
     */
    @Override
    public Class<CategoriaSocio> claseObjeto() {
        return CategoriaSocio.class;
    }

    /**
     * Tabla de la clase.
     *
     * <p>Casos de uso: todos
     * @return {@code "categoria_socio"}
     */
    @Override
    protected String tabla() {
        return "categoria_socio";
    }

    /**
     * Desmaterialización: {@code CategoriaSocio.reconstituir(id, nombre, importe, activa)}.
     *
     * <p>Casos de uso: todos
     * @param fila fila de {@code categoria_socio}
     * @return la categoría
     */
    @Override
    protected CategoriaSocio crearObjeto(FilaResultado fila) {
        return CategoriaSocio.reconstituir(fila.largo("id"), fila.texto("nombre"), fila.dinero("importe"),
                fila.booleano("activa"));
    }

    /**
     * Columnas del INSERT (CU-06, alta).
     *
     * <p>Casos de uso: CU-06
     * @param objeto categoría nueva
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(CategoriaSocio objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("nombre", objeto.getNombre());
        valores.put("importe", objeto.getImporte());
        valores.put("activa", objeto.getActiva());
        return valores;
    }

    /**
     * Columnas del UPDATE (CU-06, modificación): las mismas que el alta.
     *
     * <p>Casos de uso: CU-06
     * @param objeto categoría persistida
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(CategoriaSocio objeto) {
        return valoresInsercion(objeto);
    }
}

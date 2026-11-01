package ar.edu.ub.club.recursos.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.recursos.dominio.Deporte;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Intermediario de base de datos de {@code Deporte} (tabla {@code deporte}): lo materializa por {@code get} o
 * por las consultas {@code Deportes} y {@code DeportePorNombre}, y lo inserta o
 * actualiza con {@code put} (CU-18, CU-19, CU-20).
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26, CU-27, CU-28, CU-32, CU-38
 */
@Component
public class BrokerDeporte extends BrokerBaseDatos<Deporte> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26
     * @param proveedor proveedor de conexiones
     */
    public BrokerDeporte(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase que maneja el broker (clave del registro en {@code FachadaPersistencia}).
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26
     * @return {@code Deporte.class}
     */
    @Override
    public Class<Deporte> claseObjeto() {
        return Deporte.class;
    }

    /**
     * Gancho del Template Method: tabla principal.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26
     * @return nombre de la tabla {@code deporte}
     */
    @Override
    protected String tabla() {
        return "deporte";
    }

    /**
     * Gancho del Template Method: reconstituye el deporte con {@code Deporte.reconstituir(id, nombre, descripcion,
     * activo)}. Lee con {@code FilaResultado}, así también sirve para filas con prefijo (JOIN de otro broker).
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26
     * @param fila fila de {@code deporte}
     * @return el deporte
     */
    @Override
    protected Deporte crearObjeto(FilaResultado fila) {
        return Deporte.reconstituir(fila.largo("id"), fila.texto("nombre"), fila.texto("descripcion"),
                fila.booleano("activo"));
    }

    /**
     * Gancho del Template Method: columnas del INSERT (el id lo genera la base, CU-18 paso 6).
     *
     * <p>Casos de uso: CU-18
     * @param objeto deporte nuevo
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(Deporte objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("nombre", objeto.getNombre());
        valores.put("descripcion", objeto.getDescripcion());
        valores.put("activo", objeto.getActivo());
        return valores;
    }

    /**
     * Gancho del Template Method: columnas del UPDATE (CU-19 modificación o reactivación, CU-20 baja).
     *
     * <p>Casos de uso: CU-19, CU-20
     * @param objeto deporte persistido
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Deporte objeto) {
        return valoresInsercion(objeto);
    }
}

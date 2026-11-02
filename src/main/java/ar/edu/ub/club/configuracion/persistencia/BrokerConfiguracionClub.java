package ar.edu.ub.club.configuracion.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.configuracion.dominio.ConfiguracionClub;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker concreto de {@code ConfiguracionClub} (tabla {@code configuracion_club}, fila única con id 1). Database
 * Broker (Larman, cap. 34): completa los ganchos del Template Method de {@code BrokerBaseDatos}. La fila la crea
 * la migración V6; la aplicación solo la lee y la actualiza.
 *
 * <p>Casos de uso: CU-41
 */
@Component
public class BrokerConfiguracionClub extends BrokerBaseDatos<ConfiguracionClub> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: CU-41
     * @param proveedor proveedor de conexiones
     */
    public BrokerConfiguracionClub(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase que maneja.
     *
     * <p>Casos de uso: CU-41
     * @return {@code ConfiguracionClub.class}
     */
    @Override
    public Class<ConfiguracionClub> claseObjeto() {
        return ConfiguracionClub.class;
    }

    /**
     * Tabla: {@code configuracion_club}.
     *
     * <p>Casos de uso: CU-41
     * @return nombre de la tabla
     */
    @Override
    protected String tabla() {
        return "configuracion_club";
    }

    /**
     * Desmaterializa con {@code ConfiguracionClub.reconstituir(...)}.
     *
     * <p>Casos de uso: CU-41
     * @param fila fila del resultado
     * @return configuración
     */
    @Override
    protected ConfiguracionClub crearObjeto(FilaResultado fila) {
        return ConfiguracionClub.reconstituir(fila.largo("id"), fila.texto("nombre"), fila.texto("direccion"),
                fila.texto("telefono"), fila.texto("email"));
    }

    /**
     * Columnas del INSERT (incluye el id fijo 1).
     *
     * <p>Casos de uso: CU-41
     * @param objeto configuración
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(ConfiguracionClub objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("id", ConfiguracionClub.ID_UNICO);
        valores.putAll(valoresActualizacion(objeto));
        return valores;
    }

    /**
     * Columnas del UPDATE.
     *
     * <p>Casos de uso: CU-41
     * @param objeto configuración
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(ConfiguracionClub objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("nombre", objeto.getNombre());
        valores.put("direccion", objeto.getDireccion());
        valores.put("telefono", objeto.getTelefono());
        valores.put("email", objeto.getEmail());
        return valores;
    }
}

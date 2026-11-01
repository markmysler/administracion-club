package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import ar.edu.ub.club.pagos.dominio.ModoPasarela;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de la fila única {@code configuracion_pasarela} ({@code id = 1}, la crea la migración). La clave es
 * {@code SMALLINT}: {@code consultaBase} la convierte a {@code BIGINT} para que el mapa de identidad la registre
 * con la misma clave {@code Long} que usa {@code get(ConfiguracionPasarela.ID_UNICO, ...)}.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-34, CU-35
 */
@Component
public class BrokerConfiguracionPasarela extends BrokerBaseDatos<ConfiguracionPasarela> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @param proveedor proveedor de conexiones
     */
    public BrokerConfiguracionPasarela(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @return ConfiguracionPasarela.class
     */
    @Override
    public Class<ConfiguracionPasarela> claseObjeto() {
        return ConfiguracionPasarela.class;
    }

    /**
     * Tabla principal.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @return "configuracion_pasarela"
     */
    @Override
    protected String tabla() {
        return "configuracion_pasarela";
    }

    /**
     * SELECT con la clave convertida a BIGINT.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @return SELECT sin WHERE
     */
    @Override
    protected String consultaBase() {
        return "SELECT CAST(configuracion_pasarela.id AS BIGINT) AS id, configuracion_pasarela.proveedor,"
                + " configuracion_pasarela.modo, configuracion_pasarela.habilitada,"
                + " configuracion_pasarela.credenciales_configuradas FROM configuracion_pasarela";
    }

    /**
     * Reconstituye la configuración.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @param fila fila de {@code configuracion_pasarela}
     * @return la configuración
     */
    @Override
    protected ConfiguracionPasarela crearObjeto(FilaResultado fila) {
        return ConfiguracionPasarela.reconstituir(fila.largo("id"), fila.texto("proveedor"),
                ModoPasarela.valueOf(fila.texto("modo").trim()), fila.booleano("habilitada"),
                fila.booleano("credenciales_configuradas"));
    }

    /**
     * Columnas del INSERT (no se usa en la aplicación: la fila la crea la migración).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param objeto configuración
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(ConfiguracionPasarela objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("id", ConfiguracionPasarela.ID_UNICO);
        v.put("proveedor", objeto.getProveedor());
        v.put("modo", objeto.getModo());
        v.put("habilitada", objeto.getHabilitada());
        v.put("credenciales_configuradas", objeto.getCredencialesConfiguradas());
        return v;
    }

    /**
     * Columnas del UPDATE (CU-34): modo, habilitación y credenciales.
     *
     * <p>Casos de uso: CU-34
     * @param objeto configuración
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(ConfiguracionPasarela objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("modo", objeto.getModo());
        v.put("habilitada", objeto.getHabilitada());
        v.put("credenciales_configuradas", objeto.getCredencialesConfiguradas());
        return v;
    }
}

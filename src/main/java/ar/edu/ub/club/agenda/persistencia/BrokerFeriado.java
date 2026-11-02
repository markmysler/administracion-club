package ar.edu.ub.club.agenda.persistencia;

import ar.edu.ub.club.agenda.dominio.Feriado;
import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Intermediario de base de datos de {@code Feriado} (tabla {@code feriado}): materializa los feriados que
 * piden las consultas {@code Feriados} y {@code FeriadosEntre} y los inserta o borra (CU-24 FA1 y FA2).
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
 */
@Component
public class BrokerFeriado extends BrokerBaseDatos<Feriado> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @param proveedor proveedor de conexiones
     */
    public BrokerFeriado(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase que maneja el broker (clave del registro en {@code FachadaPersistencia}).
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @return {@code Feriado.class}
     */
    @Override
    public Class<Feriado> claseObjeto() {
        return Feriado.class;
    }

    /**
     * Gancho del Template Method: tabla principal.
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @return nombre de la tabla {@code feriado}
     */
    @Override
    protected String tabla() {
        return "feriado";
    }

    /**
     * Gancho del Template Method: reconstituye el feriado con {@code Feriado.reconstituir(id, fecha, descripcion)}.
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @param fila fila de {@code feriado}
     * @return el feriado
     */
    @Override
    protected Feriado crearObjeto(FilaResultado fila) {
        return Feriado.reconstituir(fila.largo("id"), fila.fecha("fecha"), fila.texto("descripcion"));
    }

    /**
     * Gancho del Template Method: columnas del INSERT ({@code fecha}, {@code descripcion}; el id lo genera la base).
     *
     * <p>Casos de uso: CU-24
     * @param objeto feriado nuevo
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(Feriado objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("fecha", objeto.getFecha());
        valores.put("descripcion", objeto.getDescripcion());
        return valores;
    }

    /**
     * Gancho del Template Method: columnas del UPDATE (las mismas que el INSERT; ningún caso de uso modifica un
     * feriado: se elimina y se vuelve a registrar).
     *
     * <p>Casos de uso: CU-24
     * @param objeto feriado persistido
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Feriado objeto) {
        return valoresInsercion(objeto);
    }
}

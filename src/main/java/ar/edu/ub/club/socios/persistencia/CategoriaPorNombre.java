package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import java.util.List;

/**
 * Categorías con ese nombre (sin distinguir mayúsculas ni espacios de los extremos). La usa {@code ServicioSocios}
 * para verificar que no exista otra categoría con el mismo nombre (CU-06 paso 7, FA3).
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code CategoriaSocio} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-06
 */
public class CategoriaPorNombre extends ConsultaEntidades<CategoriaSocio> {

    /** Nombre buscado. */
    private final String nombre;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-06
     * @param nombre nombre buscado
     */
    public CategoriaPorNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code CategoriaSocio}.
     *
     * <p>Casos de uso: CU-06
     * @return {@code CategoriaSocio.class}
     */
    @Override
    public Class<CategoriaSocio> claseResultado() {
        return CategoriaSocio.class;
    }

    /**
     * Condición por nombre sin distinguir mayúsculas.
     *
     * <p>Casos de uso: CU-06
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE lower(categoria_socio.nombre) = lower(?)";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-06
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(nombre == null ? "" : nombre.trim());
    }
}

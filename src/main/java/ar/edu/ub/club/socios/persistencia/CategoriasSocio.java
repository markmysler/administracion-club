package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import java.util.List;

/**
 * Todas las categorías de socio ordenadas por nombre (P-10 y selector de P-07).
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code CategoriaSocio} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-06
 */
public class CategoriasSocio extends ConsultaEntidades<CategoriaSocio> {

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06
     */
    public CategoriasSocio() {
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code CategoriaSocio}.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06
     * @return {@code CategoriaSocio.class}
     */
    @Override
    public Class<CategoriaSocio> claseResultado() {
        return CategoriaSocio.class;
    }

    /**
     * Sin condición, orden por nombre.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "ORDER BY categoria_socio.nombre";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}

package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import java.util.List;

/**
 * Categorías activas (asignables) ordenadas por nombre.
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code CategoriaSocio} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-01, CU-02
 */
public class CategoriasActivas extends ConsultaEntidades<CategoriaSocio> {

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-01, CU-02
     */
    public CategoriasActivas() {
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code CategoriaSocio}.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return {@code CategoriaSocio.class}
     */
    @Override
    public Class<CategoriaSocio> claseResultado() {
        return CategoriaSocio.class;
    }

    /**
     * Condición {@code activa = true}, orden por nombre.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE categoria_socio.activa = true ORDER BY categoria_socio.nombre";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}

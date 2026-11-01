package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.List;

/**
 * Socios activos con esa categoría. {@code ServicioSocios.modificarCategoria} la usa con {@code contar} para impedir
 * desactivar una categoría asignada a socios activos (CU-06 paso 8, FA4).
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code Socio} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-06
 */
public class SociosActivosDeCategoria extends ConsultaEntidades<Socio> {

    /** Categoría. */
    private final Long idCategoria;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-06
     * @param idCategoria categoría
     */
    public SociosActivosDeCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code Socio}.
     *
     * <p>Casos de uso: CU-06
     * @return {@code Socio.class}
     */
    @Override
    public Class<Socio> claseResultado() {
        return Socio.class;
    }

    /**
     * Condición por categoría y cuenta activa.
     *
     * <p>Casos de uso: CU-06
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE socio.categoria_id = ? AND usuario.estado = 'ACTIVA'";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-06
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(idCategoria);
    }
}

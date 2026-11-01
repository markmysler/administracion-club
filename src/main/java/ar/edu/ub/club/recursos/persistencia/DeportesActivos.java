package ar.edu.ub.club.recursos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.recursos.dominio.Deporte;
import java.util.List;

/**
 * Deportes activos ordenados por nombre (07C, reemplaza a {@code RepositorioDeportes.buscarActivos()}); la usan
 * otros módulos que solo ofrecen deportes que admiten reservas (por ejemplo, reportes).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-21, CU-26, CU-38
 */
public class DeportesActivos extends ConsultaEntidades<Deporte> {

    /**
     * Crea la consulta (sin parámetros).
     *
     * <p>Casos de uso: CU-21, CU-26, CU-38
     */
    public DeportesActivos() {
        super();
    }

    /**
     * Clase de los objetos que devuelve la consulta (la resuelve {@code BrokerDeporte}).
     *
     * <p>Casos de uso: CU-21, CU-26, CU-38
     * @return {@code Deporte.class}
     */
    @Override
    public Class<Deporte> claseResultado() {
        return Deporte.class;
    }

    /**
     * Condición y orden que se agregan a {@code SELECT * FROM deporte}.
     *
     * <p>Casos de uso: CU-21, CU-26, CU-38
     * @return SQL sin parámetros
     */
    @Override
    public String sql() {
        return "WHERE deporte.activo = true ORDER BY deporte.nombre";
    }

    /**
     * Sin parámetros.
     *
     * <p>Casos de uso: CU-21, CU-26, CU-38
     * @return lista vacía
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}

package ar.edu.ub.club.recursos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.recursos.dominio.Deporte;
import java.util.List;

/**
 * Todos los deportes (activos e inactivos) ordenados por nombre; la usa {@code ServicioRecursos.listarDeportes}
 * para P-15, P-16 y P-19.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26, CU-38
 */
public class Deportes extends ConsultaEntidades<Deporte> {

    /**
     * Crea la consulta (sin parámetros).
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26, CU-38
     */
    public Deportes() {
        super();
    }

    /**
     * Clase de los objetos que devuelve la consulta (la resuelve {@code BrokerDeporte}).
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26, CU-38
     * @return {@code Deporte.class}
     */
    @Override
    public Class<Deporte> claseResultado() {
        return Deporte.class;
    }

    /**
     * Orden que se agrega a {@code SELECT * FROM deporte}.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26, CU-38
     * @return SQL sin parámetros
     */
    @Override
    public String sql() {
        return "ORDER BY deporte.nombre";
    }

    /**
     * Sin parámetros.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26, CU-38
     * @return lista vacía
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}

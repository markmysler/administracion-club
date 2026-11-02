package ar.edu.ub.club.agenda.persistencia;

import ar.edu.ub.club.agenda.dominio.Feriado;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * Todos los feriados del club ordenados por fecha; la usa {@code BrokerDisponibilidadClub.cargarAsociaciones}
 * para completar {@code DisponibilidadClub.feriados}.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
 */
public class Feriados extends ConsultaEntidades<Feriado> {

    /**
     * Crea la consulta (sin parámetros).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     */
    public Feriados() {
        super();
    }

    /**
     * Clase de los objetos que devuelve la consulta (la resuelve {@code BrokerFeriado}).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return {@code Feriado.class}
     */
    @Override
    public Class<Feriado> claseResultado() {
        return Feriado.class;
    }

    /**
     * Orden que se agrega a {@code SELECT * FROM feriado}.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return SQL sin parámetros
     */
    @Override
    public String sql() {
        return "ORDER BY feriado.fecha";
    }

    /**
     * Sin parámetros.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return lista vacía
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}

package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.reservas.dominio.Reserva;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: todos
 */
public class ReservasQueSeSuperponen extends ConsultaEntidades<Reserva> {

    /** Atributo idCancha. */
    private Long idCancha;

    /** Atributo turno. */
    private Turno turno;

    /** Atributo idExcluida. */
    private Long idExcluida;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    public Class<Reserva> claseResultado() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    @Override
    public String sql() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

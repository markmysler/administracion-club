package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.reservas.dominio.Reserva;
import java.util.List;

/**
 * Reservas CONFIRMADAS de la cancha cuyo horario se superpone con el turno (intervalos semiabiertos), sin la
 * excluida. Definida en 07C (consulta 32); el diseño 6.0 no la usa: la cadena trabaja con los turnos ocupados y la
 * base revalida con la restricción de exclusión.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve {@code BrokerReserva}:
 * {@code sql()} se agrega a su {@code consultaBase()} (JOIN con usuario, socio y cancha).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ReservasQueSeSuperponen extends ConsultaEntidades<Reserva> {

    /** Cancha. */
    private final Long idCancha;

    /** Turno a comparar. */
    private final Turno turno;

    /** Reserva que no se considera (la que se modifica) o null. */
    private final Long idExcluida;

    /**
     * Crea la consulta; los parámetros se reciben por constructor y no cambian (Query Object).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param idCancha cancha
     * @param turno turno a comparar
     * @param idExcluida reserva que no se considera (la que se modifica) o null
     */
    public ReservasQueSeSuperponen(Long idCancha, Turno turno, Long idExcluida) {
        this.idCancha = idCancha;
        this.turno = turno;
        this.idExcluida = idExcluida;
    }

    /**
     * Clase que resuelve {@code BrokerReserva}.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return {@code Reserva}
     */
    @Override
    public Class<Reserva> claseResultado() {
        return Reserva.class;
    }

    /**
     * Condición y orden que se agregan a {@code BrokerReserva.consultaBase()}.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return WHERE y ORDER BY con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE reserva.cancha_id = ? AND reserva.estado = 'CONFIRMADA' AND reserva.fecha = ?"
                + " AND reserva.hora_inicio < ? AND reserva.hora_fin > ?"
                + (idExcluida == null ? "" : " AND reserva.id <> ?") + " ORDER BY reserva.hora_inicio";
    }

    /**
     * Valores de los parámetros en el orden de los {@code ?}.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        return idExcluida == null
                ? List.of(idCancha, turno.getFecha(), turno.getHoraFin(), turno.getHoraInicio())
                : List.of(idCancha, turno.getFecha(), turno.getHoraFin(), turno.getHoraInicio(), idExcluida);
    }
}

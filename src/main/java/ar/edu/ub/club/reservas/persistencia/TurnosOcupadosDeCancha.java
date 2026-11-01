package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.time.LocalDate;
import java.util.List;

/**
 * Turnos de las reservas CONFIRMADAS de una cancha en una fecha (grilla de disponibilidad de CU-26 y {@code
 * ValidadorTurnoLibre}). La usa {@code ConsultaTurnosOcupadosReservas}.
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker.
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
 */
public class TurnosOcupadosDeCancha extends ConsultaProyeccion<Turno> {

    /** Cancha. */
    private final Long idCancha;

    /** Fecha. */
    private final LocalDate fecha;

    /**
     * Crea la consulta; los parámetros se reciben por constructor (Query Object).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param idCancha cancha
     * @param fecha fecha
     */
    public TurnosOcupadosDeCancha(Long idCancha, LocalDate fecha) {
        this.idCancha = idCancha;
        this.fecha = fecha;
    }

    /**
     * Tipo de cada fila.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @return {@code Turno}
     */
    @Override
    public Class<Turno> claseResultado() {
        return Turno.class;
    }

    /**
     * Convierte una fila en un {@code Turno}.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param fila fila del resultado
     * @return {@code new Turno(fecha, hora_inicio, hora_fin)}
     */
    @Override
    public Turno leerFila(FilaResultado fila) {
        return new Turno(fila.fecha("fecha"), fila.hora("hora_inicio"), fila.hora("hora_fin"));
    }

    /**
     * Sentencia completa.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @return SELECT de los turnos confirmados de la cancha en la fecha
     */
    @Override
    public String sql() {
        return "SELECT reserva.fecha, reserva.hora_inicio, reserva.hora_fin FROM reserva"
                + " WHERE reserva.cancha_id = ? AND reserva.fecha = ? AND reserva.estado = 'CONFIRMADA'"
                + " ORDER BY reserva.hora_inicio";
    }

    /**
     * Valores de los parámetros.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @return {@code [idCancha, fecha]}
     */
    @Override
    public List<Object> parametros() {
        return List.of(idCancha, fecha);
    }
}

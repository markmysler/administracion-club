package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.reservas.dominio.Reserva;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Reservas CONFIRMADAS cuyo turno ya terminó (fecha anterior, o la de hoy con la hora de fin cumplida); las
 * pasa a FINALIZADA {@code ServicioReservas.finalizarReservasCumplidas}.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve {@code BrokerReserva}:
 * {@code sql()} se agrega a su {@code consultaBase()} (JOIN con usuario, socio y cancha).
 *
 * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
 */
public class ReservasCumplidas extends ConsultaEntidades<Reserva> {

    /** Momento de la ejecución de la tarea. */
    private final LocalDateTime ahora;

    /**
     * Crea la consulta; los parámetros se reciben por constructor y no cambian (Query Object).
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     * @param ahora momento de la ejecución de la tarea
     */
    public ReservasCumplidas(LocalDateTime ahora) {
        this.ahora = ahora;
    }

    /**
     * Clase que resuelve {@code BrokerReserva}.
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     * @return {@code Reserva}
     */
    @Override
    public Class<Reserva> claseResultado() {
        return Reserva.class;
    }

    /**
     * Condición y orden que se agregan a {@code BrokerReserva.consultaBase()}.
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     * @return WHERE y ORDER BY con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE reserva.estado = 'CONFIRMADA' AND (reserva.fecha < ? OR (reserva.fecha = ? AND reserva.hora_fin <= ?))"
                + " ORDER BY reserva.fecha, reserva.hora_inicio";
    }

    /**
     * Valores de los parámetros en el orden de los {@code ?}.
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(ahora.toLocalDate(), ahora.toLocalDate(), ahora.toLocalTime());
    }
}

package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.reservas.dominio.Reserva;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Reservas CONFIRMADAS futuras de las canchas del deporte, que cancela la baja del deporte (RN-16).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve {@code BrokerReserva}:
 * {@code sql()} se agrega a su {@code consultaBase()} (JOIN con usuario, socio y cancha).
 *
 * <p>Casos de uso: CU-20
 */
public class ReservasFuturasDeDeporte extends ConsultaEntidades<Reserva> {

    /** Deporte dado de baja. */
    private final Long idDeporte;

    /** Momento a partir del cual una reserva es futura. */
    private final LocalDateTime ahora;

    /**
     * Crea la consulta; los parámetros se reciben por constructor y no cambian (Query Object).
     *
     * <p>Casos de uso: CU-20
     * @param idDeporte deporte dado de baja
     * @param ahora momento a partir del cual una reserva es futura
     */
    public ReservasFuturasDeDeporte(Long idDeporte, LocalDateTime ahora) {
        this.idDeporte = idDeporte;
        this.ahora = ahora;
    }

    /**
     * Clase que resuelve {@code BrokerReserva}.
     *
     * <p>Casos de uso: CU-20
     * @return {@code Reserva}
     */
    @Override
    public Class<Reserva> claseResultado() {
        return Reserva.class;
    }

    /**
     * Condición y orden que se agregan a {@code BrokerReserva.consultaBase()}.
     *
     * <p>Casos de uso: CU-20
     * @return WHERE y ORDER BY con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE cancha.deporte_id = ? AND reserva.estado = 'CONFIRMADA' AND (reserva.fecha > ? OR (reserva.fecha = ? AND reserva.hora_inicio > ?))"
                + " ORDER BY reserva.fecha, reserva.hora_inicio";
    }

    /**
     * Valores de los parámetros en el orden de los {@code ?}.
     *
     * <p>Casos de uso: CU-20
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(idDeporte, ahora.toLocalDate(), ahora.toLocalDate(), ahora.toLocalTime());
    }
}

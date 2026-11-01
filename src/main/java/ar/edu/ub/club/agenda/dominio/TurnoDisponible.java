package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.recursos.dominio.Cancha;

/**
 * Elemento de la grilla de turnos (resultado de {@code generarTurnos}).
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-26
 */
public class TurnoDisponible {

    /** Cancha. */
    private Cancha cancha;

    /** Fecha y horario. */
    private Turno turno;

    /** Disponibilidad del turno. */
    private EstadoTurno estado;

    /** Apellido y nombre del socio de la reserva que ocupa el turno; solo en OCUPADO. El presentador lo muestra solo a quien tiene GESTIONAR_RESERVAS_CLUB (RN-20). */
    private String ocupadoPor;

    /**
     * Crea una instancia de {@code TurnoDisponible}.
     *
     * <p>Casos de uso: CU-26
     * @param cancha valor de {@code cancha}
     * @param turno valor de {@code turno}
     * @param estado valor de {@code estado}
     */
    public TurnoDisponible(Cancha cancha, Turno turno, EstadoTurno estado) {
        // PENDIENTE
    }

    /**
     * Devuelve {@code cancha}: Cancha.
     *
     * <p>Casos de uso: CU-26
     * @return el valor del atributo
     */
    public Cancha getCancha() {
        return cancha;
    }

    /**
     * Devuelve {@code turno}: Fecha y horario.
     *
     * <p>Casos de uso: CU-26
     * @return el valor del atributo
     */
    public Turno getTurno() {
        return turno;
    }

    /**
     * Devuelve {@code estado}: Disponibilidad del turno.
     *
     * <p>Casos de uso: CU-26
     * @return el valor del atributo
     */
    public EstadoTurno getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code ocupadoPor}: Apellido y nombre del socio de la reserva que ocupa el turno; solo en OCUPADO. El presentador lo muestra solo a quien tiene GESTIONAR_RESERVAS_CLUB (RN-20).
     *
     * <p>Casos de uso: CU-26
     * @return el valor del atributo
     */
    public String getOcupadoPor() {
        return ocupadoPor;
    }
}

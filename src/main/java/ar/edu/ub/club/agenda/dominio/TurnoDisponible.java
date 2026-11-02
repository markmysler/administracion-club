package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.recursos.dominio.Cancha;

/**
 * Elemento de la grilla de turnos (resultado de {@code CalendarioTurnos.generarTurnos}): una cancha, un turno y
 * su estado (LIBRE, OCUPADO o NO_DISPONIBLE). Objeto de datos del dominio de agenda que el presentador de P-19
 * convierte en {@code FilaTurno} con {@code FilaTurno.desde(t, verOcupante)}.
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
 */
public class TurnoDisponible {

    /** Cancha. */
    private final Cancha cancha;

    /** Fecha y horario. */
    private final Turno turno;

    /** Disponibilidad del turno. */
    private final EstadoTurno estado;

    /** Apellido y nombre del socio de la reserva que ocupa el turno; solo en OCUPADO. El presentador lo muestra solo a quien tiene GESTIONAR_RESERVAS_CLUB (RN-20). */
    private final String ocupadoPor;

    /**
     * Crea el elemento de la grilla; {@code ocupadoPor} queda en null porque {@code ConsultaTurnosOcupados}
     * devuelve solo {@code Turno}, sin el socio (ver «Cambios respecto del diseño»).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param cancha cancha del turno
     * @param turno fecha y horario
     * @param estado estado calculado por {@code CalendarioTurnos}
     */
    public TurnoDisponible(Cancha cancha, Turno turno, EstadoTurno estado) {
        this.cancha = cancha;
        this.turno = turno;
        this.estado = estado;
        this.ocupadoPor = null;
    }

    /**
     * Devuelve {@code cancha}: Cancha.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public Cancha getCancha() {
        return cancha;
    }

    /**
     * Devuelve {@code turno}: Fecha y horario.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public Turno getTurno() {
        return turno;
    }

    /**
     * Devuelve {@code estado}: Disponibilidad del turno.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public EstadoTurno getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code ocupadoPor}: Apellido y nombre del socio de la reserva que ocupa el turno; solo en OCUPADO. El presentador lo muestra solo a quien tiene GESTIONAR_RESERVAS_CLUB (RN-20).
     *
     * <p>Casos de uso: CU-26, CU-28, CU-32
     * @return el valor del atributo
     */
    public String getOcupadoPor() {
        return ocupadoPor;
    }
}

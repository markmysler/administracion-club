package ar.edu.ub.club.agenda.dominio;

/**
 * Enumeración del estado de un turno de la grilla ({@code TurnoDisponible.estado}) que arma
 * {@code CalendarioTurnos.generarTurnos}: libre para reservar, ocupado por una reserva confirmada (RN-12) o no
 * disponible porque no cumple la anticipación mínima para reservar (RN-13).
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
 */
public enum EstadoTurno {

    /** El turno se puede reservar. */
    LIBRE,
    /** Se superpone con una reserva confirmada de la cancha (RN-12). */
    OCUPADO,
    /** No cumple la anticipación mínima para reservar (RN-13). */
    NO_DISPONIBLE;
}

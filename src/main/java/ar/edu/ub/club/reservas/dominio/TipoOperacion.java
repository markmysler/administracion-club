package ar.edu.ub.club.reservas.dominio;

/**
 * Enumeración del tipo de solicitud de reserva que recorre la cadena de validadores.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public enum TipoOperacion {

    /** Reserva nueva (CU-27, CU-28). */
    ALTA,
    /** Cambio de cancha o turno de una reserva existente (CU-32). */
    MODIFICACION;
}

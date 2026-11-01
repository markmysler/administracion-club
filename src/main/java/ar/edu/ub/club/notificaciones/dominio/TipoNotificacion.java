package ar.edu.ub.club.notificaciones.dominio;

/**
 * Tipo de una {@code Notificacion}; coincide con {@code EventoReserva.accion()} del evento que la originó. Se guarda
 * por nombre en {@code notificacion.tipo}.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
 */
public enum TipoNotificacion {

    /** Reserva confirmada (CU-27, CU-28). */
    CONFIRMACION,
    /** Reserva modificada (CU-32). */
    MODIFICACION,
    /** Reserva cancelada (CU-30, CU-33 y bajas de RN-16). */
    CANCELACION;
}

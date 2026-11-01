package ar.edu.ub.club.reservas.dominio;

/**
 * Enumeración que indica quién origina una solicitud de reserva (RN-11, CU-32). {@code ValidadorSocioHabilitado}
 * la usa (a través de {@code SolicitudReserva.controlaDeuda()}) para omitir el control de deuda en una modificación
 * del administrador y para elegir el texto del mensaje de deuda.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public enum OrigenSolicitud {

    /** El socio reserva en línea (CU-27). */
    SOCIO,
    /** Un administrador registra o modifica (CU-28, CU-32). */
    ADMINISTRADOR;
}

package ar.edu.ub.club.pagos.web;

/**
 * Cuerpo JSON de la notificación de la pasarela que recibe el webhook.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class NotificacionPasarelaRequest {

    /** Tipo de evento informado. */
    private String tipo;

    /** Identificador del pago en la pasarela. */
    private String idPagoPasarela;
}

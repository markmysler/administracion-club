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

    /**
     * Devuelve {@code tipo}: Tipo de evento informado.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Devuelve {@code idPagoPasarela}: Identificador del pago en la pasarela.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getIdPagoPasarela() {
        return idPagoPasarela;
    }
}

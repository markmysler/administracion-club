package ar.edu.ub.club.pagos.web;

import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;

/**
 * Controlador REST que recibe las notificaciones de la pasarela de pago (AC-03). Representa el evento de sistema
 * externo «la pasarela informa un pago». Valida la firma de la notificación y, aun con firma válida, no confía
 * en su contenido: solo extrae el identificador de pago y la fachada consulta el resultado real del lado del
 * servidor.
 *
 * <p>Casos de uso: CU-35
 */
public class WebhookPasarelaController {

    /** Fachada del módulo pagos. */
    private ServicioPagos servicioPagos;

    /** Clave secreta del webhook de Mercado Pago, leída de una variable de entorno (no se guarda en la base). */
    private String claveSecretaWebhook;

    /**
     * Verifica la firma HMAC-SHA256 de la cabecera {@code x-signature} calculada con la clave secreta sobre el
     * identificador del pago, {@code x-request-id} y la marca de tiempo; si es inválida responde 401 y descarta la
     * notificación. Si es válida, extrae el identificador de pago de la pasarela y delega la confirmación. Responde
     * 200 también ante una notificación repetida (idempotencia); ante un error transitorio responde 5xx para que la
     * pasarela reintente.
     *
     * <p>Casos de uso: CU-35
     * @param notificacion Tipo de evento e identificador del pago en la pasarela.
     * @param firma Cabecera {@code x-signature} (marca de tiempo y firma).
     * @param idSolicitud Cabecera {@code x-request-id}.
     */
    public void recibirNotificacion(NotificacionPasarelaRequest notificacion, String firma, String idSolicitud) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

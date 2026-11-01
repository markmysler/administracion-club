package ar.edu.ub.club.pagos.web;

import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que recibe las notificaciones de la pasarela de pago (AC-03). Representa el evento de sistema
 * externo «la pasarela informa un pago». Valida la firma de la notificación y, aun con firma válida, no confía
 * en su contenido: solo extrae el identificador de pago y la fachada consulta el resultado real del lado del
 * servidor.
 *
 * <p>Ruta pública ({@code POST /api/pasarela/notificaciones}) sin sesión y sin CSRF ({@code ConfiguracionSeguridad}).
 * La clave del webhook es {@code club.pasarela.mercadopago.clave-webhook} (variable {@code MP_CLAVE_WEBHOOK});
 * sin clave toda notificación HTTP se rechaza con 401. En modo SIMULADA el simulador confirma en el mismo proceso
 * ({@code PasarelaSimuladaAdapter.resolverPago}).
 *
 * <p>Casos de uso: CU-35
 */
@RestController
public class WebhookPasarelaController {

    /** Fachada del módulo pagos. */
    private final ServicioPagos servicioPagos;

    /** Clave secreta del webhook de Mercado Pago, leída de una variable de entorno (no se guarda en la base). */
    private final String claveSecretaWebhook;

    /**
     * Crea el controlador.
     *
     * <p>Casos de uso: CU-35
     * @param servicioPagos fachada de pagos
     * @param claveSecretaWebhook clave del webhook
     */
    public WebhookPasarelaController(ServicioPagos servicioPagos,
            @Value("${club.pasarela.mercadopago.clave-webhook:}") String claveSecretaWebhook) {
        this.servicioPagos = servicioPagos;
        this.claveSecretaWebhook = claveSecretaWebhook == null ? "" : claveSecretaWebhook.trim();
    }

    /**
     * Verifica la firma HMAC-SHA256 de la cabecera {@code x-signature} calculada con la clave secreta sobre el
     * identificador del pago, {@code x-request-id} y la marca de tiempo; si es inválida responde 401 y descarta la
     * notificación. Si es válida, extrae el identificador de pago de la pasarela y delega la confirmación. Responde
     * 200 también ante una notificación repetida (idempotencia); ante un error transitorio responde 5xx para que la
     * pasarela reintente. Un evento que no es de pago se ignora con 200.
     *
     * <p>Casos de uso: CU-35
     * @param notificacion Tipo de evento e identificador del pago en la pasarela.
     * @param firma Cabecera {@code x-signature} (ts=...,v1=...).
     * @param idSolicitud Cabecera {@code x-request-id}.
     * @throws AccesoDenegadoException si la firma falta o es inválida (401)
     * @throws IllegalArgumentException si falta el identificador del pago (400)
     */
    @PostMapping("/api/pasarela/notificaciones")
    @ResponseStatus(HttpStatus.OK)
    public void recibirNotificacion(@RequestBody NotificacionPasarelaRequest notificacion,
            @RequestHeader(value = "x-signature", required = false) String firma,
            @RequestHeader(value = "x-request-id", required = false) String idSolicitud) {
        if (notificacion.getIdPagoPasarela() == null || notificacion.getIdPagoPasarela().isBlank()) {
            throw new IllegalArgumentException("La notificación no trae el identificador del pago");
        }
        if (!firmaValida(notificacion.getIdPagoPasarela(), firma, idSolicitud)) {
            throw new AccesoDenegadoException("Firma de la notificación inválida");
        }
        if (notificacion.getTipo() != null && !"payment".equalsIgnoreCase(notificacion.getTipo())) {
            return;
        }
        servicioPagos.confirmarResultadoPasarela(notificacion.getIdPagoPasarela());
    }

    /**
     * Verifica la firma de Mercado Pago: {@code v1 = HMAC-SHA256(clave, "id:<id>;request-id:<x-request-id>;ts:<ts>;")}
     * en hexadecimal, comparada en tiempo constante.
     *
     * <p>Casos de uso: CU-35
     * @param idPago identificador del pago
     * @param firma cabecera x-signature
     * @param idSolicitud cabecera x-request-id
     * @return true si la firma coincide
     */
    boolean firmaValida(String idPago, String firma, String idSolicitud) {
        if (claveSecretaWebhook.isEmpty() || firma == null || idSolicitud == null) {
            return false;
        }
        String ts = null;
        String v1 = null;
        for (String parte : firma.split(",")) {
            String[] kv = parte.trim().split("=", 2);
            if (kv.length == 2 && "ts".equals(kv[0])) {
                ts = kv[1];
            } else if (kv.length == 2 && "v1".equals(kv[0])) {
                v1 = kv[1];
            }
        }
        if (ts == null || v1 == null) {
            return false;
        }
        String manifiesto = "id:" + idPago.toLowerCase() + ";request-id:" + idSolicitud + ";ts:" + ts + ";";
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(claveSecretaWebhook.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            String esperada = HexFormat.of().formatHex(mac.doFinal(manifiesto.getBytes(StandardCharsets.UTF_8)));
            return MessageDigest.isEqual(esperada.getBytes(StandardCharsets.UTF_8), v1.toLowerCase().getBytes(StandardCharsets.UTF_8));
        } catch (java.security.GeneralSecurityException ex) {
            return false;
        }
    }
}

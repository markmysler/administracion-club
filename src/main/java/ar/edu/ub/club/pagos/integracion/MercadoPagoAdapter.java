package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Adapta la API REST de Mercado Pago (Checkout Pro) al puerto {@code PasarelaPago}: crea la preferencia ({@code
 * POST /checkout/preferences}), consulta el pago del lado del servidor ({@code GET /v1/payments/{id}}) y valida
 * las credenciales ({@code GET /users/me}). Usa el token {@code club.pasarela.mercadopago.access-token} (variable
 * de entorno {@code MP_ACCESS_TOKEN}); sin token queda deshabilitado: {@code validarConfiguracion} devuelve false
 * y la pasarela no puede habilitarse en modo MERCADO_PAGO. En la demostración académica no se usa.
 *
 * <p>Adaptador concreto del patrón Adapter (GoF).
 *
 * <p>Casos de uso: CU-34, CU-35; tareas programadas: vencimiento de pagos online pendientes
 */
@Component
public class MercadoPagoAdapter implements PasarelaPago {

    /** Bitácora técnica. */
    private static final Logger BITACORA = LoggerFactory.getLogger(MercadoPagoAdapter.class);

    /** URL base de la API de Mercado Pago. */
    private static final String API = "https://api.mercadopago.com";

    /** Formato de fecha con zona que exige la API. */
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    /** Token de acceso (vacío = adaptador deshabilitado). */
    private final String accessToken;

    /** Cliente HTTP. */
    private final RestClient cliente;

    /**
     * Crea el adaptador con el token de la configuración.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @param accessToken token de acceso de Mercado Pago (puede estar vacío)
     */
    public MercadoPagoAdapter(@Value("${club.pasarela.mercadopago.access-token:}") String accessToken) {
        this.accessToken = accessToken == null ? "" : accessToken.trim();
        this.cliente = RestClient.builder().baseUrl(API).build();
    }

    /**
     * Agregado en la implementación: indica si el entorno tiene token configurado
     * ({@code ConfiguracionPasarela.credencialesConfiguradas}).
     *
     * <p>Casos de uso: CU-34
     * @return true si hay token
     */
    public boolean tieneCredenciales() {
        return !accessToken.isEmpty();
    }

    /**
     * Crea la preferencia de Checkout Pro con la referencia externa, el ítem (concepto e importe en ARS), las
     * direcciones de retorno y de notificación y el vencimiento; devuelve su id y el {@code init_point}.
     *
     * <p>Casos de uso: CU-35
     * @param solicitud Datos del pago.
     * @return intención creada
     * @throws IllegalStateException si no hay credenciales o la API falla (CU-35 FA6)
     */
    @Override
    @SuppressWarnings("unchecked")
    public IntencionPago crearIntencionPago(SolicitudPagoOnline solicitud) {
        exigirCredenciales();
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("title", solicitud.getConcepto());
        item.put("quantity", 1);
        item.put("currency_id", "ARS");
        item.put("unit_price", solicitud.getImporte().getMonto());
        Map<String, Object> retorno = new LinkedHashMap<>();
        retorno.put("success", solicitud.getUrlRetorno());
        retorno.put("failure", solicitud.getUrlRetorno());
        retorno.put("pending", solicitud.getUrlRetorno());
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("items", List.of(item));
        cuerpo.put("external_reference", solicitud.getReferenciaExterna().toString());
        cuerpo.put("back_urls", retorno);
        cuerpo.put("auto_return", "approved");
        cuerpo.put("notification_url", solicitud.getUrlNotificacion());
        cuerpo.put("expires", true);
        cuerpo.put("expiration_date_to", solicitud.getVencimiento().atZone(RelojSistema.ZONA_CLUB).format(FORMATO_FECHA));
        try {
            Map<String, Object> respuesta = cliente.post().uri("/checkout/preferences")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON).body(cuerpo).retrieve().body(Map.class);
            if (respuesta == null || respuesta.get("id") == null) {
                throw new IllegalStateException("Mercado Pago no devolvió la preferencia");
            }
            return new IntencionPago(respuesta.get("id").toString(), String.valueOf(respuesta.get("init_point")));
        } catch (RestClientException ex) {
            throw new IllegalStateException("Mercado Pago rechazó la creación de la preferencia", ex);
        }
    }

    /**
     * Consulta el pago del lado del servidor y traduce su {@code status}: approved → APROBADO; rejected,
     * cancelled, refunded, charged_back → RECHAZADO; el resto → Pendiente (Adapter).
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param idPagoPasarela Identificador del pago en Mercado Pago.
     * @return resultado, o null si el pago no existe
     * @throws IllegalStateException si no hay credenciales o la API falla (error transitorio: el webhook responde 5xx)
     */
    @Override
    @SuppressWarnings("unchecked")
    public ResultadoPasarela obtenerResultado(String idPagoPasarela) {
        exigirCredenciales();
        try {
            Map<String, Object> pago = cliente.get().uri("/v1/payments/{id}", idPagoPasarela)
                    .header("Authorization", "Bearer " + accessToken).retrieve().body(Map.class);
            if (pago == null) {
                return null;
            }
            String referencia = pago.get("external_reference") == null ? null : pago.get("external_reference").toString();
            Object monto = pago.get("transaction_amount");
            Dinero importe = monto == null ? null : Dinero.de(new BigDecimal(monto.toString()));
            Object detalle = pago.get("status_detail");
            return new ResultadoPasarela(idPagoPasarela, uuidOnull(referencia), traducirEstado(String.valueOf(pago.get("status"))),
                    importe, detalle == null ? null : detalle.toString());
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound ex) {
            return null;
        } catch (RestClientException ex) {
            throw new IllegalStateException("No se pudo consultar el pago en Mercado Pago", ex);
        }
    }

    /**
     * Valida las credenciales con {@code GET /users/me}: sin token o con un token rechazado devuelve false.
     *
     * <p>Casos de uso: CU-34
     * @param config Configuración a validar.
     * @return true si Mercado Pago acepta el token
     */
    @Override
    public boolean validarConfiguracion(ConfiguracionPasarela config) {
        if (!tieneCredenciales()) {
            return false;
        }
        try {
            cliente.get().uri("/users/me").header("Authorization", "Bearer " + accessToken).retrieve().toBodilessEntity();
            return true;
        } catch (RestClientException ex) {
            BITACORA.warn("Mercado Pago rechazó las credenciales configuradas", ex);
            return false;
        }
    }

    /**
     * Traduce el estado de Mercado Pago al del sistema.
     *
     * <p>Casos de uso: CU-35
     * @param status estado de Mercado Pago
     * @return APROBADO, RECHAZADO o Pendiente
     */
    static String traducirEstado(String status) {
        return switch (status) {
            case "approved" -> ResultadoPasarela.APROBADO;
            case "rejected", "cancelled", "refunded", "charged_back" -> ResultadoPasarela.RECHAZADO;
            default -> ResultadoPasarela.PENDIENTE;
        };
    }

    /**
     * Convierte la referencia externa en UUID (null si no lo es).
     *
     * <p>Casos de uso: CU-35
     * @param texto referencia
     * @return UUID o null
     */
    private static UUID uuidOnull(String texto) {
        try {
            return texto == null ? null : UUID.fromString(texto);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Falla si el adaptador está deshabilitado (sin token).
     *
     * <p>Casos de uso: CU-35
     * @throws IllegalStateException sin credenciales
     */
    private void exigirCredenciales() {
        if (!tieneCredenciales()) {
            throw new IllegalStateException("Mercado Pago no está configurado (falta MP_ACCESS_TOKEN)");
        }
    }
}

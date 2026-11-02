package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Parámetro de {@code PasarelaPago.crearIntencionPago}: lo arma {@code ServicioPagos.iniciarPagoOnline} con la
 * referencia externa, el concepto, el importe a pagar, las direcciones de retorno y de notificación y el
 * vencimiento de 24 horas (RN-22).
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-35
 */
public class SolicitudPagoOnline {

    /** Referencia del pago. */
    private final UUID referenciaExterna;

    /** Concepto (cuota y período). */
    private final String concepto;

    /** Importe a pagar. */
    private final Dinero importe;

    /** Ruta pública de retorno https://&lt;club&gt;/pagos/retorno?referencia={uuid}, que lleva a P-26. */
    private final String urlRetorno;

    /** URL del webhook. */
    private final String urlNotificacion;

    /** Vencimiento de la intención. */
    private final LocalDateTime vencimiento;

    /**
     * Agregado en la implementación (el modelo no lista el constructor): crea la solicitud.
     *
     * <p>Casos de uso: CU-35
     * @param referenciaExterna Referencia.
     * @param concepto Concepto.
     * @param importe Importe.
     * @param urlRetorno Retorno del navegador.
     * @param urlNotificacion Webhook.
     * @param vencimiento Vencimiento.
     */
    public SolicitudPagoOnline(UUID referenciaExterna, String concepto, Dinero importe, String urlRetorno, String urlNotificacion, LocalDateTime vencimiento) {
        this.referenciaExterna = referenciaExterna;
        this.concepto = concepto;
        this.importe = importe;
        this.urlRetorno = urlRetorno;
        this.urlNotificacion = urlNotificacion;
        this.vencimiento = vencimiento;
    }

    /**
     * Devuelve {@code referenciaExterna}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public UUID getReferenciaExterna() {
        return referenciaExterna;
    }

    /**
     * Devuelve {@code concepto}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * Devuelve {@code importe}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code urlRetorno}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getUrlRetorno() {
        return urlRetorno;
    }

    /**
     * Devuelve {@code urlNotificacion}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getUrlNotificacion() {
        return urlNotificacion;
    }

    /**
     * Devuelve {@code vencimiento}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public LocalDateTime getVencimiento() {
        return vencimiento;
    }
}

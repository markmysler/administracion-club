package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Parámetro de {@code PasarelaPago.crearIntencionPago}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-35
 */
public class SolicitudPagoOnline {

    /** Referencia del pago. */
    private UUID referenciaExterna;

    /** Concepto (cuota y período). */
    private String concepto;

    /** Importe a pagar. */
    private Dinero importe;

    /** Ruta pública de retorno https://&lt;club&gt;/pagos/retorno?referencia=(uuid}, que lleva a P-26. */
    private String urlRetorno;

    /** URL del webhook. */
    private String urlNotificacion;

    /** Vencimiento de la intención. */
    private LocalDateTime vencimiento;

    /**
     * Devuelve {@code referenciaExterna}: Referencia del pago.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public UUID getReferenciaExterna() {
        return referenciaExterna;
    }

    /**
     * Devuelve {@code concepto}: Concepto (cuota y período).
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * Devuelve {@code importe}: Importe a pagar.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code urlRetorno}: Ruta pública de retorno https://&lt;club&gt;/pagos/retorno?referencia=(uuid}, que lleva a P-26.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getUrlRetorno() {
        return urlRetorno;
    }

    /**
     * Devuelve {@code urlNotificacion}: URL del webhook.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getUrlNotificacion() {
        return urlNotificacion;
    }

    /**
     * Devuelve {@code vencimiento}: Vencimiento de la intención.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public LocalDateTime getVencimiento() {
        return vencimiento;
    }
}

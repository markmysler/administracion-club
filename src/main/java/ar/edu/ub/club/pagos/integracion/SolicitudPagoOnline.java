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
}

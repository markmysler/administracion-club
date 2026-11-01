package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import java.util.UUID;

/**
 * Modelo de vista (record inmutable) del estado de un pago online que muestra P-26 (MODELO 11.6.1). Se arma siempre
 * con el estado guardado en el servidor, nunca con los parámetros de la URL de retorno (RN-17).
 *
 * <p>Casos de uso: CU-35
 * @param referencia referencia externa del pago
 * @param estado código del estado (pendiente, aprobado, rechazado, acreditado o vencido)
 * @param importe importe del pago
 * @param periodoCuota período de la cuota
 * @param motivo motivo de rechazo o vencimiento, si hay
 * @param idComprobante comprobante o constancia, si hay
 */
public record ResumenPagoOnline(UUID referencia, String estado, Dinero importe, String periodoCuota, String motivo,
                                Long idComprobante) {

    /**
     * Fábrica a partir del pago online.
     *
     * <p>Casos de uso: CU-35
     * @param pago el pago online
     * @return el resumen
     */
    public static ResumenPagoOnline desde(PagoOnline pago) {
        return new ResumenPagoOnline(pago.getReferenciaExterna(),
                pago.getEstado() == null ? null : pago.getEstado().codigo(), pago.getImporte(),
                pago.getCuota() == null ? null : pago.getCuota().getPeriodo(), pago.getMotivoRechazo(),
                pago.getComprobante() == null ? null : pago.getComprobante().getId());
    }
}

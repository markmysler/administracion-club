package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import java.util.UUID;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-35
 */
public record ResumenPagoOnline(UUID referencia, String estado, Dinero importe, String periodoCuota, String motivo, Long idComprobante) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param pago valor de {@code pago}
     * @return el resultado
     */
    public static ResumenPagoOnline desde(PagoOnline pago) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

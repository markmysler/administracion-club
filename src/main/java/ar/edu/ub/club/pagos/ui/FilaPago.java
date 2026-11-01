package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: PENDIENTE
 */
public record FilaPago(Long id, LocalDateTime fecha, String periodoCuota, Long idGrupo, Dinero importe, String origen, String medio, String estado, String motivo, Long idComprobante, String numeroComprobante) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param pago valor de {@code pago}
     * @return el resultado
     */
    public static FilaPago desde(Pago pago) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

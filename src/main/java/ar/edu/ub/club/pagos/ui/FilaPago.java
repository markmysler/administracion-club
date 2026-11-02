package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.time.LocalDateTime;

/**
 * Modelo de vista (record inmutable) de una fila de la grilla de pagos de P-28 (MODELO 11.6.1).
 *
 * <p>Casos de uso: CU-05, CU-37
 * @param id id del pago
 * @param fecha fecha y hora del pago
 * @param periodoCuota período de la cuota (AAAA-MM)
 * @param idGrupo grupo familiar de la cuota
 * @param importe importe del pago
 * @param origen origen del pago ({@code Pago.origen()}: online, manual o con saldo a favor)
 * @param medio descripción del medio ({@code Pago.descripcionMedio()})
 * @param estado código del estado del pago
 * @param motivo motivo de rechazo o vencimiento, si hay
 * @param idComprobante id del comprobante o constancia, si hay (solo APROBADO o ACREDITADO)
 * @param numeroComprobante número del comprobante, si hay
 */
public record FilaPago(Long id, LocalDateTime fecha, String periodoCuota, Long idGrupo, Dinero importe,
                       String origen, String medio, String estado, String motivo, Long idComprobante,
                       String numeroComprobante) {

    /**
     * Fábrica: usa {@code origen()}, {@code descripcionMedio()} y {@code getComprobante()} del pago (polimorfismo
     * de las subclases de {@code Pago}).
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param pago el pago
     * @return la fila
     */
    public static FilaPago desde(Pago pago) {
        Comprobante comprobante = pago.getComprobante();
        return new FilaPago(pago.getId(), pago.getFecha(),
                pago.getCuota() == null ? null : pago.getCuota().getPeriodo(),
                pago.getCuota() == null ? null : pago.getCuota().getIdGrupo(),
                pago.getImporte(), pago.origen(), pago.descripcionMedio(),
                pago.getEstado() == null ? null : pago.getEstado().codigo(), pago.getMotivoRechazo(),
                comprobante == null ? null : comprobante.getId(),
                comprobante == null ? null : comprobante.getNumero());
    }
}

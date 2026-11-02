package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.time.LocalDate;
import java.util.List;

/**
 * Fila de una cuota en P-25 (estado de cuenta), P-30 (pago manual) y en los perfiles: período, importe, crédito
 * aplicado, importe a pagar, fechas, estado, si admite pago y si se ofrece «Pagar online» (titular, cuota impaga
 * y pasarela habilitada), con sus ítems.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-04, CU-05, CU-35, CU-36
 * @param id identificador de la cuota
 * @param periodo AAAA-MM
 * @param importe total
 * @param creditoAplicado saldo a favor descontado
 * @param importeAPagar importe menos crédito
 * @param fechaEmision emisión
 * @param fechaVencimiento vencimiento
 * @param estado código del estado
 * @param admitePago true si está Pendiente o Vencida
 * @param admitePagoOnline true si se muestra «Pagar online»
 * @param items aportes de los integrantes
 */
public record FilaCuota(Long id, String periodo, Dinero importe, Dinero creditoAplicado, Dinero importeAPagar, LocalDate fechaEmision, LocalDate fechaVencimiento, String estado, boolean admitePago, boolean admitePagoOnline, List<FilaItemCuota> items) {

    /**
     * Convierte la cuota en fila; {@code admitePagoOnline = pagoOnlinePermitido && cuota.admitePago()}.
     *
     * <p>Casos de uso: CU-04, CU-05, CU-35, CU-36
     * @param cuota Cuota (con los ítems ya cargados).
     * @param pagoOnlinePermitido titular y pasarela habilitada.
     * @return la fila
     */
    public static FilaCuota desde(Cuota cuota, boolean pagoOnlinePermitido) {
        List<FilaItemCuota> items = cuota.getItems() == null ? List.of()
                : cuota.getItems().stream().map(FilaItemCuota::desde).toList();
        return new FilaCuota(cuota.getId(), cuota.getPeriodo(), cuota.getImporte(), cuota.getCreditoAplicado(),
                cuota.importeAPagar(), cuota.getFechaEmision(), cuota.getFechaVencimiento(), cuota.getEstado().codigo(),
                cuota.admitePago(), pagoOnlinePermitido && cuota.admitePago(), items);
    }
}

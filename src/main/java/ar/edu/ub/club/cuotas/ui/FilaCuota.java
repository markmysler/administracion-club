package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.time.LocalDate;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-28, CU-35
 */
public record FilaCuota(Long id, String periodo, Dinero importe, Dinero creditoAplicado, Dinero importeAPagar, LocalDate fechaEmision, LocalDate fechaVencimiento, String estado, boolean admitePago, boolean admitePagoOnline, List<FilaItemCuota> items) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param cuota valor de {@code cuota}
     * @param pagoOnlinePermitido valor de {@code pagoOnlinePermitido}
     * @return el resultado
     */
    public static FilaCuota desde(Cuota cuota, boolean pagoOnlinePermitido) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import java.time.LocalDateTime;

/**
 * Movimiento del saldo a favor (crédito o débito) en P-24, P-25 y P-28.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-05, CU-37
 * @param fecha momento
 * @param tipo CREDITO o DEBITO
 * @param importe importe
 * @param motivo descripción
 */
public record FilaMovimientoSaldo(LocalDateTime fecha, String tipo, Dinero importe, String motivo) {

    /**
     * Convierte el movimiento en fila.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param movimiento movimiento de saldo
     * @return la fila
     */
    public static FilaMovimientoSaldo desde(MovimientoSaldo movimiento) {
        return new FilaMovimientoSaldo(movimiento.getFecha(), movimiento.getTipo().name(), movimiento.getImporte(),
                movimiento.getMotivo());
    }
}

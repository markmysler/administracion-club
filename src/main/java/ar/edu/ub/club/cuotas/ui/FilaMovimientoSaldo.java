package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: PENDIENTE
 */
public record FilaMovimientoSaldo(LocalDateTime fecha, String tipo, Dinero importe, String motivo) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param movimiento valor de {@code movimiento}
     * @return el resultado
     */
    public static FilaMovimientoSaldo desde(MovimientoSaldo movimiento) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

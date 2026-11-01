package ar.edu.ub.club.pagos.dominio;

import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
 */
public class EstadoPagoPendiente extends EstadoPago {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param pago valor de {@code pago}
     * @param ahora valor de {@code ahora}
     */
    public void aprobar(Pago pago, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param pago valor de {@code pago}
     * @param motivo valor de {@code motivo}
     */
    public void rechazar(Pago pago, String motivo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param pago valor de {@code pago}
     * @param ahora valor de {@code ahora}
     */
    public void acreditar(Pago pago, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param pago valor de {@code pago}
     * @param motivo valor de {@code motivo}
     */
    public void vencer(Pago pago, String motivo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @return el resultado
     */
    public String codigo() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

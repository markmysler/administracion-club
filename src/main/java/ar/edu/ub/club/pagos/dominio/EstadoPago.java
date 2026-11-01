package ar.edu.ub.club.pagos.dominio;

import java.time.LocalDateTime;

/**
 * Clase abstracta del patrón State para {@code Pago}, con subclases {@code EstadoPagoPendiente}, {@code
 * EstadoPagoAprobado}, {@code EstadoPagoRechazado}, {@code EstadoPagoAcreditado} y {@code EstadoPagoVencido}.
 * Las operaciones lanzan {@code TransicionInvalidaException} por omisión; {@code EstadoPagoPendiente} redefine
 * las cuatro y {@code EstadoPagoVencido} redefine {@code aprobar} y {@code acreditar}.
 *
 * <p>Casos de uso: CU-35
 */
public abstract class EstadoPago {

    /**
     * Transición a Aprobado.
     *
     * <p>Casos de uso: CU-35
     * @param pago valor de {@code pago}
     * @param ahora Momento.
     */
    public void aprobar(Pago pago, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Transición a Rechazado.
     *
     * <p>Casos de uso: CU-35
     * @param pago valor de {@code pago}
     * @param motivo Motivo.
     */
    public void rechazar(Pago pago, String motivo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Transición a Acreditado.
     *
     * <p>Casos de uso: CU-35
     * @param pago valor de {@code pago}
     * @param ahora Momento.
     */
    public void acreditar(Pago pago, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Código persistido.
     *
     * <p>Casos de uso: CU-35
     * @return PENDIENTE, APROBADO, RECHAZADO, ACREDITADO o VENCIDO.
     */
    public abstract String codigo();

    /**
     * Devuelve la instancia única del estado del código; un código desconocido lanza {@code IllegalStateException}.
     * La usa {@code BrokerPago.crearObjeto}.
     *
     * <p>Casos de uso: CU-35
     * @param codigo valor de {@code codigo}
     * @return el resultado
     */
    public static EstadoPago desdeCodigo(String codigo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Transición a Vencido.
     *
     * <p>Casos de uso: CU-35
     * @param pago valor de {@code pago}
     * @param motivo Motivo del vencimiento.
     */
    public void vencer(Pago pago, String motivo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException;
import java.time.LocalDateTime;

/**
 * Clase abstracta del patrón State para {@code Pago}, con subclases {@code EstadoPagoPendiente}, {@code
 * EstadoPagoAprobado}, {@code EstadoPagoRechazado}, {@code EstadoPagoAcreditado} y {@code EstadoPagoVencido}.
 * Las operaciones lanzan {@code TransicionInvalidaException} por omisión; {@code EstadoPagoPendiente} redefine
 * las cuatro y {@code EstadoPagoVencido} redefine {@code aprobar} y {@code acreditar} (diagrama {@code est_pago}:
 * Pendiente → Aprobado | Rechazado | Acreditado | Vencido; Vencido → Aprobado | Acreditado).
 *
 * <p>Cada estado concreto tiene una instancia única ({@code INSTANCIA}); en la base se guarda su {@code codigo()}.
 *
 * <p>Casos de uso: CU-35, CU-36, CU-37; tareas programadas: vencimiento de pagos online pendientes, emisión mensual de cuotas
 */
public abstract class EstadoPago {

    /**
     * Transición a Aprobado. Por omisión no está permitida.
     *
     * <p>Casos de uso: CU-35
     * @param pago Contexto del patrón State.
     * @param ahora Momento de la aprobación.
     * @throws TransicionInvalidaException si el estado actual no la admite
     */
    public void aprobar(Pago pago, LocalDateTime ahora) {
        throw invalida("aprobar");
    }

    /**
     * Transición a Rechazado. Por omisión no está permitida.
     *
     * <p>Casos de uso: CU-35
     * @param pago Contexto del patrón State.
     * @param motivo Motivo informado por la pasarela.
     * @throws TransicionInvalidaException si el estado actual no la admite
     */
    public void rechazar(Pago pago, String motivo) {
        throw invalida("rechazar");
    }

    /**
     * Transición a Acreditado. Por omisión no está permitida.
     *
     * <p>Casos de uso: CU-35
     * @param pago Contexto del patrón State.
     * @param ahora Momento de la acreditación.
     * @throws TransicionInvalidaException si el estado actual no la admite
     */
    public void acreditar(Pago pago, LocalDateTime ahora) {
        throw invalida("acreditar");
    }

    /**
     * Código persistido en {@code pago.estado}.
     *
     * <p>Casos de uso: todos
     * @return Pendiente, APROBADO, RECHAZADO, ACREDITADO o VENCIDO.
     */
    public abstract String codigo();

    /**
     * Devuelve la instancia única del estado del código; un código desconocido lanza {@code IllegalStateException}.
     * La usa {@code BrokerPago.crearObjeto}.
     *
     * <p>Casos de uso: todos
     * @param codigo Código guardado.
     * @return Estado concreto.
     * @throws IllegalStateException si el código no corresponde a ningún estado
     */
    public static EstadoPago desdeCodigo(String codigo) {
        String limpio = codigo == null ? "" : codigo.trim();
        return switch (limpio) {
            case EstadoPagoPendiente.CODIGO -> EstadoPagoPendiente.INSTANCIA;
            case EstadoPagoAprobado.CODIGO -> EstadoPagoAprobado.INSTANCIA;
            case EstadoPagoRechazado.CODIGO -> EstadoPagoRechazado.INSTANCIA;
            case EstadoPagoAcreditado.CODIGO -> EstadoPagoAcreditado.INSTANCIA;
            case EstadoPagoVencido.CODIGO -> EstadoPagoVencido.INSTANCIA;
            default -> throw new IllegalStateException("Estado de pago desconocido: " + codigo);
        };
    }

    /**
     * Transición a Vencido. Por omisión no está permitida.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param pago Contexto del patrón State.
     * @param motivo Motivo del vencimiento («Abandonado», RN-22).
     * @throws TransicionInvalidaException si el estado actual no la admite
     */
    public void vencer(Pago pago, String motivo) {
        throw invalida("vencer");
    }

    /**
     * Indica si el estado es final para la confirmación de la pasarela (Aprobado, Rechazado o Acreditado): una
     * notificación repetida sobre un pago en estado final no cambia nada (idempotencia, RN-18). Agregado en la
     * implementación.
     *
     * <p>Casos de uso: CU-35
     * @return true en Aprobado, Rechazado y Acreditado
     */
    public boolean esFinal() {
        return true;
    }

    /**
     * Arma la excepción de una transición no admitida.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: vencimiento de pagos online pendientes
     * @param operacion Transición pedida.
     * @return la excepción
     */
    private TransicionInvalidaException invalida(String operacion) {
        return new TransicionInvalidaException("No se puede " + operacion + " un pago en estado " + codigo() + ".");
    }

    /**
     * Representación legible: el código.
     *
     * <p>Casos de uso: todos
     * @return el código
     */
    @Override
    public String toString() {
        return codigo();
    }
}

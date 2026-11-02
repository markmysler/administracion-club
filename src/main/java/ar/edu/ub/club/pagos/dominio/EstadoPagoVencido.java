package ar.edu.ub.club.pagos.dominio;

import java.time.LocalDateTime;

/**
 * Pago online pendiente que nadie confirmó en 24 horas (motivo «Abandonado», RN-22). No es final: si la pasarela
 * informa después una aprobación, el pago se aprueba o se acredita igual, para que ningún dinero cobrado quede
 * sin registrar. No puede rechazarse ni volver a vencer.
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
 */
public class EstadoPagoVencido extends EstadoPago {

    /** Código persistido. */
    static final String CODIGO = "VENCIDO";

    /** Instancia única. */
    static final EstadoPagoVencido INSTANCIA = new EstadoPagoVencido();

    /**
     * Vencido → Aprobado (aprobación tardía de la pasarela); borra el motivo del vencimiento.
     *
     * <p>Casos de uso: CU-35
     * @param pago Contexto.
     * @param ahora Momento.
     */
    @Override
    public void aprobar(Pago pago, LocalDateTime ahora) {
        pago.asignarMotivo(null);
        pago.registrarMomento(ahora);
        pago.cambiarEstado(EstadoPagoAprobado.INSTANCIA);
    }

    /**
     * Vencido → Acreditado (aprobación tardía con la cuota ya pagada, RN-21).
     *
     * <p>Casos de uso: CU-35
     * @param pago Contexto.
     * @param ahora Momento.
     */
    @Override
    public void acreditar(Pago pago, LocalDateTime ahora) {
        pago.asignarMotivo(null);
        pago.registrarMomento(ahora);
        pago.cambiarEstado(EstadoPagoAcreditado.INSTANCIA);
    }

    /**
     * Vencido no es final (admite una aprobación tardía).
     *
     * <p>Casos de uso: CU-35
     * @return false
     */
    @Override
    public boolean esFinal() {
        return false;
    }

    /**
     * Código persistido.
     *
     * <p>Casos de uso: todos
     * @return "VENCIDO"
     */
    @Override
    public String codigo() {
        return CODIGO;
    }
}

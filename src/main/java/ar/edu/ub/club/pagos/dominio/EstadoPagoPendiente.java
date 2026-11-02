package ar.edu.ub.club.pagos.dominio;

import java.time.LocalDateTime;

/**
 * Estado inicial del pago online: creado al iniciar el pago (CU-35, paso 9) y a la espera del resultado de la
 * pasarela. Redefine las cuatro transiciones: aprobar, rechazar, acreditar (cuota ya pagada, RN-21) y vencer
 * (abandonado después de 24 horas, RN-22).
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
 */
public class EstadoPagoPendiente extends EstadoPago {

    /** Código persistido. */
    static final String CODIGO = "PENDIENTE";

    /** Instancia única. */
    static final EstadoPagoPendiente INSTANCIA = new EstadoPagoPendiente();

    /**
     * Pendiente → Aprobado; registra el momento de la aprobación.
     *
     * <p>Casos de uso: CU-35
     * @param pago Contexto.
     * @param ahora Momento.
     */
    @Override
    public void aprobar(Pago pago, LocalDateTime ahora) {
        pago.registrarMomento(ahora);
        pago.cambiarEstado(EstadoPagoAprobado.INSTANCIA);
    }

    /**
     * Pendiente → Rechazado con el motivo de la pasarela (la cuota sigue impaga, RN-17).
     *
     * <p>Casos de uso: CU-35
     * @param pago Contexto.
     * @param motivo Motivo.
     */
    @Override
    public void rechazar(Pago pago, String motivo) {
        pago.asignarMotivo(motivo);
        pago.cambiarEstado(EstadoPagoRechazado.INSTANCIA);
    }

    /**
     * Pendiente → Acreditado: la pasarela aprobó el pago pero la cuota ya estaba pagada (RN-21).
     *
     * <p>Casos de uso: CU-35
     * @param pago Contexto.
     * @param ahora Momento.
     */
    @Override
    public void acreditar(Pago pago, LocalDateTime ahora) {
        pago.registrarMomento(ahora);
        pago.cambiarEstado(EstadoPagoAcreditado.INSTANCIA);
    }

    /**
     * Pendiente → Vencido con el motivo «Abandonado» (RN-22).
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param pago Contexto.
     * @param motivo Motivo.
     */
    @Override
    public void vencer(Pago pago, String motivo) {
        pago.asignarMotivo(motivo);
        pago.cambiarEstado(EstadoPagoVencido.INSTANCIA);
    }

    /**
     * Pendiente no es un estado final.
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
     * @return el código de Pendiente
     */
    @Override
    public String codigo() {
        return CODIGO;
    }
}

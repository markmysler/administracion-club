package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.time.LocalDateTime;

/**
 * Clase abstracta especializada en {@code PagoOnline}, {@code PagoManual} y {@code PagoConSaldo}, creada por los
 * {@code ProcesadorPago} (Factory Method). Contexto del patrón State: los estados concretos cambian su estado
 * con {@code ~cambiarEstado(nuevo)}.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
 */
public abstract class Pago extends EntidadPersistente {

    /** Cuota que se paga. */
    private Cuota cuota;

    /** Momento del pago. */
    private LocalDateTime fecha;

    /** Importe. */
    private Dinero importe;

    /** Estado actual. */
    private EstadoPago estado;

    /** Motivo del rechazo informado por la pasarela o «Abandonado» del vencimiento (RN-22). */
    private String motivoRechazo;

    /** Comprobante emitido; lo asigna {@code GeneradorComprobante.generar} con {@code asociarComprobante(c)}. */
    private Comprobante comprobante;

    /**
     * Delega en el estado: Pendiente o Vencido → Aprobado.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @param ahora Momento de la aprobación.
     */
    public void aprobar(LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Delega en el estado: Pendiente → Rechazado.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @param motivo Motivo informado por la pasarela.
     */
    public void rechazar(String motivo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Delega en el estado: Pendiente o Vencido → Acreditado, cuando el pago se aprueba pero la cuota ya está pagada
     * (RN-21).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @param ahora Momento de la acreditación.
     */
    public void acreditar(LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Operación abstracta: {@code PagoOnline} devuelve "ONLINE", {@code PagoManual} "MANUAL" y {@code PagoConSaldo}
     * "CON_SALDO". La usan {@code FilaPago} y {@code DetalleComprobante} sin preguntar por la clase.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @return Origen del pago.
     */
    public abstract String origen();

    /**
     * Medio para mostrar: "Mercado Pago" en {@code PagoOnline}, "Saldo a favor" en {@code PagoConSaldo} y el nombre
     * del medio en {@code PagoManual}.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @return Descripción del medio.
     */
    public String descripcionMedio() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Asigna y devuelve el comprobante del pago.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @param c valor de {@code c}
     */
    public void asociarComprobante(Comprobante c) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Asigna y devuelve el comprobante del pago.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @return el resultado
     */
    public Comprobante getComprobante() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Reemplaza el objeto estado (patrón State). Solo lo invocan los estados concretos.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @param nuevo Estado destino.
     */
     void cambiarEstado(EstadoPago nuevo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Delega en el estado: Pendiente → Vencido con el motivo recibido («Abandonado», RN-22).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @param motivo Motivo del vencimiento; se guarda en {@code motivoRechazo}.
     */
    public void vencer(String motivo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

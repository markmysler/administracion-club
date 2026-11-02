package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.pagos.dominio.Pago;
import java.time.LocalDate;

/**
 * Estado inicial de toda cuota emitida: la cuota todavía no se pagó y no pasó su fecha de vencimiento. Admite
 * el pago (pasa a Pagada) y el vencimiento (pasa a Vencida si la fecha de proceso es posterior a la de
 * vencimiento).
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas, vencimiento de cuotas
 */
public class EstadoCuotaPendiente extends EstadoCuota {

    /** Código persistido. */
    static final String CODIGO = "PENDIENTE";

    /** Instancia única (el estado no tiene datos propios). */
    static final EstadoCuotaPendiente INSTANCIA = new EstadoCuotaPendiente();

    /**
     * Pendiente → Pagada: registra el pago aprobado de la cuota (RN-18).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param cuota Contexto del patrón State.
     * @param pago Pago aprobado.
     */
    @Override
    public void registrarPagoAprobado(Cuota cuota, Pago pago) {
        cuota.cambiarEstado(EstadoCuotaPagada.INSTANCIA);
    }

    /**
     * Pendiente → Vencida si {@code hoy} es posterior a la fecha de vencimiento; si todavía no venció no hace
     * nada (sec_tmp_vencer_cuotas).
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @param cuota Contexto del patrón State.
     * @param hoy Fecha de proceso.
     */
    @Override
    public void vencer(Cuota cuota, LocalDate hoy) {
        if (hoy.isAfter(cuota.getFechaVencimiento())) {
            cuota.cambiarEstado(EstadoCuotaVencida.INSTANCIA);
        }
    }

    /**
     * Una cuota pendiente admite pago.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return true
     */
    @Override
    public boolean admitePago() {
        return true;
    }

    /**
     * Código persistido de este estado.
     *
     * <p>Casos de uso: todos
     * @return el código de Pendiente
     */
    @Override
    public String codigo() {
        return CODIGO;
    }
}

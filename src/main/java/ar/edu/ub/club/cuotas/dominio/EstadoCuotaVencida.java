package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.pagos.dominio.Pago;

/**
 * Estado de la cuota impaga cuya fecha de vencimiento ya pasó (lo asigna la tarea del día 11). Mientras el grupo
 * tenga una cuota vencida sus socios no pueden reservar (RN-11). Admite el pago (pasa a Pagada); no vuelve a
 * vencer.
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: CU-27, CU-35, CU-36; tareas programadas: vencimiento de cuotas
 */
public class EstadoCuotaVencida extends EstadoCuota {

    /** Código persistido. */
    static final String CODIGO = "VENCIDA";

    /** Instancia única (el estado no tiene datos propios). */
    static final EstadoCuotaVencida INSTANCIA = new EstadoCuotaVencida();

    /**
     * Vencida → Pagada: registra el pago aprobado de la cuota (RN-18).
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
     * Una cuota vencida sigue admitiendo pago.
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
     * @return "VENCIDA"
     */
    @Override
    public String codigo() {
        return CODIGO;
    }
}

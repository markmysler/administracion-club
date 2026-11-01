package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.compartido.dominio.Reloj;

/**
 * Controlador del evento temporal de vencimiento de pagos online pendientes (RN-22), clase con {@code
 * &#64;Scheduled}. El despliegue usa una sola instancia de la aplicación; si se escalara a varias, las tareas
 * programadas usarían un bloqueo distribuido sobre la base (por ejemplo ShedLock) para ejecutarse una sola vez.
 *
 * <p>Casos de uso: tareas programadas: disparo de tareas programadas
 */
public class PlanificadorPagos {

    /** Fachada de pagos. */
    private ServicioPagos servicioPagos;

    /** Momento actual. */
    private Reloj reloj;

    /**
     * Cada hora (cron {@code 0 0 * * * *}) invoca {@code ServicioPagos.vencerPagosPendientes(ahora)}.
     *
     * <p>Casos de uso: tareas programadas: disparo de tareas programadas
     */
    public void vencerPagosPendientes() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

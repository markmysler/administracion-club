package ar.edu.ub.club.cuotas.aplicacion;

import ar.edu.ub.club.compartido.dominio.Reloj;

/**
 * Controlador de los eventos temporales de cuotas (RF-45), clase con {@code &#64;Scheduled} en la zona
 * America/Argentina/Buenos_Aires. El despliegue usa una sola instancia de la aplicación; si se escalara a
 * varias, las tareas programadas usarían un bloqueo distribuido sobre la base (por ejemplo ShedLock) para
 * ejecutarse una sola vez.
 *
 * <p>Casos de uso: tareas programadas: disparo de tareas programadas
 */
public class PlanificadorCuotas {

    /** Fachada de cuotas. */
    private ServicioCuotas servicioCuotas;

    /** Fecha actual. */
    private Reloj reloj;

    /**
     * El día 1 a las 00:00 (cron {@code 0 0 0 1 * *}) invoca {@code emitirCuotasDelMes(periodo actual)}. Si una
     * ejecución falla, la siguiente ejecución manual o programada emite solo lo faltante (idempotencia).
     *
     * <p>Casos de uso: tareas programadas: disparo de tareas programadas
     */
    public void emitirCuotasMensuales() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * El día 11 a las 00:00 (cron {@code 0 0 0 11 * *}) invoca {@code marcarCuotasVencidas(hoy)}.
     *
     * <p>Casos de uso: tareas programadas: disparo de tareas programadas
     */
    public void vencerCuotas() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

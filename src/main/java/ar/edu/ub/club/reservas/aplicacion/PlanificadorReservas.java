package ar.edu.ub.club.reservas.aplicacion;

/**
 * Controlador del evento temporal de finalización de reservas (clase con {@code &#64;Scheduled}). El despliegue
 * usa una sola instancia de la aplicación; si se escalara a varias, las tareas programadas usarían un bloqueo
 * distribuido sobre la base (por ejemplo ShedLock) para ejecutarse una sola vez.
 *
 * <p>Casos de uso: tareas programadas: disparo de tareas programadas
 */
public class PlanificadorReservas {

    /** Fachada de reservas. */
    private ServicioReservas servicioReservas;

    /**
     * Cada 15 minutos (cron {@code 0 * /15 * * * *}, zona America/Argentina/Buenos_Aires) invoca {@code
     * ServicioReservas.finalizarReservasCumplidas()}.
     *
     * <p>Casos de uso: tareas programadas: disparo de tareas programadas
     */
    public void finalizarReservas() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.reservas.aplicacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Controlador GRASP del evento temporal de finalización de reservas (clase con {@code @Scheduled}; la aplicación
 * tiene {@code @EnableScheduling}). No tiene ruta HTTP ni usuario de sesión. El despliegue usa una sola instancia
 * de la aplicación; si se escalara a varias, las tareas programadas usarían un bloqueo distribuido sobre la base
 * (por ejemplo ShedLock) para ejecutarse una sola vez (sec_tmp_disparo).
 *
 * <p>Casos de uso: tareas programadas: disparo de tareas programadas, finalización de reservas cumplidas
 */
@Component
public class PlanificadorReservas {

    /** Bitácora técnica: cantidad finalizada en cada ejecución. */
    private static final Logger BITACORA = LoggerFactory.getLogger(PlanificadorReservas.class);

    /** Fachada de reservas. */
    private final ServicioReservas servicioReservas;

    /**
     * Crea el planificador (bean de Spring).
     *
     * <p>Casos de uso: tareas programadas: disparo de tareas programadas
     * @param servicioReservas fachada de reservas
     */
    public PlanificadorReservas(ServicioReservas servicioReservas) {
        this.servicioReservas = servicioReservas;
    }

    /**
     * Cada 15 minutos (cron {@code 0 * /15 * * * *}, zona America/Argentina/Buenos_Aires) invoca {@code
     * ServicioReservas.finalizarReservasCumplidas()}.
     *
     * <p>Casos de uso: tareas programadas: disparo de tareas programadas, finalización de reservas cumplidas
     */
    @Scheduled(cron = "0 */15 * * * *", zone = "America/Argentina/Buenos_Aires")
    public void finalizarReservas() {
        int cantidad = servicioReservas.finalizarReservasCumplidas();
        if (cantidad > 0) {
            BITACORA.info("Reservas finalizadas: {}", cantidad);
        }
    }
}

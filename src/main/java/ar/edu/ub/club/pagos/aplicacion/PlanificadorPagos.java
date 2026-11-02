package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.compartido.dominio.Reloj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Controlador del evento temporal de vencimiento de pagos online pendientes (RN-22), clase con {@code
 * &#64;Scheduled}. El despliegue usa una sola instancia de la aplicación; si se escalara a varias, las tareas
 * programadas usarían un bloqueo distribuido sobre la base (por ejemplo ShedLock) para ejecutarse una sola vez.
 *
 * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
 */
@Component
public class PlanificadorPagos {

    /** Bitácora de la tarea. */
    private static final Logger BITACORA = LoggerFactory.getLogger(PlanificadorPagos.class);

    /** Fachada de pagos. */
    private final ServicioPagos servicioPagos;

    /** Momento actual. */
    private final Reloj reloj;

    /**
     * Crea el planificador.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param servicioPagos fachada de pagos
     * @param reloj reloj del club
     */
    public PlanificadorPagos(ServicioPagos servicioPagos, Reloj reloj) {
        this.servicioPagos = servicioPagos;
        this.reloj = reloj;
    }

    /**
     * Cada hora (cron {@code 0 0 * * * *}) invoca {@code ServicioPagos.vencerPagosPendientes(ahora)}.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     */
    @Scheduled(cron = "0 0 * * * *", zone = "America/Argentina/Buenos_Aires")
    public void vencerPagosPendientes() {
        int vencidos = servicioPagos.vencerPagosPendientes(reloj.ahora());
        BITACORA.info("Vencimiento de pagos online pendientes: {} pagos vencidos", vencidos);
    }
}

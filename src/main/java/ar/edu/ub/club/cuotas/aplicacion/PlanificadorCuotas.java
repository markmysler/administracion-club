package ar.edu.ub.club.cuotas.aplicacion;

import ar.edu.ub.club.compartido.dominio.Reloj;
import java.time.YearMonth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Controlador de los eventos temporales de cuotas (RF-45), clase con {@code &#64;Scheduled} en la zona
 * America/Argentina/Buenos_Aires. El despliegue usa una sola instancia de la aplicación; si se escalara a
 * varias, las tareas programadas usarían un bloqueo distribuido sobre la base (por ejemplo ShedLock) para
 * ejecutarse una sola vez.
 *
 * <p>Casos de uso: tareas programadas: emisión mensual de cuotas, vencimiento de cuotas
 */
@Component
public class PlanificadorCuotas {

    /** Bitácora de las tareas. */
    private static final Logger BITACORA = LoggerFactory.getLogger(PlanificadorCuotas.class);

    /** Fachada de cuotas. */
    private final ServicioCuotas servicioCuotas;

    /** Fecha actual. */
    private final Reloj reloj;

    /**
     * Crea el planificador.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas, vencimiento de cuotas
     * @param servicioCuotas fachada de cuotas
     * @param reloj reloj del club
     */
    public PlanificadorCuotas(ServicioCuotas servicioCuotas, Reloj reloj) {
        this.servicioCuotas = servicioCuotas;
        this.reloj = reloj;
    }

    /**
     * El día 1 a las 00:00 (cron {@code 0 0 0 1 * *}) invoca {@code emitirCuotasDelMes(periodo actual)}. Si una
     * ejecución falla, la siguiente ejecución manual o programada emite solo lo faltante (idempotencia).
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     */
    @Scheduled(cron = "0 0 0 1 * *", zone = "America/Argentina/Buenos_Aires")
    public void emitirCuotasMensuales() {
        YearMonth periodo = YearMonth.from(reloj.hoy());
        int emitidas = servicioCuotas.emitirCuotasDelMes(periodo);
        BITACORA.info("Emisión de cuotas {}: {} cuotas emitidas", periodo, emitidas);
    }

    /**
     * El día 11 a las 00:00 (cron {@code 0 0 0 11 * *}) invoca {@code marcarCuotasVencidas(hoy)}.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     */
    @Scheduled(cron = "0 0 0 11 * *", zone = "America/Argentina/Buenos_Aires")
    public void vencerCuotas() {
        int vencidas = servicioCuotas.marcarCuotasVencidas(reloj.hoy());
        BITACORA.info("Vencimiento de cuotas: {} cuotas vencidas", vencidas);
    }
}

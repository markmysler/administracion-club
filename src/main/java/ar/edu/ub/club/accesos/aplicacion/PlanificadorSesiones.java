package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.compartido.dominio.Reloj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Controlador GRASP del evento temporal de limpieza de los tokens de «Mantener la sesión iniciada» vencidos (clase con
 * {@code @Scheduled}; la aplicación tiene {@code @EnableScheduling}). No tiene ruta HTTP ni usuario de sesión. Un token
 * vence a los 30 días de su último uso (RNF de seguridad de la sesión persistente, CU-11 FA1): Spring Security ya lo
 * rechaza al recibirlo, y esta tarea borra las filas vencidas de {@code token_sesion_persistente} para que la tabla no
 * crezca sin límite. Agregado en la implementación: el diseño define {@code AlmacenTokensSesionPersistente
 * .eliminarVencidos(ahora)} y la consulta {@code TokensSesionVencidos}, pero no la tarea que los usa.
 *
 * <p>Casos de uso: CU-11; tareas programadas: limpieza de tokens de sesión persistente vencidos
 */
@Component
public class PlanificadorSesiones {

    /** Bitácora técnica: cantidad de tokens borrados en cada ejecución. */
    private static final Logger BITACORA = LoggerFactory.getLogger(PlanificadorSesiones.class);

    /** Almacén de tokens persistentes (adapta {@code PersistentTokenRepository} de Spring Security). */
    private final AlmacenTokensSesionPersistente almacenTokens;

    /** Fecha y hora actual del club. */
    private final Reloj reloj;

    /**
     * Crea el planificador (bean de Spring).
     *
     * <p>Casos de uso: tareas programadas: limpieza de tokens de sesión persistente vencidos
     * @param almacenTokens almacén de tokens persistentes
     * @param reloj reloj del club
     */
    public PlanificadorSesiones(AlmacenTokensSesionPersistente almacenTokens, Reloj reloj) {
        this.almacenTokens = almacenTokens;
        this.reloj = reloj;
    }

    /**
     * Todos los días a las 03:00 (zona America/Argentina/Buenos_Aires) invoca
     * {@code AlmacenTokensSesionPersistente.eliminarVencidos(reloj.ahora())}, que busca con el objeto consulta
     * {@code TokensSesionVencidos(ahora)} y borra cada token con {@code FachadaPersistencia.eliminar} (en su propia
     * transacción).
     *
     * <p>Casos de uso: CU-11; tareas programadas: limpieza de tokens de sesión persistente vencidos
     */
    @Scheduled(cron = "0 0 3 * * *", zone = "America/Argentina/Buenos_Aires")
    public void eliminarTokensVencidos() {
        int cantidad = almacenTokens.eliminarVencidos(reloj.ahora());
        if (cantidad > 0) {
            BITACORA.info("Tokens de sesión persistente vencidos eliminados: {}", cantidad);
        }
    }
}

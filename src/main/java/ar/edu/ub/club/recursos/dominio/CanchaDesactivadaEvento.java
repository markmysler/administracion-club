package ar.edu.ub.club.recursos.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Evento de dominio ({@code recursos.dominio}) que publica la fachada al confirmar la baja de la cancha. Lo
 * atiende {@code CanceladorReservasPorBaja} dentro de la misma transacción.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class CanchaDesactivadaEvento {

    /** Identificador de la cancha dada de baja. */
    private Long idCancha;

    /** Cuenta que realizó la baja (queda como {@code canceladaPor}). */
    private Usuario usuario;

    /** Momento de la baja. */
    private LocalDateTime fechaHora;
}

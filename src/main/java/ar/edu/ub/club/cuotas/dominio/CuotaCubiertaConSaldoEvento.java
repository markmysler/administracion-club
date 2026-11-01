package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.time.LocalDateTime;

/**
 * Evento de dominio ({@code cuotas.dominio}) que publica {@code ServicioCuotas} cuando el crédito aplicado cubre
 * todo el importe de la cuota emitida. Lo atiende {@code RegistradorPagoConSaldo} (módulo pagos) dentro de la
 * misma transacción.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class CuotaCubiertaConSaldoEvento {

    /** Cuota cubierta. */
    private Long idCuota;

    /** Grupo familiar. */
    private Long idGrupo;

    /** Importe cubierto con saldo. */
    private Dinero importe;

    /** Momento de la emisión. */
    private LocalDateTime fechaHora;
}

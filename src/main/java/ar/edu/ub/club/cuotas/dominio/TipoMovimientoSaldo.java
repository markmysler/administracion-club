package ar.edu.ub.club.cuotas.dominio;

/**
 * Tipo de un {@code MovimientoSaldo}: CREDITO suma al saldo a favor del grupo (pago ACREDITADO) y DEBITO lo
 * descuenta (saldo aplicado a una cuota). Se guarda por nombre en {@code movimiento_saldo.tipo} (RN-21).
 *
 * <p>Casos de uso: CU-05, CU-35, CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public enum TipoMovimientoSaldo {

    /** Valor CREDITO. */
    CREDITO,
    /** Valor DEBITO. */
    DEBITO;
}

package ar.edu.ub.club.pagos.dominio;

/**
 * Estado final del pago que canceló la cuota: tiene comprobante y es el único pago aprobado de su cuota (índice
 * único {@code ux_pago_aprobado_por_cuota}, RN-18). No admite transiciones.
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: CU-35, CU-36, CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class EstadoPagoAprobado extends EstadoPago {

    /** Código persistido. */
    static final String CODIGO = "APROBADO";

    /** Instancia única. */
    static final EstadoPagoAprobado INSTANCIA = new EstadoPagoAprobado();

    /**
     * Código persistido.
     *
     * <p>Casos de uso: todos
     * @return "APROBADO"
     */
    @Override
    public String codigo() {
        return CODIGO;
    }
}

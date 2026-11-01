package ar.edu.ub.club.cuotas.dominio;

/**
 * Estado final de la cuota cancelada por un pago aprobado (online, manual o con saldo a favor). No redefine
 * ninguna transición: pagarla de nuevo o vencerla lanza {@code TransicionInvalidaException} y no admite pago
 * (RN-18; un pago aprobado tardío queda ACREDITADO como saldo a favor, RN-21).
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: CU-35, CU-36, CU-37; tareas programadas: emisión mensual de cuotas
 */
public class EstadoCuotaPagada extends EstadoCuota {

    /** Código persistido. */
    static final String CODIGO = "PAGADA";

    /** Instancia única (el estado no tiene datos propios). */
    static final EstadoCuotaPagada INSTANCIA = new EstadoCuotaPagada();

    /**
     * Código persistido de este estado.
     *
     * <p>Casos de uso: todos
     * @return "PAGADA"
     */
    @Override
    public String codigo() {
        return CODIGO;
    }
}

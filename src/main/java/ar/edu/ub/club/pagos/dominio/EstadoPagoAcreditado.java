package ar.edu.ub.club.pagos.dominio;

/**
 * Estado final del pago online aprobado por la pasarela cuando la cuota ya estaba pagada: su importe quedó como
 * saldo a favor del grupo ({@code MovimientoSaldo} CREDITO) y tiene constancia de acreditación (RN-21). No cuenta
 * para el índice único de pagos aprobados. No admite transiciones.
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: CU-35, CU-37
 */
public class EstadoPagoAcreditado extends EstadoPago {

    /** Código persistido. */
    static final String CODIGO = "ACREDITADO";

    /** Instancia única. */
    static final EstadoPagoAcreditado INSTANCIA = new EstadoPagoAcreditado();

    /**
     * Código persistido.
     *
     * <p>Casos de uso: todos
     * @return "ACREDITADO"
     */
    @Override
    public String codigo() {
        return CODIGO;
    }
}

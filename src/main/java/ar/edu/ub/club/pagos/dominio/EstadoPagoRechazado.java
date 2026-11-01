package ar.edu.ub.club.pagos.dominio;

/**
 * Estado final del pago online rechazado por la pasarela (o porque la pasarela no pudo crear la intención): la
 * cuota sigue impaga y el titular puede reintentar (RN-17). No admite transiciones.
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: CU-35, CU-37
 */
public class EstadoPagoRechazado extends EstadoPago {

    /** Código persistido. */
    static final String CODIGO = "RECHAZADO";

    /** Instancia única. */
    static final EstadoPagoRechazado INSTANCIA = new EstadoPagoRechazado();

    /**
     * Código persistido.
     *
     * <p>Casos de uso: todos
     * @return "RECHAZADO"
     */
    @Override
    public String codigo() {
        return CODIGO;
    }
}

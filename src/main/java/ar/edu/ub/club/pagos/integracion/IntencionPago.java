package ar.edu.ub.club.pagos.integracion;

/**
 * Intención de pago (preferencia de Checkout Pro) creada por la pasarela: su identificador y la dirección de la
 * página de pago a la que se redirige al titular (P-27 en modo SIMULADA).
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-35
 */
public class IntencionPago {

    /** Identificador de la preferencia en la pasarela. */
    private final String idPreferencia;

    /** Dirección de la página de pago. */
    private final String urlCheckout;

    /**
     * Agregado en la implementación (el modelo no lista el constructor): crea la intención.
     *
     * <p>Casos de uso: CU-35
     * @param idPreferencia Identificador de la preferencia.
     * @param urlCheckout Dirección de la página de pago.
     */
    public IntencionPago(String idPreferencia, String urlCheckout) {
        this.idPreferencia = idPreferencia;
        this.urlCheckout = urlCheckout;
    }

    /**
     * Devuelve {@code idPreferencia}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getIdPreferencia() {
        return idPreferencia;
    }

    /**
     * Devuelve {@code urlCheckout}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getUrlCheckout() {
        return urlCheckout;
    }
}

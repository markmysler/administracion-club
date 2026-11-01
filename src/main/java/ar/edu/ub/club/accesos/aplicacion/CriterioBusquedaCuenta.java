package ar.edu.ub.club.accesos.aplicacion;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class CriterioBusquedaCuenta {

    /** Atributo texto. */
    private String texto;

    /**
     * Crea una instancia de {@code CriterioBusquedaCuenta}.
     *
     * <p>Casos de uso: PENDIENTE
     * @param texto valor de {@code texto}
     */
    public CriterioBusquedaCuenta(String texto) {
        // PENDIENTE
    }

    /**
     * Devuelve {@code texto}: texto.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Asigna {@code texto} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param texto nuevo valor
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: PENDIENTE
     */
    public CriterioBusquedaCuenta() {
    }
}

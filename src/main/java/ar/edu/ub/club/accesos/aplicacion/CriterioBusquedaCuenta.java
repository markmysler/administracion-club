package ar.edu.ub.club.accesos.aplicacion;

/**
 * Criterio de la búsqueda de cuentas de cualquier tipo de P-13 (texto contenido en usuario, correo, nombre o
 * apellido). Lo arma {@code PresentadorCuentasAdministrador.alBuscar} y lo resuelve
 * {@code ServicioAccesos.buscarCuentas} con el objeto consulta {@code CuentasPorCriterio}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-15, CU-17
 */
public class CriterioBusquedaCuenta {

    /** Texto de búsqueda (vacío: todas las cuentas). */
    private String texto;

    /**
     * Crea el criterio con el texto ingresado.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @param texto texto de búsqueda
     */
    public CriterioBusquedaCuenta(String texto) {
        this.texto = texto;
    }

    /**
     * Devuelve {@code texto}: texto de búsqueda.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return el valor del atributo
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Asigna {@code texto} (lo usa el {@code Binder} de Vaadin).
     *
     * <p>Casos de uso: CU-15, CU-17
     * @param texto nuevo valor
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: CU-15, CU-17
     */
    public CriterioBusquedaCuenta() {
    }
}

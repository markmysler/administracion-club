package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;

/**
 * Datos de acceso editables de cualquier cuenta en P-14 (CU-15): usuario, correo y, opcionalmente, una nueva
 * contraseña. Lo vincula el {@code FormularioEntidad} de {@code VistaFormularioCuenta} y lo recibe
 * {@code ServicioAccesos.modificarDatosAcceso}. La clave nunca se precarga.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-15
 */
public class DatosAcceso {

    /** Nombre de usuario (4 a 50 caracteres; único). */
    private String username;

    /** Correo (único). */
    private String email;

    /** Nueva contraseña; vacía o nula si no se cambia. */
    private String nuevaClave;

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Agregado en la implementación (el esqueleto lo omitía porque tenía {@code desde}).
     *
     * <p>Casos de uso: CU-15
     */
    public DatosAcceso() {
    }

    /**
     * Precarga usuario y correo de la cuenta (la nueva clave queda vacía).
     *
     * <p>Casos de uso: CU-15
     * @param usuario cuenta de cualquier tipo
     * @return datos para el formulario
     */
    public static DatosAcceso desde(Usuario usuario) {
        DatosAcceso datos = new DatosAcceso();
        datos.username = usuario.getUsername();
        datos.email = usuario.getEmail();
        datos.nuevaClave = "";
        return datos;
    }

    /**
     * Devuelve {@code username}.
     *
     * <p>Casos de uso: CU-15
     * @return el valor del atributo
     */
    public String getUsername() {
        return username;
    }

    /**
     * Asigna {@code username}.
     *
     * <p>Casos de uso: CU-15
     * @param username nuevo valor
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve {@code email}.
     *
     * <p>Casos de uso: CU-15
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna {@code email}.
     *
     * <p>Casos de uso: CU-15
     * @param email nuevo valor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve {@code nuevaClave}.
     *
     * <p>Casos de uso: CU-15
     * @return el valor del atributo
     */
    public String getNuevaClave() {
        return nuevaClave;
    }

    /**
     * Asigna {@code nuevaClave}.
     *
     * <p>Casos de uso: CU-15
     * @param nuevaClave nuevo valor
     */
    public void setNuevaClave(String nuevaClave) {
        this.nuevaClave = nuevaClave;
    }
}

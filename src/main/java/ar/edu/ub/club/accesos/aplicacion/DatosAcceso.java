package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-15
 */
public class DatosAcceso {

    /** Atributo username. */
    private String username;

    /** Atributo email. */
    private String email;

    /** Atributo nuevaClave. */
    private String nuevaClave;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-15
     * @param usuario valor de {@code usuario}
     * @return el resultado
     */
    public static DatosAcceso desde(Usuario usuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code username}: username.
     *
     * <p>Casos de uso: CU-15
     * @return el valor del atributo
     */
    public String getUsername() {
        return username;
    }

    /**
     * Asigna {@code username} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-15
     * @param username nuevo valor
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve {@code email}: email.
     *
     * <p>Casos de uso: CU-15
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna {@code email} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-15
     * @param email nuevo valor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve {@code nuevaClave}: nuevaClave.
     *
     * <p>Casos de uso: CU-15
     * @return el valor del atributo
     */
    public String getNuevaClave() {
        return nuevaClave;
    }

    /**
     * Asigna {@code nuevaClave} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-15
     * @param nuevaClave nuevo valor
     */
    public void setNuevaClave(String nuevaClave) {
        this.nuevaClave = nuevaClave;
    }
}

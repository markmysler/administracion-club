package ar.edu.ub.club.accesos.aplicacion;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-14
 */
public class DatosAdministrador {

    /** Atributo nombre. */
    private String nombre;

    /** Atributo apellido. */
    private String apellido;

    /** Atributo username. */
    private String username;

    /** Atributo email. */
    private String email;

    /** Atributo claveInicial. */
    private String claveInicial;

    /**
     * Crea una instancia de {@code DatosAdministrador}.
     *
     * <p>Casos de uso: CU-14
     */
    public DatosAdministrador() {
        // PENDIENTE
    }

    /**
     * Devuelve {@code nombre}: nombre.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-14
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve {@code apellido}: apellido.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Asigna {@code apellido} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-14
     * @param apellido nuevo valor
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Devuelve {@code username}: username.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getUsername() {
        return username;
    }

    /**
     * Asigna {@code username} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-14
     * @param username nuevo valor
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve {@code email}: email.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna {@code email} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-14
     * @param email nuevo valor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve {@code claveInicial}: claveInicial.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getClaveInicial() {
        return claveInicial;
    }

    /**
     * Asigna {@code claveInicial} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-14
     * @param claveInicial nuevo valor
     */
    public void setClaveInicial(String claveInicial) {
        this.claveInicial = claveInicial;
    }
}

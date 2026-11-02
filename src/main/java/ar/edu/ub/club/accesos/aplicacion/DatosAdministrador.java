package ar.edu.ub.club.accesos.aplicacion;

/**
 * Datos del alta de una cuenta de administrador en P-14 (CU-14): nombre, apellido, usuario, correo y clave
 * inicial. Lo vincula el {@code FormularioEntidad} de {@code VistaFormularioCuenta} y lo recibe
 * {@code ServicioAccesos.crearAdministrador}. No tiene campo de tipo de cuenta: el tipo lo fija la clase
 * instanciada (CU-16, RN-04).
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-14, CU-16
 */
public class DatosAdministrador {

    /** Nombre. */
    private String nombre;

    /** Apellido. */
    private String apellido;

    /** Nombre de usuario (4 a 50 caracteres; único). */
    private String username;

    /** Correo (único). */
    private String email;

    /** Clave inicial (política de contraseñas RNF-03). */
    private String claveInicial;

    /**
     * Crea los datos vacíos del formulario de alta (también lo requiere el {@code Binder}).
     *
     * <p>Casos de uso: CU-14
     */
    public DatosAdministrador() {
        this.nombre = "";
        this.apellido = "";
        this.username = "";
        this.email = "";
        this.claveInicial = "";
    }

    /**
     * Devuelve {@code nombre}.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre}.
     *
     * <p>Casos de uso: CU-14
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve {@code apellido}.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Asigna {@code apellido}.
     *
     * <p>Casos de uso: CU-14
     * @param apellido nuevo valor
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Devuelve {@code username}.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getUsername() {
        return username;
    }

    /**
     * Asigna {@code username}.
     *
     * <p>Casos de uso: CU-14
     * @param username nuevo valor
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve {@code email}.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna {@code email}.
     *
     * <p>Casos de uso: CU-14
     * @param email nuevo valor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve {@code claveInicial}.
     *
     * <p>Casos de uso: CU-14
     * @return el valor del atributo
     */
    public String getClaveInicial() {
        return claveInicial;
    }

    /**
     * Asigna {@code claveInicial}.
     *
     * <p>Casos de uso: CU-14
     * @param claveInicial nuevo valor
     */
    public void setClaveInicial(String claveInicial) {
        this.claveInicial = claveInicial;
    }
}

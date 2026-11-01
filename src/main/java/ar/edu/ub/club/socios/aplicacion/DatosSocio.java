package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-01, CU-02
 */
public class DatosSocio {

    /** Atributo dni. */
    private String dni;

    /** Atributo nombre. */
    private String nombre;

    /** Atributo apellido. */
    private String apellido;

    /** Atributo fechaNacimiento. */
    private LocalDate fechaNacimiento;

    /** Atributo telefono. */
    private String telefono;

    /** Atributo direccion. */
    private String direccion;

    /** Atributo email. */
    private String email;

    /** Atributo idCategoria. */
    private Long idCategoria;

    /** Atributo username. */
    private String username;

    /** Atributo claveInicial. */
    private String claveInicial;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param socio valor de {@code socio}
     * @return el resultado
     */
    public static DatosSocio desde(Socio socio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code dni}: dni.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public String getDni() {
        return dni;
    }

    /**
     * Asigna {@code dni} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param dni nuevo valor
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Devuelve {@code nombre}: nombre.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve {@code apellido}: apellido.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Asigna {@code apellido} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param apellido nuevo valor
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Devuelve {@code fechaNacimiento}: fechaNacimiento.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Asigna {@code fechaNacimiento} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param fechaNacimiento nuevo valor
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Devuelve {@code telefono}: telefono.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Asigna {@code telefono} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param telefono nuevo valor
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve {@code direccion}: direccion.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Asigna {@code direccion} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param direccion nuevo valor
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve {@code email}: email.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna {@code email} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param email nuevo valor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve {@code idCategoria}: idCategoria.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public Long getIdCategoria() {
        return idCategoria;
    }

    /**
     * Asigna {@code idCategoria} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param idCategoria nuevo valor
     */
    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Devuelve {@code username}: username.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public String getUsername() {
        return username;
    }

    /**
     * Asigna {@code username} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param username nuevo valor
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve {@code claveInicial}: claveInicial.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el valor del atributo
     */
    public String getClaveInicial() {
        return claveInicial;
    }

    /**
     * Asigna {@code claveInicial} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param claveInicial nuevo valor
     */
    public void setClaveInicial(String claveInicial) {
        this.claveInicial = claveInicial;
    }
}

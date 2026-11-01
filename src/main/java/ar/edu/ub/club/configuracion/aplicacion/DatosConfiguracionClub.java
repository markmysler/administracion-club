package ar.edu.ub.club.configuracion.aplicacion;

import ar.edu.ub.club.configuracion.dominio.ConfiguracionClub;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-41
 */
public class DatosConfiguracionClub {

    /** Atributo nombre. */
    private String nombre;

    /** Atributo direccion. */
    private String direccion;

    /** Atributo telefono. */
    private String telefono;

    /** Atributo email. */
    private String email;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-41
     * @param configuracion valor de {@code configuracion}
     * @return el resultado
     */
    public static DatosConfiguracionClub desde(ConfiguracionClub configuracion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code nombre}: nombre.
     *
     * <p>Casos de uso: CU-41
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-41
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve {@code direccion}: direccion.
     *
     * <p>Casos de uso: CU-41
     * @return el valor del atributo
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Asigna {@code direccion} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-41
     * @param direccion nuevo valor
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve {@code telefono}: telefono.
     *
     * <p>Casos de uso: CU-41
     * @return el valor del atributo
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Asigna {@code telefono} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-41
     * @param telefono nuevo valor
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve {@code email}: email.
     *
     * <p>Casos de uso: CU-41
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna {@code email} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-41
     * @param email nuevo valor
     */
    public void setEmail(String email) {
        this.email = email;
    }
}

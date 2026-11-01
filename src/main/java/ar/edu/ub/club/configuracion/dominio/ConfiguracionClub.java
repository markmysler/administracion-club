package ar.edu.ub.club.configuracion.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Datos institucionales del club (configuracion.dominio, fila única, CU-41). Extiende {@code
 * EntidadPersistente}.
 *
 * <p>Casos de uso: todos
 */
public class ConfiguracionClub extends EntidadPersistente {

    /** Identificador de la única fila. */
    private Long ID_UNICO;

    /** Nombre del club. */
    private String nombre;

    /** Dirección. */
    private String direccion;

    /** Teléfono de contacto. */
    private String telefono;

    /** Correo de contacto. */
    private String email;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param nombre valor de {@code nombre}
     * @param direccion valor de {@code direccion}
     * @param telefono valor de {@code telefono}
     * @param email valor de {@code email}
     * @return el resultado
     */
    public static ConfiguracionClub reconstituir(Long id, String nombre, String direccion, String telefono, String email) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code ID_UNICO}: Identificador de la única fila.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Long getID_UNICO() {
        return ID_UNICO;
    }

    /**
     * Devuelve {@code nombre}: Nombre del club.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve {@code direccion}: Dirección.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Devuelve {@code telefono}: Teléfono de contacto.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Devuelve {@code email}: Correo de contacto.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }
}

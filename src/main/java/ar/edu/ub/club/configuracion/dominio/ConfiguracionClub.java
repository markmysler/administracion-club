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
}

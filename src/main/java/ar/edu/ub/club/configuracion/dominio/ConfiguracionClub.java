package ar.edu.ub.club.configuracion.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Datos institucionales del club (configuracion.dominio, fila única, CU-41). Extiende {@code
 * EntidadPersistente}. Los usan el encabezado de la interfaz ({@code InformacionClubConfigurada}) y los
 * comprobantes.
 *
 * <p>Entidad del dominio; la persiste {@code BrokerConfiguracionClub} (tabla {@code configuracion_club}).
 *
 * <p>Casos de uso: CU-41
 */
public class ConfiguracionClub extends EntidadPersistente {

    /** Identificador de la única fila (constante de clase, {@code ck_club_fila}). */
    public static final Long ID_UNICO = 1L;

    /** Nombre del club. */
    private String nombre;

    /** Dirección. */
    private String direccion;

    /** Teléfono de contacto. */
    private String telefono;

    /** Correo de contacto. */
    private String email;

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: CU-41
     */
    private ConfiguracionClub() {
        // lo completa reconstituir
    }

    /**
     * Reemplaza los datos institucionales; los valida antes {@code ServicioConfiguracionClub.modificar}
     * ({@code sec_cu41_p3.puml}: «actualiza nombre, dirección, teléfono y correo»).
     *
     * <p>Agregado en la implementación: el diseño dice que la fachada «actualiza sus atributos» sin nombrar la
     * operación; los atributos son privados.
     *
     * <p>Casos de uso: CU-41
     * @param nombre nombre del club
     * @param direccion dirección o {@code null}
     * @param telefono teléfono o {@code null}
     * @param email correo o {@code null}
     */
    public void actualizar(String nombre, String direccion, String telefono, String email) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    /**
     * Reconstituye la fila única desde la base.
     *
     * <p>Casos de uso: CU-41
     * @param id identificador (siempre 1)
     * @param nombre nombre del club
     * @param direccion dirección
     * @param telefono teléfono
     * @param email correo
     * @return la configuración
     */
    public static ConfiguracionClub reconstituir(Long id, String nombre, String direccion, String telefono, String email) {
        ConfiguracionClub c = new ConfiguracionClub();
        c.asignarId(id);
        c.actualizar(nombre, direccion, telefono, email);
        return c;
    }

    /**
     * Devuelve {@code ID_UNICO}: identificador de la única fila.
     *
     * <p>Casos de uso: CU-41
     * @return 1
     */
    public Long getID_UNICO() {
        return ID_UNICO;
    }

    /**
     * Devuelve {@code nombre}: nombre del club.
     *
     * <p>Casos de uso: CU-41
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve {@code direccion}.
     *
     * <p>Casos de uso: CU-41
     * @return el valor del atributo
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Devuelve {@code telefono}.
     *
     * <p>Casos de uso: CU-41
     * @return el valor del atributo
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Devuelve {@code email}.
     *
     * <p>Casos de uso: CU-41
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }
}

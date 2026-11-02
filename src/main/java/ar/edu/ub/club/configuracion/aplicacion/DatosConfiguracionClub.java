package ar.edu.ub.club.configuracion.aplicacion;

import ar.edu.ub.club.configuracion.dominio.ConfiguracionClub;

/**
 * Datos editables de P-34 (nombre, dirección, teléfono y correo del club). Lo vincula el
 * {@code FormularioEntidad} de {@code VistaConfiguracionClub} y lo recibe {@code ServicioConfiguracionClub.modificar}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-41
 */
public class DatosConfiguracionClub {

    /** Nombre del club. */
    private String nombre;

    /** Dirección. */
    private String direccion;

    /** Teléfono. */
    private String telefono;

    /** Correo de contacto. */
    private String email;

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: CU-41
     */
    public DatosConfiguracionClub() {
    }

    /**
     * Copia los datos de la configuración para precargar el formulario.
     *
     * <p>Casos de uso: CU-41
     * @param configuracion fila única
     * @return datos del formulario
     */
    public static DatosConfiguracionClub desde(ConfiguracionClub configuracion) {
        DatosConfiguracionClub datos = new DatosConfiguracionClub();
        datos.nombre = configuracion.getNombre();
        datos.direccion = configuracion.getDireccion();
        datos.telefono = configuracion.getTelefono();
        datos.email = configuracion.getEmail();
        return datos;
    }

    /**
     * Devuelve {@code nombre}.
     *
     * <p>Casos de uso: CU-41
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre}.
     *
     * <p>Casos de uso: CU-41
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
     * Asigna {@code direccion}.
     *
     * <p>Casos de uso: CU-41
     * @param direccion nuevo valor
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
     * Asigna {@code telefono}.
     *
     * <p>Casos de uso: CU-41
     * @param telefono nuevo valor
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    /**
     * Asigna {@code email}.
     *
     * <p>Casos de uso: CU-41
     * @param email nuevo valor
     */
    public void setEmail(String email) {
        this.email = email;
    }
}

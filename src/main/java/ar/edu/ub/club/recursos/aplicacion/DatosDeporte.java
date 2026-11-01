package ar.edu.ub.club.recursos.aplicacion;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-18, CU-19
 */
public class DatosDeporte {

    /** Atributo nombre. */
    private String nombre;

    /** Atributo descripcion. */
    private String descripcion;

    /** Atributo activo. */
    private boolean activo;

    /**
     * Crea una instancia de {@code DatosDeporte}.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param nombre valor de {@code nombre}
     * @param descripcion valor de {@code descripcion}
     * @param activo valor de {@code activo}
     */
    public DatosDeporte(String nombre, String descripcion, boolean activo) {
        // PENDIENTE
    }

    /**
     * Devuelve {@code nombre}: nombre.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve {@code descripcion}: descripcion.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @return el valor del atributo
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna {@code descripcion} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param descripcion nuevo valor
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve {@code activo}: activo.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @return el valor del atributo
     */
    public boolean getActivo() {
        return activo;
    }

    /**
     * Asigna {@code activo} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param activo nuevo valor
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: CU-18, CU-19
     */
    public DatosDeporte() {
    }
}

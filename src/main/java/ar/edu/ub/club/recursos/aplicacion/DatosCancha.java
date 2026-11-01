package ar.edu.ub.club.recursos.aplicacion;

/**
 * Objeto de comando del formulario de P-16 (CU-21 alta, CU-22 modificación): nombre, deporte, ubicación,
 * características, {@code activa} (con true sobre una cancha inactiva se reactiva, CU-22 FA1) y {@code confirmar}
 * (true cuando el administrador ya aceptó cambiar el deporte de una cancha con reservas futuras, CU-22 FA6). Lo
 * vincula el {@code Binder} y lo reciben {@code ServicioRecursos.registrarCancha} y {@code modificarCancha}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-21, CU-22
 */
public class DatosCancha {

    /** Atributo nombre. */
    private String nombre;

    /** Atributo idDeporte. */
    private Long idDeporte;

    /** Atributo ubicacion. */
    private String ubicacion;

    /** Atributo caracteristicas. */
    private String caracteristicas;

    /** Atributo activa. */
    private boolean activa;

    /** Atributo confirmar. */
    private boolean confirmar;

    /**
     * Crea una instancia de {@code DatosCancha}.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param nombre valor de {@code nombre}
     * @param idDeporte valor de {@code idDeporte}
     * @param ubicacion valor de {@code ubicacion}
     * @param caracteristicas valor de {@code caracteristicas}
     * @param activa valor de {@code activa}
     * @param confirmar valor de {@code confirmar}
     */
    public DatosCancha(String nombre, Long idDeporte, String ubicacion, String caracteristicas, boolean activa, boolean confirmar) {
        this.nombre = nombre;
        this.idDeporte = idDeporte;
        this.ubicacion = ubicacion;
        this.caracteristicas = caracteristicas;
        this.activa = activa;
        this.confirmar = confirmar;
    }

    /**
     * Devuelve {@code nombre}: nombre.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve {@code idDeporte}: idDeporte.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return el valor del atributo
     */
    public Long getIdDeporte() {
        return idDeporte;
    }

    /**
     * Asigna {@code idDeporte} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param idDeporte nuevo valor
     */
    public void setIdDeporte(Long idDeporte) {
        this.idDeporte = idDeporte;
    }

    /**
     * Devuelve {@code ubicacion}: ubicacion.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return el valor del atributo
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Asigna {@code ubicacion} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param ubicacion nuevo valor
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Devuelve {@code caracteristicas}: caracteristicas.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return el valor del atributo
     */
    public String getCaracteristicas() {
        return caracteristicas;
    }

    /**
     * Asigna {@code caracteristicas} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param caracteristicas nuevo valor
     */
    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    /**
     * Devuelve {@code activa}: activa.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return el valor del atributo
     */
    public boolean getActiva() {
        return activa;
    }

    /**
     * Asigna {@code activa} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param activa nuevo valor
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    /**
     * Devuelve {@code confirmar}: confirmar.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return el valor del atributo
     */
    public boolean getConfirmar() {
        return confirmar;
    }

    /**
     * Asigna {@code confirmar} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param confirmar nuevo valor
     */
    public void setConfirmar(boolean confirmar) {
        this.confirmar = confirmar;
    }

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: CU-21, CU-22
     */
    public DatosCancha() {
    }
}

package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;

/**
 * Objeto de comando del formulario de categorías (P-10): nombre, importe y estado que el presentador entrega a
 * {@code ServicioSocios.registrarCategoria} o {@code modificarCategoria}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-06
 */
public class DatosCategoria {

    /** Atributo nombre. */
    private String nombre;

    /** Atributo importe. */
    private Dinero importe;

    /** Atributo activa. */
    private boolean activa;

    /**
     * Crea una instancia de {@code DatosCategoria}.
     *
     * <p>Casos de uso: CU-06
     * @param nombre valor de {@code nombre}
     * @param importe valor de {@code importe}
     * @param activa valor de {@code activa}
     */
    public DatosCategoria(String nombre, Dinero importe, boolean activa) {
        this.nombre = nombre;
        this.importe = importe;
        this.activa = activa;
    }

    /**
     * Devuelve {@code nombre}: nombre.
     *
     * <p>Casos de uso: CU-06
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-06
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve {@code importe}: importe.
     *
     * <p>Casos de uso: CU-06
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Asigna {@code importe} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-06
     * @param importe nuevo valor
     */
    public void setImporte(Dinero importe) {
        this.importe = importe;
    }

    /**
     * Devuelve {@code activa}: activa.
     *
     * <p>Casos de uso: CU-06
     * @return el valor del atributo
     */
    public boolean getActiva() {
        return activa;
    }

    /**
     * Asigna {@code activa} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-06
     * @param activa nuevo valor
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin (alta: categoría activa por omisión).
     *
     * <p>Casos de uso: CU-06
     */
    public DatosCategoria() {
        this.activa = true;
    }
}

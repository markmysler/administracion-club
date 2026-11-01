package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.pagos.dominio.MedioPago;

/**
 * Objeto de comando de un medio de pago en el formulario de P-31: un {@code id} nulo es un medio nuevo (CU-34 FA1).
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-34
 */
public class DatosMedioPago {

    /** Identificador (null en un medio nuevo). */
    private Long id;

    /** Nombre del medio. */
    private String nombre;

    /** Si está habilitado. */
    private boolean habilitado;

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: CU-34
     */
    public DatosMedioPago() {
        this.habilitado = true;
    }

    /**
     * Copia los datos de un medio para editarlos.
     *
     * <p>Casos de uso: CU-34
     * @param medio Medio de pago.
     * @return objeto de comando
     */
    public static DatosMedioPago desde(MedioPago medio) {
        DatosMedioPago d = new DatosMedioPago();
        d.id = medio.getId();
        d.nombre = medio.getNombre();
        d.habilitado = Boolean.TRUE.equals(medio.getHabilitado());
        return d;
    }

    /**
     * Devuelve {@code id}.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public Long getId() {
        return id;
    }

    /**
     * Asigna {@code id}.
     *
     * <p>Casos de uso: CU-34
     * @param id nuevo valor
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve {@code nombre}.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre}.
     *
     * <p>Casos de uso: CU-34
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve {@code habilitado}.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public boolean getHabilitado() {
        return habilitado;
    }

    /**
     * Asigna {@code habilitado}.
     *
     * <p>Casos de uso: CU-34
     * @param habilitado nuevo valor
     */
    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}

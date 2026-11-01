package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.pagos.dominio.MedioPago;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class DatosMedioPago {

    /** Atributo id. */
    private Long id;

    /** Atributo nombre. */
    private String nombre;

    /** Atributo habilitado. */
    private boolean habilitado;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param medio valor de {@code medio}
     * @return el resultado
     */
    public static DatosMedioPago desde(MedioPago medio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code id}: id.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getId() {
        return id;
    }

    /**
     * Asigna {@code id} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param id nuevo valor
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve {@code nombre}: nombre.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna {@code nombre} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param nombre nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve {@code habilitado}: habilitado.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public boolean getHabilitado() {
        return habilitado;
    }

    /**
     * Asigna {@code habilitado} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param habilitado nuevo valor
     */
    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}

package ar.edu.ub.club.agenda.aplicacion;

import java.time.LocalDate;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class DatosFeriado {

    /** Atributo fecha. */
    private LocalDate fecha;

    /** Atributo descripcion. */
    private String descripcion;

    /** Atributo confirmar. */
    private boolean confirmar;

    /**
     * Devuelve {@code fecha}: fecha.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Asigna {@code fecha} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param fecha nuevo valor
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Devuelve {@code descripcion}: descripcion.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna {@code descripcion} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param descripcion nuevo valor
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve {@code confirmar}: confirmar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public boolean getConfirmar() {
        return confirmar;
    }

    /**
     * Asigna {@code confirmar} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param confirmar nuevo valor
     */
    public void setConfirmar(boolean confirmar) {
        this.confirmar = confirmar;
    }
}

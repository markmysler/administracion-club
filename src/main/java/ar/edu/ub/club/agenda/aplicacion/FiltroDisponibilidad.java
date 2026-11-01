package ar.edu.ub.club.agenda.aplicacion;

import java.time.LocalDate;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class FiltroDisponibilidad {

    /** Atributo fecha. */
    private LocalDate fecha;

    /** Atributo idDeporte. */
    private Long idDeporte;

    /** Atributo idCancha. */
    private Long idCancha;

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
     * Devuelve {@code idDeporte}: idDeporte.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getIdDeporte() {
        return idDeporte;
    }

    /**
     * Asigna {@code idDeporte} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param idDeporte nuevo valor
     */
    public void setIdDeporte(Long idDeporte) {
        this.idDeporte = idDeporte;
    }

    /**
     * Devuelve {@code idCancha}: idCancha.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getIdCancha() {
        return idCancha;
    }

    /**
     * Asigna {@code idCancha} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param idCancha nuevo valor
     */
    public void setIdCancha(Long idCancha) {
        this.idCancha = idCancha;
    }
}

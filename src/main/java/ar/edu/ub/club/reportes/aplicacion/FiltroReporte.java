package ar.edu.ub.club.reportes.aplicacion;

import java.time.LocalDate;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class FiltroReporte {

    /** Atributo desde. */
    private LocalDate desde;

    /** Atributo hasta. */
    private LocalDate hasta;

    /** Atributo idDeporte. */
    private Long idDeporte;

    /** Atributo idCancha. */
    private Long idCancha;

    /**
     * Devuelve {@code desde}: desde.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalDate getDesde() {
        return desde;
    }

    /**
     * Asigna {@code desde} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param desde nuevo valor
     */
    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    /**
     * Devuelve {@code hasta}: hasta.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalDate getHasta() {
        return hasta;
    }

    /**
     * Asigna {@code hasta} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param hasta nuevo valor
     */
    public void setHasta(LocalDate hasta) {
        this.hasta = hasta;
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

package ar.edu.ub.club.pagos.aplicacion;

import java.time.LocalDate;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class FiltroPagos {

    /** Atributo desde. */
    private LocalDate desde;

    /** Atributo hasta. */
    private LocalDate hasta;

    /** Atributo periodo. */
    private String periodo;

    /** Atributo estado. */
    private String estado;

    /** Atributo idGrupo. */
    private Long idGrupo;

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
     * Devuelve {@code periodo}: periodo.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getPeriodo() {
        return periodo;
    }

    /**
     * Asigna {@code periodo} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param periodo nuevo valor
     */
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    /**
     * Devuelve {@code estado}: estado.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Asigna {@code estado} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param estado nuevo valor
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve {@code idGrupo}: idGrupo.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getIdGrupo() {
        return idGrupo;
    }

    /**
     * Asigna {@code idGrupo} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param idGrupo nuevo valor
     */
    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }
}

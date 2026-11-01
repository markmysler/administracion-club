package ar.edu.ub.club.pagos.aplicacion;

import java.time.LocalDate;

/**
 * Objeto de consulta del filtro de P-28: rango de fechas del pago, período de la cuota, estado del pago y grupo
 * (solo el administrador). Todos los campos son opcionales.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-37
 */
public class FiltroPagos {

    /** Desde (inclusive). */
    private LocalDate desde;

    /** Hasta (inclusive). */
    private LocalDate hasta;

    /** Período de la cuota AAAA-MM. */
    private String periodo;

    /** Código del estado del pago. */
    private String estado;

    /** Grupo familiar (administrador). */
    private Long idGrupo;

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin (filtro sin restricciones).
     *
     * <p>Casos de uso: CU-37
     */
    public FiltroPagos() {
        // todos los filtros nulos
    }

    /**
     * Devuelve {@code desde}.
     *
     * <p>Casos de uso: CU-37
     * @return el valor del atributo
     */
    public LocalDate getDesde() {
        return desde;
    }

    /**
     * Asigna {@code desde}.
     *
     * <p>Casos de uso: CU-37
     * @param desde nuevo valor
     */
    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    /**
     * Devuelve {@code hasta}.
     *
     * <p>Casos de uso: CU-37
     * @return el valor del atributo
     */
    public LocalDate getHasta() {
        return hasta;
    }

    /**
     * Asigna {@code hasta}.
     *
     * <p>Casos de uso: CU-37
     * @param hasta nuevo valor
     */
    public void setHasta(LocalDate hasta) {
        this.hasta = hasta;
    }

    /**
     * Devuelve {@code periodo}.
     *
     * <p>Casos de uso: CU-37
     * @return el valor del atributo
     */
    public String getPeriodo() {
        return periodo;
    }

    /**
     * Asigna {@code periodo}.
     *
     * <p>Casos de uso: CU-37
     * @param periodo nuevo valor
     */
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    /**
     * Devuelve {@code estado}.
     *
     * <p>Casos de uso: CU-37
     * @return el valor del atributo
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Asigna {@code estado}.
     *
     * <p>Casos de uso: CU-37
     * @param estado nuevo valor
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve {@code idGrupo}.
     *
     * <p>Casos de uso: CU-37
     * @return el valor del atributo
     */
    public Long getIdGrupo() {
        return idGrupo;
    }

    /**
     * Asigna {@code idGrupo}.
     *
     * <p>Casos de uso: CU-37
     * @param idGrupo nuevo valor
     */
    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }
}

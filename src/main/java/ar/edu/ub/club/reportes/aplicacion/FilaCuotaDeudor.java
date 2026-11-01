package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class FilaCuotaDeudor {

    /** Atributo idGrupo. */
    private Long idGrupo;

    /** Atributo titular. */
    private String titular;

    /** Atributo dniTitular. */
    private String dniTitular;

    /** Atributo cuotasAdeudadas. */
    private Integer cuotasAdeudadas;

    /** Atributo deudaTotal. */
    private Dinero deudaTotal;

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
     * Devuelve {@code titular}: titular.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getTitular() {
        return titular;
    }

    /**
     * Devuelve {@code dniTitular}: dniTitular.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getDniTitular() {
        return dniTitular;
    }

    /**
     * Devuelve {@code cuotasAdeudadas}: cuotasAdeudadas.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Integer getCuotasAdeudadas() {
        return cuotasAdeudadas;
    }

    /**
     * Devuelve {@code deudaTotal}: deudaTotal.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Dinero getDeudaTotal() {
        return deudaTotal;
    }
}

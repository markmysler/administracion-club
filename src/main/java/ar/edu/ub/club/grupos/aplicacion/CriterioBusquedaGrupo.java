package ar.edu.ub.club.grupos.aplicacion;

/**
 * Criterio de búsqueda de grupos familiares: texto que se compara con el DNI o el nombre de un integrante o del
 * titular, y vigencia opcional ({@code null} = todos). Lo arman los presentadores de P-11 y P-12 y lo recibe
 * {@code ServicioGruposFamiliares.buscarGrupos}, que arma la consulta {@code GruposPorCriterio}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-07, CU-10, CU-36
 */
public class CriterioBusquedaGrupo {

    /** Atributo texto. */
    private String texto;

    /** Atributo vigente. */
    private Boolean vigente;

    /**
     * Crea una instancia de {@code CriterioBusquedaGrupo}.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36
     * @param texto valor de {@code texto}
     * @param vigente valor de {@code vigente}
     */
    public CriterioBusquedaGrupo(String texto, Boolean vigente) {
        this.texto = texto;
        this.vigente = vigente;
    }

    /**
     * Devuelve {@code texto}: texto.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36
     * @return el valor del atributo
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Asigna {@code texto} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36
     * @param texto nuevo valor
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Devuelve {@code vigente}: vigente.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36
     * @return el valor del atributo
     */
    public Boolean getVigente() {
        return vigente;
    }

    /**
     * Asigna {@code vigente} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36
     * @param vigente nuevo valor
     */
    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36
     */
    public CriterioBusquedaGrupo() {
    }
}

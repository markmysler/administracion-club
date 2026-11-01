package ar.edu.ub.club.grupos.aplicacion;

/**
 * PENDIENTE: documentar responsabilidad.
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
        // PENDIENTE
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

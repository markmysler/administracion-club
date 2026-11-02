package ar.edu.ub.club.grupos.aplicacion;

/**
 * Objeto de datos de salida de {@code ServicioGruposFamiliares.buscarGrupos}: una fila del resultado de la
 * búsqueda de grupos (titular, su DNI, cantidad de integrantes y vigencia). Lo convierte {@code FilaGrupo.desde}.
 * Inmutable, sin comportamiento.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
 */
public class ResumenGrupo {

    /** Atributo idGrupo. */
    private Long idGrupo;

    /** Atributo titular. */
    private String titular;

    /** Atributo dniTitular. */
    private String dniTitular;

    /** Atributo cantidadIntegrantes. */
    private Integer cantidadIntegrantes;

    /** Atributo vigente. */
    private Boolean vigente;

    /**
     * Crea el resumen.
     *
     * <p>Agregado en la implementación: constructor (el modelo no lo declaraba).
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @param idGrupo grupo
     * @param titular apellido y nombre del titular (vacío si no tiene)
     * @param dniTitular DNI del titular (vacío si no tiene)
     * @param cantidadIntegrantes integrantes del grupo
     * @param vigente vigencia
     */
    public ResumenGrupo(Long idGrupo, String titular, String dniTitular, Integer cantidadIntegrantes, Boolean vigente) {
        this.idGrupo = idGrupo;
        this.titular = titular;
        this.dniTitular = dniTitular;
        this.cantidadIntegrantes = cantidadIntegrantes;
        this.vigente = vigente;
    }

    /**
     * Devuelve {@code idGrupo}: idGrupo.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @return el valor del atributo
     */
    public Long getIdGrupo() {
        return idGrupo;
    }

    /**
     * Devuelve {@code titular}: titular.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @return el valor del atributo
     */
    public String getTitular() {
        return titular;
    }

    /**
     * Devuelve {@code dniTitular}: dniTitular.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @return el valor del atributo
     */
    public String getDniTitular() {
        return dniTitular;
    }

    /**
     * Devuelve {@code cantidadIntegrantes}: cantidadIntegrantes.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @return el valor del atributo
     */
    public Integer getCantidadIntegrantes() {
        return cantidadIntegrantes;
    }

    /**
     * Devuelve {@code vigente}: vigente.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @return el valor del atributo
     */
    public Boolean getVigente() {
        return vigente;
    }
}

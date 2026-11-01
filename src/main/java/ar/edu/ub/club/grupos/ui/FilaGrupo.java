package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.grupos.aplicacion.ResumenGrupo;

/**
 * Fila del resultado de la búsqueda de grupos (P-11, P-12): titular, su DNI, cantidad de integrantes y vigencia.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador con {@code desde(resumen)}.
 *
 * <p>Casos de uso: CU-07, CU-10
 *
 * @param idGrupo grupo
 * @param titular apellido y nombre del titular
 * @param dniTitular DNI del titular
 * @param cantidadIntegrantes integrantes
 * @param vigente vigencia
 */
public record FilaGrupo(Long idGrupo, String titular, String dniTitular, Integer cantidadIntegrantes, boolean vigente) {

    /**
     * Construye la fila.
     *
     * <p>Casos de uso: CU-07, CU-10
     * @param resumen resumen del grupo
     * @return fila
     */
    public static FilaGrupo desde(ResumenGrupo resumen) {
        return new FilaGrupo(resumen.getIdGrupo(), resumen.getTitular(), resumen.getDniTitular(),
                resumen.getCantidadIntegrantes(), Boolean.TRUE.equals(resumen.getVigente()));
    }

    /**
     * Texto con el titular y su DNI, para mostrar el grupo actual en P-12.
     *
     * <p>Agregado en la implementación: formato único del «grupoActual» de {@code abrirAsociacion}.
     *
     * <p>Casos de uso: CU-07
     * @return «Titular (DNI ...)»
     */
    public String comoTexto() {
        return titular + " (DNI " + dniTitular + ")";
    }
}

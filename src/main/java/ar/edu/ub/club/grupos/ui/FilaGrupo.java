package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.grupos.aplicacion.ResumenGrupo;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-07, CU-10
 */
public record FilaGrupo(Long idGrupo, String titular, String dniTitular, Integer cantidadIntegrantes, boolean vigente) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-07, CU-10
     * @param resumen valor de {@code resumen}
     * @return el resultado
     */
    public static FilaGrupo desde(ResumenGrupo resumen) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

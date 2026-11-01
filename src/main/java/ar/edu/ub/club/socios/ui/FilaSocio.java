package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.socios.dominio.Socio;

/**
 * Fila del listado de socios (P-06) y de la búsqueda de socios de P-11: datos básicos, categoría, estado, grupo
 * vigente y si es titular.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador con {@code desde(socio)}.
 *
 * <p>Casos de uso: CU-04, CU-07, CU-10
 *
 * @param id socio
 * @param dni documento
 * @param apellido apellido
 * @param nombre nombre
 * @param categoria nombre de la categoría
 * @param estado estado de la cuenta
 * @param idGrupo grupo vigente
 * @param esTitular si es el titular de su grupo
 */
public record FilaSocio(Long id, String dni, String apellido, String nombre, String categoria, String estado, Long idGrupo, boolean esTitular) {

    /**
     * Construye la fila; el grupo vigente y la titularidad se toman de {@code socio.grupoFamiliar}.
     *
     * <p>Casos de uso: CU-04, CU-07, CU-10
     * @param socio socio
     * @return fila
     */
    public static FilaSocio desde(Socio socio) {
        GrupoFamiliar grupo = socio.getGrupoFamiliar();
        return new FilaSocio(socio.getId(), socio.getDni(), socio.getApellido(), socio.getNombre(),
                socio.getCategoria() == null ? "" : socio.getCategoria().getNombre(), socio.getEstado().name(),
                grupo == null ? null : grupo.getId(), grupo != null && grupo.esTitular(socio));
    }
}

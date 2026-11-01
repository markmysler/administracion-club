package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.socios.dominio.Socio;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-04, CU-28
 */
public record FilaSocio(Long id, String dni, String apellido, String nombre, String categoria, String estado, Long idGrupo, boolean esTitular) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-04, CU-28
     * @param socio valor de {@code socio}
     * @return el resultado
     */
    public static FilaSocio desde(Socio socio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.socios.dominio.Socio;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: PENDIENTE
 */
public record FilaIntegrante(Long idSocio, String dni, String apellido, String nombre, String categoria, Dinero importe, String estado, boolean esTitular) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param socio valor de {@code socio}
     * @param titular valor de {@code titular}
     * @return el resultado
     */
    public static FilaIntegrante desde(Socio socio, Socio titular) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

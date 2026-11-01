package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.dominio.Usuario;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-15
 */
public record DetalleCuenta(Long id, String username, String nombreCompleto, String email, String tipoCuenta, String estado) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-15
     * @param usuario valor de {@code usuario}
     * @return el resultado
     */
    public static DetalleCuenta desde(Usuario usuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

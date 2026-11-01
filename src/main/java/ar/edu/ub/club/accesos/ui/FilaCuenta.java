package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ResumenCuenta;
import ar.edu.ub.club.accesos.dominio.Administrador;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-17
 */
public record FilaCuenta(Long id, String username, String nombreCompleto, String email, String tipoCuenta, String estado, boolean bloqueada) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param administrador valor de {@code administrador}
     * @param ahora valor de {@code ahora}
     * @return el resultado
     */
    public static FilaCuenta desde(Administrador administrador, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param resumen valor de {@code resumen}
     * @return el resultado
     */
    public static FilaCuenta desde(ResumenCuenta resumen) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

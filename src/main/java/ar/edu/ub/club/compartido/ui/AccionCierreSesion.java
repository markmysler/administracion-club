package ar.edu.ub.club.compartido.ui;

/**
 * Interfaz de la acción «Cerrar sesión» del menú (P-35); la implementa {@code accesos.ui.PresentadorCierreSesion},
 * de modo que el menú común no depende del módulo accesos.
 *
 * <p>Casos de uso: CU-12
 */
public interface AccionCierreSesion {

    /**
     * Cierra la sesión de la cuenta.
     *
     * <p>Casos de uso: CU-12
     */
    void cerrarSesion();
}

package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-01: Formulario de usuario y contraseña, casilla «Mantener la sesión iniciada»
 * y enlace «Olvidé mi contraseña» (punto de extensión hacia CU-13).
 *
 * <p>Casos de uso: CU-11, CU-13
 */
public interface PantallaInicioSesion extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-11
     */
    void limpiarClave();

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-11
     * @param habilitado valor de {@code habilitado}
     */
    void habilitarEnvio(boolean habilitado);
}

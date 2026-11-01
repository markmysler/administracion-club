package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-01: Formulario de usuario y contraseña, casilla «Mantener la sesión iniciada»
 * y enlace «Olvidé mi contraseña» (punto de extensión hacia CU-13). MVP, Vista Pasiva.
 *
 * <p>Casos de uso: CU-11, CU-13
 */
public interface PantallaInicioSesion extends PantallaBase {

    /**
     * Borra la contraseña escrita (se pide después de cada envío: la clave no queda en la pantalla).
     *
     * <p>Casos de uso: CU-11
     */
    void limpiarClave();

    /**
     * Habilita o deshabilita el botón Ingresar (el formulario lo deshabilita al enviar y el presentador lo vuelve a
     * habilitar si el ingreso fue rechazado).
     *
     * <p>Casos de uso: CU-11
     * @param habilitado true para habilitarlo
     */
    void habilitarEnvio(boolean habilitado);
}

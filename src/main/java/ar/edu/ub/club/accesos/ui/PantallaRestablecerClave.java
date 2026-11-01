package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-03: Página de destino del enlace de recuperación: lee el token de la URL y
 * pide la nueva contraseña dos veces (MVP, Vista Pasiva).
 *
 * <p>Casos de uso: CU-13
 */
public interface PantallaRestablecerClave extends PantallaBase {

    /**
     * Borra las dos contraseñas escritas (después de cada envío).
     *
     * <p>Casos de uso: CU-13
     */
    void limpiarClaves();
}

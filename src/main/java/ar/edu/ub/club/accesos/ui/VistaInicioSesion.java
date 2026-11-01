package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.ui.VistaBase;

/**
 * Vista de la pantalla P-01: Formulario de usuario y contraseña, casilla «Mantener la sesión iniciada» y enlace
 * «Olvidé mi contraseña» (punto de extensión hacia CU-13).
 *
 * <p>Ruta: {@code inicio-sesion}.
 *
 * <p>Casos de uso: CU-11, CU-13
 */
public class VistaInicioSesion extends VistaBase implements PantallaInicioSesion {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-11, CU-13
     */
    @Override
    public void limpiarClave() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-11, CU-13
     * @param habilitado valor de {@code habilitado}
     */
    @Override
    public void habilitarEnvio(boolean habilitado) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

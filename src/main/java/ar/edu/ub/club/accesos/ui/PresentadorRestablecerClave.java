package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.ui.PresentadorBase;

/**
 * Presentador de la pantalla P-03: Página de destino del enlace de recuperación: lee el token de la URL y pide
 * la nueva contraseña dos veces.
 *
 * <p>Casos de uso: CU-13
 */
public class PresentadorRestablecerClave extends PresentadorBase<PantallaRestablecerClave> {

    /** Atributo servicioAccesos. */
    private ServicioAccesos servicioAccesos;

    /** Atributo token. */
    private String token;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-13
     * @param token valor de {@code token}
     */
    public void alEntrar(String token) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * si difieren {@code marcarCampo("repeticion", …)}; si no {@code ServicioAccesos.restablecerClave(token,
     * nuevaClave)} y {@code navegarA("inicio-sesion")}
     *
     * <p>Casos de uso: CU-13
     * @param nuevaClave valor de {@code nuevaClave}
     * @param repeticion valor de {@code repeticion}
     */
    public void alRestablecer(String nuevaClave, String repeticion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

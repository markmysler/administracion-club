package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.ui.PresentadorBase;

/**
 * Presentador de la pantalla P-02: Pide el correo electrónico y solicita el envío del enlace de recuperación.
 *
 * <p>Casos de uso: CU-13
 */
public class PresentadorRecuperarClave extends PresentadorBase<PantallaRecuperarClave> {

    /** Atributo servicioAccesos. */
    private ServicioAccesos servicioAccesos;

    /**
     * {@code ServicioAccesos.solicitarRecuperacion(email)}; siempre el mismo mensaje de confirmación
     *
     * <p>Casos de uso: CU-13
     * @param email valor de {@code email}
     */
    public void alSolicitar(String email) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

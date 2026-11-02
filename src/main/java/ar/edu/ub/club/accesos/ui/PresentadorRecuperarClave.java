package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-02: Pide el correo electrónico y solicita el envío del enlace de recuperación.
 * MVP (Vista Pasiva). Muestra siempre el mismo mensaje para no revelar si el correo corresponde a una cuenta
 * (CU-13 FA2).
 *
 * <p>Casos de uso: CU-13
 */
@SpringComponent
@UIScope
public class PresentadorRecuperarClave extends PresentadorBase<PantallaRecuperarClave> {

    /** Mensaje único de confirmación (CU-13 paso 9). */
    static final String MENSAJE_CONFIRMACION =
            "Si el correo corresponde a una cuenta, recibirá un enlace para restablecer la contraseña.";

    /** Fachada del módulo accesos. */
    private final ServicioAccesos servicioAccesos;

    /**
     * Crea el presentador.
     *
     * <p>Casos de uso: CU-13
     * @param servicioAccesos fachada de accesos
     */
    public PresentadorRecuperarClave(ServicioAccesos servicioAccesos) {
        this.servicioAccesos = servicioAccesos;
    }

    /**
     * {@code ServicioAccesos.solicitarRecuperacion(email)}; siempre el mismo mensaje de confirmación
     * ({@code sec_cu13_p1.puml}).
     *
     * <p>Casos de uso: CU-13
     * @param email correo ingresado (ya validado en formato por la vista)
     */
    public void alSolicitar(String email) {
        if (ejecutar(() -> servicioAccesos.solicitarRecuperacion(email))) {
            pantalla.mostrarMensaje(MENSAJE_CONFIRMACION);
        }
    }
}

package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.ui.VistaBase;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Vista de la pantalla P-02: Pide el correo electrónico y solicita el envío del enlace de recuperación. Vista
 * pública (MVP, Vista Pasiva): si el formato del correo no es válido marca el campo sin llamar al presentador
 * (CU-13 FA1).
 *
 * <p>Ruta: {@code recuperar-clave}.
 *
 * <p>Casos de uso: CU-13
 */
@Route("recuperar-clave")
@PageTitle("Recuperar contraseña")
@AnonymousAllowed
public class VistaRecuperarClave extends VistaBase implements PantallaRecuperarClave {

    /** Campo del correo. */
    private final EmailField correo = new EmailField("Correo electrónico");

    /**
     * Arma P-02 y conecta el botón Enviar con el presentador.
     *
     * <p>Casos de uso: CU-13
     * @param presentador presentador de P-02
     */
    public VistaRecuperarClave(PresentadorRecuperarClave presentador) {
        presentador.vincular(this);
        correo.setRequiredIndicatorVisible(true);
        correo.setErrorMessage("Ingrese un correo electrónico válido.");
        correo.setWidth("320px");
        Button enviar = new Button("Enviar enlace", e -> {
            if (correo.isEmpty() || correo.isInvalid()) {
                correo.setInvalid(true);
                return;
            }
            presentador.alSolicitar(correo.getValue());
        });
        enviar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(new H2("Recuperar contraseña"),
                new Paragraph("Ingrese el correo de su cuenta y le enviaremos un enlace para elegir una nueva contraseña."),
                mensajes(), correo, enviar, new RouterLink("Volver a iniciar sesión", VistaInicioSesion.class));
    }
}

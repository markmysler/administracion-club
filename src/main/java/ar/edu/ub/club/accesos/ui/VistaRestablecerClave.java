package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.ui.VistaBase;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.List;

/**
 * Vista de la pantalla P-03: Página de destino del enlace de recuperación: lee el token de la URL y pide la
 * nueva contraseña dos veces. Vista pública (MVP, Vista Pasiva).
 *
 * <p>Ruta: {@code restablecer-clave?token=…}.
 *
 * <p>Casos de uso: CU-13
 */
@Route("restablecer-clave")
@PageTitle("Restablecer contraseña")
@AnonymousAllowed
public class VistaRestablecerClave extends VistaBase implements PantallaRestablecerClave, BeforeEnterObserver {

    /** Presentador de P-03. */
    private final PresentadorRestablecerClave presentador;

    /** Nueva contraseña. */
    private final PasswordField nuevaClave = new PasswordField("Nueva contraseña");

    /** Repetición de la nueva contraseña. */
    private final PasswordField repeticion = new PasswordField("Repita la nueva contraseña");

    /**
     * Arma P-03 y conecta el botón Guardar con el presentador.
     *
     * <p>Casos de uso: CU-13
     * @param presentador presentador de P-03
     */
    public VistaRestablecerClave(PresentadorRestablecerClave presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        nuevaClave.setHelperText("De 15 a 64 caracteres; puede usar espacios.");
        nuevaClave.getElement().setAttribute("autocomplete", "new-password");
        repeticion.getElement().setAttribute("autocomplete", "new-password");
        Button guardar = new Button("Guardar", e -> {
            nuevaClave.setInvalid(false);
            repeticion.setInvalid(false);
            presentador.alRestablecer(nuevaClave.getValue(), repeticion.getValue());
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(new H2("Restablecer contraseña"), new Paragraph("Elija su nueva contraseña."), mensajes(),
                nuevaClave, repeticion, guardar,
                new RouterLink("Solicitar un nuevo enlace", VistaRecuperarClave.class));
    }

    /**
     * Lee el parámetro {@code token} de la URL y se lo entrega al presentador (CU-13 paso 10).
     *
     * <p>Casos de uso: CU-13
     * @param evento evento de navegación
     */
    @Override
    public void beforeEnter(BeforeEnterEvent evento) {
        List<String> valores = evento.getLocation().getQueryParameters().getParameters().get("token");
        presentador.alEntrar(valores == null || valores.isEmpty() ? null : valores.get(0));
    }

    /**
     * Borra las dos contraseñas escritas.
     *
     * <p>Casos de uso: CU-13
     */
    @Override
    public void limpiarClaves() {
        nuevaClave.clear();
        repeticion.clear();
    }

    /**
     * Marca el campo indicado por el presentador ({@code nuevaClave} o {@code repeticion}); otro campo se muestra
     * como error general.
     *
     * <p>Casos de uso: CU-13
     * @param campo nombre del campo
     * @param mensaje motivo
     */
    @Override
    public void marcarCampo(String campo, String mensaje) {
        PasswordField destino = "repeticion".equals(campo) ? repeticion : "nuevaClave".equals(campo) ? nuevaClave : null;
        if (destino == null) {
            mostrarError(mensaje);
            return;
        }
        destino.setErrorMessage(mensaje);
        destino.setInvalid(true);
    }

    /**
     * Muestra el mensaje como notificación, porque después del cambio la vista navega a P-01 (CU-13 paso 17).
     *
     * <p>Casos de uso: CU-13
     * @param mensaje mensaje
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        Notification.show(mensaje, 6000, Notification.Position.TOP_CENTER);
    }
}

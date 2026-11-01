package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Zona de mensajes accesible ({@code role="status"}, {@code aria-live="polite"}; RNF-16 y accesibilidad): muestra
 * el resultado de las operaciones como notificación de Vaadin y deja el último texto para lectores de pantalla.
 *
 * <p>Casos de uso: todos
 */
public class MensajeEstado extends Composite<Div> {

    /**
     * Crea la zona de mensajes con los atributos de accesibilidad.
     *
     * <p>Casos de uso: todos
     */
    public MensajeEstado() {
        getContent().getElement().setAttribute("role", "status");
        getContent().getElement().setAttribute("aria-live", "polite");
        getContent().addClassName("mensaje-estado");
    }

    /**
     * Anuncia un mensaje de éxito.
     *
     * <p>Casos de uso: todos
     * @param mensaje texto
     */
    public void anunciar(String mensaje) {
        getContent().getElement().setAttribute("aria-live", "polite");
        getContent().setText(mensaje);
        Notification aviso = Notification.show(mensaje, 4000, Notification.Position.TOP_CENTER);
        aviso.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Anuncia un mensaje de error ({@code aria-live="assertive"}).
     *
     * <p>Casos de uso: todos
     * @param mensaje texto del error
     */
    public void anunciarError(String mensaje) {
        getContent().getElement().setAttribute("aria-live", "assertive");
        getContent().setText(mensaje);
        Notification aviso = Notification.show(mensaje, 6000, Notification.Position.TOP_CENTER);
        aviso.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    /**
     * Borra el texto anunciado.
     *
     * <p>Casos de uso: todos
     */
    public void limpiar() {
        getContent().setText("");
    }
}

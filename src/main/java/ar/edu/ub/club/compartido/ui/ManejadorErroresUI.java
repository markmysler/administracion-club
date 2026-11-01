package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Red de seguridad de Vaadin para los errores que ocurren fuera de los presentadores: registra el error con un
 * código y muestra un mensaje genérico con ese código, sin detalles técnicos (RNF-16). Lo registra
 * {@code ConfiguracionInterfaz} en cada {@code VaadinSession}.
 *
 * <p>Casos de uso: todos
 */
public class ManejadorErroresUI implements ErrorHandler {

    /** Bitácora técnica. */
    private static final Logger BITACORA = LoggerFactory.getLogger(ManejadorErroresUI.class);

    /**
     * Registra el error con un código y avisa al usuario.
     *
     * <p>Casos de uso: todos
     * @param evento evento de error de Vaadin
     */
    @Override
    public void error(ErrorEvent evento) {
        String idError = UUID.randomUUID().toString();
        BITACORA.error("Error no controlado en la interfaz {}", idError, evento.getThrowable());
        if (UI.getCurrent() != null) {
            UI.getCurrent().access(() -> {
                Notification aviso = Notification.show("Ocurrió un error inesperado. Código: " + idError, 6000,
                        Notification.Position.TOP_CENTER);
                aviso.addThemeVariants(NotificationVariant.LUMO_ERROR);
            });
        }
    }
}

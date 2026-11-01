package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.theme.Theme;

/**
 * Configuración de la interfaz Vaadin: {@code @Push} (server push por WebSocket para P-26), tema {@code club}
 * (Lumo con los colores del club) y configuración regional es-AR. Registra {@code ManejadorErroresUI} en cada
 * {@code VaadinSession}.
 *
 * <p>Casos de uso: ninguno (configuración)
 */
@Push
@Theme("club")
@SpringComponent
public class ConfiguracionInterfaz implements AppShellConfigurator, VaadinServiceInitListener {

    /**
     * Registra el manejador de errores y fija la configuración regional es-AR en cada sesión de Vaadin.
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param evento evento de inicio del servicio de Vaadin
     */
    @Override
    public void serviceInit(ServiceInitEvent evento) {
        evento.getSource().addSessionInitListener(inicio -> {
            inicio.getSession().setErrorHandler(new ManejadorErroresUI());
            inicio.getSession().setLocale(FormatoUI.ES_AR);
        });
        evento.getSource().addUIInitListener(ui -> ui.getUI().setLocale(FormatoUI.ES_AR));
    }
}

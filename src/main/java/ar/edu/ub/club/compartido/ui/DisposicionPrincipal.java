package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLayout;

/**
 * Disposición común de las pantallas protegidas («componente» sobre {@code AppLayout}): encabezado con el nombre
 * del club, menú lateral ({@code MenuNavegacion}, cajón desplegable en teléfonos) y zona de mensajes. Las vistas
 * protegidas la declaran como {@code layout} de su {@code @Route}.
 *
 * <p>Casos de uso: todos
 */
public class DisposicionPrincipal extends AppLayout implements RouterLayout, AfterNavigationObserver {

    /** Menú lateral con las opciones visibles para la cuenta. */
    private final MenuNavegacion menu;

    /** Zona de mensajes que usan las vistas. */
    private final MensajeEstado mensajes = new MensajeEstado();

    /** Nombre de la pantalla actual. */
    private final H1 titulo = new H1();

    /** Nombre del club en el encabezado (inyectado). */
    private final InformacionClub informacionClub;

    /**
     * Arma el encabezado, el menú y la zona de mensajes.
     *
     * <p>Casos de uso: todos
     * @param menu menú lateral
     * @param informacionClub fuente del nombre del club
     */
    public DisposicionPrincipal(MenuNavegacion menu, InformacionClub informacionClub) {
        this.menu = menu;
        this.informacionClub = informacionClub;
        Span club = new Span(informacionClub.nombreClub());
        club.addClassName("nombre-club");
        titulo.addClassName("titulo-pantalla");
        addToNavbar(new DrawerToggle(), club, titulo, mensajes);
        addToDrawer(menu);
        setPrimarySection(Section.DRAWER);
    }

    /**
     * Actualiza el título con la opción del menú de la ruta actual y reconstruye el menú (la cuenta pudo cambiar).
     *
     * <p>Casos de uso: todos
     * @param evento evento de navegación
     */
    @Override
    public void afterNavigation(AfterNavigationEvent evento) {
        menu.construir();
        String ruta = evento.getLocation().getPath();
        titulo.setText(menu.opcionesVisibles().stream()
                .filter(o -> ruta.equals(o.ruta()) || (!o.ruta().isEmpty() && ruta.startsWith(o.ruta() + "/")))
                .map(OpcionMenu::etiqueta)
                .findFirst()
                .orElse(""));
    }

    /**
     * Zona de mensajes que usan las vistas.
     *
     * <p>Casos de uso: todos
     * @return zona de mensajes
     */
    public MensajeEstado mensajes() {
        return mensajes;
    }
}

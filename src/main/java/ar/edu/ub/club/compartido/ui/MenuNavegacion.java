package ar.edu.ub.club.compartido.ui;

import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.SesionActual;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;

/**
 * Menú lateral («componente» sobre {@code SideNav}): reúne las opciones de todos los módulos (beans
 * {@code ProveedorOpcionesMenu}), muestra solo las que la cuenta puede usar ({@code ControlAcceso.tienePermiso},
 * sin nombres de rol), agrupadas por área, y agrega al final «Cerrar sesión» (P-35).
 *
 * <p>Casos de uso: todos
 */
@SpringComponent
@Scope("prototype")
public class MenuNavegacion extends Composite<SideNav> {

    /** Proveedores de opciones de todos los módulos (inyectados por Spring). */
    private final List<ProveedorOpcionesMenu> proveedores;

    /** Cuenta de la sesión. */
    private final SesionActual sesionActual;

    /** Verificación de permisos. */
    private final ControlAcceso controlAcceso;

    /** Acción de cierre de sesión (la implementa accesos). */
    private final AccionCierreSesion cierreSesion;

    /**
     * Crea el menú con sus colaboradores.
     *
     * <p>Casos de uso: todos
     * @param proveedores proveedores de opciones de los módulos
     * @param sesionActual cuenta de la sesión
     * @param controlAcceso verificación de permisos
     * @param cierreSesion acción de cierre
     */
    public MenuNavegacion(List<ProveedorOpcionesMenu> proveedores, SesionActual sesionActual,
                          ControlAcceso controlAcceso, AccionCierreSesion cierreSesion) {
        this.proveedores = proveedores;
        this.sesionActual = sesionActual;
        this.controlAcceso = controlAcceso;
        this.cierreSesion = cierreSesion;
    }

    /**
     * Arma el menú: {@code opcionesVisibles()} agrupadas por área y ordenadas; al final «Cerrar sesión».
     *
     * <p>Casos de uso: todos
     */
    public void construir() {
        SideNav nav = getContent();
        nav.removeAll();
        Map<String, List<OpcionMenu>> porArea = new LinkedHashMap<>();
        for (OpcionMenu opcion : opcionesVisibles()) {
            porArea.computeIfAbsent(opcion.area(), a -> new ArrayList<>()).add(opcion);
        }
        for (Map.Entry<String, List<OpcionMenu>> area : porArea.entrySet()) {
            SideNavItem grupo = new SideNavItem(area.getKey());
            grupo.setExpanded(true);
            for (OpcionMenu opcion : area.getValue()) {
                grupo.addItem(new SideNavItem(opcion.etiqueta(), opcion.ruta()));
            }
            nav.addItem(grupo);
        }
        if (sesionActual.autenticada()) {
            SideNavItem salir = new SideNavItem("Cerrar sesión");
            salir.setPrefixComponent(VaadinIcon.SIGN_OUT.create());
            salir.getElement().addEventListener("click", e -> alCerrarSesion());
            nav.addItem(salir);
        }
    }

    /**
     * Opciones con permiso: {@code controlAcceso.tienePermiso(sesionActual.cuenta(), opcion.permiso())}, sin
     * repetir rutas, ordenadas por área y orden.
     *
     * <p>Casos de uso: todos
     * @return opciones visibles
     */
    public List<OpcionMenu> opcionesVisibles() {
        if (!sesionActual.autenticada()) {
            return List.of();
        }
        var cuenta = sesionActual.cuenta();
        Map<String, OpcionMenu> porRuta = new LinkedHashMap<>();
        proveedores.stream()
                .flatMap(p -> p.opciones().stream())
                .filter(o -> controlAcceso.tienePermiso(cuenta, o.permiso()))
                .sorted(Comparator.comparing(OpcionMenu::orden))
                .forEach(o -> porRuta.putIfAbsent(o.ruta(), o));
        return new ArrayList<>(porRuta.values());
    }

    /**
     * Cierra la sesión ({@code cierreSesion.cerrarSesion()}) y lleva el navegador a {@code inicio-sesion}.
     *
     * <p>Casos de uso: CU-12
     */
    public void alCerrarSesion() {
        cierreSesion.cerrarSesion();
        UI.getCurrent().getPage().setLocation("inicio-sesion");
    }
}

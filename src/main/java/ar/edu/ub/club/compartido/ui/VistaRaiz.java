package ar.edu.ub.club.compartido.ui;

import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.seguridad.SesionActual;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Vista de la ruta raíz {@code ""}: decide a dónde ir por permiso, no por tipo de cuenta. También es la entrada
 * con la cookie «Mantener la sesión iniciada» (CU-11 FA1/FA2): Spring Security restaura la sesión y esta vista
 * lleva a P-04 o P-05.
 *
 * <p>Casos de uso: CU-11
 */
@Route("")
@AnonymousAllowed
public class VistaRaiz extends Div implements BeforeEnterObserver {

    /** Cuenta de la sesión. */
    private final SesionActual sesionActual;

    /** Verificación de permisos. */
    private final ControlAcceso controlAcceso;

    /**
     * Crea la vista.
     *
     * <p>Casos de uso: CU-11
     * @param sesionActual cuenta de la sesión
     * @param controlAcceso verificación de permisos
     */
    public VistaRaiz(SesionActual sesionActual, ControlAcceso controlAcceso) {
        this.sesionActual = sesionActual;
        this.controlAcceso = controlAcceso;
    }

    /**
     * Sin sesión: {@code inicio-sesion}; con CONSULTAR_DASHBOARD: {@code panel}; si no: {@code inicio}.
     *
     * <p>Casos de uso: CU-11
     * @param evento evento de navegación
     */
    @Override
    public void beforeEnter(BeforeEnterEvent evento) {
        if (!sesionActual.autenticada()) {
            evento.forwardTo("inicio-sesion");
        } else if (controlAcceso.tienePermiso(sesionActual.cuenta(), Permiso.CONSULTAR_DASHBOARD)) {
            evento.forwardTo("panel");
        } else {
            evento.forwardTo("inicio");
        }
    }
}

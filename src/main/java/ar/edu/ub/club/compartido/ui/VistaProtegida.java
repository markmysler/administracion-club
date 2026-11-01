package ar.edu.ub.club.compartido.ui;

import ar.edu.ub.club.compartido.seguridad.Autorizable;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.seguridad.SesionActual;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base abstracta de las vistas que requieren sesión (GoF Template Method): {@code beforeEnter} es el método
 * plantilla final que verifica la sesión y el {@code Permiso} de la pantalla con {@code ControlAcceso} y
 * {@code Usuario.obtenerPermisos()} (sin nombres de rol) y luego llama al gancho {@code alEntrar}. Las subclases
 * definen {@code permisoRequerido()} y, si corresponde, {@code permisoAlternativo()}. Las fachadas vuelven a
 * verificar el permiso de cada operación.
 *
 * <p>{@code sesionActual} y {@code controlAcceso} los inyecta Spring en la vista (que es un bean de Vaadin).
 *
 * <p>Casos de uso: todos
 */
@PermitAll
public abstract class VistaProtegida extends VistaBase implements BeforeEnterObserver {

    /** Cuenta de la sesión. */
    @Autowired
    protected SesionActual sesionActual;

    /** Verificación polimórfica de permisos. */
    @Autowired
    protected ControlAcceso controlAcceso;

    /**
     * Método plantilla: 1) sin sesión autenticada → {@code inicio-sesion}; 2) sin el permiso requerido ni el
     * alternativo → {@code acceso-denegado}; 3) {@code alEntrar(parametros, consulta)}.
     *
     * <p>Casos de uso: todos
     * @param evento evento de navegación de Vaadin
     */
    @Override
    public final void beforeEnter(BeforeEnterEvent evento) {
        if (!sesionActual.autenticada()) {
            evento.forwardTo("inicio-sesion");
            return;
        }
        Autorizable cuenta = sesionActual.cuenta();
        Permiso alternativo = permisoAlternativo();
        boolean permitido = controlAcceso.tienePermiso(cuenta, permisoRequerido())
                || (alternativo != null && controlAcceso.tienePermiso(cuenta, alternativo));
        if (!permitido) {
            evento.forwardTo("acceso-denegado");
            return;
        }
        alEntrar(evento.getRouteParameters(), evento.getLocation().getQueryParameters());
    }

    /**
     * Gancho: permiso de la pantalla.
     *
     * <p>Casos de uso: todos
     * @return permiso requerido
     */
    protected abstract Permiso permisoRequerido();

    /**
     * Gancho: permiso alternativo (por omisión ninguno; solo P-28 y P-29 lo redefinen).
     *
     * <p>Casos de uso: CU-37
     * @return permiso alternativo o {@code null}
     */
    protected Permiso permisoAlternativo() {
        return null;
    }

    /**
     * Gancho: llama al método de carga del presentador con los parámetros de la ruta y de la consulta.
     *
     * <p>Casos de uso: todos
     * @param parametros parámetros de la ruta ({@code :id})
     * @param consulta parámetros de consulta ({@code ?referencia=...})
     */
    protected abstract void alEntrar(RouteParameters parametros, QueryParameters consulta);
}

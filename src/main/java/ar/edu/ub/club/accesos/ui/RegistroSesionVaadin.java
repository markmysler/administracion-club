package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.SesionUsuario;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

/**
 * Implementa {@code RegistroSesionWeb} con {@code VaadinServletRequest}/{@code VaadinServletResponse}, {@code
 * SecurityContextRepository} y el {@code SessionRegistry} de Spring Security. Guarda el {@code SecurityContext}
 * en la sesión HTTP, renueva su identificador y registra la sesión en el {@code SessionRegistry}.
 *
 * <p>El principal es {@code CuentasParaRecordarme.principalDe(id, permisos)}: su nombre es el id de la cuenta, el
 * mismo que usa el ingreso por remember-me.
 *
 * <p>Casos de uso: CU-11, CU-12
 */
@Component
public class RegistroSesionVaadin implements RegistroSesionWeb {

    /** Guarda el contexto autenticado en la sesión HTTP en memoria. */
    private final SecurityContextRepository almacenContexto;

    /** Registro de las sesiones activas por cuenta. */
    private final SessionRegistry registroSesiones;

    /** Escribe y borra la cookie remember-me. */
    private final ServicioRecordarmeClub servicioRecordarme;

    /**
     * Crea el componente.
     *
     * <p>Casos de uso: CU-11, CU-12
     * @param almacenContexto repositorio del contexto de seguridad
     * @param registroSesiones registro de sesiones
     * @param servicioRecordarme servicio remember-me (subclase de {@code PersistentTokenBasedRememberMeServices})
     */
    public RegistroSesionVaadin(SecurityContextRepository almacenContexto, SessionRegistry registroSesiones,
                                ServicioRecordarmeClub servicioRecordarme) {
        this.almacenContexto = almacenContexto;
        this.registroSesiones = registroSesiones;
        this.servicioRecordarme = servicioRecordarme;
    }

    /**
     * Cambia el id de la sesión ({@code changeSessionId}), guarda el {@code SecurityContext} en la sesión HTTP,
     * registra la sesión ({@code registerNewSession(idSesion, principal)}) y, si hay token persistente, escribe la
     * cookie remember-me de 30 días ({@code sec_cu11_p1.puml}).
     *
     * <p>Casos de uso: CU-11
     * @param sesion resultado de {@code ServicioAccesos.iniciarSesion}
     */
    @Override
    public void iniciar(SesionUsuario sesion) {
        HttpServletRequest request = VaadinServletRequest.getCurrent().getHttpServletRequest();
        HttpServletResponse response = VaadinServletResponse.getCurrent().getHttpServletResponse();
        request.getSession(true);
        request.changeSessionId();
        UserDetails principal = CuentasParaRecordarme.principalDe(sesion.getIdUsuario(), sesion.getPermisos());
        Authentication autenticacion = UsernamePasswordAuthenticationToken.authenticated(principal, null,
                principal.getAuthorities());
        SecurityContext contexto = SecurityContextHolder.createEmptyContext();
        contexto.setAuthentication(autenticacion);
        SecurityContextHolder.setContext(contexto);
        almacenContexto.saveContext(contexto, request, response);
        registroSesiones.registerNewSession(request.getSession().getId(), principal);
        if (sesion.getTokenPersistente() != null) {
            servicioRecordarme.escribirCookie(sesion.getTokenPersistente(), request, response);
        }
    }

    /**
     * Borra la cookie remember-me, ejecuta {@code SecurityContextLogoutHandler} (limpia el contexto e invalida la
     * sesión HTTP; con ella se destruye la {@code VaadinSession} y sale del {@code SessionRegistry}). Antes pide
     * al navegador ir a {@code inicio-sesion}, como recomienda Vaadin al cerrar la sesión ({@code sec_cu12.puml}).
     *
     * <p>Casos de uso: CU-12
     */
    @Override
    public void cerrar() {
        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.getPage().setLocation("inicio-sesion");
        }
        VaadinServletRequest vaadinRequest = VaadinServletRequest.getCurrent();
        VaadinServletResponse vaadinResponse = VaadinServletResponse.getCurrent();
        if (vaadinRequest == null || vaadinResponse == null) {
            SecurityContextHolder.clearContext();
            return;
        }
        HttpServletRequest request = vaadinRequest.getHttpServletRequest();
        HttpServletResponse response = vaadinResponse.getHttpServletResponse();
        servicioRecordarme.borrarCookie(request, response);
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler cierre = new SecurityContextLogoutHandler();
        cierre.setInvalidateHttpSession(true);
        cierre.logout(request, response, autenticacion);
        VaadinSession sesionVaadin = VaadinSession.getCurrent();
        if (sesionVaadin != null) {
            sesionVaadin.close();
        }
    }
}

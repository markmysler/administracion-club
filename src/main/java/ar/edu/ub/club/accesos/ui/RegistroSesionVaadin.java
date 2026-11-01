package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.SesionUsuario;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * Implementa {@code RegistroSesionWeb} con {@code VaadinServletRequest}/{@code VaadinServletResponse}, {@code
 * SecurityContextRepository} y el {@code SessionRegistry} de Spring Security. Guarda el {@code SecurityContext}
 * en la sesión HTTP, renueva su identificador y registra la sesión en el {@code SessionRegistry}.
 *
 * <p>Casos de uso: CU-11, CU-12
 */
public class RegistroSesionVaadin implements RegistroSesionWeb {

    /** Guarda el contexto autenticado en la sesión HTTP en memoria. */
    private SecurityContextRepository almacenContexto;

    /** Registro de las sesiones activas por cuenta. */
    private SessionRegistry registroSesiones;

    /** Escribe y borra la cookie remember-me. */
    private PersistentTokenBasedRememberMeServices servicioRecordarme;

    /**
     * Implementan las operaciones de {@code RegistroSesionWeb}.
     *
     * <p>Casos de uso: CU-11
     * @param sesion valor de {@code sesion}
     */
    public void iniciar(SesionUsuario sesion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Implementan las operaciones de {@code RegistroSesionWeb}.
     *
     * <p>Casos de uso: CU-12
     */
    public void cerrar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

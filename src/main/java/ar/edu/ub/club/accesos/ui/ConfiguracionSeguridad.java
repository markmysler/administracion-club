package ar.edu.ub.club.accesos.ui;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;

/**
 * Extiende {@code VaadinWebSecurity} (Spring Security para Vaadin).
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ConfiguracionSeguridad {

    /**
     * Fija {@code VistaInicioSesion} como vista de ingreso; configura remember-me con {@code
     * PersistentTokenBasedRememberMeServices} sobre {@code AlmacenTokensSesionPersistente} (30 días, rotación, solo
     * socios); sesión del servidor en memoria con {@code SessionRegistry} y {@code closeIdleSessions = true} (30
     * minutos de inactividad de los administradores aunque Vaadin envíe latidos); cookies {@code HttpOnly}, {@code
     * Secure} y {@code SameSite=Strict}; {@code permitAll} y sin CSRF solo para {@code POST
     * /api/pasarela/notificaciones} (webhook); {@code permitAll} y sin creación de sesión ni {@code RequestCache}
     * para {@code GET /pagos/retorno}.
     *
     * <p>Casos de uso: PENDIENTE
     * @param http valor de {@code http}
     */
    public void configure(HttpSecurity http) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Declara el bean {@code SessionRegistryImpl} que usan {@code RegistroSesionVaadin} y {@code GestorSesiones}.
     *
     * <p>Casos de uso: PENDIENTE
     * @return {@code SessionRegistry}.
     */
    public SessionRegistry registroSesiones() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

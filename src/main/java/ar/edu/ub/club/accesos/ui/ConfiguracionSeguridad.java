package ar.edu.ub.club.accesos.ui;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Extiende {@code VaadinWebSecurity} (Spring Security para Vaadin): configuración de seguridad HTTP de toda la
 * aplicación (vista de ingreso, remember-me, sesiones en memoria, excepciones públicas del webhook y del retorno
 * de la pasarela). Las cookies de sesión ({@code HttpOnly}, {@code SameSite=Strict}, {@code Secure}) y la
 * inactividad de 30 minutos se fijan en {@code application.yml}; {@code vaadin.closeIdleSessions=true} se carga
 * desde {@code ar/edu/ub/club/accesos/ui/seguridad.properties}.
 *
 * <p>Casos de uso: CU-11, CU-12, CU-13, CU-35
 */
@EnableWebSecurity
@Configuration
@PropertySource("classpath:ar/edu/ub/club/accesos/ui/seguridad.properties")
public class ConfiguracionSeguridad extends VaadinWebSecurity {

    /**
     * Fija {@code VistaInicioSesion} como vista de ingreso; configura remember-me con {@code
     * PersistentTokenBasedRememberMeServices} sobre {@code AlmacenTokensSesionPersistente} (30 días, rotación, solo
     * socios); sesión del servidor en memoria con {@code SessionRegistry} y {@code closeIdleSessions = true} (30
     * minutos de inactividad de los administradores aunque Vaadin envíe latidos); cookies {@code HttpOnly}, {@code
     * Secure} y {@code SameSite=Strict}; {@code permitAll} y sin CSRF solo para {@code POST
     * /api/pasarela/notificaciones} (webhook); {@code permitAll} y sin creación de sesión ni {@code RequestCache}
     * para {@code GET /pagos/retorno} (es una ruta pública que no autentica, así que Spring Security no crea sesión
     * ni guarda la petición).
     *
     * <p>Casos de uso: CU-11, CU-12, CU-35
     * @param http Configuración de seguridad HTTP.
     * @throws Exception si Spring Security rechaza la configuración
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        RequestMatcher webhook = PathPatternRequestMatcher.withDefaults()
                .matcher(HttpMethod.POST, "/api/pasarela/notificaciones");
        RequestMatcher retornoPasarela = PathPatternRequestMatcher.withDefaults()
                .matcher(HttpMethod.GET, "/pagos/retorno");
        http.authorizeHttpRequests(autorizacion -> autorizacion
                .requestMatchers(webhook).permitAll()
                .requestMatchers(retornoPasarela).permitAll());
        http.csrf(csrf -> csrf.ignoringRequestMatchers(webhook));
        super.configure(http);
        setLoginView(http, VistaInicioSesion.class);
        ServicioRecordarmeClub recordarme = http.getSharedObject(ApplicationContext.class)
                .getBean(ServicioRecordarmeClub.class);
        http.rememberMe(r -> r.rememberMeServices(recordarme).key(recordarme.getKey()));
        http.sessionManagement(sesiones -> sesiones
                .sessionFixation(fijacion -> fijacion.changeSessionId())
                .maximumSessions(-1)
                .sessionRegistry(registroSesiones())
                .expiredUrl("/inicio-sesion"));
    }

    /**
     * Declara el bean {@code SessionRegistryImpl} que usan {@code RegistroSesionVaadin} y {@code GestorSesiones}.
     *
     * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
     * @return {@code SessionRegistry}.
     */
    @Bean
    public SessionRegistry registroSesiones() {
        return new SessionRegistryImpl();
    }

    /**
     * Bean que guarda el {@code SecurityContext} en la sesión HTTP en memoria (lo usa {@code RegistroSesionVaadin}).
     *
     * <p>Agregado en la implementación: {@code RegistroSesionVaadin} recibe un {@code SecurityContextRepository}.
     *
     * <p>Casos de uso: CU-11
     * @return repositorio de contexto sobre la sesión HTTP
     */
    @Bean
    public SecurityContextRepository almacenContexto() {
        return new HttpSessionSecurityContextRepository();
    }

    /**
     * Publica los eventos de creación, cambio de id y destrucción de sesiones HTTP para que el
     * {@code SessionRegistry} se mantenga al día (sesión invalidada en CU-12, id renovado en CU-11).
     *
     * <p>Agregado en la implementación: requisito de Spring Security para usar {@code SessionRegistryImpl}.
     *
     * <p>Casos de uso: CU-11, CU-12
     * @return publicador de eventos de sesión
     */
    @Bean
    public static HttpSessionEventPublisher publicadorEventosSesion() {
        return new HttpSessionEventPublisher();
    }
}

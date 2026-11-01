package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.AlmacenTokensSesionPersistente;
import ar.edu.ub.club.accesos.aplicacion.GestorSesiones;
import ar.edu.ub.club.accesos.dominio.TokenSesionPersistente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

/**
 * {@code PersistentTokenBasedRememberMeServices} de Spring Security (tokens persistentes con rotación) adaptado a
 * este sistema: la base guarda solo el hash SHA-256 del token, así que el token presentado en la cookie se
 * convierte en su hash antes de compararlo; cookie de 30 días, {@code HttpOnly}, {@code Secure} según la
 * configuración y {@code SameSite=Strict}. Además expone la escritura y el borrado de la cookie para
 * {@code RegistroSesionVaadin} (el token lo emite {@code GestorSesiones} al iniciar sesión, CU-11 paso 10) y
 * registra en el {@code SessionRegistry} la sesión creada por un ingreso con la cookie (CU-11 FA1, mensaje 1.3).
 *
 * <p>Agregado en la implementación (subclase del componente que nombra el diseño).
 *
 * <p>Casos de uso: CU-11, CU-12
 */
@Component
public class ServicioRecordarmeClub extends PersistentTokenBasedRememberMeServices {

    /** Registro de sesiones donde se anota la sesión creada por remember-me. */
    private final SessionRegistry registroSesiones;

    /**
     * Configura el servicio: clave, cuentas, almacén de tokens, vigencia de 30 días y atributos de la cookie.
     *
     * <p>Casos de uso: CU-11
     * @param clave clave de la aplicación para remember-me ({@code club.remember-me.clave})
     * @param cuentas servicio de cuentas para remember-me
     * @param almacenTokens almacén de tokens persistentes
     * @param registroSesiones registro de sesiones
     * @param cookieSegura si la cookie es {@code Secure} ({@code server.servlet.session.cookie.secure})
     */
    public ServicioRecordarmeClub(@Value("${club.remember-me.clave}") String clave, CuentasParaRecordarme cuentas,
                                  AlmacenTokensSesionPersistente almacenTokens, SessionRegistry registroSesiones,
                                  @Value("${server.servlet.session.cookie.secure:false}") boolean cookieSegura) {
        super(clave, cuentas, almacenTokens);
        this.registroSesiones = registroSesiones;
        setTokenValiditySeconds((int) TokenSesionPersistente.VIGENCIA.getSeconds());
        setUseSecureCookie(cookieSegura);
        setCookieCustomizer(cookie -> cookie.setAttribute("SameSite", "Strict"));
    }

    /**
     * Ingreso con la cookie: reemplaza el token presentado por su hash (lo que guarda la base) y delega en Spring
     * Security, que busca la serie ({@code getTokenForSeries}), compara, rota ({@code updateToken}) y reescribe la
     * cookie; si la serie existe con otro hash revoca todas las series de la cuenta ({@code removeUserTokens}).
     * Si el ingreso es válido, registra la sesión del servidor en el {@code SessionRegistry}.
     *
     * <p>Casos de uso: CU-11
     * @param tokensCookie serie y token decodificados de la cookie
     * @param request petición
     * @param response respuesta
     * @return principal de la cuenta
     */
    @Override
    protected UserDetails processAutoLoginCookie(String[] tokensCookie, HttpServletRequest request,
                                                 HttpServletResponse response) {
        String[] conHash = tokensCookie;
        if (tokensCookie.length == 2) {
            conHash = new String[] {tokensCookie[0], GestorSesiones.hashSha256(tokensCookie[1])};
        }
        UserDetails principal = super.processAutoLoginCookie(conHash, request, response);
        registroSesiones.registerNewSession(request.getSession(true).getId(), principal);
        return principal;
    }

    /**
     * Escribe la cookie remember-me con el valor {@code "serie:token"} que emitió {@code GestorSesiones}.
     *
     * <p>Casos de uso: CU-11
     * @param valorCookie valor {@code "serie:token"}
     * @param request petición
     * @param response respuesta
     */
    public void escribirCookie(String valorCookie, HttpServletRequest request, HttpServletResponse response) {
        String[] partes = valorCookie.split(":", 2);
        setCookie(partes, getTokenValiditySeconds(), request, response);
    }

    /**
     * Borra la cookie remember-me (CU-12, y CU-11 FA2 lo hace Spring Security).
     *
     * <p>Casos de uso: CU-12
     * @param request petición
     * @param response respuesta
     */
    public void borrarCookie(HttpServletRequest request, HttpServletResponse response) {
        cancelCookie(request, response);
    }
}

package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.TokenSesionPersistente;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.HexFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Componente que administra las sesiones del servidor (en memoria, registradas en el {@code SessionRegistry} de
 * Spring Security) y los tokens de «Mantener la sesión iniciada» (remember-me con tokens persistentes y
 * rotación). Lo usan las fachadas al iniciar y cerrar sesión, al cambiar o recuperar la contraseña, al modificar
 * datos de acceso y al desactivar una cuenta o un socio. Pure Fabrication (GRASP) que aísla a las fachadas de
 * Spring Security.
 *
 * <p>Principal de la sesión: en esta implementación el nombre del principal es el id de la cuenta en texto
 * ({@code String.valueOf(usuario.getId())}), tanto en el ingreso por formulario como en el ingreso por
 * remember-me; por eso las sesiones de una cuenta se buscan por ese nombre.
 *
 * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
 */
@Component
public class GestorSesiones {

    /** Bitácora técnica. */
    private static final Logger BITACORA = LoggerFactory.getLogger(GestorSesiones.class);

    /** Generador de series y tokens aleatorios. */
    private static final SecureRandom ALEATORIO = new SecureRandom();

    /** Sesiones activas del servidor por cuenta. */
    private final SessionRegistry registroSesiones;

    /** Tokens persistentes (se guarda solo su hash); adapta la persistencia a {@code PersistentTokenRepository}. */
    private final AlmacenTokensSesionPersistente almacenTokens;

    /** Guarda el {@code TokenSesionPersistente} nuevo. */
    private final FachadaPersistencia fachadaPersistencia;

    /** Vigencia del token persistente. */
    private final Duration duracionToken;

    /** Momento actual. */
    private final Reloj reloj;

    /**
     * Crea el gestor con sus colaboradores (inyectados por Spring); la vigencia del token es fija: 30 días.
     *
     * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
     * @param registroSesiones registro de sesiones de Spring Security
     * @param almacenTokens almacén de tokens persistentes
     * @param fachadaPersistencia fachada de persistencia
     * @param reloj reloj del sistema
     */
    public GestorSesiones(SessionRegistry registroSesiones, AlmacenTokensSesionPersistente almacenTokens,
                          FachadaPersistencia fachadaPersistencia, Reloj reloj) {
        this.registroSesiones = registroSesiones;
        this.almacenTokens = almacenTokens;
        this.fachadaPersistencia = fachadaPersistencia;
        this.reloj = reloj;
        this.duracionToken = TokenSesionPersistente.VIGENCIA;
    }

    /**
     * Crea una serie y un token aleatorios, crea el {@code TokenSesionPersistente} con el hash del token y
     * vencimiento a 30 días, lo guarda con {@code fachadaPersistencia.put(token)} y devuelve el valor de la cookie
     * (serie y token). Solo se invoca cuando {@code usuario.admiteSesionPersistente()} es verdadero (cuentas de
     * socio).
     *
     * <p>Casos de uso: CU-11
     * @param usuario Cuenta autenticada.
     * @return Valor de la cookie de sesión persistente ({@code "serie:token"}).
     */
    public String emitirTokenPersistente(Usuario usuario) {
        String serie = aleatorio();
        String token = aleatorio();
        TokenSesionPersistente t = new TokenSesionPersistente(serie, usuario, hashSha256(token), reloj.ahora());
        fachadaPersistencia.put(t);
        BITACORA.debug("Token persistente emitido para la cuenta {} (vigencia {})", usuario.getId(), duracionToken);
        return serie + ":" + token;
    }

    /**
     * Borra todas las series de tokens persistentes de la cuenta con {@code
     * almacenTokens.eliminarDeUsuario(usuario.getId())}.
     *
     * <p>Casos de uso: CU-03, CU-12, CU-13, CU-15, CU-17
     * @param usuario Cuenta.
     */
    public void revocarTokensDe(Usuario usuario) {
        int borrados = almacenTokens.eliminarDeUsuario(usuario.getId());
        BITACORA.debug("Tokens persistentes revocados de la cuenta {}: {}", usuario.getId(), borrados);
    }

    /**
     * Obtiene las sesiones de la cuenta con {@code registroSesiones.getAllSessions(principal, false)} y llama a
     * {@code expireNow()} en cada una; Vaadin cierra la interfaz en la siguiente petición. El principal se busca
     * entre {@code getAllPrincipals()} por su nombre (el id de la cuenta).
     *
     * <p>Casos de uso: CU-03, CU-13, CU-15, CU-17
     * @param usuario Cuenta cuyas sesiones se cierran.
     */
    public void invalidarSesionesDe(Usuario usuario) {
        String nombre = String.valueOf(usuario.getId());
        for (Object principal : registroSesiones.getAllPrincipals()) {
            if (nombre.equals(nombreDe(principal))) {
                for (SessionInformation sesion : registroSesiones.getAllSessions(principal, false)) {
                    sesion.expireNow();
                }
            }
        }
    }

    /**
     * Hash SHA-256 en hexadecimal (64 caracteres) de un token: se guarda solo el hash de los tokens persistentes y
     * de los de recuperación (07C).
     *
     * <p>Agregado en la implementación: lo comparten {@code GestorSesiones}, {@code AlmacenTokensSesionPersistente}
     * y {@code ServicioAccesos} (CU-13).
     *
     * <p>Casos de uso: CU-11, CU-13
     * @param valor token en claro
     * @return hash hexadecimal
     */
    public static String hashSha256(String valor) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(valor.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    /**
     * Valor aleatorio de 16 bytes en Base64 (el formato que usa Spring Security para series y tokens).
     *
     * <p>Casos de uso: CU-11
     * @return valor aleatorio
     */
    private static String aleatorio() {
        byte[] bytes = new byte[16];
        ALEATORIO.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Nombre de un principal del registro de sesiones.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-15, CU-17
     * @param principal principal registrado
     * @return su nombre
     */
    private static String nombreDe(Object principal) {
        return principal instanceof UserDetails detalles ? detalles.getUsername() : String.valueOf(principal);
    }
}

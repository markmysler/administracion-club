package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import java.time.Duration;
import org.springframework.security.core.session.SessionRegistry;

/**
 * Componente que administra las sesiones del servidor (en memoria, registradas en el {@code SessionRegistry} de
 * Spring Security) y los tokens de «Mantener la sesión iniciada» (remember-me con tokens persistentes y
 * rotación). Lo usan las fachadas al iniciar y cerrar sesión, al cambiar o recuperar la contraseña, al modificar
 * datos de acceso y al desactivar una cuenta o un socio.
 *
 * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
 */
public class GestorSesiones {

    /** Sesiones activas del servidor por cuenta. */
    private SessionRegistry registroSesiones;

    /** Tokens persistentes (se guarda solo su hash); adapta la persistencia a {@code PersistentTokenRepository}. */
    private AlmacenTokensSesionPersistente almacenTokens;

    /** Guarda el {@code TokenSesionPersistente} nuevo. */
    private FachadaPersistencia fachadaPersistencia;

    /** Vigencia del token persistente. */
    private Duration duracionToken;

    /** Momento actual. */
    private Reloj reloj;

    /**
     * Crea una serie y un token aleatorios, crea el {@code TokenSesionPersistente} con el hash del token y
     * vencimiento a 30 días, lo guarda con {@code fachadaPersistencia.put(token)} y devuelve el valor de la cookie
     * (serie y token). Solo se invoca cuando {@code usuario.admiteSesionPersistente()} es verdadero (cuentas de
     * socio).
     *
     * <p>Casos de uso: CU-11
     * @param usuario Cuenta autenticada.
     * @return Valor de la cookie de sesión persistente.
     */
    public String emitirTokenPersistente(Usuario usuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Borra todas las series de tokens persistentes de la cuenta con {@code
     * almacenTokens.eliminarDeUsuario(usuario.getId())}.
     *
     * <p>Casos de uso: CU-03, CU-12, CU-13, CU-15, CU-17
     * @param usuario Cuenta.
     */
    public void revocarTokensDe(Usuario usuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Obtiene las sesiones de la cuenta con {@code registroSesiones.getAllSessions(principal, false)} y llama a
     * {@code expireNow()} en cada una; Vaadin cierra la interfaz en la siguiente petición.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-15, CU-17
     * @param usuario Cuenta cuyas sesiones se cierran.
     */
    public void invalidarSesionesDe(Usuario usuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

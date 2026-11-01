package ar.edu.ub.club.accesos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDateTime;

/**
 * Token de un solo uso para restablecer la contraseña (CU-13). Navega a su cuenta por la asociación {@code
 * usuario}. Solo se guarda el hash SHA-256 del valor enviado por correo; vence a los 30 minutos (RN-05).
 *
 * <p>Entidad del dominio; la persiste {@code BrokerTokenRecuperacion}.
 *
 * <p>Casos de uso: CU-13
 */
public class TokenRecuperacion extends EntidadPersistente {

    /** Cuenta que solicitó la recuperación. */
    private Usuario usuario;

    /** Hash SHA-256 del token enviado. */
    private String tokenHash;

    /** Vencimiento. */
    private LocalDateTime expiraEn;

    /** Indica si ya se usó. */
    private boolean usado;

    /**
     * Crea un token nuevo, sin usar (CU-13 paso 6, {@code sec_cu13_p2.puml}).
     *
     * <p>Casos de uso: CU-13
     * @param usuario Cuenta que solicitó la recuperación.
     * @param tokenHash Hash SHA-256 del token enviado.
     * @param expiraEn Vencimiento (solicitud + 30 minutos).
     */
    public TokenRecuperacion(Usuario usuario, String tokenHash, LocalDateTime expiraEn) {
        this.usuario = usuario;
        this.tokenHash = tokenHash;
        this.expiraEn = expiraEn;
        this.usado = false;
    }

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: CU-13
     */
    private TokenRecuperacion() {
        // lo completa reconstituir
    }

    /**
     * No usado y no vencido (CU-13 paso 11; FA4 si es falso).
     *
     * <p>Casos de uso: CU-13
     * @param ahora Momento actual.
     * @return true si es válido.
     */
    public boolean esValido(LocalDateTime ahora) {
        return !usado && expiraEn.isAfter(ahora);
    }

    /**
     * Lo marca como usado (un solo uso; también invalida los tokens anteriores al pedir uno nuevo).
     *
     * <p>Casos de uso: CU-13
     */
    public void marcarUsado() {
        this.usado = true;
    }

    /**
     * Reconstituye el token desde la fila de {@code token_recuperacion} (la cuenta llega luego por
     * {@code restaurarAsociaciones}).
     *
     * <p>Casos de uso: CU-13
     * @param id identificador
     * @param tokenHash hash SHA-256
     * @param expiraEn vencimiento
     * @param usado si ya se usó
     * @return el token reconstituido
     */
    public static TokenRecuperacion reconstituir(Long id, String tokenHash, LocalDateTime expiraEn, boolean usado) {
        TokenRecuperacion t = new TokenRecuperacion();
        t.asignarId(id);
        t.tokenHash = tokenHash;
        t.expiraEn = expiraEn;
        t.usado = usado;
        return t;
    }

    /**
     * Entrega la cuenta dueña del token (la resuelve {@code BrokerTokenRecuperacion.cargarAsociaciones}).
     *
     * <p>Casos de uso: CU-13
     * @param usuario cuenta dueña
     */
    public void restaurarAsociaciones(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Devuelve {@code usuario}: Cuenta que solicitó la recuperación.
     *
     * <p>Casos de uso: CU-13
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code tokenHash}: Hash SHA-256 del token enviado.
     *
     * <p>Casos de uso: CU-13
     * @return el valor del atributo
     */
    public String getTokenHash() {
        return tokenHash;
    }

    /**
     * Devuelve {@code expiraEn}: Vencimiento.
     *
     * <p>Casos de uso: CU-13
     * @return el valor del atributo
     */
    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    /**
     * Devuelve {@code usado}: Indica si ya se usó.
     *
     * <p>Casos de uso: CU-13
     * @return el valor del atributo
     */
    public boolean getUsado() {
        return usado;
    }
}

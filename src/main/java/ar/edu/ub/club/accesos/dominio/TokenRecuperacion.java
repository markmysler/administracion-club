package ar.edu.ub.club.accesos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDateTime;

/**
 * Token de un solo uso para restablecer la contraseña (CU-13). Navega a su cuenta por la asociación {@code
 * usuario}.
 *
 * <p>Entidad del dominio; la persiste su broker.
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
     * Crea una instancia de {@code TokenRecuperacion}.
     *
     * <p>Casos de uso: CU-13
     * @param usuario valor de {@code usuario}
     * @param tokenHash valor de {@code tokenHash}
     * @param expiraEn valor de {@code expiraEn}
     */
    public TokenRecuperacion(Usuario usuario, String tokenHash, LocalDateTime expiraEn) {
        // PENDIENTE
    }

    /**
     * No usado y no vencido.
     *
     * <p>Casos de uso: CU-13
     * @param ahora Momento actual.
     * @return true si es válido.
     */
    public boolean esValido(LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Lo marca como usado.
     *
     * <p>Casos de uso: CU-13
     */
    public void marcarUsado() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-13
     * @param id valor de {@code id}
     * @param tokenHash valor de {@code tokenHash}
     * @param expiraEn valor de {@code expiraEn}
     * @param usado valor de {@code usado}
     * @return el resultado
     */
    public static TokenRecuperacion reconstituir(Long id, String tokenHash, LocalDateTime expiraEn, boolean usado) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-13
     * @param usuario valor de {@code usuario}
     */
    public void restaurarAsociaciones(Usuario usuario) {
        throw new UnsupportedOperationException("PENDIENTE");
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

package ar.edu.ub.club.accesos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class TokenSesionPersistente extends EntidadPersistente {

    /** Atributo serie. */
    private String serie;

    /** Atributo usuario. */
    private Usuario usuario;

    /** Atributo tokenHash. */
    private String tokenHash;

    /** Atributo ultimoUso. */
    private LocalDateTime ultimoUso;

    /** Atributo expiraEn. */
    private LocalDateTime expiraEn;

    /** Atributo persistido. */
    private boolean persistido;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param serie valor de {@code serie}
     * @param tokenHash valor de {@code tokenHash}
     * @param ultimoUso valor de {@code ultimoUso}
     * @param expiraEn valor de {@code expiraEn}
     * @return el resultado
     */
    public static TokenSesionPersistente reconstituir(String serie, String tokenHash, LocalDateTime ultimoUso, LocalDateTime expiraEn) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param usuario valor de {@code usuario}
     */
    public void restaurarAsociaciones(Usuario usuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code serie}: serie.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Devuelve {@code usuario}: usuario.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code tokenHash}: tokenHash.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getTokenHash() {
        return tokenHash;
    }

    /**
     * Devuelve {@code ultimoUso}: ultimoUso.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalDateTime getUltimoUso() {
        return ultimoUso;
    }

    /**
     * Devuelve {@code expiraEn}: expiraEn.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    /**
     * Devuelve {@code persistido}: persistido.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public boolean getPersistido() {
        return persistido;
    }
}

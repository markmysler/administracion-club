package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.TokenRecuperacion;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * Token de recuperación por el hash SHA-256 del valor recibido en el enlace (CU-13 parte 4).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-13
 */
public class TokenRecuperacionPorHash extends ConsultaEntidades<TokenRecuperacion> {

    /** Hash SHA-256 (hexadecimal) del token. */
    private final String tokenHash;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-13
     * @param tokenHash hash SHA-256 (hexadecimal) del token.
     */
    public TokenRecuperacionPorHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    /**
     * Clase de resultado; elige el broker (o los brokers de la jerarquía) que la resuelve.
     *
     * <p>Casos de uso: CU-13
     * @return {@code TokenRecuperacion.class}
     */
    @Override
    public Class<TokenRecuperacion> claseResultado() {
        return TokenRecuperacion.class;
    }

    /**
     * Condición y orden que se agregan a {@code consultaBase()} del broker.
     *
     * <p>Casos de uso: CU-13
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE token_recuperacion.token_hash = ?";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-13
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(tokenHash);
    }
}

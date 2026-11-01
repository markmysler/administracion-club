package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.TokenRecuperacion;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Tokens de recuperación de una cuenta que todavía no se usaron ni vencieron; se invalidan al pedir uno nuevo (CU-13 parte 2).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-13
 */
public class TokensRecuperacionVigentes extends ConsultaEntidades<TokenRecuperacion> {

    /** Cuenta dueña de los tokens. */
    private final Long idUsuario;

    /** Momento actual. */
    private final LocalDateTime ahora;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-13
     * @param idUsuario cuenta dueña de los tokens.
     * @param ahora momento actual.
     */
    public TokensRecuperacionVigentes(Long idUsuario, LocalDateTime ahora) {
        this.idUsuario = idUsuario;
        this.ahora = ahora;
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
        return "WHERE token_recuperacion.usuario_id = ? AND token_recuperacion.usado = false AND token_recuperacion.expira_en > ?";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-13
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(idUsuario, ahora);
    }
}

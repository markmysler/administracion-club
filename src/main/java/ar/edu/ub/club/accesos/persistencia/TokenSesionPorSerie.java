package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.TokenSesionPersistente;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * Token de sesión persistente por su serie (clave), leído de la cookie remember-me (CU-11 FA1/FA2).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-11
 */
public class TokenSesionPorSerie extends ConsultaEntidades<TokenSesionPersistente> {

    /** Serie del token. */
    private final String serie;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-11
     * @param serie serie del token.
     */
    public TokenSesionPorSerie(String serie) {
        this.serie = serie;
    }

    /**
     * Clase de resultado; elige el broker (o los brokers de la jerarquía) que la resuelve.
     *
     * <p>Casos de uso: CU-11
     * @return {@code TokenSesionPersistente.class}
     */
    @Override
    public Class<TokenSesionPersistente> claseResultado() {
        return TokenSesionPersistente.class;
    }

    /**
     * Condición y orden que se agregan a {@code consultaBase()} del broker.
     *
     * <p>Casos de uso: CU-11
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE token_sesion_persistente.serie = ?";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-11
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(serie);
    }
}

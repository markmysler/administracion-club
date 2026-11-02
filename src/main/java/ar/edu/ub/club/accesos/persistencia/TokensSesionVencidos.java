package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.TokenSesionPersistente;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Tokens persistentes vencidos, que borra la tarea de mantenimiento ({@code AlmacenTokensSesionPersistente.eliminarVencidos}).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-11
 */
public class TokensSesionVencidos extends ConsultaEntidades<TokenSesionPersistente> {

    /** Momento actual. */
    private final LocalDateTime ahora;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-11
     * @param ahora momento actual.
     */
    public TokensSesionVencidos(LocalDateTime ahora) {
        this.ahora = ahora;
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
        return "WHERE token_sesion_persistente.expira_en <= ?";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-11
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(ahora);
    }
}

package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.TokenSesionPersistente;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * Todas las series de tokens persistentes de una cuenta; se borran al cerrar sesión, cambiar o recuperar la contraseña, modificar datos de acceso, desactivar la cuenta o detectar un posible robo del token.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
 */
public class TokensSesionDeUsuario extends ConsultaEntidades<TokenSesionPersistente> {

    /** Cuenta dueña. */
    private final Long idUsuario;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
     * @param idUsuario cuenta dueña.
     */
    public TokensSesionDeUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Clase de resultado; elige el broker (o los brokers de la jerarquía) que la resuelve.
     *
     * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
     * @return {@code TokenSesionPersistente.class}
     */
    @Override
    public Class<TokenSesionPersistente> claseResultado() {
        return TokenSesionPersistente.class;
    }

    /**
     * Condición y orden que se agregan a {@code consultaBase()} del broker.
     *
     * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE token_sesion_persistente.usuario_id = ?";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(idUsuario);
    }
}

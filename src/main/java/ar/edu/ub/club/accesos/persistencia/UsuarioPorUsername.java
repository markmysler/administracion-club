package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * Cuenta (Socio o Administrador) con ese nombre de usuario exacto. Consulta polimórfica: la resuelve cada broker de la jerarquía {@code Usuario} (INNER JOIN con su tabla hija) y se unen los resultados. Se usa con {@code buscarUno} para iniciar sesión (CU-11 paso 4) y con {@code contar} para verificar la unicidad (CU-01, CU-14, CU-15, CU-16).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-01, CU-11, CU-14, CU-15, CU-16
 */
public class UsuarioPorUsername extends ConsultaEntidades<Usuario> {

    /** Nombre de usuario buscado. */
    private final String username;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-01, CU-11, CU-14, CU-15, CU-16
     * @param username nombre de usuario buscado.
     */
    public UsuarioPorUsername(String username) {
        this.username = username;
    }

    /**
     * Clase de resultado; elige el broker (o los brokers de la jerarquía) que la resuelve.
     *
     * <p>Casos de uso: CU-01, CU-11, CU-14, CU-15, CU-16
     * @return {@code Usuario.class}
     */
    @Override
    public Class<Usuario> claseResultado() {
        return Usuario.class;
    }

    /**
     * Condición y orden que se agregan a {@code consultaBase()} del broker.
     *
     * <p>Casos de uso: CU-01, CU-11, CU-14, CU-15, CU-16
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE usuario.username = ?";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-01, CU-11, CU-14, CU-15, CU-16
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(username);
    }
}

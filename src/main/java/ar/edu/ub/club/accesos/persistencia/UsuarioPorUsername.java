package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: todos
 */
public class UsuarioPorUsername extends ConsultaEntidades<Usuario> {

    /** Atributo username. */
    private final String username;

    /**
     * TEMPORAL (API acordada de accesos).
     *
     * <p>Casos de uso: todos
     * @param username valor buscado
     */
    public UsuarioPorUsername(String username) {
        this.username = username;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    public Class<Usuario> claseResultado() {
        return Usuario.class;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    @Override
    public String sql() {
        return "WHERE usuario.username = ?";
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        return List.of(username);
    }
}

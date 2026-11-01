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
public class UsuarioPorEmail extends ConsultaEntidades<Usuario> {

    /** Atributo email. */
    private final String email;

    /**
     * TEMPORAL (API acordada de accesos).
     *
     * <p>Casos de uso: todos
     * @param email valor buscado
     */
    public UsuarioPorEmail(String email) {
        this.email = email;
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
        return "WHERE usuario.email = ?";
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        return List.of(email);
    }
}

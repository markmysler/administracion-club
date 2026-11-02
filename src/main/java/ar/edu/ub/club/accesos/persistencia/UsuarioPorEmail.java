package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * Cuenta (Socio o Administrador) con ese correo, sin distinguir mayúsculas. Consulta polimórfica de la jerarquía {@code Usuario}. Se usa para la recuperación de contraseña (CU-13 paso 5) y para verificar la unicidad del correo (CU-01, CU-14, CU-15, CU-16).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15, CU-16
 */
public class UsuarioPorEmail extends ConsultaEntidades<Usuario> {

    /** Correo buscado. */
    private final String email;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15, CU-16
     * @param email correo buscado.
     */
    public UsuarioPorEmail(String email) {
        this.email = email;
    }

    /**
     * Clase de resultado; elige el broker (o los brokers de la jerarquía) que la resuelve.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15, CU-16
     * @return {@code Usuario.class}
     */
    @Override
    public Class<Usuario> claseResultado() {
        return Usuario.class;
    }

    /**
     * Condición y orden que se agregan a {@code consultaBase()} del broker.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15, CU-16
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE lower(usuario.email) = lower(?)";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15, CU-16
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(email);
    }
}

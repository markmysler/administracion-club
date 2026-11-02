package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * Administradores en estado ACTIVA. Bloquea sus filas de {@code usuario} ({@code FOR UPDATE OF usuario}) para que dos desactivaciones simultáneas no dejen el club sin administradores activos (CU-17 FA3, {@code sec_cu17_p2.puml}: «contar con bloqueo»).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-17
 */
public class AdministradoresActivos extends ConsultaEntidades<Administrador> {

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-17
     */
    public AdministradoresActivos() {
    }

    /**
     * Clase de resultado; elige el broker (o los brokers de la jerarquía) que la resuelve.
     *
     * <p>Casos de uso: CU-17
     * @return {@code Administrador.class}
     */
    @Override
    public Class<Administrador> claseResultado() {
        return Administrador.class;
    }

    /**
     * Condición y orden que se agregan a {@code consultaBase()} del broker.
     *
     * <p>Casos de uso: CU-17
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE usuario.estado = ? ORDER BY usuario.id FOR UPDATE OF usuario";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-17
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of("ACTIVA");
    }
}

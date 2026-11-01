package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * Todas las cuentas de administrador, activas e inactivas, ordenadas por apellido y nombre (P-13).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-14, CU-15, CU-17
 */
public class Administradores extends ConsultaEntidades<Administrador> {

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     */
    public Administradores() {
    }

    /**
     * Clase de resultado; elige el broker (o los brokers de la jerarquía) que la resuelve.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @return {@code Administrador.class}
     */
    @Override
    public Class<Administrador> claseResultado() {
        return Administrador.class;
    }

    /**
     * Condición y orden que se agregan a {@code consultaBase()} del broker.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "ORDER BY administrador.apellido, administrador.nombre";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}

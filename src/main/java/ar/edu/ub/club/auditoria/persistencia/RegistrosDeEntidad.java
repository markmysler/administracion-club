package ar.edu.ub.club.auditoria.persistencia;

import ar.edu.ub.club.auditoria.dominio.RegistroAuditoria;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * Registros de auditoría de una entidad, del más reciente al más antiguo (consulta del historial de cambios, RNF-12).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: todos
 */
public class RegistrosDeEntidad extends ConsultaEntidades<RegistroAuditoria> {

    /** Nombre de la entidad (por ejemplo "Socio"). */
    private final String entidad;

    /** Identificador de la entidad. */
    private final Long idEntidad;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: todos
     * @param entidad nombre de la entidad (por ejemplo "Socio").
     * @param idEntidad identificador de la entidad.
     */
    public RegistrosDeEntidad(String entidad, Long idEntidad) {
        this.entidad = entidad;
        this.idEntidad = idEntidad;
    }

    /**
     * Clase de resultado; elige el broker (o los brokers de la jerarquía) que la resuelve.
     *
     * <p>Casos de uso: todos
     * @return {@code RegistroAuditoria.class}
     */
    @Override
    public Class<RegistroAuditoria> claseResultado() {
        return RegistroAuditoria.class;
    }

    /**
     * Condición y orden que se agregan a {@code consultaBase()} del broker.
     *
     * <p>Casos de uso: todos
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE registro_auditoria.entidad = ? AND registro_auditoria.entidad_id = ? ORDER BY registro_auditoria.fecha_hora DESC";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: todos
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(entidad, idEntidad);
    }
}

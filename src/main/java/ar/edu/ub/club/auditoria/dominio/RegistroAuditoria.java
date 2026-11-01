package ar.edu.ub.club.auditoria.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class RegistroAuditoria extends EntidadPersistente {

    /** Atributo usuario. */
    private Usuario usuario;

    /** Atributo fechaHora. */
    private LocalDateTime fechaHora;

    /** Atributo accion. */
    private String accion;

    /** Atributo entidad. */
    private String entidad;

    /** Atributo idEntidad. */
    private Long idEntidad;

    /** Atributo detalle. */
    private String detalle;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param id valor de {@code id}
     * @param fechaHora valor de {@code fechaHora}
     * @param accion valor de {@code accion}
     * @param entidad valor de {@code entidad}
     * @param idEntidad valor de {@code idEntidad}
     * @param detalle valor de {@code detalle}
     * @return el resultado
     */
    public static RegistroAuditoria reconstituir(Long id, LocalDateTime fechaHora, String accion, String entidad, Long idEntidad, String detalle) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param usuario valor de {@code usuario}
     */
    public void restaurarAsociaciones(Usuario usuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code usuario}: usuario.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code fechaHora}: fechaHora.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Devuelve {@code accion}: accion.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getAccion() {
        return accion;
    }

    /**
     * Devuelve {@code entidad}: entidad.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * Devuelve {@code idEntidad}: idEntidad.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getIdEntidad() {
        return idEntidad;
    }

    /**
     * Devuelve {@code detalle}: detalle.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getDetalle() {
        return detalle;
    }
}

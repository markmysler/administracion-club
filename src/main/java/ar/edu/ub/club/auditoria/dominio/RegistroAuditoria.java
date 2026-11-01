package ar.edu.ub.club.auditoria.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDateTime;

/**
 * Registro inmutable de una operación relevante (alta, baja, modificación, pago, reserva) para la trazabilidad
 * exigida por RNF-12: quién (cuenta, nula en procesos automáticos), cuándo, qué acción, sobre qué entidad y un
 * detalle en JSON sin credenciales.
 *
 * <p>Entidad del dominio; la persiste {@code BrokerRegistroAuditoria} (tabla {@code registro_auditoria}).
 *
 * <p>Casos de uso: todos
 */
public class RegistroAuditoria extends EntidadPersistente {

    /** Cuenta que realizó la acción (nula en procesos automáticos). */
    private Usuario usuario;

    /** Momento de la acción. */
    private LocalDateTime fechaHora;

    /** Acción: ALTA, MODIFICACION, BAJA, PAGO, etc. */
    private String accion;

    /** Nombre de la entidad afectada. */
    private String entidad;

    /** Identificador de la entidad afectada. */
    private Long idEntidad;

    /** Detalle en JSON. */
    private String detalle;

    /**
     * Crea un registro nuevo (lo usa {@code ServicioAuditoria.registrar}).
     *
     * <p>Agregado en la implementación: el diseño solo definía {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     * @param usuario cuenta que actúa o {@code null}
     * @param fechaHora momento
     * @param accion acción
     * @param entidad entidad
     * @param idEntidad id de la entidad
     * @param detalle detalle JSON
     */
    public RegistroAuditoria(Usuario usuario, LocalDateTime fechaHora, String accion, String entidad, Long idEntidad,
                             String detalle) {
        this.usuario = usuario;
        this.fechaHora = fechaHora;
        this.accion = accion;
        this.entidad = entidad;
        this.idEntidad = idEntidad;
        this.detalle = detalle;
    }

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     */
    private RegistroAuditoria() {
        // lo completa reconstituir
    }

    /**
     * Reconstituye un registro desde la fila de {@code registro_auditoria}.
     *
     * <p>Casos de uso: todos
     * @param id identificador
     * @param fechaHora momento
     * @param accion acción
     * @param entidad entidad
     * @param idEntidad id de la entidad
     * @param detalle detalle JSON
     * @return el registro
     */
    public static RegistroAuditoria reconstituir(Long id, LocalDateTime fechaHora, String accion, String entidad, Long idEntidad, String detalle) {
        RegistroAuditoria r = new RegistroAuditoria();
        r.asignarId(id);
        r.fechaHora = fechaHora;
        r.accion = accion;
        r.entidad = entidad;
        r.idEntidad = idEntidad;
        r.detalle = detalle;
        return r;
    }

    /**
     * Entrega la cuenta que actuó (la resuelve el broker; puede ser nula).
     *
     * <p>Casos de uso: todos
     * @param usuario cuenta o {@code null}
     */
    public void restaurarAsociaciones(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Devuelve {@code usuario}.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code fechaHora}.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Devuelve {@code accion}.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getAccion() {
        return accion;
    }

    /**
     * Devuelve {@code entidad}.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * Devuelve {@code idEntidad}.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Long getIdEntidad() {
        return idEntidad;
    }

    /**
     * Devuelve {@code detalle}.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getDetalle() {
        return detalle;
    }
}

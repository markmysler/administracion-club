package ar.edu.ub.club.auditoria.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.auditoria.dominio.RegistroAuditoria;
import ar.edu.ub.club.auditoria.persistencia.RegistrosDeEntidad;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicación que escribe {@code RegistroAuditoria} (RNF-12). Lo usan las fachadas en altas, bajas y
 * modificaciones de socios, pagos, cuentas y categorías; para reservas lo usa el observador {@code
 * AuditorReservas}. Pure Fabrication (GRASP): la auditoría es transversal y no pertenece a ninguna entidad.
 *
 * <p>Casos de uso: todos
 */
@Service
public class ServicioAuditoria {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code RegistrosDeEntidad}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Fecha y hora del registro. */
    private final Reloj reloj;

    /**
     * Crea el servicio.
     *
     * <p>Casos de uso: todos
     * @param fachadaPersistencia fachada de persistencia
     * @param reloj reloj del sistema
     */
    public ServicioAuditoria(FachadaPersistencia fachadaPersistencia, Reloj reloj) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.reloj = reloj;
    }

    /**
     * Crea y guarda un {@code RegistroAuditoria} dentro de la transacción del llamador; si la transacción se
     * revierte, el registro también. Se ejecuta con propagación {@code MANDATORY}: fuera de una transacción lanza
     * {@code IllegalTransactionStateException}.
     *
     * <p>Casos de uso: todos
     * @param usuario Cuenta que realiza la acción (nulo en procesos automáticos).
     * @param accion ALTA, MODIFICACION, BAJA, PAGO, etc.
     * @param entidad Nombre de la entidad.
     * @param id Identificador de la entidad.
     * @param detalle Datos relevantes en JSON, sin credenciales.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void registrar(Usuario usuario, String accion, String entidad, Long id, String detalle) {
        fachadaPersistencia.put(new RegistroAuditoria(usuario, reloj.ahora(), accion, entidad, id, detalle));
    }

    /**
     * Historial de auditoría de una entidad, del más reciente al más antiguo
     * ({@code fachadaPersistencia.buscar(RegistrosDeEntidad(entidad, id))}).
     *
     * <p>Agregado en la implementación: 07C documenta la consulta {@code RegistrosDeEntidad}
     * ({@code RepositorioAuditoria.buscarPorEntidad}) pero ninguna fachada la exponía.
     *
     * <p>Casos de uso: todos
     * @param entidad nombre de la entidad
     * @param id identificador de la entidad
     * @return registros
     */
    @Transactional(readOnly = true)
    public List<RegistroAuditoria> consultarHistorial(String entidad, Long id) {
        return fachadaPersistencia.buscar(new RegistrosDeEntidad(entidad, id));
    }
}

package ar.edu.ub.club.auditoria.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;

/**
 * Servicio de aplicación que escribe {@code RegistroAuditoria} (RNF-12). Lo usan las fachadas en altas, bajas y
 * modificaciones de socios, pagos, cuentas y categorías; para reservas lo usa el observador {@code
 * AuditorReservas}.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
 */
public class ServicioAuditoria {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code RegistrosDeEntidad}). */
    private FachadaPersistencia fachadaPersistencia;

    /** Fecha y hora del registro. */
    private Reloj reloj;

    /**
     * Crea y guarda un {@code RegistroAuditoria} dentro de la transacción del llamador; si la transacción se
     * revierte, el registro también.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06, CU-07, CU-08, CU-09, CU-14, CU-15, CU-17, CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-24, CU-25, CU-27, CU-28, CU-30, CU-32, CU-33, CU-34, CU-35, CU-36, CU-41; tareas programadas: vencimiento de pagos online pendientes
     * @param usuario Cuenta que realiza la acción (nulo en procesos automáticos).
     * @param accion ALTA, MODIFICACION, BAJA, PAGO, etc.
     * @param entidad Nombre de la entidad.
     * @param id Identificador de la entidad.
     * @param detalle Datos relevantes en JSON, sin credenciales.
     */
    public void registrar(Usuario usuario, String accion, String entidad, Long id, String detalle) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

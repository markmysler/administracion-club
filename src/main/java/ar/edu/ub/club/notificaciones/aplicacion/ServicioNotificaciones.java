package ar.edu.ub.club.notificaciones.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import java.util.List;

/**
 * Fachada de consulta del módulo notificaciones. El envío de las notificaciones lo hace el observador {@code
 * NotificadorReservas}; esta fachada solo expone su estado.
 *
 * <p>Casos de uso: CU-29, CU-31
 */
public class ServicioNotificaciones {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code NotificacionesDeReserva}). */
    private FachadaPersistencia fachadaPersistencia;

    /** Verifica permisos. */
    private ControlAcceso controlAcceso;

    /**
     * Obtiene {@code fachadaPersistencia.buscar(NotificacionesDeReserva(idReserva))}. Si la cuenta no tiene
     * GESTIONAR_RESERVAS_CLUB, solo las devuelve si el socio destinatario es la cuenta de la sesión (RN-20); la
     * verificación usa el socio de la notificación, sin consultar el módulo reservas.
     *
     * <p>Permiso: {@code GESTIONAR_RESERVAS_CLUB}.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param usuario Cuenta de la sesión.
     * @param idReserva Reserva.
     * @return Lista de {@code Notificacion}.
     */
    public List<Notificacion> consultarPorReserva(Usuario usuario, Long idReserva) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

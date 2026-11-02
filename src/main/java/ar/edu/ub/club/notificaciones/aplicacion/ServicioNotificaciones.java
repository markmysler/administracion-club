package ar.edu.ub.club.notificaciones.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import ar.edu.ub.club.notificaciones.persistencia.NotificacionesDeReserva;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada de consulta del módulo notificaciones. El envío de las notificaciones lo hace el observador {@code
 * NotificadorReservas}; esta fachada solo expone su estado. Clase anotada con {@code @Service} y
 * {@code @Transactional(readOnly = true)}.
 *
 * <p>Casos de uso: CU-29, CU-31
 */
@Service
@Transactional(readOnly = true)
public class ServicioNotificaciones {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code NotificacionesDeReserva}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /**
     * Crea la fachada (bean de Spring).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param fachadaPersistencia fachada de persistencia
     * @param controlAcceso verificación de permisos
     */
    public ServicioNotificaciones(FachadaPersistencia fachadaPersistencia, ControlAcceso controlAcceso) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.controlAcceso = controlAcceso;
    }

    /**
     * Obtiene {@code fachadaPersistencia.buscar(NotificacionesDeReserva(idReserva))}. Si la cuenta no tiene
     * GESTIONAR_RESERVAS_CLUB, exige GESTIONAR_RESERVAS_PROPIAS y solo las devuelve si el socio destinatario es la
     * cuenta de la sesión (RN-20); la verificación usa el socio de la notificación, sin consultar el módulo
     * reservas (sec_cu29, sec_cu31_p3).
     *
     * <p>Permiso: {@code GESTIONAR_RESERVAS_CLUB} o {@code GESTIONAR_RESERVAS_PROPIAS}.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param usuario Cuenta de la sesión.
     * @param idReserva Reserva.
     * @return Lista de {@code Notificacion}.
     * @throws AccesoDenegadoException si las notificaciones son de otro socio
     */
    public List<Notificacion> consultarPorReserva(Usuario usuario, Long idReserva) {
        boolean administrativo = controlAcceso.tienePermiso(usuario, Permiso.GESTIONAR_RESERVAS_CLUB);
        if (!administrativo) {
            controlAcceso.verificar(usuario, Permiso.GESTIONAR_RESERVAS_PROPIAS);
        }
        List<Notificacion> notificaciones = fachadaPersistencia.buscar(new NotificacionesDeReserva(idReserva));
        if (!administrativo) {
            for (Notificacion n : notificaciones) {
                if (n.getSocio() == null || !Objects.equals(n.getSocio().getId(), usuario.getId())) {
                    throw new AccesoDenegadoException();
                }
            }
        }
        return notificaciones;
    }
}

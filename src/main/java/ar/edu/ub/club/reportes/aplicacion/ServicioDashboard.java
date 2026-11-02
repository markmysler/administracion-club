package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.reportes.persistencia.ConsultasReportes;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada del panel de administración (patrón Facade, GRASP Controlador de CU-40): calcula los indicadores con
 * consultas de lectura de {@code ConsultasReportes}. Transacción de solo lectura; la primera instrucción es {@code
 * controlAcceso.verificar(usuario, CONSULTAR_DASHBOARD)} (RNF-04). Diagrama sec_cu40.
 *
 * <p>Casos de uso: CU-40
 */
@Service
@Transactional(readOnly = true)
public class ServicioDashboard {

    /** Consultas de lectura agregadas. */
    private final ConsultasReportes consultas;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea la fachada con sus dependencias.
     *
     * <p>Casos de uso: CU-40
     * @param consultas consultas de reportes
     * @param controlAcceso control de acceso
     * @param usuarioActual cuenta de la sesión
     */
    public ServicioDashboard(ConsultasReportes consultas, ControlAcceso controlAcceso, UsuarioActual usuarioActual) {
        this.consultas = consultas;
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Calcula socios activos, reservas del día y de la semana, cuotas pendientes y vencidas, recaudación y cuotas
     * pagadas del mes y canchas más usadas del mes (pasos 3 a 7 de CU-40). Un indicador que falla queda en {@code
     * Indicadores.noDisponibles} (FA1).
     *
     * <p>Permiso: CONSULTAR_DASHBOARD · Transacción: solo lectura.
     *
     * <p>Casos de uso: CU-40
     * @param hoy fecha de referencia (la del {@code Reloj}, paso 2)
     * @return los indicadores
     * @throws ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException sin el permiso
     */
    public Indicadores obtenerIndicadores(LocalDate hoy) {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.CONSULTAR_DASHBOARD);
        return consultas.indicadores(hoy);
    }
}

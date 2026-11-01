package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.reportes.persistencia.ConsultasReportes;
import java.time.LocalDate;

/**
 * Fachada del panel de administración: calcula los indicadores con consultas de lectura.
 *
 * <p>Casos de uso: CU-40
 */
public class ServicioDashboard {

    /** Consultas de lectura agregadas. */
    private ConsultasReportes consultas;

    /** Verifica permisos. */
    private ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private UsuarioActual usuarioActual;

    /**
     * Calcula socios activos, reservas del día y de la semana, cuotas pendientes y vencidas y recaudación del mes.
     *
     * <p>Permiso: {@code CONSULTAR_DASHBOARD}.
     *
     * <p>Casos de uso: CU-40
     * @param hoy Fecha de referencia.
     * @return {@code Indicadores}.
     */
    public Indicadores obtenerIndicadores(LocalDate hoy) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

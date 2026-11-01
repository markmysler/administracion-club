package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.agenda.aplicacion.FiltroDisponibilidad;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.socios.aplicacion.CriterioBusquedaSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;

/**
 * Presentador de la pantalla P-19: Grilla de turnos por cancha y horario para una fecha, con filtros por deporte
 * y cancha.
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28
 */
public class PresentadorDisponibilidadCanchas extends PresentadorBase<PantallaDisponibilidadCanchas> {

    /** Atributo servicioRecursos. */
    private ServicioRecursos servicioRecursos;

    /** Atributo servicioAgenda. */
    private ServicioAgenda servicioAgenda;

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /** Atributo usuarioActual. */
    private UsuarioActual usuarioActual;

    /** Atributo controlAcceso. */
    private ControlAcceso controlAcceso;

    /** Atributo idSocioElegido. */
    private Long idSocioElegido;

    /**
     * {@code ServicioRecursos.listarDeportes()} y {@code listarCanchas(null)} (filtros); {@code
     * ServicioAgenda.consultarReglas(): ReglaReserva} para acotar el calendario de fechas a la anticipación mínima y
     * máxima (RN-13) → {@code pantalla.mostrarRangoCalendario(desde, hasta)}; {@code
     * pantalla.mostrarEleccionSocio(controlAcceso.tienePermiso(usuarioActual.obtener(), GESTIONAR_RESERVAS_CLUB))}
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioRecursos.listarCanchas(idDeporte)} → {@code pantalla.mostrarCanchas(opciones)}
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param idDeporte valor de {@code idDeporte}
     */
    public void alCambiarDeporte(Long idDeporte) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAgenda.consultarDisponibilidad(filtro): List&lt;TurnoDisponible&gt;} → {@code
     * FilaTurno.desde(t, esAdministrador)} ({@code esAdministrador} = {@code
     * controlAcceso.tienePermiso(usuarioActual.obtener(), GESTIONAR_RESERVAS_CLUB)})
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param filtro valor de {@code filtro}
     */
    public void alConsultar(FiltroDisponibilidad filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioSocios.buscarSocios(criterio)} → {@code FilaSocio}
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param criterio valor de {@code criterio}
     */
    public void alBuscarSocio(CriterioBusquedaSocio criterio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioSocios.consultarSocio(idSocio): FichaSocio} → {@code
     * pantalla.mostrarSocioElegido(FilaSocio.desde(ficha.socio), FilaCuota.desde(cuotaVigente, false))}, con la
     * cuota más reciente de {@code ficha.estadoCuenta} («Cuota del grupo»; null si no hay)
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param idSocio valor de {@code idSocio}
     */
    public void alElegirSocio(Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code pantalla.abrirConfirmacion(turno, idSocioElegido)} (P-20)
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param turno valor de {@code turno}
     */
    public void alElegirTurno(FilaTurno turno) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

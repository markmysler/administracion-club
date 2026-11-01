package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.notificaciones.aplicacion.ServicioNotificaciones;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.reservas.aplicacion.FiltroReservas;
import ar.edu.ub.club.reservas.aplicacion.ServicioReservas;

/**
 * Presentador de la pantalla P-22: Reservas del club con filtros, acceso a modificar y cancelación
 * administrativa con motivo.
 *
 * <p>Casos de uso: CU-31, CU-32, CU-33
 */
public class PresentadorReservasClub extends PresentadorBase<PantallaReservasClub> {

    /** Atributo servicioRecursos. */
    private ServicioRecursos servicioRecursos;

    /** Atributo servicioReservas. */
    private ServicioReservas servicioReservas;

    /** Atributo servicioNotificaciones. */
    private ServicioNotificaciones servicioNotificaciones;

    /** Atributo servicioAgenda. */
    private ServicioAgenda servicioAgenda;

    /** Atributo usuarioActual. */
    private UsuarioActual usuarioActual;

    /** Atributo ultimoFiltro. */
    private FiltroReservas ultimoFiltro;

    /**
     * {@code ServicioRecursos.listarDeportes()} (filtros, primera vez) y {@code
     * ServicioReservas.consultarReservasClub(filtro): List&lt;Reserva&gt;} → {@code FilaReserva}
     *
     * <p>Casos de uso: CU-31, CU-33
     * @param filtro valor de {@code filtro}
     */
    public void cargar(FiltroReservas filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioReservas.consultarReserva(idReserva)} y {@code
     * ServicioNotificaciones.consultarPorReserva(usuarioActual.obtener(), idReserva)} y {@code
     * ServicioAgenda.consultarReglas()} → {@code DetalleReserva.desde(r, notificaciones, regla)}
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @param idReserva valor de {@code idReserva}
     */
    public void alSeleccionar(Long idReserva) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioReservas.cancelarReservaAdministrativa(usuarioActual.obtenerAdministrador(), idReserva,
     * motivo)}; recarga; {@code alConflicto} → «la reserva cambió» y recarga
     *
     * <p>Casos de uso: CU-31, CU-33
     * @param idReserva valor de {@code idReserva}
     * @param motivo valor de {@code motivo}
     */
    public void alCancelar(Long idReserva, String motivo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("reservas-club/modificar/" + idReserva)}
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @param idReserva valor de {@code idReserva}
     */
    public void alModificar(Long idReserva) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-33
     * @param ex valor de {@code ex}
     */
    protected void alConflicto(ConcurrenciaException ex) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

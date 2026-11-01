package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.notificaciones.aplicacion.ServicioNotificaciones;
import ar.edu.ub.club.reservas.aplicacion.FiltroReservas;
import ar.edu.ub.club.reservas.aplicacion.ServicioReservas;
import ar.edu.ub.club.socios.aplicacion.SocioActual;

/**
 * Presentador de la pantalla P-21: Reservas propias vigentes e históricas con acción de cancelar dentro del
 * plazo.
 *
 * <p>Casos de uso: CU-29, CU-30
 */
public class PresentadorMisReservas extends PresentadorBase<PantallaMisReservas> {

    /** Atributo servicioReservas. */
    private ServicioReservas servicioReservas;

    /** Atributo servicioNotificaciones. */
    private ServicioNotificaciones servicioNotificaciones;

    /** Atributo servicioAgenda. */
    private ServicioAgenda servicioAgenda;

    /** Atributo socioActual. */
    private SocioActual socioActual;

    /** Atributo usuarioActual. */
    private UsuarioActual usuarioActual;

    /** Atributo ultimoFiltro. */
    private FiltroReservas ultimoFiltro;

    /**
     * {@code ServicioReservas.consultarReservasPropias(socioActual.obtener(), filtro): List&lt;Reserva&gt;} → {@code
     * FilaReserva.desde(r)}
     *
     * <p>Casos de uso: CU-29, CU-30
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
     * <p>Casos de uso: CU-29, CU-30
     * @param idReserva valor de {@code idReserva}
     */
    public void alVerDetalle(Long idReserva) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioReservas.cancelarReservaPropia(socioActual.obtener(), idReserva)}; luego {@code
     * cargar(filtro)}; {@code alConflicto} → «la reserva cambió» y recarga
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param idReserva valor de {@code idReserva}
     */
    public void alCancelar(Long idReserva) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-30
     * @param ex valor de {@code ex}
     */
    protected void alConflicto(ConcurrenciaException ex) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

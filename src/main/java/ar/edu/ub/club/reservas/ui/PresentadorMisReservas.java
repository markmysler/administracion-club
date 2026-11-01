package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.notificaciones.aplicacion.ServicioNotificaciones;
import ar.edu.ub.club.reservas.aplicacion.FiltroReservas;
import ar.edu.ub.club.reservas.aplicacion.ServicioReservas;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.socios.aplicacion.SocioActual;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-21 (MVP, Vista Pasiva): reservas propias vigentes e históricas con acción de
 * cancelar dentro del plazo. Compone el detalle con dos fachadas (reservas y notificaciones), porque reservas no
 * depende de notificaciones (sec_cu29, sec_cu30_p1).
 *
 * <p>Casos de uso: CU-29, CU-30
 */
@SpringComponent
@UIScope
public class PresentadorMisReservas extends PresentadorBase<PantallaMisReservas> {

    /** Fachada de reservas. */
    private final ServicioReservas servicioReservas;

    /** Fachada de notificaciones (estado de los avisos del detalle). */
    private final ServicioNotificaciones servicioNotificaciones;

    /** Fachada de agenda (plazos de la regla de reserva). */
    private final ServicioAgenda servicioAgenda;

    /** Socio de la sesión. */
    private final SocioActual socioActual;

    /** Cuenta de la sesión. */
    private final UsuarioActual usuarioActual;

    /** Último filtro usado, para recargar después de cancelar o de un conflicto. */
    private FiltroReservas ultimoFiltro;

    /**
     * Crea el presentador (bean de Spring de alcance de UI).
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param servicioReservas fachada de reservas
     * @param servicioNotificaciones fachada de notificaciones
     * @param servicioAgenda fachada de agenda
     * @param socioActual socio de la sesión
     * @param usuarioActual cuenta de la sesión
     */
    public PresentadorMisReservas(ServicioReservas servicioReservas, ServicioNotificaciones servicioNotificaciones,
                                  ServicioAgenda servicioAgenda, SocioActual socioActual, UsuarioActual usuarioActual) {
        this.servicioReservas = servicioReservas;
        this.servicioNotificaciones = servicioNotificaciones;
        this.servicioAgenda = servicioAgenda;
        this.socioActual = socioActual;
        this.usuarioActual = usuarioActual;
    }

    /**
     * {@code ServicioReservas.consultarReservasPropias(socioActual.obtener(), filtro): List<Reserva>} → {@code
     * FilaReserva.desde(r)}; sin reservas lo informa y sugiere reservar (CU-29 FA1).
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param filtro rango de fechas y estado ({@code null}: todas)
     */
    public void cargar(FiltroReservas filtro) {
        ultimoFiltro = filtro == null ? new FiltroReservas() : filtro;
        consultar(() -> servicioReservas.consultarReservasPropias(socioActual.obtener(), ultimoFiltro))
                .ifPresent(reservas -> {
                    pantalla.mostrarReservas(reservas.stream().map(FilaReserva::desde).toList());
                    if (reservas.isEmpty()) {
                        pantalla.mostrarMensaje("No tiene reservas en el período. Puede reservar desde «Turnos disponibles».");
                    }
                });
    }

    /**
     * {@code ServicioReservas.consultarReserva(idReserva)} y {@code
     * ServicioNotificaciones.consultarPorReserva(usuarioActual.obtener(), idReserva)} y {@code
     * ServicioAgenda.consultarReglas()} → {@code DetalleReserva.desde(r, notificaciones, regla)}.
     *
     * <p>Casos de uso: CU-29
     * @param idReserva reserva elegida
     */
    public void alVerDetalle(Long idReserva) {
        consultar(() -> {
            Reserva r = servicioReservas.consultarReserva(idReserva);
            return DetalleReserva.desde(r, servicioNotificaciones.consultarPorReserva(usuarioActual.obtener(), idReserva),
                    servicioAgenda.consultarReglas());
        }).ifPresent(pantalla::mostrarDetalle);
    }

    /**
     * {@code ServicioReservas.cancelarReservaPropia(socioActual.obtener(), idReserva)}; luego {@code
     * cargar(filtro)}; {@code alConflicto} → «la reserva cambió» y recarga.
     *
     * <p>Casos de uso: CU-30
     * @param idReserva reserva a cancelar
     */
    public void alCancelar(Long idReserva) {
        if (ejecutar(() -> servicioReservas.cancelarReservaPropia(socioActual.obtener(), idReserva))) {
            pantalla.mostrarMensaje("La reserva fue cancelada.");
            cargar(ultimoFiltro);
        }
    }

    /**
     * Gancho de {@code PresentadorBase} para la {@code ConcurrenciaException} del control optimista: informa que
     * la reserva cambió y vuelve a cargar (CU-30 FA5).
     *
     * <p>Casos de uso: CU-30
     * @param ex excepción de concurrencia
     */
    @Override
    protected void alConflicto(ConcurrenciaException ex) {
        pantalla.mostrarError("La reserva cambió mientras la editaba; se muestran los datos actuales.");
        cargar(ultimoFiltro);
    }
}

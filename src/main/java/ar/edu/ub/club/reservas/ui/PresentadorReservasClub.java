package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.notificaciones.aplicacion.ServicioNotificaciones;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.reservas.aplicacion.FiltroReservas;
import ar.edu.ub.club.reservas.aplicacion.ServicioReservas;
import ar.edu.ub.club.reservas.dominio.Reserva;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-22 (MVP, Vista Pasiva): reservas del club con filtros, acceso a modificar y
 * cancelación administrativa con motivo (sec_cu31_p1 a p3, sec_cu32_p1, sec_cu33_p1).
 *
 * <p>Casos de uso: CU-31, CU-32, CU-33
 */
@SpringComponent
@UIScope
public class PresentadorReservasClub extends PresentadorBase<PantallaReservasClub> {

    /** Fachada de recursos (filtro de deporte). */
    private final ServicioRecursos servicioRecursos;

    /** Fachada de reservas. */
    private final ServicioReservas servicioReservas;

    /** Fachada de notificaciones (estado de los avisos del detalle). */
    private final ServicioNotificaciones servicioNotificaciones;

    /** Fachada de agenda (plazos de la regla de reserva). */
    private final ServicioAgenda servicioAgenda;

    /** Cuenta de la sesión. */
    private final UsuarioActual usuarioActual;

    /** Último filtro usado ({@code null} hasta la primera carga, que además muestra los filtros). */
    private FiltroReservas ultimoFiltro;

    /**
     * Crea el presentador (bean de Spring de alcance de UI).
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @param servicioRecursos fachada de recursos
     * @param servicioReservas fachada de reservas
     * @param servicioNotificaciones fachada de notificaciones
     * @param servicioAgenda fachada de agenda
     * @param usuarioActual cuenta de la sesión
     */
    public PresentadorReservasClub(ServicioRecursos servicioRecursos, ServicioReservas servicioReservas,
                                   ServicioNotificaciones servicioNotificaciones, ServicioAgenda servicioAgenda,
                                   UsuarioActual usuarioActual) {
        this.servicioRecursos = servicioRecursos;
        this.servicioReservas = servicioReservas;
        this.servicioNotificaciones = servicioNotificaciones;
        this.servicioAgenda = servicioAgenda;
        this.usuarioActual = usuarioActual;
    }

    /**
     * {@code ServicioRecursos.listarDeportes()} (filtros, primera vez) y {@code
     * ServicioReservas.consultarReservasClub(filtro): List<Reserva>} → {@code FilaReserva}; sin coincidencias lo
     * informa (CU-31 FA2).
     *
     * <p>Casos de uso: CU-31, CU-33
     * @param filtro rango, deporte, socio y estado
     */
    public void cargar(FiltroReservas filtro) {
        if (ultimoFiltro == null) {
            consultar(servicioRecursos::listarDeportes).ifPresent(deportes -> pantalla.mostrarFiltros(
                    deportes.stream().map(d -> new OpcionSeleccion(d.getId(), d.getNombre())).toList()));
        }
        ultimoFiltro = filtro == null ? new FiltroReservas() : filtro;
        consultar(() -> servicioReservas.consultarReservasClub(ultimoFiltro)).ifPresent(reservas -> {
            pantalla.mostrarReservas(reservas.stream().map(FilaReserva::desde).toList());
            if (reservas.isEmpty()) {
                pantalla.mostrarMensaje("No hay reservas que coincidan con el filtro.");
            }
        });
    }

    /**
     * {@code ServicioReservas.consultarReserva(idReserva)} y {@code
     * ServicioNotificaciones.consultarPorReserva(usuarioActual.obtener(), idReserva)} y {@code
     * ServicioAgenda.consultarReglas()} → {@code DetalleReserva.desde(r, notificaciones, regla)}.
     *
     * <p>Casos de uso: CU-31
     * @param idReserva reserva elegida
     */
    public void alSeleccionar(Long idReserva) {
        consultar(() -> {
            Reserva r = servicioReservas.consultarReserva(idReserva);
            return DetalleReserva.desde(r, servicioNotificaciones.consultarPorReserva(usuarioActual.obtener(), idReserva),
                    servicioAgenda.consultarReglas());
        }).ifPresent(pantalla::mostrarDetalle);
    }

    /**
     * {@code ServicioReservas.cancelarReservaAdministrativa(usuarioActual.obtenerAdministrador(), idReserva,
     * motivo)}; recarga; {@code alConflicto} → «la reserva cambió» y recarga.
     *
     * <p>Casos de uso: CU-33
     * @param idReserva reserva a cancelar
     * @param motivo motivo obligatorio
     */
    public void alCancelar(Long idReserva, String motivo) {
        if (ejecutar(() -> servicioReservas.cancelarReservaAdministrativa(usuarioActual.obtenerAdministrador(),
                idReserva, motivo))) {
            pantalla.mostrarMensaje("La reserva fue cancelada.");
            cargar(ultimoFiltro);
        }
    }

    /**
     * {@code navegarA("reservas-club/modificar/" + idReserva)} (P-23).
     *
     * <p>Casos de uso: CU-32
     * @param idReserva reserva a modificar
     */
    public void alModificar(Long idReserva) {
        pantalla.navegarA("reservas-club/modificar/" + idReserva);
    }

    /**
     * Gancho de {@code PresentadorBase} para la {@code ConcurrenciaException} del control optimista: informa que
     * la reserva cambió y recarga (CU-33 FA4).
     *
     * <p>Casos de uso: CU-33
     * @param ex excepción de concurrencia
     */
    @Override
    protected void alConflicto(ConcurrenciaException ex) {
        pantalla.mostrarError("La reserva cambió mientras la editaba; se muestran los datos actuales.");
        cargar(ultimoFiltro);
    }
}

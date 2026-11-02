package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.agenda.aplicacion.FiltroDisponibilidad;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.reservas.aplicacion.ServicioReservas;
import ar.edu.ub.club.reservas.dominio.Reserva;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Presentador de la pantalla P-23 (MVP, Vista Pasiva): formulario para cambiar fecha, horario o cancha de una
 * reserva, con consulta de turnos libres. Guarda la {@code version} leída para el control optimista; la vista solo
 * habilita el formulario: las mismas reglas las vuelven a controlar en el servidor el estado de la reserva y la
 * cadena de validadores (sec_cu32_p1, sec_cu32_p2).
 *
 * <p>Casos de uso: CU-32
 */
@SpringComponent
@UIScope
public class PresentadorModificarReserva extends PresentadorBase<PantallaModificarReserva> {

    /** Fachada de reservas. */
    private final ServicioReservas servicioReservas;

    /** Fachada de recursos (canchas del deporte). */
    private final ServicioRecursos servicioRecursos;

    /** Fachada de agenda (reglas y disponibilidad). */
    private final ServicioAgenda servicioAgenda;

    /** Cuenta de la sesión. */
    private final UsuarioActual usuarioActual;

    /** Reserva que se modifica. */
    private Long idReserva;

    /** Versión leída al cargar (control optimista). */
    private Integer version;

    /**
     * Crea el presentador (bean de Spring de alcance de UI).
     *
     * <p>Casos de uso: CU-32
     * @param servicioReservas fachada de reservas
     * @param servicioRecursos fachada de recursos
     * @param servicioAgenda fachada de agenda
     * @param usuarioActual cuenta de la sesión
     */
    public PresentadorModificarReserva(ServicioReservas servicioReservas, ServicioRecursos servicioRecursos,
                                       ServicioAgenda servicioAgenda, UsuarioActual usuarioActual) {
        this.servicioReservas = servicioReservas;
        this.servicioRecursos = servicioRecursos;
        this.servicioAgenda = servicioAgenda;
        this.usuarioActual = usuarioActual;
    }

    /**
     * {@code ServicioReservas.consultarReserva(idReserva)} y {@code ServicioAgenda.consultarReglas()} → {@code
     * DetalleReserva.desde(r, List.of(), regla)} (guarda {@code version}) y {@code
     * ServicioRecursos.listarCanchas(idDeporte)} (→ {@code OpcionSeleccion}). Si la reserva no está confirmada o
     * venció el plazo de modificación lo informa (FA1, FA2).
     *
     * <p>Casos de uso: CU-32
     * @param idReserva reserva a modificar
     */
    public void cargar(Long idReserva) {
        this.idReserva = idReserva;
        consultar(() -> {
            Reserva r = servicioReservas.consultarReserva(idReserva);
            return DetalleReserva.desde(r, List.of(), servicioAgenda.consultarReglas());
        }).ifPresent(detalle -> {
            version = detalle.version();
            List<OpcionSeleccion> canchas = consultar(() -> servicioRecursos.listarCanchas(detalle.idDeporte()))
                    .map(lista -> lista.stream().filter(Cancha::estaDisponibleParaReservar)
                            .map(c -> new OpcionSeleccion(c.getId(), c.getNombre())).toList())
                    .orElse(List.of());
            pantalla.mostrarReserva(detalle, canchas);
            informarSiNoModificable(detalle);
        });
    }

    /**
     * {@code ServicioAgenda.consultarDisponibilidad(filtro)} → {@code FilaTurno.desde(t, true)}.
     *
     * <p>Casos de uso: CU-32
     * @param filtro fecha, deporte y cancha
     */
    public void alConsultarTurnos(FiltroDisponibilidad filtro) {
        if (filtro == null || filtro.getFecha() == null) {
            pantalla.mostrarError("Elija la fecha a consultar.");
            return;
        }
        consultar(() -> servicioAgenda.consultarDisponibilidad(filtro))
                .ifPresent(turnos -> pantalla.mostrarTurnos(turnos.stream().map(t -> FilaTurno.desde(t, true)).toList()));
    }

    /**
     * {@code ServicioReservas.modificarReserva(usuarioActual.obtenerAdministrador(), idReserva, nuevoTurno.aTurno(),
     * nuevoTurno.idCancha, version): Reserva}; {@code navegarA("reservas-club")}; {@code alConflicto} → mensaje y
     * {@code cargar(idReserva)}.
     *
     * <p>Casos de uso: CU-32
     * @param nuevoTurno turno elegido
     */
    public void alGuardar(FilaTurno nuevoTurno) {
        if (nuevoTurno == null || !"LIBRE".equals(nuevoTurno.estado())) {
            pantalla.mostrarError("Elija un turno libre.");
            return;
        }
        consultar(() -> servicioReservas.modificarReserva(usuarioActual.obtenerAdministrador(), idReserva,
                nuevoTurno.aTurno(), nuevoTurno.idCancha(), version))
                .ifPresent(r -> {
                    pantalla.mostrarMensaje("La reserva fue modificada.");
                    pantalla.navegarA("reservas-club");
                });
    }

    /**
     * Gancho de {@code PresentadorBase} para la {@code ConcurrenciaException} del control optimista: informa que
     * la reserva cambió y vuelve a cargar los datos actuales (CU-32 FA11).
     *
     * <p>Casos de uso: CU-32
     * @param ex excepción de concurrencia
     */
    @Override
    protected void alConflicto(ConcurrenciaException ex) {
        pantalla.mostrarError("La reserva cambió mientras la editaba; se muestran los datos actuales.");
        cargar(idReserva);
    }

    /**
     * Informa si la reserva no admite modificación (no confirmada o dentro del plazo mínimo, RN-15). Es solo un
     * aviso: el servidor vuelve a controlarlo.
     *
     * <p>Casos de uso: CU-32
     * @param detalle detalle de la reserva
     */
    private void informarSiNoModificable(DetalleReserva detalle) {
        if (!"CONFIRMADA".equals(detalle.estado())) {
            pantalla.mostrarError("La reserva ya no está confirmada y no puede modificarse.");
            return;
        }
        if (detalle.plazoModificacionHoras() != null) {
            LocalDateTime inicio = detalle.fecha().atTime(detalle.horaInicio());
            long minutos = Duration.between(LocalDateTime.now(RelojSistema.ZONA_CLUB), inicio).toMinutes();
            if (minutos < detalle.plazoModificacionHoras() * 60L) {
                pantalla.mostrarError("Faltan menos de " + detalle.plazoModificacionHoras()
                        + " horas para el turno: la reserva ya no puede modificarse; puede cancelarla con motivo.");
            }
        }
    }
}

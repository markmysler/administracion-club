package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.agenda.aplicacion.FiltroDisponibilidad;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.reservas.aplicacion.ServicioReservas;

/**
 * Presentador de la pantalla P-23: Formulario para cambiar fecha, horario o cancha de una reserva, con consulta
 * de turnos libres.
 *
 * <p>Casos de uso: CU-32
 */
public class PresentadorModificarReserva extends PresentadorBase<PantallaModificarReserva> {

    /** Atributo servicioReservas. */
    private ServicioReservas servicioReservas;

    /** Atributo servicioRecursos. */
    private ServicioRecursos servicioRecursos;

    /** Atributo servicioAgenda. */
    private ServicioAgenda servicioAgenda;

    /** Atributo usuarioActual. */
    private UsuarioActual usuarioActual;

    /** Atributo idReserva. */
    private Long idReserva;

    /** Atributo version. */
    private Integer version;

    /**
     * {@code ServicioReservas.consultarReserva(idReserva)} y {@code ServicioAgenda.consultarReglas()} → {@code
     * DetalleReserva.desde(r, List.of(), regla)} (guarda {@code version}) y {@code
     * ServicioRecursos.listarCanchas(idDeporte)} (→ {@code OpcionSeleccion})
     *
     * <p>Casos de uso: CU-32
     * @param idReserva valor de {@code idReserva}
     */
    public void cargar(Long idReserva) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAgenda.consultarDisponibilidad(filtro)} → {@code FilaTurno.desde(t, true)}
     *
     * <p>Casos de uso: CU-32
     * @param filtro valor de {@code filtro}
     */
    public void alConsultarTurnos(FiltroDisponibilidad filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioReservas.modificarReserva(usuarioActual.obtenerAdministrador(), idReserva, nuevoTurno.aTurno(),
     * nuevoTurno.idCancha, version): Reserva}; {@code navegarA("reservas-club")}; {@code alConflicto} → mensaje y
     * {@code cargar(idReserva)}
     *
     * <p>Casos de uso: CU-32
     * @param nuevoTurno valor de {@code nuevoTurno}
     */
    public void alGuardar(FilaTurno nuevoTurno) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-32
     * @param ex valor de {@code ex}
     */
    protected void alConflicto(ConcurrenciaException ex) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

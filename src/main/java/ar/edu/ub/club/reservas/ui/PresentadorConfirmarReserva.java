package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.reservas.aplicacion.ServicioReservas;
import ar.edu.ub.club.socios.aplicacion.SocioActual;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-20 (MVP, Vista Pasiva): diálogo que muestra cancha, fecha y horario elegidos y
 * confirma la reserva. La identidad de quien reserva la obtiene con {@code SocioActual} o {@code UsuarioActual}
 * (RN-20): la vista nunca la envía (sec_cu27_p1, sec_cu28_p2).
 *
 * <p>Casos de uso: CU-27, CU-28
 */
@SpringComponent
@UIScope
public class PresentadorConfirmarReserva extends PresentadorBase<PantallaConfirmarReserva> {

    /** Fachada de reservas. */
    private final ServicioReservas servicioReservas;

    /** Socio de la sesión (reserva online). */
    private final SocioActual socioActual;

    /** Administrador de la sesión (reserva manual). */
    private final UsuarioActual usuarioActual;

    /** Turno elegido. */
    private FilaTurno turno;

    /** Socio elegido por el administrador ({@code null}: reserva online). */
    private Long idSocio;

    /**
     * Crea el presentador (bean de Spring de alcance de UI).
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param servicioReservas fachada de reservas
     * @param socioActual socio de la sesión
     * @param usuarioActual cuenta de la sesión
     */
    public PresentadorConfirmarReserva(ServicioReservas servicioReservas, SocioActual socioActual,
                                       UsuarioActual usuarioActual) {
        this.servicioReservas = servicioReservas;
        this.socioActual = socioActual;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Guarda el turno y el socio y le pide al diálogo que los muestre (paso 2 de CU-27, paso 8 de CU-28).
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param turno turno elegido
     * @param idSocio socio elegido por el administrador o {@code null}
     */
    public void alAbrir(FilaTurno turno, Long idSocio) {
        this.turno = turno;
        this.idSocio = idSocio;
        pantalla.mostrarTurno(turno, idSocio == null ? null : "Socio n.º " + idSocio);
    }

    /**
     * Sin socio elegido: {@code ServicioReservas.reservarOnline(socioActual.obtener(), turno.idCancha,
     * turno.aTurno()): Reserva}; con socio: {@code
     * ServicioReservas.registrarReservaManual(usuarioActual.obtenerAdministrador(), idSocio, turno.idCancha,
     * turno.aTurno()): Reserva}; si se registró, {@code cerrarConExito()} dispara {@code DialogoResueltoEvento}; si
     * el turno se ocupó entretanto ({@code ErrorNegocio}) {@code consultar} muestra el aviso y, al cerrar el
     * diálogo, la vista anfitriona recarga la grilla.
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    public void alConfirmar() {
        consultar(() -> idSocio == null
                ? servicioReservas.reservarOnline(socioActual.obtener(), turno.idCancha(), turno.aTurno())
                : servicioReservas.registrarReservaManual(usuarioActual.obtenerAdministrador(), idSocio,
                        turno.idCancha(), turno.aTurno()))
                .ifPresent(r -> pantalla.cerrarConExito());
    }
}

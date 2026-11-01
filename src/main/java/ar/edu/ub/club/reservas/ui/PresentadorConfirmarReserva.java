package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.reservas.aplicacion.ServicioReservas;
import ar.edu.ub.club.socios.aplicacion.SocioActual;

/**
 * Presentador de la pantalla P-20: Diálogo que muestra cancha, fecha y horario elegidos y confirma la reserva.
 *
 * <p>Casos de uso: CU-27, CU-28
 */
public class PresentadorConfirmarReserva extends PresentadorBase<PantallaConfirmarReserva> {

    /** Atributo servicioReservas. */
    private ServicioReservas servicioReservas;

    /** Atributo socioActual. */
    private SocioActual socioActual;

    /** Atributo usuarioActual. */
    private UsuarioActual usuarioActual;

    /** Atributo turno. */
    private FilaTurno turno;

    /** Atributo idSocio. */
    private Long idSocio;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param turno valor de {@code turno}
     * @param idSocio valor de {@code idSocio}
     */
    public void alAbrir(FilaTurno turno, Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * sin socio elegido: {@code ServicioReservas.reservarOnline(socioActual.obtener(), turno.idCancha,
     * turno.aTurno()): Reserva}; con socio: {@code
     * ServicioReservas.registrarReservaManual(usuarioActual.obtenerAdministrador(), idSocio, turno.idCancha,
     * turno.aTurno()): Reserva}; dispara {@code DialogoResueltoEvento}; si el turno se ocupó entretanto ({@code
     * ErrorNegocio}) muestra el aviso y la vista anfitriona recarga la grilla
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    public void alConfirmar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

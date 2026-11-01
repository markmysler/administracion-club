package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;

/**
 * Objeto de valor que recorre la cadena de validadores.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class SolicitudReserva {

    /** Socio de la reserva. */
    private Socio socio;

    /** Cancha. */
    private Cancha cancha;

    /** Turno pedido. */
    private Turno turno;

    /** Reserva que se modifica. */
    private Reserva reservaOriginal;

    /** Alta o modificación. */
    private TipoOperacion tipoOperacion;

    /** Quién la origina. */
    private OrigenSolicitud origen;

    /** Momento de la solicitud. */
    private LocalDateTime ahora;

    /**
     * Crea una instancia de {@code SolicitudReserva}.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param socio valor de {@code socio}
     * @param cancha valor de {@code cancha}
     * @param turno valor de {@code turno}
     * @param reservaOriginal valor de {@code reservaOriginal}
     * @param tipoOperacion valor de {@code tipoOperacion}
     * @param origen valor de {@code origen}
     * @param ahora valor de {@code ahora}
     */
    public SolicitudReserva(Socio socio, Cancha cancha, Turno turno, Reserva reservaOriginal, TipoOperacion tipoOperacion, OrigenSolicitud origen, LocalDateTime ahora) {
        // PENDIENTE
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return el resultado
     */
    public boolean esModificacion() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return el resultado
     */
    public boolean controlaDeuda() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code socio}: Socio de la reserva.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public Socio getSocio() {
        return socio;
    }

    /**
     * Devuelve {@code cancha}: Cancha.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public Cancha getCancha() {
        return cancha;
    }

    /**
     * Devuelve {@code turno}: Turno pedido.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public Turno getTurno() {
        return turno;
    }

    /**
     * Devuelve {@code reservaOriginal}: Reserva que se modifica.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public Reserva getReservaOriginal() {
        return reservaOriginal;
    }

    /**
     * Devuelve {@code tipoOperacion}: Alta o modificación.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Devuelve {@code origen}: Quién la origina.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public OrigenSolicitud getOrigen() {
        return origen;
    }

    /**
     * Devuelve {@code ahora}: Momento de la solicitud.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public LocalDateTime getAhora() {
        return ahora;
    }
}

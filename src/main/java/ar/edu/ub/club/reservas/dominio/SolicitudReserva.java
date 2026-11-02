package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;

/**
 * Objeto de valor inmutable que recorre la cadena de validadores (Chain of Responsibility): quién, qué cancha,
 * qué turno, si es un alta o una modificación, quién la origina y en qué momento. Lo crea {@code ServicioReservas}.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class SolicitudReserva {

    /** Socio de la reserva. */
    private final Socio socio;

    /** Cancha. */
    private final Cancha cancha;

    /** Turno pedido. */
    private final Turno turno;

    /** Reserva que se modifica. */
    private final Reserva reservaOriginal;

    /** Alta o modificación. */
    private final TipoOperacion tipoOperacion;

    /** Quién la origina. */
    private final OrigenSolicitud origen;

    /** Momento de la solicitud. */
    private final LocalDateTime ahora;

    /**
     * Crea la solicitud y verifica la coherencia entre {@code tipoOperacion} y {@code reservaOriginal}
     * (obligatoria en MODIFICACION, nula en ALTA) y que el turno esté informado.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param socio socio de la reserva
     * @param cancha cancha pedida
     * @param turno turno pedido
     * @param reservaOriginal reserva a modificar o {@code null} en un alta
     * @param tipoOperacion ALTA o MODIFICACION
     * @param origen SOCIO o ADMINISTRADOR
     * @param ahora momento de la solicitud
     * @throws ErrorNegocio si falta el turno
     * @throws IllegalArgumentException si el tipo y la reserva original no son coherentes
     */
    public SolicitudReserva(Socio socio, Cancha cancha, Turno turno, Reserva reservaOriginal, TipoOperacion tipoOperacion, OrigenSolicitud origen, LocalDateTime ahora) {
        if (turno == null) {
            throw new ErrorNegocio("Elija el turno de la reserva.");
        }
        if ((tipoOperacion == TipoOperacion.MODIFICACION) != (reservaOriginal != null)) {
            throw new IllegalArgumentException("La reserva original es obligatoria solo en una modificación");
        }
        this.socio = socio;
        this.cancha = cancha;
        this.turno = turno;
        this.reservaOriginal = reservaOriginal;
        this.tipoOperacion = tipoOperacion;
        this.origen = origen;
        this.ahora = ahora;
    }

    /**
     * Indica si la solicitud es una modificación.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return true si {@code tipoOperacion = MODIFICACION}
     */
    public boolean esModificacion() {
        return tipoOperacion == TipoOperacion.MODIFICACION;
    }

    /**
     * Indica si corresponde el control de cuota vencida: false solo en una MODIFICACION con origen ADMINISTRADOR
     * (RN-11, RN-15, CU-32); true en los demás casos.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return true si se controla la deuda del grupo
     */
    public boolean controlaDeuda() {
        return !(esModificacion() && origen == OrigenSolicitud.ADMINISTRADOR);
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
     * <p>Casos de uso: CU-32
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

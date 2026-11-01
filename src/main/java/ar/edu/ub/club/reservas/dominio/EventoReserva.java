package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Clase abstracta de los eventos del patrón Observer de las reservas (sujeto {@code PublicadorEventosReserva}).
 * Es inmutable: sus atributos se asignan en el constructor y solo se leen. Subclases: {@code
 * ReservaConfirmadaEvento}, {@code ReservaModificadaEvento} y {@code ReservaCanceladaEvento}.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public abstract class EventoReserva {

    /** Reserva afectada, ya guardada (con identificador). */
    private final Reserva reserva;

    /** Cuenta que realizó la operación (socio o administrador). */
    private final Usuario usuario;

    /** Momento de la operación. */
    private final LocalDateTime fechaHora;

    /**
     * Constructor que usan las subclases.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param reserva reserva afectada
     * @param usuario cuenta que opera
     * @param fechaHora momento de la operación
     */
    public EventoReserva(Reserva reserva, Usuario usuario, LocalDateTime fechaHora) {
        this.reserva = reserva;
        this.usuario = usuario;
        this.fechaHora = fechaHora;
    }

    /**
     * Nombre de la acción: lo usa {@code AuditorReservas} como acción auditada y {@code NotificadorReservas} como
     * {@code TipoNotificacion}.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return CONFIRMACION, MODIFICACION o CANCELACION
     */
    public abstract String accion();

    /**
     * Devuelve {@code reserva}: reserva afectada.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public Reserva getReserva() {
        return reserva;
    }

    /**
     * Devuelve {@code usuario}: cuenta que realizó la operación.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code fechaHora}: momento de la operación.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}

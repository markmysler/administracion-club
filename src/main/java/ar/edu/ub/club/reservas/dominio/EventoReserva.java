package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Casos de uso: PENDIENTE
 */
public abstract class EventoReserva {

    /** Atributo reserva. */
    private Reserva reserva;

    /** Atributo usuario. */
    private Usuario usuario;

    /** Atributo fechaHora. */
    private LocalDateTime fechaHora;

    /**
     * Crea una instancia de {@code EventoReserva}.
     *
     * <p>Casos de uso: PENDIENTE
     * @param reserva valor de {@code reserva}
     * @param usuario valor de {@code usuario}
     * @param fechaHora valor de {@code fechaHora}
     */
    public EventoReserva(Reserva reserva, Usuario usuario, LocalDateTime fechaHora) {
        // PENDIENTE
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    public abstract String accion();
}

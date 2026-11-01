package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Evento del dominio publicado con el mecanismo de eventos de Spring (Observer).
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public class ReservaModificadaEvento extends EventoReserva {

    /** Atributo turnoAnterior. */
    private Turno turnoAnterior;

    /** Atributo canchaAnterior. */
    private Cancha canchaAnterior;

    /**
     * Crea una instancia de {@code ReservaModificadaEvento}.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param reserva valor de {@code reserva}
     * @param usuario valor de {@code usuario}
     * @param fechaHora valor de {@code fechaHora}
     * @param turnoAnterior valor de {@code turnoAnterior}
     * @param canchaAnterior valor de {@code canchaAnterior}
     */
    public ReservaModificadaEvento(Reserva reserva, Usuario usuario, LocalDateTime fechaHora, Turno turnoAnterior, Cancha canchaAnterior) {
        super(reserva, usuario, fechaHora);
        // PENDIENTE
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el resultado
     */
    public String accion() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code turnoAnterior}: turnoAnterior.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public Turno getTurnoAnterior() {
        return turnoAnterior;
    }

    /**
     * Devuelve {@code canchaAnterior}: canchaAnterior.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public Cancha getCanchaAnterior() {
        return canchaAnterior;
    }
}

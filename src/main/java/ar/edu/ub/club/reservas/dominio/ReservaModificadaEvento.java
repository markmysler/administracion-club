package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.time.LocalDateTime;

/**
 * Evento del patrón Observer que se publica al modificar una reserva. Guarda la cancha y el turno anteriores para
 * que la auditoría registre los valores anteriores y los nuevos y el correo los informe (RN-15, RNF-12, CU-32
 * paso 15).
 *
 * <p>Casos de uso: CU-32
 */
public class ReservaModificadaEvento extends EventoReserva {

    /** Turno antes de la modificación. */
    private final Turno turnoAnterior;

    /** Cancha antes de la modificación. */
    private final Cancha canchaAnterior;

    /**
     * Crea el evento; los tres primeros parámetros son los de {@code EventoReserva}.
     *
     * <p>Casos de uso: CU-32
     * @param reserva reserva ya modificada
     * @param usuario administrador que modificó
     * @param fechaHora momento
     * @param turnoAnterior turno previo
     * @param canchaAnterior cancha previa
     */
    public ReservaModificadaEvento(Reserva reserva, Usuario usuario, LocalDateTime fechaHora, Turno turnoAnterior, Cancha canchaAnterior) {
        super(reserva, usuario, fechaHora);
        this.turnoAnterior = turnoAnterior;
        this.canchaAnterior = canchaAnterior;
    }

    /**
     * Acción del evento.
     *
     * <p>Casos de uso: CU-32
     * @return «MODIFICACION»
     */
    @Override
    public String accion() {
        return "MODIFICACION";
    }

    /**
     * Devuelve {@code turnoAnterior}: turno antes de la modificación.
     *
     * <p>Casos de uso: CU-32
     * @return el valor del atributo
     */
    public Turno getTurnoAnterior() {
        return turnoAnterior;
    }

    /**
     * Devuelve {@code canchaAnterior}: cancha antes de la modificación.
     *
     * <p>Casos de uso: CU-32
     * @return el valor del atributo
     */
    public Cancha getCanchaAnterior() {
        return canchaAnterior;
    }
}

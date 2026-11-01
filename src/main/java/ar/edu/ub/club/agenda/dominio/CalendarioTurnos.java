package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de dominio que genera la grilla de turnos de una cancha para una fecha.
 *
 * <p>Casos de uso: CU-26
 */
public class CalendarioTurnos {

    /** Hora actual para marcar NO_DISPONIBLE los turnos que no cumplen la anticipación mínima. */
    private Reloj reloj;

    /**
     * Divide el horario en turnos de la duración vigente medidos desde la apertura y marca cada uno LIBRE, OCUPADO o
     * NO_DISPONIBLE.
     *
     * <p>Casos de uso: CU-26
     * @param cancha Cancha.
     * @param fecha Fecha.
     * @param disponibilidad Horario.
     * @param regla Reglas.
     * @param ocupados Turnos de reservas confirmadas.
     * @return Lista de {@code TurnoDisponible}.
     */
    public List<TurnoDisponible> generarTurnos(Cancha cancha, LocalDate fecha, DisponibilidadClub disponibilidad, ReglaReserva regla, List<Turno> ocupados) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

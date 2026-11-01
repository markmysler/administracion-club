package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.agenda.dominio.TurnoDisponible;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Turno de la grilla de disponibilidad (P-19, P-20, P-23): cancha, deporte, fecha, horario, estado (LIBRE, OCUPADO o
 * NO_DISPONIBLE) y, solo para el administrador, el socio que lo ocupa (RN-20).
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
 * @param idCancha cancha
 * @param cancha nombre de la cancha
 * @param deporte nombre del deporte
 * @param fecha fecha del turno
 * @param horaInicio hora de inicio
 * @param horaFin hora de fin
 * @param estado LIBRE, OCUPADO o NO_DISPONIBLE
 * @param ocupadoPor socio que ocupa el turno ({@code null} si no corresponde mostrarlo)
 */
public record FilaTurno(Long idCancha, String cancha, String deporte, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String estado, String ocupadoPor) {

    /**
     * Construye la fila desde el turno que devuelve {@code ServicioAgenda.consultarDisponibilidad}; con {@code
     * verOcupante = false} deja {@code ocupadoPor} en null (RN-20).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param turno turno disponible de la agenda
     * @param verOcupante true si la cuenta es administrativa
     * @return la fila
     */
    public static FilaTurno desde(TurnoDisponible turno, boolean verOcupante) {
        Cancha c = turno.getCancha();
        String deporte = c.getDeporte() == null ? "" : c.getDeporte().getNombre();
        Turno t = turno.getTurno();
        return new FilaTurno(c.getId(), c.getNombre(), deporte, t.getFecha(), t.getHoraInicio(), t.getHoraFin(),
                turno.getEstado() == null ? null : turno.getEstado().name(),
                verOcupante ? turno.getOcupadoPor() : null);
    }

    /**
     * Convierte la fila en el {@code Turno} que reciben las fachadas.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return turno con la fecha y el horario de la fila
     */
    public Turno aTurno() {
        return new Turno(fecha, horaInicio, horaFin);
    }
}

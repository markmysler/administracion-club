package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.agenda.dominio.TurnoDisponible;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-26
 */
public record FilaTurno(Long idCancha, String cancha, String deporte, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String estado, String ocupadoPor) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @param turno valor de {@code turno}
     * @param verOcupante valor de {@code verOcupante}
     * @return el resultado
     */
    public static FilaTurno desde(TurnoDisponible turno, boolean verOcupante) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @return el resultado
     */
    public Turno aTurno() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

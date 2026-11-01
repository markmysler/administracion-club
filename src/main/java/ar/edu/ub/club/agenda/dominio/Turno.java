package ar.edu.ub.club.agenda.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Casos de uso: CU-26
 */
public class Turno {

    /** Atributo fecha. */
    private LocalDate fecha;

    /** Atributo horaInicio. */
    private LocalTime horaInicio;

    /** Atributo horaFin. */
    private LocalTime horaFin;

    /**
     * Crea una instancia de {@code Turno}.
     *
     * <p>Casos de uso: CU-26
     * @param fecha valor de {@code fecha}
     * @param horaInicio valor de {@code horaInicio}
     * @param horaFin valor de {@code horaFin}
     */
    public Turno(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        // PENDIENTE
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @return el resultado
     */
    public Integer duracionMinutos() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @param otro valor de {@code otro}
     * @return el resultado
     */
    public boolean seSuperponeCon(Turno otro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @return el resultado
     */
    public LocalDateTime fin() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @return el resultado
     */
    public LocalDateTime inicio() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

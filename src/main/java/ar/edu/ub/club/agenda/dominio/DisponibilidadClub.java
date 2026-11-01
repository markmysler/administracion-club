package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * Días y horario de atención del club (fila única) con sus feriados.
 *
 * <p>Casos de uso: todos
 */
public class DisponibilidadClub extends EntidadPersistente {

    /** Feriados del club (0..*); se cargan siempre con la fila única. */
    private List<Feriado> feriados;

    /** Días de atención. */
    private Set<DiaSemana> diasAtencion;

    /** Apertura. */
    private LocalTime horaApertura;

    /** Cierre. */
    private LocalTime horaCierre;

    /** Identificador de la única fila. */
    private Long ID_UNICO;

    /**
     * Valida al menos un día, cierre posterior a apertura y lugar para un turno (CU-24).
     *
     * <p>Casos de uso: CU-24
     * @param dias Días.
     * @param apertura Apertura.
     * @param cierre Cierre.
     * @param duracionTurnoMinutos Duración vigente del turno.
     */
    public void modificar(Set<DiaSemana> dias, LocalTime apertura, LocalTime cierre, Integer duracionTurnoMinutos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Día de atención y no feriado.
     *
     * <p>Casos de uso: CU-24, CU-26
     * @param fecha Fecha.
     * @return true si atiende.
     */
    public boolean atiende(LocalDate fecha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * El turno empieza y termina dentro del horario.
     *
     * <p>Casos de uso: CU-24
     * @param turno Turno.
     * @return true si está dentro.
     */
    public boolean dentroDelHorario(Turno turno) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve la hora de apertura; {@code CalendarioTurnos} la usa para alinear la grilla de turnos (CU-26).
     *
     * <p>Casos de uso: CU-26
     * @return Hora de apertura.
     */
    public LocalTime getHoraApertura() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve la hora de cierre; {@code CalendarioTurnos} la usa como límite de la grilla de turnos (CU-26).
     *
     * <p>Casos de uso: todos
     * @return Hora de cierre.
     */
    public LocalTime getHoraCierre() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param horaApertura valor de {@code horaApertura}
     * @param horaCierre valor de {@code horaCierre}
     * @return el resultado
     */
    public static DisponibilidadClub reconstituir(Long id, LocalTime horaApertura, LocalTime horaCierre) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param diasAtencion valor de {@code diasAtencion}
     * @param feriados valor de {@code feriados}
     */
    public void restaurarAsociaciones(Set<DiaSemana> diasAtencion, List<Feriado> feriados) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

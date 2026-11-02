package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Días y horario de atención del club (fila única {@code ID_UNICO}) con sus feriados. Information Expert de la
 * disponibilidad: decide si el club atiende en una fecha ({@code atiende}) y si un turno cae dentro del horario
 * ({@code dentroDelHorario}); lo usan {@code CalendarioTurnos} (CU-26), {@code ValidadorHorarioClub} (cadena de
 * validación de reservas, RN-13) y {@code ServicioAgenda.configurarDisponibilidad} para detectar las reservas que
 * quedan fuera de una nueva configuración (CU-24).
 *
 * <p>Entidad del dominio; la persiste {@code BrokerDisponibilidadClub} (tablas {@code disponibilidad_club} y
 * {@code dia_atencion}).
 *
 * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
 */
public class DisponibilidadClub extends EntidadPersistente {

    /** Identificador de la única fila ({@code disponibilidad_club.id = 1}, CHECK en la base). */
    public static final Long ID_UNICO = 1L;

    /** Feriados del club (0..*); se cargan siempre con la fila única. */
    private List<Feriado> feriados = new ArrayList<>();

    /** Días de atención. */
    private Set<DiaSemana> diasAtencion = EnumSet.noneOf(DiaSemana.class);

    /** Apertura. */
    private LocalTime horaApertura;

    /** Cierre. */
    private LocalTime horaCierre;

    /**
     * Constructor de materialización; la fila única la crea la migración V6 y solo se modifica (CU-24).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     */
    private DisponibilidadClub() {
    }

    /**
     * Valida y aplica la nueva disponibilidad (CU-24, pasos 4 y 5): al menos un día de atención (FA3), horas
     * completas, cierre posterior a la apertura (FA3) y lugar para al menos un turno de la duración vigente
     * (FA4). Si algo falla lanza {@code ErrorNegocio} y no cambia nada.
     *
     * <p>Casos de uso: CU-24
     * @param dias Días.
     * @param apertura Apertura.
     * @param cierre Cierre.
     * @param duracionTurnoMinutos Duración vigente del turno.
     * @throws ErrorNegocio con el campo del formulario de P-17 que hay que corregir
     */
    public void modificar(Set<DiaSemana> dias, LocalTime apertura, LocalTime cierre, Integer duracionTurnoMinutos) {
        if (dias == null || dias.isEmpty()) {
            throw new ErrorNegocio("diasAtencion", "Elija al menos un día de atención.");
        }
        if (apertura == null) {
            throw new ErrorNegocio("horaApertura", "Ingrese la hora de apertura.");
        }
        if (cierre == null) {
            throw new ErrorNegocio("horaCierre", "Ingrese la hora de cierre.");
        }
        if (!cierre.isAfter(apertura)) {
            throw new ErrorNegocio("horaCierre", "La hora de cierre debe ser posterior a la de apertura.");
        }
        long minutos = Duration.between(apertura, cierre).toMinutes();
        if (duracionTurnoMinutos != null && minutos < duracionTurnoMinutos) {
            throw new ErrorNegocio("horaCierre", "El horario debe admitir al menos un turno de "
                    + duracionTurnoMinutos + " minutos (duración vigente de los turnos).");
        }
        this.diasAtencion = EnumSet.copyOf(dias);
        this.horaApertura = apertura;
        this.horaCierre = cierre;
    }

    /**
     * Indica si el club atiende en la fecha: es un día de atención y no es feriado (RN-13; CU-26 paso 5).
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @param fecha Fecha.
     * @return true si atiende.
     */
    public boolean atiende(LocalDate fecha) {
        if (!diasAtencion.contains(DiaSemana.de(fecha))) {
            return false;
        }
        for (Feriado feriado : feriados) {
            if (feriado.getFecha().equals(fecha)) {
                return false;
            }
        }
        return true;
    }

    /**
     * El turno empieza y termina dentro del horario: {@code horaInicio >= apertura} y {@code horaFin <= cierre}.
     *
     * <p>Casos de uso: CU-24, CU-27, CU-28, CU-32
     * @param turno Turno.
     * @return true si está dentro.
     */
    public boolean dentroDelHorario(Turno turno) {
        return !turno.getHoraInicio().isBefore(horaApertura) && !turno.getHoraFin().isAfter(horaCierre);
    }

    /**
     * Devuelve la hora de apertura; {@code CalendarioTurnos} la usa para alinear la grilla de turnos (CU-26).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return Hora de apertura.
     */
    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    /**
     * Devuelve la hora de cierre; {@code CalendarioTurnos} la usa como límite de la grilla de turnos (CU-26).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return Hora de cierre.
     */
    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    /**
     * Desmaterialización (estereotipo «persistencia», solo para {@code BrokerDisponibilidadClub}): reconstituye
     * la fila única sin días ni feriados (los entrega luego {@code restaurarAsociaciones}).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @param id identificador (siempre {@code ID_UNICO})
     * @param horaApertura hora de apertura
     * @param horaCierre hora de cierre
     * @return la disponibilidad reconstituida
     */
    public static DisponibilidadClub reconstituir(Long id, LocalTime horaApertura, LocalTime horaCierre) {
        DisponibilidadClub disponibilidad = new DisponibilidadClub();
        disponibilidad.asignarId(id);
        disponibilidad.horaApertura = horaApertura;
        disponibilidad.horaCierre = horaCierre;
        return disponibilidad;
    }

    /**
     * Recibe los días de atención (consulta {@code DiasAtencion}) y los feriados (consulta {@code Feriados}) que
     * carga {@code BrokerDisponibilidadClub.cargarAsociaciones} (listas chicas, se cargan siempre).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @param diasAtencion días de atención
     * @param feriados feriados del club
     */
    public void restaurarAsociaciones(Set<DiaSemana> diasAtencion, List<Feriado> feriados) {
        this.diasAtencion = diasAtencion.isEmpty() ? EnumSet.noneOf(DiaSemana.class) : EnumSet.copyOf(diasAtencion);
        this.feriados = new ArrayList<>(feriados);
    }

    /**
     * Devuelve {@code feriados}: Feriados del club (0..*); se cargan siempre con la fila única.
     *
     * <p>Casos de uso: CU-24, CU-26
     * @return lista no modificable de feriados
     */
    public List<Feriado> getFeriados() {
        return Collections.unmodifiableList(feriados);
    }

    /**
     * Devuelve {@code diasAtencion}: Días de atención.
     *
     * <p>Casos de uso: CU-24
     * @return conjunto no modificable de días
     */
    public Set<DiaSemana> getDiasAtencion() {
        return Collections.unmodifiableSet(diasAtencion);
    }

    /**
     * Devuelve {@code ID_UNICO}: Identificador de la única fila (equivale a la constante de clase).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26
     * @return 1
     */
    public Long getID_UNICO() {
        return ID_UNICO;
    }
}

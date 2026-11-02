package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Objeto de valor inmutable ({@code agenda.dominio}) con la fecha y el horario de un turno: es el tipo del
 * atributo {@code turno} de {@code Reserva} y el elemento con el que {@code CalendarioTurnos} arma la grilla.
 * {@code BrokerReserva} lo lee y lo escribe en las columnas {@code fecha}, {@code hora_inicio} y {@code hora_fin}.
 * Dos turnos son iguales si tienen la misma fecha y las mismas horas (TP6).
 *
 * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
 */
public class Turno {

    /** Día del turno. */
    private final LocalDate fecha;

    /** Hora de inicio (hora local del club). */
    private final LocalTime horaInicio;

    /** Hora de fin, posterior a la de inicio. */
    private final LocalTime horaFin;

    /**
     * Crea el turno y valida que el horario sea coherente (TP6: «El horario del turno es inválido»).
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @param fecha día del turno
     * @param horaInicio hora de inicio
     * @param horaFin hora de fin
     * @throws ErrorNegocio si falta un dato o la hora de fin no es posterior a la de inicio
     */
    public Turno(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        if (fecha == null || horaInicio == null || horaFin == null || !horaFin.isAfter(horaInicio)) {
            throw new ErrorNegocio("El horario del turno es inválido.");
        }
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    /**
     * Minutos entre la hora de inicio y la de fin; lo usa {@code ValidadorDuracionTurno} para comparar con la
     * duración vigente (RN-13).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @return duración en minutos
     */
    public Integer duracionMinutos() {
        return (int) Duration.between(horaInicio, horaFin).toMinutes();
    }

    /**
     * Indica si dos turnos se superponen: misma fecha y {@code inicio() < otro.fin()} y
     * {@code otro.inicio() < fin()}. Usa intervalos semiabiertos, igual que la restricción de exclusión de la
     * base, por lo que dos turnos consecutivos (10:00–11:00 y 11:00–12:00) no se superponen (RN-12).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param otro turno a comparar
     * @return true si se superponen
     */
    public boolean seSuperponeCon(Turno otro) {
        return fecha.equals(otro.fecha) && inicio().isBefore(otro.fin()) && otro.inicio().isBefore(fin());
    }

    /**
     * Fecha y hora de finalización (fecha del turno con la hora de fin, en la hora local del club).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-31, CU-32
     * @return momento de fin
     */
    public LocalDateTime fin() {
        return LocalDateTime.of(fecha, horaFin);
    }

    /**
     * Fecha y hora de inicio; base de las anticipaciones de {@code ReglaReserva} (RN-13, RN-14, RN-15).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return momento de inicio
     */
    public LocalDateTime inicio() {
        return LocalDateTime.of(fecha, horaInicio);
    }

    /**
     * Devuelve {@code fecha}: día del turno.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Devuelve {@code horaInicio}: hora de inicio.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Devuelve {@code horaFin}: hora de fin.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * Igualdad de objeto de valor: misma fecha y mismas horas (TP6).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param o otro objeto
     * @return true si representan el mismo turno
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turno otro)) {
            return false;
        }
        return fecha.equals(otro.fecha) && horaInicio.equals(otro.horaInicio) && horaFin.equals(otro.horaFin);
    }

    /**
     * Código hash coherente con {@code equals}.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @return hash de la fecha y las horas
     */
    @Override
    public int hashCode() {
        return Objects.hash(fecha, horaInicio, horaFin);
    }

    /**
     * Texto del turno para la bitácora y los mensajes ({@code 2026-11-16 10:00-11:00}).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @return representación legible
     */
    @Override
    public String toString() {
        return fecha + " " + horaInicio + "-" + horaFin;
    }
}

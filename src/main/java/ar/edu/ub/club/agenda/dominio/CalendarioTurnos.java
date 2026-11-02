package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Servicio de dominio (GRASP Pure Fabrication) que genera la grilla de turnos de una cancha para una fecha: divide
 * el horario del club en turnos de la duración vigente medidos desde la apertura y marca cada uno LIBRE, OCUPADO
 * (se superpone con una reserva confirmada, RN-12) o NO_DISPONIBLE (no cumple la anticipación mínima, RN-13).
 * Es un bean ({@code @Component}) porque {@code ServicioAgenda} lo recibe por constructor; la hora actual la toma
 * de {@code Reloj} (Protected Variations: se prueba con un reloj fijo).
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
 */
@Component
public class CalendarioTurnos {

    /** Hora actual para marcar NO_DISPONIBLE los turnos que no cumplen la anticipación mínima. */
    private final Reloj reloj;

    /**
     * Crea el servicio con el reloj del sistema (inyectado por Spring).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param reloj fecha y hora actual
     */
    public CalendarioTurnos(Reloj reloj) {
        this.reloj = reloj;
    }

    /**
     * Divide el horario en turnos de la duración vigente medidos desde la apertura y marca cada uno LIBRE, OCUPADO o
     * NO_DISPONIBLE (sec_cu26_p3, pasos 8 y 9). Si el club no atiende ese día (día no hábil o feriado) devuelve una
     * lista vacía. Un turno ocupado se informa como OCUPADO aunque además no cumpla la anticipación.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param cancha Cancha.
     * @param fecha Fecha.
     * @param disponibilidad Horario.
     * @param regla Reglas.
     * @param ocupados Turnos de reservas confirmadas.
     * @return Lista de {@code TurnoDisponible}.
     */
    public List<TurnoDisponible> generarTurnos(Cancha cancha, LocalDate fecha, DisponibilidadClub disponibilidad, ReglaReserva regla, List<Turno> ocupados) {
        List<TurnoDisponible> grilla = new ArrayList<>();
        if (!disponibilidad.atiende(fecha)) {
            return grilla;
        }
        int apertura = minutosDelDia(disponibilidad.getHoraApertura());
        int cierre = minutosDelDia(disponibilidad.getHoraCierre());
        int duracion = regla.getDuracionTurnoMinutos();
        LocalDateTime ahora = reloj.ahora();
        for (int inicio = apertura; inicio + duracion <= cierre; inicio += duracion) {
            Turno turno = new Turno(fecha, LocalTime.of(inicio / 60, inicio % 60),
                    LocalTime.of((inicio + duracion) / 60, (inicio + duracion) % 60));
            grilla.add(new TurnoDisponible(cancha, turno, estadoDe(turno, regla, ocupados, ahora)));
        }
        return grilla;
    }

    /**
     * Calcula el estado de un turno: OCUPADO si se superpone con alguno de {@code ocupados} (RN-12),
     * NO_DISPONIBLE si no cumple la anticipación mínima (RN-13) y LIBRE en otro caso.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param turno turno de la grilla
     * @param regla reglas vigentes
     * @param ocupados turnos de reservas confirmadas de la cancha en la fecha
     * @param ahora momento actual
     * @return estado del turno
     */
    private EstadoTurno estadoDe(Turno turno, ReglaReserva regla, List<Turno> ocupados, LocalDateTime ahora) {
        for (Turno ocupado : ocupados) {
            if (turno.seSuperponeCon(ocupado)) {
                return EstadoTurno.OCUPADO;
            }
        }
        if (!regla.cumpleAnticipacionMinima(turno, ahora)) {
            return EstadoTurno.NO_DISPONIBLE;
        }
        return EstadoTurno.LIBRE;
    }

    /**
     * Minutos transcurridos desde la medianoche; evita el desborde de {@code LocalTime.plusMinutes} al armar la
     * grilla hasta el cierre.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param hora hora local
     * @return minutos desde las 00:00
     */
    private static int minutosDelDia(LocalTime hora) {
        return hora.getHour() * 60 + hora.getMinute();
    }
}

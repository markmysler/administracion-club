package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.time.Duration;

/**
 * Eslabón concreto de la Cadena de Responsabilidad (posición 4) de RN-13: duración del turno y alineación con la
 * grilla de turnos medida desde la hora de apertura del club.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorDuracionTurno extends ValidadorReservaEncadenado {

    /** Duración configurada de los turnos. */
    private final ReglaReserva regla;

    /** Hora de apertura, origen de la grilla. */
    private final DisponibilidadClub disponibilidad;

    /**
     * Crea el validador con los datos que obtuvo la fábrica.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param regla reglas de reserva (duración del turno)
     * @param disponibilidad disponibilidad del club (hora de apertura)
     */
    public ValidadorDuracionTurno(ReglaReserva regla, DisponibilidadClub disponibilidad) {
        this.regla = regla;
        this.disponibilidad = disponibilidad;
    }

    /**
     * Si {@code s.turno.duracionMinutos()} es distinta de {@code regla.duracionTurnoMinutos}, o si los minutos entre
     * {@code disponibilidad.horaApertura} y {@code s.turno.horaInicio} no son un múltiplo no negativo de esa
     * duración, lanza {@code ErrorNegocio} («Los turnos duran N minutos y comienzan cada N minutos desde la
     * apertura»).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s solicitud
     * @throws ErrorNegocio si el turno no respeta la grilla
     */
    @Override
    protected void verificar(SolicitudReserva s) {
        int duracion = regla.getDuracionTurnoMinutos();
        long desdeApertura = Duration.between(disponibilidad.getHoraApertura(), s.getTurno().getHoraInicio()).toMinutes();
        Integer duracionTurno = s.getTurno().duracionMinutos();
        if (duracionTurno == null || duracionTurno != duracion || desdeApertura < 0 || desdeApertura % duracion != 0) {
            throw new ErrorNegocio("Los turnos duran " + duracion + " minutos y comienzan cada " + duracion
                    + " minutos desde la apertura.");
        }
    }
}

package ar.edu.ub.club.agenda.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.recursos.dominio.Deporte;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del servicio de dominio {@code CalendarioTurnos} (grilla de turnos de CU-26) sin base de
 * datos: división del horario en turnos, feriados, días no hábiles, turnos ocupados y anticipación mínima.
 *
 * <p>Casos de uso: CU-26
 */
class CalendarioTurnosTest {

    /** Lunes de la semana de prueba. */
    private static final LocalDate LUNES = LocalDate.of(2026, 11, 16);

    /** Reglas de demostración: 2 h de anticipación, 14 días, turnos de 60 minutos. */
    private ReglaReserva regla;

    /** Disponibilidad de demostración: 08 a 22 todos los días, feriado el 25/12. */
    private DisponibilidadClub disponibilidad;

    /** Cancha activa de un deporte activo. */
    private Cancha cancha;

    /** Calendario con el reloj fijo el domingo 15/11/2026 a las 10:00. */
    private CalendarioTurnos calendario;

    /**
     * Prepara los datos de demostración.
     *
     * <p>Casos de uso: CU-26
     */
    @BeforeEach
    void preparar() {
        regla = ReglaReserva.reconstituir(1L, 2, 14, 60, 3, 4, 4);
        disponibilidad = DisponibilidadClub.reconstituir(1L, LocalTime.of(8, 0), LocalTime.of(22, 0));
        disponibilidad.restaurarAsociaciones(EnumSet.allOf(DiaSemana.class),
                List.of(Feriado.reconstituir(1L, LocalDate.of(2026, 12, 25), "Navidad")));
        cancha = new Cancha("Cancha 1", new Deporte("Tenis", null), null, null);
        calendario = calendarioA(ZonedDateTime.of(2026, 11, 15, 10, 0, 0, 0, RelojSistema.ZONA_CLUB));
    }

    /**
     * Crea un calendario con el reloj fijo en el instante dado.
     *
     * <p>Casos de uso: CU-26
     * @param momento instante actual simulado
     * @return calendario
     */
    private static CalendarioTurnos calendarioA(ZonedDateTime momento) {
        return new CalendarioTurnos(new RelojSistema(Clock.fixed(momento.toInstant(), RelojSistema.ZONA_CLUB)));
    }

    /**
     * De 08 a 22 con turnos de 60 minutos salen 14 turnos alineados a la apertura, todos libres.
     *
     * <p>Casos de uso: CU-26
     */
    @Test
    void generaLaGrillaDesdeLaApertura() {
        List<TurnoDisponible> grilla = calendario.generarTurnos(cancha, LUNES, disponibilidad, regla, List.of());
        assertEquals(14, grilla.size());
        assertEquals(LocalTime.of(8, 0), grilla.get(0).getTurno().getHoraInicio());
        assertEquals(LocalTime.of(22, 0), grilla.get(13).getTurno().getHoraFin());
        assertTrue(grilla.stream().allMatch(t -> t.getEstado() == EstadoTurno.LIBRE));
    }

    /**
     * Con turnos de 90 minutos el último turno que entra termina a las 21:30.
     *
     * <p>Casos de uso: CU-26
     */
    @Test
    void noGeneraTurnosQueExcedanElCierre() {
        ReglaReserva larga = ReglaReserva.reconstituir(1L, 2, 14, 90, 3, 4, 4);
        List<TurnoDisponible> grilla = calendario.generarTurnos(cancha, LUNES, disponibilidad, larga, List.of());
        assertEquals(9, grilla.size());
        assertEquals(LocalTime.of(21, 30), grilla.get(8).getTurno().getHoraFin());
    }

    /**
     * Un feriado no tiene turnos.
     *
     * <p>Casos de uso: CU-26
     */
    @Test
    void feriadoSinTurnos() {
        assertTrue(calendario.generarTurnos(cancha, LocalDate.of(2026, 12, 25), disponibilidad, regla, List.of()).isEmpty());
    }

    /**
     * Un día que no es de atención no tiene turnos.
     *
     * <p>Casos de uso: CU-26
     */
    @Test
    void diaNoHabilSinTurnos() {
        disponibilidad.restaurarAsociaciones(EnumSet.range(DiaSemana.MARTES, DiaSemana.DOMINGO), List.of());
        assertTrue(calendario.generarTurnos(cancha, LUNES, disponibilidad, regla, List.of()).isEmpty());
    }

    /**
     * Los turnos que se superponen con una reserva confirmada quedan OCUPADO; los consecutivos no (RN-12).
     *
     * <p>Casos de uso: CU-26
     */
    @Test
    void marcaLosTurnosOcupados() {
        Turno reserva = new Turno(LUNES, LocalTime.of(10, 30), LocalTime.of(11, 30));
        List<TurnoDisponible> grilla = calendario.generarTurnos(cancha, LUNES, disponibilidad, regla, List.of(reserva));
        assertEquals(EstadoTurno.LIBRE, grilla.get(1).getEstado());      // 09-10
        assertEquals(EstadoTurno.OCUPADO, grilla.get(2).getEstado());    // 10-11
        assertEquals(EstadoTurno.OCUPADO, grilla.get(3).getEstado());    // 11-12
        assertEquals(EstadoTurno.LIBRE, grilla.get(4).getEstado());      // 12-13
    }

    /**
     * Los turnos que no cumplen la anticipación mínima de 2 horas quedan NO_DISPONIBLE (RN-13).
     *
     * <p>Casos de uso: CU-26
     */
    @Test
    void marcaNoDisponiblesLosQueNoCumplenLaAnticipacion() {
        CalendarioTurnos hoy = calendarioA(ZonedDateTime.of(2026, 11, 16, 9, 30, 0, 0, RelojSistema.ZONA_CLUB));
        List<TurnoDisponible> grilla = hoy.generarTurnos(cancha, LUNES, disponibilidad, regla, List.of());
        assertEquals(EstadoTurno.NO_DISPONIBLE, grilla.get(0).getEstado()); // 08
        assertEquals(EstadoTurno.NO_DISPONIBLE, grilla.get(3).getEstado()); // 11:00 < 11:30
        assertEquals(EstadoTurno.LIBRE, grilla.get(4).getEstado());         // 12:00
    }
}

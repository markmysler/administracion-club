package ar.edu.ub.club.agenda.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.agenda.aplicacion.DatosReglaReserva;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de las validaciones de {@code DisponibilidadClub} (CU-24), {@code ReglaReserva} (CU-25, CU-26)
 * y del objeto de valor {@code Turno}, sin base de datos.
 *
 * <p>Casos de uso: CU-24, CU-25, CU-26
 */
class DisponibilidadYReglaTest {

    /**
     * Disponibilidad de demostración (08 a 22 todos los días, feriado 25/12).
     *
     * <p>Casos de uso: CU-24
     * @return disponibilidad
     */
    private static DisponibilidadClub disponibilidad() {
        DisponibilidadClub d = DisponibilidadClub.reconstituir(1L, LocalTime.of(8, 0), LocalTime.of(22, 0));
        d.restaurarAsociaciones(EnumSet.allOf(DiaSemana.class),
                List.of(Feriado.reconstituir(1L, LocalDate.of(2026, 12, 25), "Navidad")));
        return d;
    }

    /**
     * Datos de reglas válidos (los de demostración).
     *
     * <p>Casos de uso: CU-25
     * @return datos
     */
    private static DatosReglaReserva datosValidos() {
        DatosReglaReserva datos = new DatosReglaReserva();
        datos.setAnticipacionMinimaHoras(2);
        datos.setAnticipacionMaximaDias(14);
        datos.setDuracionTurnoMinutos(60);
        datos.setLimiteReservasVigentesPorSocio(3);
        datos.setAnticipacionMinimaCancelacionHoras(4);
        datos.setAnticipacionMinimaModificacionHoras(4);
        return datos;
    }

    /**
     * Sin días de atención: FA3 con el campo {@code diasAtencion}.
     *
     * <p>Casos de uso: CU-24
     */
    @Test
    void disponibilidadExigeAlMenosUnDia() {
        ErrorNegocio e = assertThrows(ErrorNegocio.class, () -> disponibilidad().modificar(Set.of(),
                LocalTime.of(8, 0), LocalTime.of(22, 0), 60));
        assertEquals("diasAtencion", e.getCampo());
    }

    /**
     * Cierre no posterior a la apertura: FA3.
     *
     * <p>Casos de uso: CU-24
     */
    @Test
    void disponibilidadExigeCierrePosterior() {
        assertThrows(ErrorNegocio.class, () -> disponibilidad().modificar(EnumSet.of(DiaSemana.LUNES),
                LocalTime.of(20, 0), LocalTime.of(20, 0), 60));
    }

    /**
     * Horario más corto que la duración del turno: FA4.
     *
     * <p>Casos de uso: CU-24
     */
    @Test
    void disponibilidadExigeLugarParaUnTurno() {
        assertThrows(ErrorNegocio.class, () -> disponibilidad().modificar(EnumSet.of(DiaSemana.LUNES),
                LocalTime.of(20, 0), LocalTime.of(20, 30), 60));
    }

    /**
     * Valores válidos se aplican; {@code atiende} y {@code dentroDelHorario} usan los nuevos valores.
     *
     * <p>Casos de uso: CU-24
     */
    @Test
    void disponibilidadAplicaLosCambios() {
        DisponibilidadClub d = disponibilidad();
        d.modificar(EnumSet.of(DiaSemana.LUNES, DiaSemana.MARTES), LocalTime.of(9, 0), LocalTime.of(18, 0), 60);
        assertTrue(d.atiende(LocalDate.of(2026, 11, 16)));          // lunes
        assertFalse(d.atiende(LocalDate.of(2026, 11, 18)));         // miércoles
        assertTrue(d.dentroDelHorario(new Turno(LocalDate.of(2026, 11, 16), LocalTime.of(17, 0), LocalTime.of(18, 0))));
        assertFalse(d.dentroDelHorario(new Turno(LocalDate.of(2026, 11, 16), LocalTime.of(8, 0), LocalTime.of(9, 0))));
    }

    /**
     * El club no atiende en un feriado aunque sea día de atención.
     *
     * <p>Casos de uso: CU-24, CU-26
     */
    @Test
    void noAtiendeEnFeriado() {
        assertFalse(disponibilidad().atiende(LocalDate.of(2026, 12, 25)));
    }

    /**
     * Las reglas válidas se aplican.
     *
     * <p>Casos de uso: CU-25
     */
    @Test
    void reglaAplicaValoresValidos() {
        ReglaReserva regla = ReglaReserva.reconstituir(1L, 1, 1, 30, 1, 1, 1);
        regla.modificar(datosValidos(), disponibilidad());
        assertEquals(60, regla.getDuracionTurnoMinutos());
        assertEquals(14, regla.getAnticipacionMaximaDias());
    }

    /**
     * Un valor no positivo se rechaza con su campo (FA2).
     *
     * <p>Casos de uso: CU-25
     */
    @Test
    void reglaRechazaValoresNoPositivos() {
        DatosReglaReserva datos = datosValidos();
        datos.setLimiteReservasVigentesPorSocio(0);
        ErrorNegocio e = assertThrows(ErrorNegocio.class,
                () -> ReglaReserva.reconstituir(1L, 2, 14, 60, 3, 4, 4).modificar(datos, disponibilidad()));
        assertEquals("limiteReservasVigentesPorSocio", e.getCampo());
    }

    /**
     * Anticipación mínima no menor que la máxima y duración mayor que el horario: FA3.
     *
     * <p>Casos de uso: CU-25
     */
    @Test
    void reglaRechazaValoresIncompatibles() {
        DatosReglaReserva minima = datosValidos();
        minima.setAnticipacionMinimaHoras(14 * 24);
        assertEquals("anticipacionMinimaHoras", assertThrows(ErrorNegocio.class,
                () -> ReglaReserva.reconstituir(1L, 2, 14, 60, 3, 4, 4).modificar(minima, disponibilidad())).getCampo());
        DatosReglaReserva duracion = datosValidos();
        duracion.setDuracionTurnoMinutos(15 * 60);
        assertEquals("duracionTurnoMinutos", assertThrows(ErrorNegocio.class,
                () -> ReglaReserva.reconstituir(1L, 2, 14, 60, 3, 4, 4).modificar(duracion, disponibilidad())).getCampo());
    }

    /**
     * {@code fechaConsultable}, {@code cumpleAnticipacionMinima} y {@code admiteCancelacion} (RN-13, RN-14).
     *
     * <p>Casos de uso: CU-26, CU-30
     */
    @Test
    void reglaAnticipaciones() {
        ReglaReserva regla = ReglaReserva.reconstituir(1L, 2, 14, 60, 3, 4, 4);
        LocalDate hoy = LocalDate.of(2026, 11, 16);
        assertTrue(regla.fechaConsultable(hoy, hoy));
        assertTrue(regla.fechaConsultable(hoy.plusDays(14), hoy));
        assertFalse(regla.fechaConsultable(hoy.plusDays(15), hoy));
        assertFalse(regla.fechaConsultable(hoy.minusDays(1), hoy));
        Turno turno = new Turno(hoy, LocalTime.of(12, 0), LocalTime.of(13, 0));
        assertTrue(regla.cumpleAnticipacionMinima(turno, LocalDateTime.of(hoy, LocalTime.of(10, 0))));
        assertFalse(regla.cumpleAnticipacionMinima(turno, LocalDateTime.of(hoy, LocalTime.of(10, 1))));
        assertTrue(regla.admiteCancelacion(turno, LocalDateTime.of(hoy, LocalTime.of(8, 0))));
        assertFalse(regla.admiteCancelacion(turno, LocalDateTime.of(hoy, LocalTime.of(9, 0))));
    }

    /**
     * Turno: horario inválido, superposición semiabierta y duración (RN-12).
     *
     * <p>Casos de uso: CU-26
     */
    @Test
    void turnoValidaYDetectaSuperposicion() {
        LocalDate dia = LocalDate.of(2026, 11, 16);
        assertThrows(ErrorNegocio.class, () -> new Turno(dia, LocalTime.of(10, 0), LocalTime.of(10, 0)));
        Turno diez = new Turno(dia, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertFalse(diez.seSuperponeCon(new Turno(dia, LocalTime.of(11, 0), LocalTime.of(12, 0))));
        assertTrue(diez.seSuperponeCon(new Turno(dia, LocalTime.of(10, 30), LocalTime.of(11, 30))));
        assertFalse(diez.seSuperponeCon(new Turno(dia.plusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0))));
        assertEquals(60, diez.duracionMinutos());
        assertEquals(diez, new Turno(dia, LocalTime.of(10, 0), LocalTime.of(11, 0)));
    }

    /**
     * Conversión ISO de {@code DiaSemana} (lo que guarda {@code dia_atencion.dia}).
     *
     * <p>Casos de uso: CU-24
     */
    @Test
    void diaSemanaIso() {
        assertEquals(1, DiaSemana.LUNES.numeroIso());
        assertEquals(DiaSemana.DOMINGO, DiaSemana.desdeNumeroIso(7));
        assertEquals(DiaSemana.LUNES, DiaSemana.de(LocalDate.of(2026, 11, 16)));
    }
}

package ar.edu.ub.club.reservas.dominio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import ar.edu.ub.club.agenda.dominio.Feriado;
import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.quality.Strictness;

/**
 * Pruebas de la Cadena de Responsabilidad de validadores de reserva: recorrido en orden, corte en el primer error
 * (método plantilla {@code ValidadorReservaEncadenado.validar}) y la regla de cada uno de los siete validadores
 * (RN-11 a RN-15).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
class CadenaValidadoresTest {

    /** Momento de la solicitud. */
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 2, 10, 0);

    /** Fecha del turno pedido (al día siguiente). */
    private static final LocalDate FECHA = LocalDate.of(2026, 11, 3);

    /** Socio activo. */
    private Socio socio;

    /** Cancha disponible. */
    private Cancha cancha;

    /** Turno de 18 a 19 (alineado a la grilla de 60 minutos desde las 8). */
    private Turno turno;

    /** Regla: 2 h mínimas, 14 días máximos, 60 minutos, 3 vigentes, 24 h de modificación. */
    private ReglaReserva regla;

    /** Disponibilidad: atiende, de 8 a 22. */
    private DisponibilidadClub disponibilidad;

    /**
     * Prepara los dobles con una configuración que cumple todas las reglas.
     *
     * <p>Casos de uso: ninguno (pruebas)
     */
    @BeforeEach
    void preparar() {
        socio = Dobles.socio(7L, true);
        cancha = Dobles.cancha(1L, true);
        turno = Dobles.turno(FECHA, LocalTime.of(18, 0), LocalTime.of(19, 0));
        regla = mock(ReglaReserva.class, withSettings().strictness(Strictness.LENIENT));
        when(regla.getAnticipacionMinimaHoras()).thenReturn(2);
        when(regla.getAnticipacionMaximaDias()).thenReturn(14);
        when(regla.getDuracionTurnoMinutos()).thenReturn(60);
        when(regla.getLimiteReservasVigentesPorSocio()).thenReturn(3);
        when(regla.getAnticipacionMinimaModificacionHoras()).thenReturn(24);
        disponibilidad = mock(DisponibilidadClub.class, withSettings().strictness(Strictness.LENIENT));
        when(disponibilidad.getHoraApertura()).thenReturn(LocalTime.of(8, 0));
        when(disponibilidad.getHoraCierre()).thenReturn(LocalTime.of(22, 0));
        when(disponibilidad.atiende(any())).thenReturn(true);
        when(disponibilidad.dentroDelHorario(any())).thenReturn(true);
    }

    /**
     * Solicitud de alta del socio con el turno indicado.
     *
     * <p>Casos de uso: ninguno (pruebas)
     * @param t turno
     * @return solicitud
     */
    private SolicitudReserva alta(Turno t) {
        return new SolicitudReserva(socio, cancha, t, null, TipoOperacion.ALTA, OrigenSolicitud.SOCIO, AHORA);
    }

    /**
     * Grupo con o sin cuota vencida.
     *
     * <p>Casos de uso: ninguno (pruebas)
     * @param vencida true si tiene cuota vencida
     * @return grupo simulado
     */
    private static GrupoFamiliar grupo(boolean vencida) {
        GrupoFamiliar g = mock(GrupoFamiliar.class);
        when(g.tieneCuotaVencida()).thenReturn(vencida);
        return g;
    }

    /**
     * Arma la cadena completa en el orden de la fábrica.
     *
     * <p>Casos de uso: ninguno (pruebas)
     * @param g grupo
     * @param vigentes reservas vigentes
     * @param ocupados turnos ocupados
     * @return primer eslabón
     */
    private ValidadorReserva cadena(GrupoFamiliar g, List<Reserva> vigentes, List<Turno> ocupados) {
        ValidadorSocioHabilitado v1 = new ValidadorSocioHabilitado(g);
        ValidadorRecursoActivo v2 = new ValidadorRecursoActivo();
        ValidadorHorarioClub v3 = new ValidadorHorarioClub(disponibilidad, List.of());
        ValidadorDuracionTurno v4 = new ValidadorDuracionTurno(regla, disponibilidad);
        ValidadorAnticipacion v5 = new ValidadorAnticipacion(regla);
        ValidadorLimiteReservas v6 = new ValidadorLimiteReservas(regla, vigentes);
        ValidadorTurnoLibre v7 = new ValidadorTurnoLibre(ocupados);
        v1.enlazar(v2);
        v2.enlazar(v3);
        v3.enlazar(v4);
        v4.enlazar(v5);
        v5.enlazar(v6);
        v6.enlazar(v7);
        return v1;
    }

    /**
     * Eslabón de prueba que registra su paso y, si se indica, rechaza.
     *
     * <p>Casos de uso: ninguno (pruebas)
     */
    private static final class Registrador extends ValidadorReservaEncadenado {

        /** Nombre del eslabón. */
        private final String nombre;

        /** Registro compartido del recorrido. */
        private final List<String> recorrido;

        /** True si rechaza la solicitud. */
        private final boolean rechaza;

        /**
         * Crea el eslabón.
         *
         * <p>Casos de uso: ninguno (pruebas)
         * @param nombre nombre
         * @param recorrido registro del recorrido
         * @param rechaza true si lanza {@code ErrorNegocio}
         */
        Registrador(String nombre, List<String> recorrido, boolean rechaza) {
            this.nombre = nombre;
            this.recorrido = recorrido;
            this.rechaza = rechaza;
        }

        /**
         * Registra el paso y rechaza si corresponde.
         *
         * <p>Casos de uso: ninguno (pruebas)
         * @param s solicitud
         */
        @Override
        protected void verificar(SolicitudReserva s) {
            recorrido.add(nombre);
            if (rechaza) {
                throw new ErrorNegocio("rechazo de " + nombre);
            }
        }
    }

    /**
     * La cadena recorre los eslabones en el orden de enlace y {@code enlazar} devuelve el siguiente.
     *
     * <p>Casos de uso: CU-27
     */
    @Test
    void recorreEnOrden() {
        List<String> recorrido = new ArrayList<>();
        Registrador a = new Registrador("a", recorrido, false);
        Registrador b = new Registrador("b", recorrido, false);
        Registrador c = new Registrador("c", recorrido, false);
        assertEquals(b, a.enlazar(b));
        b.enlazar(c);
        a.validar(alta(turno));
        assertEquals(List.of("a", "b", "c"), recorrido);
    }

    /**
     * El primer eslabón que rechaza corta la cadena: los siguientes no se ejecutan.
     *
     * <p>Casos de uso: CU-27
     */
    @Test
    void cortaEnElPrimerError() {
        List<String> recorrido = new ArrayList<>();
        Registrador a = new Registrador("a", recorrido, false);
        Registrador b = new Registrador("b", recorrido, true);
        Registrador c = new Registrador("c", recorrido, true);
        a.enlazar(b);
        b.enlazar(c);
        ErrorNegocio e = assertThrows(ErrorNegocio.class, () -> a.validar(alta(turno)));
        assertEquals("rechazo de b", e.getMensaje());
        assertEquals(List.of("a", "b"), recorrido);
    }

    /**
     * Con todos los datos en regla, la cadena completa acepta la solicitud.
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    @Test
    void cadenaCompletaAceptaSolicitudValida() {
        assertDoesNotThrow(() -> cadena(grupo(false), List.of(), List.of()).validar(alta(turno)));
    }

    /**
     * La cadena completa corta en el socio inactivo antes de mirar la cancha.
     *
     * <p>Casos de uso: CU-27
     */
    @Test
    void cadenaCompletaCortaEnElPrimerValidador() {
        socio = Dobles.socio(7L, false);
        cancha = Dobles.cancha(1L, false);
        ErrorNegocio e = assertThrows(ErrorNegocio.class,
                () -> cadena(grupo(false), List.of(), List.of()).validar(alta(turno)));
        assertTrue(e.getMensaje().contains("inactiva"));
    }

    /**
     * Validador 1: socio inactivo y cuota vencida (alta), deuda omitida en la modificación del administrador.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     */
    @Test
    void validadorSocioHabilitado() {
        assertThrows(ErrorNegocio.class, () -> new ValidadorSocioHabilitado(grupo(false)).validar(
                new SolicitudReserva(Dobles.socio(7L, false), cancha, turno, null, TipoOperacion.ALTA,
                        OrigenSolicitud.SOCIO, AHORA)));
        ErrorNegocio socioDeuda = assertThrows(ErrorNegocio.class,
                () -> new ValidadorSocioHabilitado(grupo(true)).validar(alta(turno)));
        assertTrue(socioDeuda.getMensaje().contains("pagarla online"));
        ErrorNegocio adminDeuda = assertThrows(ErrorNegocio.class, () -> new ValidadorSocioHabilitado(grupo(true))
                .validar(new SolicitudReserva(socio, cancha, turno, null, TipoOperacion.ALTA,
                        OrigenSolicitud.ADMINISTRADOR, AHORA)));
        assertTrue(adminDeuda.getMensaje().contains("registre antes su pago"));
        Reserva original = Reserva.reconstituir(9L, 0, turno, EstadoReserva.desdeCodigo("CONFIRMADA"), AHORA, null);
        assertDoesNotThrow(() -> new ValidadorSocioHabilitado(grupo(true)).validar(new SolicitudReserva(socio, cancha,
                turno, original, TipoOperacion.MODIFICACION, OrigenSolicitud.ADMINISTRADOR, AHORA)));
    }

    /**
     * Validador 2: cancha o deporte inactivos.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     */
    @Test
    void validadorRecursoActivo() {
        assertDoesNotThrow(() -> new ValidadorRecursoActivo().validar(alta(turno)));
        cancha = Dobles.cancha(1L, false);
        assertThrows(ErrorNegocio.class, () -> new ValidadorRecursoActivo().validar(alta(turno)));
    }

    /**
     * Validador 3: feriado, día sin atención y turno fuera de horario.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     */
    @Test
    void validadorHorarioClub() {
        Feriado feriado = mock(Feriado.class);
        ErrorNegocio e = assertThrows(ErrorNegocio.class,
                () -> new ValidadorHorarioClub(disponibilidad, List.of(feriado)).validar(alta(turno)));
        assertTrue(e.getMensaje().contains("feriado"));
        when(disponibilidad.dentroDelHorario(any())).thenReturn(false);
        e = assertThrows(ErrorNegocio.class, () -> new ValidadorHorarioClub(disponibilidad, List.of()).validar(alta(turno)));
        assertTrue(e.getMensaje().contains("fuera del horario"));
        when(disponibilidad.atiende(any())).thenReturn(false);
        e = assertThrows(ErrorNegocio.class, () -> new ValidadorHorarioClub(disponibilidad, List.of()).validar(alta(turno)));
        assertTrue(e.getMensaje().contains("no atiende"));
    }

    /**
     * Validador 4: duración distinta de la regla o turno desalineado de la grilla.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     */
    @Test
    void validadorDuracionTurno() {
        ValidadorDuracionTurno v = new ValidadorDuracionTurno(regla, disponibilidad);
        assertDoesNotThrow(() -> v.validar(alta(turno)));
        assertThrows(ErrorNegocio.class, () -> v.validar(alta(Dobles.turno(FECHA, LocalTime.of(18, 0), LocalTime.of(19, 30)))));
        assertThrows(ErrorNegocio.class, () -> v.validar(alta(Dobles.turno(FECHA, LocalTime.of(18, 30), LocalTime.of(19, 30)))));
    }

    /**
     * Validador 5: anticipación mínima, máxima y la de modificación.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     */
    @Test
    void validadorAnticipacion() {
        ValidadorAnticipacion v = new ValidadorAnticipacion(regla);
        assertDoesNotThrow(() -> v.validar(alta(turno)));
        Turno muyPronto = Dobles.turno(LocalDate.of(2026, 11, 2), LocalTime.of(11, 0), LocalTime.of(12, 0));
        assertThrows(ErrorNegocio.class, () -> v.validar(alta(muyPronto)));
        Turno muyLejos = Dobles.turno(LocalDate.of(2026, 11, 20), LocalTime.of(11, 0), LocalTime.of(12, 0));
        assertThrows(ErrorNegocio.class, () -> v.validar(alta(muyLejos)));
        Turno en20Horas = Dobles.turno(LocalDate.of(2026, 11, 3), LocalTime.of(6, 0), LocalTime.of(7, 0));
        assertDoesNotThrow(() -> v.validar(alta(en20Horas)));
        Reserva original = Reserva.reconstituir(9L, 0, turno, EstadoReserva.desdeCodigo("CONFIRMADA"), AHORA, null);
        assertThrows(ErrorNegocio.class, () -> v.validar(new SolicitudReserva(socio, cancha, en20Horas, original,
                TipoOperacion.MODIFICACION, OrigenSolicitud.ADMINISTRADOR, AHORA)));
    }

    /**
     * Validador 6: límite de reservas vigentes; en una modificación no se cuenta la original.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     */
    @Test
    void validadorLimiteReservas() {
        Reserva r1 = Reserva.reconstituir(1L, 0, turno, EstadoReserva.desdeCodigo("CONFIRMADA"), AHORA, null);
        Reserva r2 = Reserva.reconstituir(2L, 0, turno, EstadoReserva.desdeCodigo("CONFIRMADA"), AHORA, null);
        Reserva r3 = Reserva.reconstituir(3L, 0, turno, EstadoReserva.desdeCodigo("CONFIRMADA"), AHORA, null);
        assertDoesNotThrow(() -> new ValidadorLimiteReservas(regla, List.of(r1, r2)).validar(alta(turno)));
        ErrorNegocio e = assertThrows(ErrorNegocio.class,
                () -> new ValidadorLimiteReservas(regla, List.of(r1, r2, r3)).validar(alta(turno)));
        assertTrue(e.getMensaje().contains("límite de 3"));
        assertDoesNotThrow(() -> new ValidadorLimiteReservas(regla, List.of(r1, r2, r3)).validar(new SolicitudReserva(
                socio, cancha, turno, r3, TipoOperacion.MODIFICACION, OrigenSolicitud.ADMINISTRADOR, AHORA)));
    }

    /**
     * Validador 7: turno superpuesto con uno ocupado; los consecutivos no se superponen.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     */
    @Test
    void validadorTurnoLibre() {
        Turno consecutivo = Dobles.turno(FECHA, LocalTime.of(17, 0), LocalTime.of(18, 0));
        assertDoesNotThrow(() -> new ValidadorTurnoLibre(List.of(consecutivo)).validar(alta(turno)));
        Turno mismo = Dobles.turno(FECHA, LocalTime.of(18, 0), LocalTime.of(19, 0));
        ErrorNegocio e = assertThrows(ErrorNegocio.class, () -> new ValidadorTurnoLibre(List.of(mismo)).validar(alta(turno)));
        assertTrue(e.getMensaje().contains("ya fue reservado"));
    }

    /**
     * {@code SolicitudReserva}: coherencia entre tipo y reserva original y control de deuda.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     */
    @Test
    void solicitudReserva() {
        assertThrows(IllegalArgumentException.class, () -> new SolicitudReserva(socio, cancha, turno, null,
                TipoOperacion.MODIFICACION, OrigenSolicitud.ADMINISTRADOR, AHORA));
        assertThrows(ErrorNegocio.class, () -> alta(null));
        assertTrue(alta(turno).controlaDeuda());
    }
}

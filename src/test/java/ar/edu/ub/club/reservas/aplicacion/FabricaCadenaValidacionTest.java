package ar.edu.ub.club.reservas.aplicacion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import ar.edu.ub.club.agenda.dominio.ConsultaTurnosOcupados;
import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.Consulta;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.reservas.dominio.EstadoReserva;
import ar.edu.ub.club.reservas.dominio.OrigenSolicitud;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.reservas.dominio.SolicitudReserva;
import ar.edu.ub.club.reservas.dominio.TipoOperacion;
import ar.edu.ub.club.reservas.persistencia.ReservasFuturasDeSocio;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.quality.Strictness;

/**
 * Pruebas de {@code FabricaCadenaValidacion} sin base de datos (fachada y consulta de turnos simuladas): en una
 * modificación sobre la misma cancha la reserva original no se cuenta como ocupada ni como vigente
 * ({@code ocupadosSinOriginal}); en un alta, el mismo turno ocupado rechaza la solicitud.
 *
 * <p>Casos de uso: CU-27, CU-32
 */
class FabricaCadenaValidacionTest {

    /** Momento de la solicitud. */
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 2, 10, 0);

    /** Fecha del turno. */
    private static final LocalDate FECHA = LocalDate.of(2026, 11, 4);

    /** Fachada de persistencia simulada. */
    private FachadaPersistencia fachada;

    /** Consulta de turnos ocupados simulada. */
    private ConsultaTurnosOcupados consultaTurnos;

    /** Socio activo. */
    private Socio socio;

    /** Cancha disponible. */
    private Cancha cancha;

    /**
     * Turno simulado con fecha, horario, inicio, duración y superposición.
     *
     * <p>Casos de uso: ninguno (pruebas)
     * @param inicio hora de inicio
     * @param fin hora de fin
     * @return turno
     */
    private static Turno turno(LocalTime inicio, LocalTime fin) {
        Turno t = mock(Turno.class, withSettings().strictness(Strictness.LENIENT));
        when(t.getFecha()).thenReturn(FECHA);
        when(t.getHoraInicio()).thenReturn(inicio);
        when(t.getHoraFin()).thenReturn(fin);
        when(t.inicio()).thenReturn(LocalDateTime.of(FECHA, inicio));
        when(t.fin()).thenReturn(LocalDateTime.of(FECHA, fin));
        when(t.duracionMinutos()).thenReturn((int) Duration.between(inicio, fin).toMinutes());
        when(t.seSuperponeCon(any())).thenAnswer(inv -> {
            Turno o = inv.getArgument(0);
            return LocalDateTime.of(FECHA, inicio).isBefore(o.fin()) && o.inicio().isBefore(LocalDateTime.of(FECHA, fin));
        });
        return t;
    }

    /**
     * Prepara la fachada simulada: sin grupo, disponibilidad de 8 a 22, regla de 60 minutos y una reserva vigente.
     *
     * <p>Casos de uso: ninguno (pruebas)
     */
    @BeforeEach
    @SuppressWarnings("unchecked")
    void preparar() {
        fachada = mock(FachadaPersistencia.class, withSettings().strictness(Strictness.LENIENT));
        consultaTurnos = mock(ConsultaTurnosOcupados.class);
        socio = mock(Socio.class, withSettings().strictness(Strictness.LENIENT));
        when(socio.getId()).thenReturn(7L);
        when(socio.estaActiva()).thenReturn(true);
        cancha = mock(Cancha.class, withSettings().strictness(Strictness.LENIENT));
        when(cancha.getId()).thenReturn(1L);
        when(cancha.estaDisponibleParaReservar()).thenReturn(true);
        DisponibilidadClub disponibilidad = mock(DisponibilidadClub.class, withSettings().strictness(Strictness.LENIENT));
        when(disponibilidad.getHoraApertura()).thenReturn(LocalTime.of(8, 0));
        when(disponibilidad.atiende(any())).thenReturn(true);
        when(disponibilidad.dentroDelHorario(any())).thenReturn(true);
        ReglaReserva regla = mock(ReglaReserva.class, withSettings().strictness(Strictness.LENIENT));
        when(regla.getAnticipacionMinimaHoras()).thenReturn(2);
        when(regla.getAnticipacionMinimaModificacionHoras()).thenReturn(24);
        when(regla.getAnticipacionMaximaDias()).thenReturn(14);
        when(regla.getDuracionTurnoMinutos()).thenReturn(60);
        when(regla.getLimiteReservasVigentesPorSocio()).thenReturn(1);
        when(fachada.buscarUno(any(Consulta.class))).thenReturn(Optional.empty());
        when(fachada.get(any(), eq(DisponibilidadClub.class))).thenReturn(disponibilidad);
        when(fachada.get(any(), eq(ReglaReserva.class))).thenReturn(regla);
    }

    /**
     * Reserva confirmada de 18 a 19 en la cancha 1 (la original de la modificación).
     *
     * <p>Casos de uso: ninguno (pruebas)
     * @return reserva
     */
    private Reserva original() {
        Reserva r = Reserva.reconstituir(9L, 0, turno(LocalTime.of(18, 0), LocalTime.of(19, 0)),
                EstadoReserva.desdeCodigo("CONFIRMADA"), AHORA, null);
        r.restaurarAsociaciones(socio, cancha, socio, null);
        return r;
    }

    /**
     * Programa la fachada para que {@code buscar} devuelva las vigentes (y ningún feriado).
     *
     * <p>Casos de uso: ninguno (pruebas)
     * @param vigentes reservas vigentes del socio
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void vigentes(List<Reserva> vigentes) {
        when(fachada.buscar(any(Consulta.class))).thenAnswer(inv -> {
            Consulta c = inv.getArgument(0);
            return c instanceof ReservasFuturasDeSocio ? vigentes : List.of();
        });
    }

    /**
     * Modificar la propia reserva a un turno que se superpone con el original es válido: la fábrica excluye el
     * turno original de los ocupados y la reserva original de las vigentes.
     *
     * <p>Casos de uso: CU-32
     */
    @Test
    void modificacionExcluyeLaReservaOriginal() {
        Reserva r = original();
        vigentes(List.of(r));
        when(consultaTurnos.turnosOcupados(1L, FECHA)).thenReturn(List.of(r.getTurno()));
        Turno nuevo = turno(LocalTime.of(18, 0), LocalTime.of(19, 0));
        SolicitudReserva s = new SolicitudReserva(socio, cancha, nuevo, r, TipoOperacion.MODIFICACION,
                OrigenSolicitud.ADMINISTRADOR, AHORA);
        FabricaCadenaValidacion fabrica = new FabricaCadenaValidacion(fachada, consultaTurnos);
        assertDoesNotThrow(() -> fabrica.crearCadena(s).validar(s));
    }

    /**
     * En un alta el mismo turno ocupado lo rechaza la cadena (límite o turno libre).
     *
     * <p>Casos de uso: CU-27
     */
    @Test
    void altaConTurnoOcupadoSeRechaza() {
        vigentes(List.of());
        Turno ocupado = turno(LocalTime.of(18, 0), LocalTime.of(19, 0));
        when(consultaTurnos.turnosOcupados(1L, FECHA)).thenReturn(List.of(ocupado));
        Turno pedido = turno(LocalTime.of(18, 0), LocalTime.of(19, 0));
        SolicitudReserva s = new SolicitudReserva(socio, cancha, pedido, null, TipoOperacion.ALTA,
                OrigenSolicitud.SOCIO, AHORA);
        FabricaCadenaValidacion fabrica = new FabricaCadenaValidacion(fachada, consultaTurnos);
        ErrorNegocio e = assertThrows(ErrorNegocio.class, () -> fabrica.crearCadena(s).validar(s));
        assertTrue(e.getMensaje().contains("ya fue reservado"));
    }
}

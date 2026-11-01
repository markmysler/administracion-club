package ar.edu.ub.club.reservas.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del patrón State de {@code Reserva}: transición inicial {@code confirmar()}, cancelar, modificar,
 * finalizar y las transiciones inválidas desde Cancelada, Finalizada y el pseudoestado inicial.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33; tareas programadas: finalización de reservas cumplidas
 */
class ReservaEstadoTest {

    /** Momento de referencia de las pruebas. */
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 2, 10, 0);

    /** Socio titular. */
    private Socio socio;

    /** Cancha de la reserva. */
    private Cancha cancha;

    /** Turno futuro (al día siguiente). */
    private Turno turno;

    /** Administrador que opera. */
    private Administrador admin;

    /**
     * Prepara los dobles.
     *
     * <p>Casos de uso: ninguno (pruebas)
     */
    @BeforeEach
    void preparar() {
        socio = Dobles.socio(7L, true);
        cancha = Dobles.cancha(1L, true);
        turno = Dobles.turno(LocalDate.of(2026, 11, 3), LocalTime.of(18, 0), LocalTime.of(19, 0));
        admin = Dobles.administrador();
    }

    /**
     * Reserva nueva confirmada.
     *
     * <p>Casos de uso: ninguno (pruebas)
     * @return reserva en estado Confirmada
     */
    private Reserva confirmada() {
        Reserva r = new Reserva(socio, cancha, turno, socio, AHORA);
        r.confirmar();
        return r;
    }

    /**
     * El constructor deja el pseudoestado inicial con versión 0 y {@code confirmar()} pasa a Confirmada.
     *
     * <p>Casos de uso: CU-27
     */
    @Test
    void confirmarRealizaLaTransicionInicial() {
        Reserva r = new Reserva(socio, cancha, turno, socio, AHORA);
        assertNull(r.getEstado());
        assertEquals(0, r.getVersion());
        assertEquals(AHORA, r.getFechaCreacion());
        r.confirmar();
        assertEquals("CONFIRMADA", r.getEstado().codigo());
        assertTrue(r.esVigente(AHORA));
        assertTrue(r.perteneceA(Dobles.socio(7L, true)));
        assertFalse(r.perteneceA(Dobles.socio(8L, true)));
    }

    /**
     * Confirmar dos veces es una transición inválida.
     *
     * <p>Casos de uso: CU-27
     */
    @Test
    void confirmarDosVecesEsInvalido() {
        Reserva r = confirmada();
        assertThrows(TransicionInvalidaException.class, r::confirmar);
    }

    /**
     * En el pseudoestado inicial ninguna transición es válida.
     *
     * <p>Casos de uso: CU-30
     */
    @Test
    void sinConfirmarNoAdmiteTransiciones() {
        Reserva r = new Reserva(socio, cancha, turno, socio, AHORA);
        assertThrows(TransicionInvalidaException.class, () -> r.cancelar("x", socio, AHORA));
        assertThrows(TransicionInvalidaException.class, r::finalizar);
    }

    /**
     * Cancelar una confirmada registra motivo y cuenta y pasa a Cancelada.
     *
     * <p>Casos de uso: CU-30, CU-33
     */
    @Test
    void cancelarDesdeConfirmada() {
        Reserva r = confirmada();
        r.cancelar("Lluvia", admin, AHORA);
        assertEquals("CANCELADA", r.getEstado().codigo());
        assertEquals("Lluvia", r.getMotivoCancelacion());
        assertSame(admin, r.getCanceladaPor());
        assertFalse(r.esVigente(AHORA));
    }

    /**
     * No se cancela una reserva cuyo turno ya comenzó.
     *
     * <p>Casos de uso: CU-30
     */
    @Test
    void cancelarTurnoComenzadoEsErrorDeNegocio() {
        Reserva r = confirmada();
        assertThrows(ErrorNegocio.class, () -> r.cancelar("x", socio, LocalDateTime.of(2026, 11, 3, 18, 30)));
        assertEquals("CONFIRMADA", r.getEstado().codigo());
    }

    /**
     * Modificar cambia turno y cancha y la reserva sigue Confirmada.
     *
     * <p>Casos de uso: CU-32
     */
    @Test
    void modificarPermaneceConfirmada() {
        Reserva r = confirmada();
        Turno nuevo = Dobles.turno(LocalDate.of(2026, 11, 4), LocalTime.of(9, 0), LocalTime.of(10, 0));
        Cancha otra = Dobles.cancha(2L, true);
        r.modificar(nuevo, otra, AHORA);
        assertSame(nuevo, r.getTurno());
        assertSame(otra, r.getCancha());
        assertEquals("CONFIRMADA", r.getEstado().codigo());
    }

    /**
     * Finalizar pasa de Confirmada a Finalizada.
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     */
    @Test
    void finalizarDesdeConfirmada() {
        Reserva r = confirmada();
        r.finalizar();
        assertEquals("FINALIZADA", r.getEstado().codigo());
    }

    /**
     * Desde Cancelada todas las transiciones son inválidas.
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33
     */
    @Test
    void canceladaRechazaTransiciones() {
        Reserva r = confirmada();
        r.cancelar("x", socio, AHORA);
        assertThrows(TransicionInvalidaException.class, () -> r.cancelar("y", admin, AHORA));
        assertThrows(TransicionInvalidaException.class, () -> r.modificar(turno, cancha, AHORA));
        assertThrows(TransicionInvalidaException.class, r::finalizar);
    }

    /**
     * Desde Finalizada todas las transiciones son inválidas.
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33; tareas programadas: finalización de reservas cumplidas
     */
    @Test
    void finalizadaRechazaTransiciones() {
        Reserva r = confirmada();
        r.finalizar();
        assertThrows(TransicionInvalidaException.class, () -> r.cancelar("y", admin, AHORA));
        assertThrows(TransicionInvalidaException.class, () -> r.modificar(turno, cancha, AHORA));
        assertThrows(TransicionInvalidaException.class, r::finalizar);
    }

    /**
     * {@code desdeCodigo} devuelve instancias únicas y rechaza códigos desconocidos.
     *
     * <p>Casos de uso: todos
     */
    @Test
    void desdeCodigoDevuelveInstanciasUnicas() {
        assertSame(EstadoReserva.desdeCodigo("CONFIRMADA"), EstadoReserva.desdeCodigo("CONFIRMADA"));
        assertTrue(EstadoReserva.desdeCodigo("CANCELADA") instanceof EstadoReservaCancelada);
        assertTrue(EstadoReserva.desdeCodigo("FINALIZADA") instanceof EstadoReservaFinalizada);
        assertThrows(IllegalStateException.class, () -> EstadoReserva.desdeCodigo("OTRO"));
    }

    /**
     * {@code reconstituir} no pasa por el pseudoestado inicial.
     *
     * <p>Casos de uso: todos
     */
    @Test
    void reconstituirConservaEstadoYVersion() {
        Reserva r = Reserva.reconstituir(5L, 3, turno, EstadoReserva.desdeCodigo("CANCELADA"), AHORA, "Baja de socio");
        assertEquals(5L, r.getId());
        assertEquals(3, r.getVersion());
        assertEquals("CANCELADA", r.getEstado().codigo());
        assertEquals("Baja de socio", r.getMotivoCancelacion());
    }
}

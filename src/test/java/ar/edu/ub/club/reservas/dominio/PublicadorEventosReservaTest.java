package ar.edu.ub.club.reservas.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.ub.club.agenda.dominio.Turno;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del sujeto del patrón Observer {@code PublicadorEventosReserva}: notifica a cada observador en el orden
 * de suscripción, ignora suscripciones repetidas y propaga la excepción de un observador (revierte la operación).
 *
 * <p>Casos de uso: CU-27, CU-30, CU-32, CU-33
 */
class PublicadorEventosReservaTest {

    /** Momento del evento. */
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 2, 10, 0);

    /**
     * Evento de confirmación de una reserva de prueba.
     *
     * <p>Casos de uso: ninguno (pruebas)
     * @return evento
     */
    private static EventoReserva evento() {
        Turno t = Dobles.turno(LocalDate.of(2026, 11, 3), LocalTime.of(18, 0), LocalTime.of(19, 0));
        Reserva r = new Reserva(Dobles.socio(1L, true), Dobles.cancha(1L, true), t, null, AHORA);
        return new ReservaConfirmadaEvento(r, null, AHORA);
    }

    /**
     * Notifica a todos los observadores en orden y una sola vez a cada uno.
     *
     * <p>Casos de uso: CU-27
     */
    @Test
    void notificaEnOrdenSinRepetidos() {
        List<String> recibidos = new ArrayList<>();
        ObservadorReserva auditor = e -> recibidos.add("auditor:" + e.accion());
        ObservadorReserva notificador = e -> recibidos.add("notificador:" + e.accion());
        PublicadorEventosReserva publicador = new PublicadorEventosReserva();
        publicador.suscribir(auditor);
        publicador.suscribir(notificador);
        publicador.suscribir(auditor);
        publicador.publicar(evento());
        assertEquals(List.of("auditor:CONFIRMACION", "notificador:CONFIRMACION"), recibidos);
    }

    /**
     * El observador recibe el mismo evento publicado.
     *
     * <p>Casos de uso: CU-27
     */
    @Test
    void entregaElEventoPublicado() {
        List<EventoReserva> recibidos = new ArrayList<>();
        PublicadorEventosReserva publicador = new PublicadorEventosReserva();
        publicador.suscribir(recibidos::add);
        EventoReserva e = evento();
        publicador.publicar(e);
        assertSame(e, recibidos.get(0));
    }

    /**
     * La excepción de un observador se propaga y los siguientes no reciben el evento.
     *
     * <p>Casos de uso: CU-27
     */
    @Test
    void propagaLaExcepcionDeUnObservador() {
        List<String> recibidos = new ArrayList<>();
        PublicadorEventosReserva publicador = new PublicadorEventosReserva();
        publicador.suscribir(e -> {
            throw new IllegalStateException("falla de auditoría");
        });
        publicador.suscribir(e -> recibidos.add("notificador"));
        assertThrows(IllegalStateException.class, () -> publicador.publicar(evento()));
        assertEquals(List.of(), recibidos);
    }

    /**
     * Cada evento informa su acción (la usan la auditoría y la notificación).
     *
     * <p>Casos de uso: CU-27, CU-30, CU-32, CU-33
     */
    @Test
    void accionesDeLosEventos() {
        EventoReserva base = evento();
        assertEquals("CONFIRMACION", base.accion());
        assertEquals("CANCELACION", new ReservaCanceladaEvento(base.getReserva(), null, AHORA).accion());
        ReservaModificadaEvento m = new ReservaModificadaEvento(base.getReserva(), null, AHORA,
                base.getReserva().getTurno(), base.getReserva().getCancha());
        assertEquals("MODIFICACION", m.accion());
        assertSame(base.getReserva().getTurno(), m.getTurnoAnterior());
    }
}

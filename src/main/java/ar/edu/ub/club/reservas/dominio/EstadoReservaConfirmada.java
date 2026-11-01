package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-32, CU-33; tareas programadas: finalización de reservas cumplidas
 */
public class EstadoReservaConfirmada extends EstadoReserva {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-32, CU-33; tareas programadas: finalización de reservas cumplidas
     * @return el resultado
     */
    public String codigo() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
     * @param r valor de {@code r}
     * @param motivo valor de {@code motivo}
     * @param por valor de {@code por}
     * @param ahora valor de {@code ahora}
     */
    public void cancelar(Reserva r, String motivo, Usuario por, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-32
     * @param r valor de {@code r}
     * @param nuevoTurno valor de {@code nuevoTurno}
     * @param nuevaCancha valor de {@code nuevaCancha}
     * @param ahora valor de {@code ahora}
     */
    public void modificar(Reserva r, Turno nuevoTurno, Cancha nuevaCancha, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     * @param r valor de {@code r}
     */
    public void finalizar(Reserva r) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

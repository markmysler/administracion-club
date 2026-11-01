package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.time.LocalDateTime;

/**
 * Clase abstracta del patrón State para {@code Reserva}, con subclases {@code EstadoReservaConfirmada}, {@code
 * EstadoReservaCancelada} y {@code EstadoReservaFinalizada} (instancias únicas). Las operaciones lanzan {@code
 * TransicionInvalidaException} por omisión; {@code EstadoReservaConfirmada} redefine las tres.
 *
 * <p>Casos de uso: todos
 */
public abstract class EstadoReserva {

    /**
     * Confirmada → Cancelada (si el turno no comenzó).
     *
     * <p>Casos de uso: todos
     * @param r valor de {@code r}
     * @param motivo valor de {@code motivo}
     * @param por valor de {@code por}
     * @param ahora valor de {@code ahora}
     */
    public void cancelar(Reserva r, String motivo, Usuario por, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * En Confirmada cambia turno y cancha y permanece Confirmada.
     *
     * <p>Casos de uso: todos
     * @param r valor de {@code r}
     * @param nuevoTurno valor de {@code nuevoTurno}
     * @param nuevaCancha valor de {@code nuevaCancha}
     * @param ahora valor de {@code ahora}
     */
    public void modificar(Reserva r, Turno nuevoTurno, Cancha nuevaCancha, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Confirmada → Finalizada.
     *
     * <p>Casos de uso: todos
     * @param r valor de {@code r}
     */
    public void finalizar(Reserva r) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Código persistido: CONFIRMADA, CANCELADA o FINALIZADA.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    public abstract String codigo();

    /**
     * Devuelve la instancia única del estado del código; un código desconocido lanza {@code IllegalStateException}.
     * La usa {@code BrokerReserva.crearObjeto} y {@code Reserva.confirmar()}.
     *
     * <p>Casos de uso: todos
     * @param codigo valor de {@code codigo}
     * @return el resultado
     */
    public static EstadoReserva desdeCodigo(String codigo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

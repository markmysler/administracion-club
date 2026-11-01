package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.time.LocalDateTime;

/**
 * Clase abstracta del patrón State (GoF) para {@code Reserva}, con subclases {@code EstadoReservaConfirmada},
 * {@code EstadoReservaCancelada} y {@code EstadoReservaFinalizada}. Los estados no tienen atributos, por eso existe
 * una única instancia compartida e inmutable de cada uno, que entrega {@code desdeCodigo}. Las tres transiciones
 * lanzan {@code TransicionInvalidaException} por omisión; {@code EstadoReservaConfirmada} es el único estado que
 * las redefine. {@code BrokerReserva} usa {@code desdeCodigo} al leer la fila y {@code codigo()} al escribirla.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33; tareas programadas:
 * finalización de reservas cumplidas
 */
public abstract class EstadoReserva {

    /** Mensaje de las transiciones que el estado actual no admite. */
    static final String MENSAJE_NO_CONFIRMADA = "La reserva ya no está confirmada.";

    /** Instancia única del estado Confirmada. */
    private static final EstadoReserva CONFIRMADA = new EstadoReservaConfirmada();

    /** Instancia única del estado Cancelada. */
    private static final EstadoReserva CANCELADA = new EstadoReservaCancelada();

    /** Instancia única del estado Finalizada. */
    private static final EstadoReserva FINALIZADA = new EstadoReservaFinalizada();

    /**
     * Constructor protegido: solo lo usan los estados concretos (una instancia por estado).
     *
     * <p>Casos de uso: todos
     */
    protected EstadoReserva() {
        // los estados no tienen atributos
    }

    /**
     * Transición Confirmada → Cancelada. Por omisión la rechaza: solo una reserva confirmada puede cancelarse
     * (RN-14, RN-16).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
     * @param r reserva contexto del estado
     * @param motivo motivo de la cancelación
     * @param por cuenta que cancela
     * @param ahora momento de la operación
     * @throws TransicionInvalidaException siempre, salvo que el estado concreto la redefina
     */
    public void cancelar(Reserva r, String motivo, Usuario por, LocalDateTime ahora) {
        throw new TransicionInvalidaException(MENSAJE_NO_CONFIRMADA);
    }

    /**
     * Cambio de turno y cancha (la reserva permanece Confirmada). Por omisión lo rechaza (RN-15).
     *
     * <p>Casos de uso: CU-32
     * @param r reserva contexto del estado
     * @param nuevoTurno turno nuevo ya validado
     * @param nuevaCancha cancha nueva ya validada
     * @param ahora momento de la operación
     * @throws TransicionInvalidaException siempre, salvo que el estado concreto la redefina
     */
    public void modificar(Reserva r, Turno nuevoTurno, Cancha nuevaCancha, LocalDateTime ahora) {
        throw new TransicionInvalidaException(MENSAJE_NO_CONFIRMADA);
    }

    /**
     * Transición Confirmada → Finalizada. Por omisión la rechaza.
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     * @param r reserva contexto del estado
     * @throws TransicionInvalidaException siempre, salvo que el estado concreto la redefina
     */
    public void finalizar(Reserva r) {
        throw new TransicionInvalidaException(MENSAJE_NO_CONFIRMADA);
    }

    /**
     * Código persistido del estado (columna {@code reserva.estado}).
     *
     * <p>Casos de uso: todos
     * @return CONFIRMADA, CANCELADA o FINALIZADA
     */
    public abstract String codigo();

    /**
     * Devuelve la instancia única del estado del código. La usan {@code BrokerReserva.crearObjeto} (al leer la fila),
     * {@code Reserva.confirmar()} y los estados concretos (al cambiar de estado). Un código desconocido indica un
     * dato corrupto (el CHECK de la tabla lo impide).
     *
     * <p>Casos de uso: todos
     * @param codigo CONFIRMADA, CANCELADA o FINALIZADA
     * @return instancia del estado
     * @throws IllegalStateException si el código es desconocido
     */
    public static EstadoReserva desdeCodigo(String codigo) {
        if (codigo == null) {
            throw new IllegalStateException("Estado de reserva nulo");
        }
        switch (codigo) {
            case "CONFIRMADA":
                return CONFIRMADA;
            case "CANCELADA":
                return CANCELADA;
            case "FINALIZADA":
                return FINALIZADA;
            default:
                throw new IllegalStateException("Estado de reserva desconocido: " + codigo);
        }
    }

    /**
     * Texto del estado (su código), útil en la bitácora.
     *
     * <p>Casos de uso: todos
     * @return el código del estado
     */
    @Override
    public String toString() {
        return codigo();
    }
}

package ar.edu.ub.club.reservas.dominio;

/**
 * Estado concreto del patrón State: reserva cancelada (estado final). Redefine solo {@code codigo()}; las
 * transiciones heredan el rechazo por omisión de {@code EstadoReserva} ({@code TransicionInvalidaException}). El
 * turno queda libre porque la restricción de exclusión de la base solo considera las reservas CONFIRMADAS (RN-15).
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33; tareas programadas: finalización de reservas cumplidas
 */
public class EstadoReservaCancelada extends EstadoReserva {

    /**
     * Crea la instancia única (la guarda {@code EstadoReserva}).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
     */
    EstadoReservaCancelada() {
        super();
    }

    /**
     * Código persistido.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-29, CU-30, CU-31, CU-33
     * @return «CANCELADA»
     */
    @Override
    public String codigo() {
        return "CANCELADA";
    }
}

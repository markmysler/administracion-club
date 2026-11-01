package ar.edu.ub.club.reservas.dominio;

/**
 * Estado concreto del patrón State: reserva cuyo turno ya se cumplió (estado final). Redefine solo
 * {@code codigo()}; las transiciones heredan el rechazo por omisión de {@code EstadoReserva}.
 *
 * <p>Casos de uso: CU-29, CU-31; tareas programadas: finalización de reservas cumplidas
 */
public class EstadoReservaFinalizada extends EstadoReserva {

    /**
     * Crea la instancia única (la guarda {@code EstadoReserva}).
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     */
    EstadoReservaFinalizada() {
        super();
    }

    /**
     * Código persistido.
     *
     * <p>Casos de uso: CU-29, CU-31; tareas programadas: finalización de reservas cumplidas
     * @return «FINALIZADA»
     */
    @Override
    public String codigo() {
        return "FINALIZADA";
    }
}

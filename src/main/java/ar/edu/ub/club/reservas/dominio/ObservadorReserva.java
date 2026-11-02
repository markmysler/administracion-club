package ar.edu.ub.club.reservas.dominio;

/**
 * Interfaz de los observadores del patrón Observer de los eventos de reserva (sujeto {@code
 * PublicadorEventosReserva}). La implementan {@code AuditorReservas} (registra la auditoría dentro de la
 * transacción, RNF-12) y {@code NotificadorReservas} (envía el correo después del commit, RN-19), que dependen de
 * {@code reservas} y no a la inversa. Todo bean de Spring que la implemente queda suscripto al iniciar la aplicación
 * ({@code ConfiguracionReservas}).
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public interface ObservadorReserva {

    /**
     * Reacciona al evento ({@code ReservaConfirmadaEvento}, {@code ReservaModificadaEvento}, {@code
     * ReservaCanceladaEvento}).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param evento Evento publicado.
     */
    void alOcurrir(EventoReserva evento);
}

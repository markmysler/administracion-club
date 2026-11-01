package ar.edu.ub.club.reservas.dominio;

/**
 * Interfaz (reservas.aplicacion) de los observadores del patrón Observer de los eventos de reserva. La
 * implementan {@code AuditorReservas} (registra la auditoría dentro de la transacción, RNF-12) y {@code
 * NotificadorReservas} (envía el correo después del commit, RN-19).
 *
 * <p>Casos de uso: PENDIENTE
 */
public interface ObservadorReserva {

    /**
     * Reacciona al evento ({@code ReservaConfirmadaEvento}, {@code ReservaModificadaEvento}, {@code
     * ReservaCanceladaEvento}).
     *
     * <p>Casos de uso: PENDIENTE
     * @param evento Evento publicado.
     */
    void alOcurrir(EventoReserva evento);
}

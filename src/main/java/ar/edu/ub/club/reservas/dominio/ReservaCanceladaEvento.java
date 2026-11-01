package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Evento del patrón Observer que se publica al cancelar una reserva (cancelación del socio, del administrador o por
 * una baja, RN-16). El motivo y quién canceló se leen de la reserva. Sin atributos propios.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
 */
public class ReservaCanceladaEvento extends EventoReserva {

    /**
     * Acción del evento.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
     * @return «CANCELACION»
     */
    @Override
    public String accion() {
        return "CANCELACION";
    }

    /**
     * Crea el evento.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
     * @param reserva reserva cancelada
     * @param usuario cuenta que canceló
     * @param fechaHora momento de la cancelación
     */
    public ReservaCanceladaEvento(Reserva reserva, Usuario usuario, LocalDateTime fechaHora) {
        super(reserva, usuario, fechaHora);
    }
}

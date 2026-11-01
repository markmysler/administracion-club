package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Evento del patrón Observer que se publica al registrar una reserva (alta online o manual). Sin atributos
 * propios.
 *
 * <p>Casos de uso: CU-27, CU-28
 */
public class ReservaConfirmadaEvento extends EventoReserva {

    /**
     * Acción del evento.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @return «CONFIRMACION»
     */
    @Override
    public String accion() {
        return "CONFIRMACION";
    }

    /**
     * Crea el evento.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param reserva reserva confirmada y guardada
     * @param usuario cuenta que la registró (socio o administrador)
     * @param fechaHora momento del alta
     */
    public ReservaConfirmadaEvento(Reserva reserva, Usuario usuario, LocalDateTime fechaHora) {
        super(reserva, usuario, fechaHora);
    }
}

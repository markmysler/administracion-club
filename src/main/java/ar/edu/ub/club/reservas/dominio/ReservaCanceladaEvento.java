package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Evento del dominio publicado con el mecanismo de eventos de Spring (Observer).
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public class ReservaCanceladaEvento extends EventoReserva {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el resultado
     */
    public String accion() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Crea una instancia de {@code ReservaCanceladaEvento}.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param reserva valor de {@code reserva}
     * @param usuario valor de {@code usuario}
     * @param fechaHora valor de {@code fechaHora}
     */
    public ReservaCanceladaEvento(Reserva reserva, Usuario usuario, LocalDateTime fechaHora) {
        super(reserva, usuario, fechaHora);
    }
}

package ar.edu.ub.club.agenda.dominio;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz del paquete {@code agenda.dominio} con la que {@code ServicioAgenda} obtiene los turnos ocupados. La
 * implementa {@code ConsultaTurnosOcupadosReservas} de {@code reservas.persistencia} con el objeto consulta
 * {@code TurnosOcupadosDeCancha} (inversión de dependencias): agenda no conoce al módulo reservas.
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
 */
public interface ConsultaTurnosOcupados {

    /**
     * Turnos de reservas CONFIRMADAS de la cancha en la fecha.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param idCancha Cancha.
     * @param fecha Fecha.
     * @return Lista de {@code Turno}.
     */
    List<Turno> turnosOcupados(Long idCancha, LocalDate fecha);
}

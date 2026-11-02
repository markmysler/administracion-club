package ar.edu.ub.club.compartido.dominio;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Objeto de datos ({@code compartido.dominio}) con lo necesario para mostrar una reserva afectada sin exponer la
 * entidad {@code Reserva} a otros módulos. La fecha y el horario se guardan como valores simples y no como
 * {@code Turno}, porque {@code compartido} no puede depender de {@code agenda.dominio}. Lo devuelven las
 * consultas de proyección {@code Resumenes*} a través de {@code ConsultaReservasFuturas}.
 *
 * <p>Casos de uso: CU-03, CU-04, CU-05, CU-20, CU-22, CU-23, CU-24
 *
 * @param idReserva reserva
 * @param nombreSocio apellido y nombre del socio
 * @param nombreCancha nombre de la cancha
 * @param fecha fecha del turno
 * @param horaInicio inicio del turno
 * @param horaFin fin del turno
 */
public record ResumenReserva(Long idReserva, String nombreSocio, String nombreCancha, LocalDate fecha,
                             LocalTime horaInicio, LocalTime horaFin) {
}

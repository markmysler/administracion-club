package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.reservas.dominio.Reserva;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-29, CU-31
 */
public record FilaReserva(Long id, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String cancha, String deporte, String socio, String dniSocio, String estado, Integer version) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param reserva valor de {@code reserva}
     * @return el resultado
     */
    public static FilaReserva desde(Reserva reserva) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import ar.edu.ub.club.reservas.dominio.Reserva;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-29, CU-31, CU-32
 */
public record DetalleReserva(Long id, Long idSocio, String socio, String dniSocio, Long idCancha, Long idDeporte, String cancha, String deporte, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String estado, LocalDateTime fechaCreacion, String registradaPor, String canceladaPor, String motivoCancelacion, Integer version, Integer plazoCancelacionHoras, Integer plazoModificacionHoras, List<FilaNotificacion> notificaciones) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-32
     * @param reserva valor de {@code reserva}
     * @param notificaciones valor de {@code notificaciones}
     * @param regla valor de {@code regla}
     * @return el resultado
     */
    public static DetalleReserva desde(Reserva reserva, List<Notificacion> notificaciones, ReglaReserva regla) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

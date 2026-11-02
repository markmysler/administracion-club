package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import ar.edu.ub.club.reservas.dominio.Reserva;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Detalle de una reserva (P-21, P-22, P-23): socio, cancha, turno, estado, quién la registró o canceló y el motivo,
 * versión (control optimista de P-23), plazos de cancelación y modificación vigentes y sus notificaciones.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-29, CU-30, CU-31, CU-32, CU-33
 * @param id reserva
 * @param idSocio socio titular
 * @param socio «Apellido, Nombre» del titular
 * @param dniSocio DNI del titular
 * @param idCancha cancha
 * @param idDeporte deporte de la cancha
 * @param cancha nombre de la cancha
 * @param deporte nombre del deporte
 * @param fecha fecha del turno
 * @param horaInicio hora de inicio
 * @param horaFin hora de fin
 * @param estado CONFIRMADA, CANCELADA o FINALIZADA
 * @param fechaCreacion momento del registro
 * @param registradaPor nombre de la cuenta que la registró
 * @param canceladaPor nombre de la cuenta que la canceló o {@code null}
 * @param motivoCancelacion motivo o {@code null}
 * @param version versión del control optimista
 * @param plazoCancelacionHoras anticipación mínima de cancelación del socio
 * @param plazoModificacionHoras anticipación mínima de modificación
 * @param notificaciones notificaciones de la reserva
 */
public record DetalleReserva(Long id, Long idSocio, String socio, String dniSocio, Long idCancha, Long idDeporte, String cancha, String deporte, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String estado, LocalDateTime fechaCreacion, String registradaPor, String canceladaPor, String motivoCancelacion, Integer version, Integer plazoCancelacionHoras, Integer plazoModificacionHoras, List<FilaNotificacion> notificaciones) {

    /**
     * Construye el detalle desde la reserva, sus notificaciones ({@code FilaNotificacion.desde}) y la regla de
     * reserva (plazos).
     *
     * <p>Casos de uso: CU-29, CU-31, CU-32
     * @param reserva reserva
     * @param notificaciones notificaciones de la reserva (puede ser vacía)
     * @param regla reglas de reserva vigentes ({@code null}: sin plazos)
     * @return el detalle
     */
    public static DetalleReserva desde(Reserva reserva, List<Notificacion> notificaciones, ReglaReserva regla) {
        Long idSocio = reserva.getSocio() == null ? null : reserva.getSocio().getId();
        String dni = reserva.getSocio() == null ? "" : reserva.getSocio().getDni();
        Long idCancha = reserva.getCancha() == null ? null : reserva.getCancha().getId();
        String cancha = reserva.getCancha() == null ? "" : reserva.getCancha().getNombre();
        Long idDeporte = null;
        String deporte = "";
        if (reserva.getCancha() != null && reserva.getCancha().getDeporte() != null) {
            idDeporte = reserva.getCancha().getDeporte().getId();
            deporte = reserva.getCancha().getDeporte().getNombre();
        }
        List<FilaNotificacion> filas = notificaciones == null ? List.of()
                : notificaciones.stream().map(FilaNotificacion::desde).toList();
        return new DetalleReserva(reserva.getId(), idSocio, FilaReserva.nombreSocio(reserva.getSocio()), dni,
                idCancha, idDeporte, cancha, deporte, reserva.getTurno().getFecha(),
                reserva.getTurno().getHoraInicio(), reserva.getTurno().getHoraFin(), reserva.getEstado().codigo(),
                reserva.getFechaCreacion(), nombre(reserva.getRegistradaPor()), nombre(reserva.getCanceladaPor()),
                reserva.getMotivoCancelacion(), reserva.getVersion(),
                regla == null ? null : regla.getAnticipacionMinimaCancelacionHoras(),
                regla == null ? null : regla.getAnticipacionMinimaModificacionHoras(), filas);
    }

    /**
     * Nombre visible de una cuenta ({@code Usuario.nombreCompleto()}).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param usuario cuenta o {@code null}
     * @return nombre o {@code null}
     */
    private static String nombre(Usuario usuario) {
        return usuario == null ? null : usuario.nombreCompleto();
    }
}

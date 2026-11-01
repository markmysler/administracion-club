package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Fila de las tablas de reservas de P-21 y P-22: fecha, horario, cancha, deporte, socio, DNI, estado y versión.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-29, CU-30, CU-31, CU-32, CU-33
 * @param id reserva
 * @param fecha fecha del turno
 * @param horaInicio hora de inicio
 * @param horaFin hora de fin
 * @param cancha nombre de la cancha
 * @param deporte nombre del deporte
 * @param socio «Apellido, Nombre» del titular
 * @param dniSocio DNI del titular
 * @param estado CONFIRMADA, CANCELADA o FINALIZADA
 * @param version versión del control optimista
 */
public record FilaReserva(Long id, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String cancha, String deporte, String socio, String dniSocio, String estado, Integer version) {

    /**
     * Construye la fila desde la reserva.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param reserva reserva
     * @return la fila
     */
    public static FilaReserva desde(Reserva reserva) {
        Socio s = reserva.getSocio();
        String cancha = reserva.getCancha() == null ? "" : reserva.getCancha().getNombre();
        String deporte = reserva.getCancha() == null || reserva.getCancha().getDeporte() == null
                ? "" : reserva.getCancha().getDeporte().getNombre();
        return new FilaReserva(reserva.getId(), reserva.getTurno().getFecha(), reserva.getTurno().getHoraInicio(),
                reserva.getTurno().getHoraFin(), cancha, deporte, nombreSocio(s), s == null ? "" : s.getDni(),
                reserva.getEstado().codigo(), reserva.getVersion());
    }

    /**
     * Nombre visible del socio («Apellido, Nombre»).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param s socio
     * @return nombre o cadena vacía
     */
    static String nombreSocio(Socio s) {
        return s == null ? "" : s.getApellido() + ", " + s.getNombre();
    }
}

package ar.edu.ub.club.compartido.ui;

import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Modelo de vista común (record inmutable) de una reserva afectada: advertencias de bajas y de cambios de agenda,
 * y próximas reservas del perfil y de la ficha del socio.
 *
 * <p>Casos de uso: CU-03, CU-04, CU-05, CU-20, CU-22, CU-23, CU-24
 *
 * @param idReserva reserva
 * @param socio apellido y nombre del socio
 * @param cancha nombre de la cancha
 * @param fecha fecha del turno
 * @param horaInicio inicio del turno
 * @param horaFin fin del turno
 */
public record FilaResumenReserva(Long idReserva, String socio, String cancha, LocalDate fecha,
                                 LocalTime horaInicio, LocalTime horaFin) {

    /**
     * Fábrica desde el resumen que devuelven las fachadas.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-20, CU-22, CU-23, CU-24
     * @param resumen resumen de la reserva
     * @return fila
     */
    public static FilaResumenReserva desde(ResumenReserva resumen) {
        return new FilaResumenReserva(resumen.idReserva(), resumen.nombreSocio(), resumen.nombreCancha(),
                resumen.fecha(), resumen.horaInicio(), resumen.horaFin());
    }

    /**
     * Texto de una línea para las advertencias de {@code DialogoConfirmacion}.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-22, CU-23, CU-24
     * @return por ejemplo «12/11/2026 18:00–19:00 · Cancha 1 · Pérez, Ana»
     */
    public String comoTexto() {
        return FormatoUI.fecha(fecha) + " " + FormatoUI.hora(horaInicio) + "–" + FormatoUI.hora(horaFin)
                + " · " + cancha + " · " + socio;
    }
}

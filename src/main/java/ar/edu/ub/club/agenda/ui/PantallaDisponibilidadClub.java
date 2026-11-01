package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosDisponibilidad;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-17: Configuración de días y horario de atención y lista de feriados.
 * Patrón MVP (Vista Pasiva): {@code PresentadorDisponibilidadClub} solo conoce esta interfaz.
 *
 * <p>Casos de uso: CU-24
 */
public interface PantallaDisponibilidadClub extends PantallaBase {

    /**
     * Muestra los días y el horario vigentes y la grilla de feriados futuros (CU-24 pasos 2 y 9).
     *
     * <p>Casos de uso: CU-24
     * @param datos días y horario
     * @param feriados feriados futuros ordenados por fecha
     */
    void mostrarDisponibilidad(DatosDisponibilidad datos, List<FilaFeriado> feriados);

    /**
     * Acota el selector de fecha del feriado a la anticipación máxima para reservar (RN-13).
     *
     * <p>Casos de uso: CU-24
     * @param desde primera fecha elegible
     * @param hasta última fecha elegible
     */
    void mostrarRangoCalendario(LocalDate desde, LocalDate hasta);

    /**
     * Advierte las reservas afectadas por el cambio de horario (FA5, 6.1) o por el feriado (FA1, 3.3), que se
     * conservan, y pide confirmación; al confirmar la vista reenvía la operación con {@code confirmar = true}.
     *
     * <p>Casos de uso: CU-24
     * @param afectadas reservas afectadas
     */
    void pedirConfirmacion(List<FilaResumenReserva> afectadas);
}

package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosDisponibilidad;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-17: Configuración de días y horario de atención y lista de feriados.
 *
 * <p>Casos de uso: todos
 */
public interface PantallaDisponibilidadClub extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-24
     * @param datos valor de {@code datos}
     * @param feriados valor de {@code feriados}
     */
    void mostrarDisponibilidad(DatosDisponibilidad datos, List<FilaFeriado> feriados);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-24
     * @param desde valor de {@code desde}
     * @param hasta valor de {@code hasta}
     */
    void mostrarRangoCalendario(LocalDate desde, LocalDate hasta);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-24
     * @param afectadas valor de {@code afectadas}
     */
    void pedirConfirmacion(List<FilaResumenReserva> afectadas);
}

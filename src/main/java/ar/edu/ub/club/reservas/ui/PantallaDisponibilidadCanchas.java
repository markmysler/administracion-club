package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.socios.ui.FilaSocio;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-19: Grilla de turnos por cancha y horario para una fecha, con filtros por
 * deporte y cancha.
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28
 */
public interface PantallaDisponibilidadCanchas extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param deportes valor de {@code deportes}
     * @param canchas valor de {@code canchas}
     */
    void mostrarFiltros(List<OpcionSeleccion> deportes, List<OpcionSeleccion> canchas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param opciones valor de {@code opciones}
     */
    void mostrarCanchas(List<OpcionSeleccion> opciones);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param desde valor de {@code desde}
     * @param hasta valor de {@code hasta}
     */
    void mostrarRangoCalendario(LocalDate desde, LocalDate hasta);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param visible valor de {@code visible}
     */
    void mostrarEleccionSocio(boolean visible);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param filas valor de {@code filas}
     */
    void mostrarSocios(List<FilaSocio> filas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param fila valor de {@code fila}
     * @param cuotaGrupo valor de {@code cuotaGrupo}
     */
    void mostrarSocioElegido(FilaSocio fila, FilaCuota cuotaGrupo);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param filas valor de {@code filas}
     */
    void mostrarTurnos(List<FilaTurno> filas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param turno valor de {@code turno}
     * @param idSocio valor de {@code idSocio}
     */
    void abrirConfirmacion(FilaTurno turno, Long idSocio);
}

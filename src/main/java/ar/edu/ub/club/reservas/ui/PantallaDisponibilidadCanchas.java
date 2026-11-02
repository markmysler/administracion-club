package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.socios.ui.FilaSocio;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-19 (MVP, Vista Pasiva): grilla de turnos por cancha y horario para una fecha,
 * con filtros por deporte y cancha y, para el administrador, la elección del socio de una reserva manual.
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28
 */
public interface PantallaDisponibilidadCanchas extends PantallaBase {

    /**
     * Muestra los filtros de deporte y cancha.
     *
     * <p>Casos de uso: CU-26
     * @param deportes deportes activos
     * @param canchas canchas disponibles
     */
    void mostrarFiltros(List<OpcionSeleccion> deportes, List<OpcionSeleccion> canchas);

    /**
     * Reemplaza las opciones del filtro de cancha (al cambiar el deporte).
     *
     * <p>Casos de uso: CU-26
     * @param opciones canchas del deporte
     */
    void mostrarCanchas(List<OpcionSeleccion> opciones);

    /**
     * Acota el calendario a las fechas consultables (anticipación mínima y máxima, RN-13).
     *
     * <p>Casos de uso: CU-26
     * @param desde primera fecha
     * @param hasta última fecha
     */
    void mostrarRangoCalendario(LocalDate desde, LocalDate hasta);

    /**
     * Muestra u oculta la elección del socio (solo con GESTIONAR_RESERVAS_CLUB).
     *
     * <p>Casos de uso: CU-26, CU-28
     * @param visible true para el administrador
     */
    void mostrarEleccionSocio(boolean visible);

    /**
     * Muestra los socios que coinciden con la búsqueda.
     *
     * <p>Casos de uso: CU-28
     * @param filas socios encontrados
     */
    void mostrarSocios(List<FilaSocio> filas);

    /**
     * Muestra el socio elegido y el estado de la cuota de su grupo.
     *
     * <p>Casos de uso: CU-28
     * @param fila socio elegido
     * @param cuotaGrupo cuota más reciente del grupo o {@code null}
     */
    void mostrarSocioElegido(FilaSocio fila, FilaCuota cuotaGrupo);

    /**
     * Muestra la grilla de turnos.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param filas turnos libres, ocupados y no disponibles
     */
    void mostrarTurnos(List<FilaTurno> filas);

    /**
     * Abre el diálogo de confirmación P-20.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param turno turno elegido
     * @param idSocio socio elegido por el administrador o {@code null}
     */
    void abrirConfirmacion(FilaTurno turno, Long idSocio);
}

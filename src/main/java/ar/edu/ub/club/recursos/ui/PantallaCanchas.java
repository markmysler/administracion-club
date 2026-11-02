package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.recursos.aplicacion.DatosCancha;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-16: Listado de canchas por deporte con alta, edición y desactivación con
 * advertencia. Patrón MVP (Vista Pasiva): el presentador {@code PresentadorCanchas} solo conoce esta interfaz.
 *
 * <p>Casos de uso: CU-21, CU-22, CU-23
 */
public interface PantallaCanchas extends PantallaBase {

    /**
     * Muestra los deportes activos para el filtro y para el combo del formulario (CU-21 paso 2).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param opciones deportes activos
     */
    void mostrarDeportes(List<OpcionSeleccion> opciones);

    /**
     * Muestra la grilla de canchas (CU-21 paso 9, CU-22 paso 10, CU-23 paso 10) y cierra el panel de edición.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param filas filas de la grilla
     */
    void mostrarCanchas(List<FilaCancha> filas);

    /**
     * Abre el panel de edición con los datos precargados (vacío para un alta).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param datos datos del formulario
     * @param idCancha cancha que se modifica (null en el alta)
     */
    void editar(DatosCancha datos, Long idCancha);

    /**
     * Pide confirmación de la baja mostrando las reservas futuras que se cancelarán (CU-23 paso 4; FA2 sin lista).
     *
     * <p>Casos de uso: CU-23
     * @param idCancha cancha a desactivar
     * @param afectadas reservas que se cancelarán
     */
    void pedirConfirmacionBaja(Long idCancha, List<FilaResumenReserva> afectadas);

    /**
     * Advierte las reservas futuras de la cancha, que se conservan, y pide confirmar el cambio de deporte
     * (CU-22 FA6, 7.1); al confirmar, la vista reenvía el guardado con {@code confirmar = true}.
     *
     * <p>Casos de uso: CU-22
     * @param afectadas reservas futuras de la cancha
     */
    void pedirConfirmacionCambio(List<FilaResumenReserva> afectadas);
}

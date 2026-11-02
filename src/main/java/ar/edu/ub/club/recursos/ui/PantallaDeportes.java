package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.recursos.aplicacion.DatosDeporte;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-15: Listado de deportes con alta, edición y desactivación con advertencia.
 * Patrón MVP (Vista Pasiva): el presentador {@code PresentadorDeportes} solo conoce esta interfaz.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20
 */
public interface PantallaDeportes extends PantallaBase {

    /**
     * Muestra la grilla de deportes (CU-18 paso 8, CU-19 paso 8, CU-20 paso 10) y cierra el panel de edición.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param filas filas de la grilla
     */
    void mostrarDeportes(List<FilaDeporte> filas);

    /**
     * Abre el panel de edición con los datos precargados (vacío para un alta, CU-18 paso 2; CU-19 paso 2).
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param datos datos del formulario
     * @param idDeporte deporte que se modifica (null en el alta)
     */
    void editar(DatosDeporte datos, Long idDeporte);

    /**
     * Pide confirmación de la baja mostrando las reservas futuras que se cancelarán (CU-20 paso 4; FA2 sin lista).
     *
     * <p>Casos de uso: CU-20
     * @param idDeporte deporte a desactivar
     * @param afectadas reservas que se cancelarán
     */
    void pedirConfirmacionBaja(Long idDeporte, List<FilaResumenReserva> afectadas);
}

package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva (MVP Vista Pasiva) de la pantalla P-04 Panel de administración: el presentador {@code
 * PresentadorPanelAdministracion} solo le entrega el modelo de vista ya armado.
 *
 * <p>Casos de uso: CU-40
 */
public interface PantallaPanelAdministracion extends PantallaBase {

    /**
     * Muestra los indicadores; los no disponibles con «No disponible» y los vacíos con «0» o «Sin datos» (paso 8,
     * FA1 y FA2).
     *
     * <p>Casos de uso: CU-40
     * @param resumen modelo de vista de los indicadores
     */
    void mostrarIndicadores(ResumenIndicadores resumen);
}

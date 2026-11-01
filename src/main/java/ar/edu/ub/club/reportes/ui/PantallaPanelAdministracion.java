package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-04: Panel de inicio del administrador con los indicadores del club y accesos
 * directos a reportes.
 *
 * <p>Casos de uso: CU-40
 */
public interface PantallaPanelAdministracion extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-40
     * @param resumen valor de {@code resumen}
     */
    void mostrarIndicadores(ResumenIndicadores resumen);
}

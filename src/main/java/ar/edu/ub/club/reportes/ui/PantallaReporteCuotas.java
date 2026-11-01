package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-33: Reporte de cuotas cobradas, pendientes y grupos deudores, con
 * exportación.
 *
 * <p>Casos de uso: CU-39
 */
public interface PantallaReporteCuotas extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-39
     * @param tabla valor de {@code tabla}
     */
    void mostrarResultado(TablaReporte tabla);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-39
     * @param nombre valor de {@code nombre}
     * @param tipoContenido valor de {@code tipoContenido}
     * @param contenido valor de {@code contenido}
     */
    void descargar(String nombre, String tipoContenido, byte[] contenido);
}

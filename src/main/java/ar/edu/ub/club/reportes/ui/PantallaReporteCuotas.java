package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva (MVP Vista Pasiva) de la pantalla P-33 Reporte de cuotas y deudores.
 *
 * <p>Casos de uso: CU-39
 */
public interface PantallaReporteCuotas extends PantallaBase {

    /**
     * Muestra el resumen por estado (totales) y la grilla de grupos deudores (paso 8).
     *
     * <p>Casos de uso: CU-39
     * @param tabla modelo de vista del reporte
     */
    void mostrarResultado(TablaReporte tabla);

    /**
     * Entrega el archivo exportado al navegador con un {@code StreamResource} y un {@code Anchor} (paso 11).
     *
     * <p>Casos de uso: CU-39
     * @param nombre nombre del archivo
     * @param tipoContenido tipo MIME
     * @param contenido bytes del archivo
     */
    void descargar(String nombre, String tipoContenido, byte[] contenido);
}

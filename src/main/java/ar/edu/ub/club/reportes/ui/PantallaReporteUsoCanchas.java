package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva (MVP Vista Pasiva) de la pantalla P-32 Reporte de uso de canchas.
 *
 * <p>Casos de uso: CU-38
 */
public interface PantallaReporteUsoCanchas extends PantallaBase {

    /**
     * Carga las opciones de los combos de deporte y cancha (paso 2).
     *
     * <p>Casos de uso: CU-38
     * @param deportes opciones de deporte (incluye inactivos)
     * @param canchas opciones de cancha
     */
    void mostrarFiltros(List<OpcionSeleccion> deportes, List<OpcionSeleccion> canchas);

    /**
     * Muestra la grilla del reporte con su fila de totales (paso 7).
     *
     * <p>Casos de uso: CU-38
     * @param tabla modelo de vista del reporte
     */
    void mostrarResultado(TablaReporte tabla);

    /**
     * Entrega el archivo exportado al navegador con un {@code StreamResource} y un {@code Anchor} (paso 10).
     *
     * <p>Casos de uso: CU-38
     * @param nombre nombre del archivo
     * @param tipoContenido tipo MIME
     * @param contenido bytes del archivo
     */
    void descargar(String nombre, String tipoContenido, byte[] contenido);
}

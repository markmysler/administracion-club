package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-32: Reporte de uso de canchas por período, deporte y cancha, con exportación.
 *
 * <p>Casos de uso: CU-38
 */
public interface PantallaReporteUsoCanchas extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param deportes valor de {@code deportes}
     * @param canchas valor de {@code canchas}
     */
    void mostrarFiltros(List<OpcionSeleccion> deportes, List<OpcionSeleccion> canchas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param tabla valor de {@code tabla}
     */
    void mostrarResultado(TablaReporte tabla);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param nombre valor de {@code nombre}
     * @param tipoContenido valor de {@code tipoContenido}
     * @param contenido valor de {@code contenido}
     */
    void descargar(String nombre, String tipoContenido, byte[] contenido);
}

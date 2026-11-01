package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-32: Reporte de uso de canchas por período, deporte y cancha, con exportación.
 *
 * <p>Ruta: {@code reportes/uso-canchas}. Permiso: {@code GENERAR_REPORTES}.
 *
 * <p>Casos de uso: CU-38
 */
public class VistaReporteUsoCanchas extends VistaProtegida implements PantallaReporteUsoCanchas {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param deportes valor de {@code deportes}
     * @param canchas valor de {@code canchas}
     */
    @Override
    public void mostrarFiltros(List<OpcionSeleccion> deportes, List<OpcionSeleccion> canchas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param tabla valor de {@code tabla}
     */
    @Override
    public void mostrarResultado(TablaReporte tabla) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param nombre valor de {@code nombre}
     * @param tipoContenido valor de {@code tipoContenido}
     * @param contenido valor de {@code contenido}
     */
    @Override
    public void descargar(String nombre, String tipoContenido, byte[] contenido) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

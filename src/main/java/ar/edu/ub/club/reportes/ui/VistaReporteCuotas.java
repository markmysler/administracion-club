package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-33: Reporte de cuotas cobradas, pendientes y grupos deudores, con exportación.
 *
 * <p>Ruta: {@code reportes/cuotas}. Permiso: {@code GENERAR_REPORTES}.
 *
 * <p>Casos de uso: CU-39
 */
public class VistaReporteCuotas extends VistaProtegida implements PantallaReporteCuotas {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-39
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-39
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-39
     * @param tabla valor de {@code tabla}
     */
    @Override
    public void mostrarResultado(TablaReporte tabla) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-39
     * @param nombre valor de {@code nombre}
     * @param tipoContenido valor de {@code tipoContenido}
     * @param contenido valor de {@code contenido}
     */
    @Override
    public void descargar(String nombre, String tipoContenido, byte[] contenido) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-04: Panel de inicio del administrador con los indicadores del club y accesos directos a
 * reportes.
 *
 * <p>Ruta: {@code panel}. Permiso: {@code CONSULTAR_DASHBOARD}.
 *
 * <p>Casos de uso: CU-40
 */
public class VistaPanelAdministracion extends VistaProtegida implements PantallaPanelAdministracion {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-40
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-40
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-40
     * @param resumen valor de {@code resumen}
     */
    @Override
    public void mostrarIndicadores(ResumenIndicadores resumen) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

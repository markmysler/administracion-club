package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-06: Listado y búsqueda de socios con acceso a la ficha y al alta.
 *
 * <p>Ruta: {@code socios}. Permiso: {@code GESTIONAR_SOCIOS}.
 *
 * <p>Casos de uso: CU-01, CU-04
 */
public class VistaListadoSocios extends VistaProtegida implements PantallaListadoSocios {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-01, CU-04
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-01, CU-04
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-01, CU-04
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarSocios(List<FilaSocio> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

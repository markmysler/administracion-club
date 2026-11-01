package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.socios.aplicacion.DatosCategoria;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-10: Listado de categorías con alta y edición en línea.
 *
 * <p>Ruta: {@code categorias}. Permiso: {@code GESTIONAR_CATEGORIAS}.
 *
 * <p>Casos de uso: CU-06
 */
public class VistaCategorias extends VistaProtegida implements PantallaCategorias {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-06
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-06
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-06
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarCategorias(List<FilaCategoria> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-06
     * @param datos valor de {@code datos}
     * @param idCategoria valor de {@code idCategoria}
     */
    @Override
    public void editar(DatosCategoria datos, Long idCategoria) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

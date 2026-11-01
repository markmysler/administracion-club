package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.socios.aplicacion.DatosSocio;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-07: Formulario de alta y modificación de socio.
 *
 * <p>Ruta: {@code socios/formulario/:id?}. Permiso: {@code GESTIONAR_SOCIOS}.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-16
 */
public class VistaFormularioSocio extends VistaProtegida implements PantallaFormularioSocio {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @param opciones valor de {@code opciones}
     */
    @Override
    public void mostrarCategorias(List<OpcionSeleccion> opciones) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @param datos valor de {@code datos}
     * @param esAlta valor de {@code esAlta}
     */
    @Override
    public void mostrarFormulario(DatosSocio datos, boolean esAlta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

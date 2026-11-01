package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.DatosAcceso;
import ar.edu.ub.club.accesos.aplicacion.DatosAdministrador;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-14: Formulario de cuenta de acceso: alta de administrador o modificación de datos de
 * acceso de cualquier cuenta.
 *
 * <p>Ruta: {@code cuentas/formulario/:id?}. Permiso: {@code GESTIONAR_CUENTAS}.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-16
 */
public class VistaFormularioCuenta extends VistaProtegida implements PantallaFormularioCuenta {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param datos valor de {@code datos}
     */
    @Override
    public void mostrarAlta(DatosAdministrador datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param datos valor de {@code datos}
     * @param cuenta valor de {@code cuenta}
     */
    @Override
    public void mostrarModificacion(DatosAcceso datos, DetalleCuenta cuenta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

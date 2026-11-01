package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-05: Inicio del socio: resumen de la cuota vigente, próximas reservas y accesos a
 * reservar y pagar.
 *
 * <p>Ruta: {@code inicio}. Permiso: {@code CONSULTAR_PERFIL_PROPIO}.
 *
 * <p>Casos de uso: CU-05, CU-29
 */
public class VistaInicioSocio extends VistaProtegida implements PantallaInicioSocio {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-29
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-29
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-29
     * @param resumen valor de {@code resumen}
     */
    @Override
    public void mostrarResumen(ResumenPerfil resumen) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

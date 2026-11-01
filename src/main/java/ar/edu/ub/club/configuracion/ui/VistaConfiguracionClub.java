package ar.edu.ub.club.configuracion.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.configuracion.aplicacion.DatosConfiguracionClub;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-34: Formulario de datos institucionales del club.
 *
 * <p>Ruta: {@code configuracion-club}. Permiso: {@code CONFIGURAR_CLUB}.
 *
 * <p>Casos de uso: todos
 */
public class VistaConfiguracionClub extends VistaProtegida implements PantallaConfiguracionClub {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param datos valor de {@code datos}
     */
    @Override
    public void mostrarDatos(DatosConfiguracionClub datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

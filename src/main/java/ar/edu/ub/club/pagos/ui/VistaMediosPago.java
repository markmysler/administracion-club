package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.pagos.aplicacion.DatosConfiguracionPagos;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-31: Configuración de medios manuales y de la pasarela (modo y habilitación).
 *
 * <p>Ruta: {@code pagos/configuracion}. Permiso: {@code CONFIGURAR_PAGOS}.
 *
 * <p>Casos de uso: todos
 */
public class VistaMediosPago extends VistaProtegida implements PantallaMediosPago {

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
     * @param pasarela valor de {@code pasarela}
     */
    @Override
    public void mostrarConfiguracion(DatosConfiguracionPagos datos, ResumenPasarela pasarela) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

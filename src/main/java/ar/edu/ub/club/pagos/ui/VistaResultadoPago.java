package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-26: Página de retorno desde la pasarela.
 *
 * <p>Ruta: {@code pagos/resultado?referencia=…}. Permiso: {@code PAGAR_CUOTA_ONLINE}.
 *
 * <p>Casos de uso: CU-35
 */
public class VistaResultadoPago extends VistaProtegida implements PantallaResultadoPago {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param resumen valor de {@code resumen}
     */
    @Override
    public void mostrarEstado(ResumenPagoOnline resumen) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     */
    @Override
    public void mostrarEsperaConfirmacion() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

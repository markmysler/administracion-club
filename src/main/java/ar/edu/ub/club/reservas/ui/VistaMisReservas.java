package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-21: Reservas propias vigentes e históricas con acción de cancelar dentro del plazo.
 *
 * <p>Ruta: {@code mis-reservas}. Permiso: {@code GESTIONAR_RESERVAS_PROPIAS}.
 *
 * <p>Casos de uso: CU-29, CU-30
 */
public class VistaMisReservas extends VistaProtegida implements PantallaMisReservas {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-29, CU-30
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarReservas(List<FilaReserva> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param detalle valor de {@code detalle}
     */
    @Override
    public void mostrarDetalle(DetalleReserva detalle) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-22: Reservas del club con filtros, acceso a modificar y cancelación administrativa con
 * motivo.
 *
 * <p>Ruta: {@code reservas-club}. Permiso: {@code GESTIONAR_RESERVAS_CLUB}.
 *
 * <p>Casos de uso: CU-31, CU-32, CU-33
 */
public class VistaReservasClub extends VistaProtegida implements PantallaReservasClub {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @param deportes valor de {@code deportes}
     */
    @Override
    public void mostrarFiltros(List<OpcionSeleccion> deportes) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarReservas(List<FilaReserva> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @param detalle valor de {@code detalle}
     */
    @Override
    public void mostrarDetalle(DetalleReserva detalle) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @param idReserva valor de {@code idReserva}
     */
    @Override
    public void pedirMotivoCancelacion(Long idReserva) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

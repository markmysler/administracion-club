package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-23: Formulario para cambiar fecha, horario o cancha de una reserva, con consulta de
 * turnos libres.
 *
 * <p>Ruta: {@code reservas-club/modificar/:id}. Permiso: {@code GESTIONAR_RESERVAS_CLUB}.
 *
 * <p>Casos de uso: CU-32
 */
public class VistaModificarReserva extends VistaProtegida implements PantallaModificarReserva {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-32
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-32
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-32
     * @param detalle valor de {@code detalle}
     * @param canchas valor de {@code canchas}
     */
    @Override
    public void mostrarReserva(DetalleReserva detalle, List<OpcionSeleccion> canchas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-32
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarTurnos(List<FilaTurno> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

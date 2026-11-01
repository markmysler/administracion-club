package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosReglaReserva;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-18: Formulario de las seis reglas de reserva.
 *
 * <p>Ruta: {@code agenda/reglas}. Permiso: {@code CONFIGURAR_AGENDA}.
 *
 * <p>Casos de uso: CU-25
 */
public class VistaReglasReserva extends VistaProtegida implements PantallaReglasReserva {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-25
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-25
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-25
     * @param datos valor de {@code datos}
     */
    @Override
    public void mostrarReglas(DatosReglaReserva datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

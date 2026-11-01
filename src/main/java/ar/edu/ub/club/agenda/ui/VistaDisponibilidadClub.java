package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosDisponibilidad;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista de la pantalla P-17: Configuración de días y horario de atención y lista de feriados.
 *
 * <p>Ruta: {@code agenda/disponibilidad}. Permiso: {@code CONFIGURAR_AGENDA}.
 *
 * <p>Casos de uso: todos
 */
public class VistaDisponibilidadClub extends VistaProtegida implements PantallaDisponibilidadClub {

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
     * @param feriados valor de {@code feriados}
     */
    @Override
    public void mostrarDisponibilidad(DatosDisponibilidad datos, List<FilaFeriado> feriados) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param desde valor de {@code desde}
     * @param hasta valor de {@code hasta}
     */
    @Override
    public void mostrarRangoCalendario(LocalDate desde, LocalDate hasta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param afectadas valor de {@code afectadas}
     */
    @Override
    public void pedirConfirmacion(List<FilaResumenReserva> afectadas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

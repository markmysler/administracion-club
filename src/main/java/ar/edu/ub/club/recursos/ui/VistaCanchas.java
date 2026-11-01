package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.recursos.aplicacion.DatosCancha;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-16: Listado de canchas por deporte con alta, edición y desactivación con advertencia.
 *
 * <p>Ruta: {@code canchas}. Permiso: {@code GESTIONAR_RECURSOS}.
 *
 * <p>Casos de uso: CU-21, CU-22, CU-23
 */
public class VistaCanchas extends VistaProtegida implements PantallaCanchas {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param opciones valor de {@code opciones}
     */
    @Override
    public void mostrarDeportes(List<OpcionSeleccion> opciones) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarCanchas(List<FilaCancha> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param datos valor de {@code datos}
     * @param idCancha valor de {@code idCancha}
     */
    @Override
    public void editar(DatosCancha datos, Long idCancha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param idCancha valor de {@code idCancha}
     * @param afectadas valor de {@code afectadas}
     */
    @Override
    public void pedirConfirmacionBaja(Long idCancha, List<FilaResumenReserva> afectadas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param afectadas valor de {@code afectadas}
     */
    @Override
    public void pedirConfirmacionCambio(List<FilaResumenReserva> afectadas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

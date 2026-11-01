package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.recursos.aplicacion.DatosDeporte;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-15: Listado de deportes con alta, edición y desactivación con advertencia.
 *
 * <p>Ruta: {@code deportes}. Permiso: {@code GESTIONAR_RECURSOS}.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20
 */
public class VistaDeportes extends VistaProtegida implements PantallaDeportes {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarDeportes(List<FilaDeporte> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param datos valor de {@code datos}
     * @param idDeporte valor de {@code idDeporte}
     */
    @Override
    public void editar(DatosDeporte datos, Long idDeporte) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param idDeporte valor de {@code idDeporte}
     * @param afectadas valor de {@code afectadas}
     */
    @Override
    public void pedirConfirmacionBaja(Long idDeporte, List<FilaResumenReserva> afectadas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

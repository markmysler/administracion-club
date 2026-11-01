package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-08: Ficha del socio con su grupo, cuotas y reservas futuras; acciones modificar, ver
 * grupo, asociar a otro grupo y desactivar.
 *
 * <p>Ruta: {@code socios/ficha/:id}. Permiso: {@code GESTIONAR_SOCIOS}.
 *
 * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
 */
public class VistaFichaSocio extends VistaProtegida implements PantallaFichaSocio {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
     * @param detalle valor de {@code detalle}
     */
    @Override
    public void mostrarFicha(DetalleSocio detalle) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
     * @param idSocio valor de {@code idSocio}
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     * @param candidatosTitular valor de {@code candidatosTitular}
     */
    @Override
    public void abrirDesactivacion(Long idSocio, String nombre, String dni, List<OpcionSeleccion> candidatosTitular) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
     * @param idSocio valor de {@code idSocio}
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     * @param idGrupoActual valor de {@code idGrupoActual}
     * @param grupoActual valor de {@code grupoActual}
     * @param idGrupoDestino valor de {@code idGrupoDestino}
     */
    @Override
    public void abrirAsociacion(Long idSocio, String nombre, String dni, Long idGrupoActual, String grupoActual, Long idGrupoDestino) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

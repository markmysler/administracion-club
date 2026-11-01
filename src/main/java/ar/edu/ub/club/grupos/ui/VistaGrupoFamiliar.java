package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.socios.ui.FilaSocio;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-11: Búsqueda de grupos y detalle del grupo familiar: integrantes, titular e importe de
 * la próxima cuota; acciones cambiar titular, quitar integrante y asociar un socio (busca primero el socio y
 * abre P-12 con este grupo preseleccionado como destino).
 *
 * <p>Ruta: {@code grupos/:id?}. Permiso: {@code GESTIONAR_GRUPOS}.
 *
 * <p>Casos de uso: todos
 */
public class VistaGrupoFamiliar extends VistaProtegida implements PantallaGrupoFamiliar {

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
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarGrupos(List<FilaGrupo> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param ficha valor de {@code ficha}
     */
    @Override
    public void mostrarGrupo(FichaGrupo ficha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarSocios(List<FilaSocio> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
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

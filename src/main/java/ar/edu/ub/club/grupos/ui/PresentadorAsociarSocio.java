package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;

/**
 * Presentador de la pantalla P-12: Diálogo que se abre desde la ficha del socio que se asocia (P-08), como
 * indica CU-07: busca el grupo destino por DNI o nombre de un integrante o del titular y asocia el socio a ese
 * grupo.
 *
 * <p>Casos de uso: CU-07
 */
public class PresentadorAsociarSocio extends PresentadorBase<PantallaAsociarSocio> {

    /** Atributo servicioGruposFamiliares. */
    private ServicioGruposFamiliares servicioGruposFamiliares;

    /** Atributo idSocio. */
    private Long idSocio;

    /**
     * {@code pantalla.mostrarSocio(nombre, dni, grupoActual)} y, si hay destino, {@code
     * pantalla.preseleccionar(idGrupoDestino)}
     *
     * <p>Casos de uso: CU-07
     * @param idSocio valor de {@code idSocio}
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     * @param idGrupoActual valor de {@code idGrupoActual}
     * @param grupoActual valor de {@code grupoActual}
     * @param idGrupoDestino valor de {@code idGrupoDestino}
     */
    public void alAbrir(Long idSocio, String nombre, String dni, Long idGrupoActual, String grupoActual, Long idGrupoDestino) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, true))} → {@code FilaGrupo}
     *
     * <p>Casos de uso: CU-07
     * @param texto valor de {@code texto}
     */
    public void alBuscar(String texto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioGruposFamiliares.asociarSocio(idSocio, idGrupoDestino): DetalleGrupo}; luego {@code
     * navegarA("grupos/" + idGrupoDestino)}
     *
     * <p>Casos de uso: CU-07
     * @param idGrupoDestino valor de {@code idGrupoDestino}
     */
    public void alConfirmar(Long idGrupoDestino) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

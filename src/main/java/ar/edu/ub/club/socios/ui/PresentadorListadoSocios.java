package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.CriterioBusquedaSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;

/**
 * Presentador de la pantalla P-06: Listado y búsqueda de socios con acceso a la ficha y al alta.
 *
 * <p>Casos de uso: CU-01, CU-04
 */
public class PresentadorListadoSocios extends PresentadorBase<PantallaListadoSocios> {

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /**
     * {@code ServicioSocios.buscarSocios(criterio): List&lt;Socio&gt;} → {@code FilaSocio.desde(socio)}
     *
     * <p>Casos de uso: CU-04
     * @param criterio valor de {@code criterio}
     */
    public void alBuscar(CriterioBusquedaSocio criterio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("socios/ficha/" + idSocio)}
     *
     * <p>Casos de uso: CU-04
     * @param idSocio valor de {@code idSocio}
     */
    public void alSeleccionar(Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("socios/formulario")}
     *
     * <p>Casos de uso: CU-01, CU-04
     */
    public void alNuevo() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

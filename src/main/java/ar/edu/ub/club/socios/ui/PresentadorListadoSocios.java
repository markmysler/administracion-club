package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.CriterioBusquedaSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de P-06 (listado y búsqueda de socios con acceso a la ficha y al alta).
 *
 * <p>Presentador del patrón MVP (Vista Pasiva): no usa tipos de Vaadin; llama a la fachada dentro de
 * {@code consultar(...)} y convierte con {@code FilaSocio.desde(...)}.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-04
 */
@SpringComponent
@UIScope
public class PresentadorListadoSocios extends PresentadorBase<PantallaListadoSocios> {

    /** Fachada de socios. */
    private final ServicioSocios servicioSocios;

    /**
     * Crea el presentador (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param servicioSocios fachada de socios
     */
    public PresentadorListadoSocios(ServicioSocios servicioSocios) {
        this.servicioSocios = servicioSocios;
    }

    /**
     * {@code ServicioSocios.buscarSocios(criterio)} → {@code FilaSocio.desde(socio)} → {@code mostrarSocios}; sin
     * coincidencias informa el mensaje de CU-04 FA1.
     *
     * <p>Casos de uso: CU-02, CU-04
     * @param criterio texto y estado
     */
    public void alBuscar(CriterioBusquedaSocio criterio) {
        consultar(() -> servicioSocios.buscarSocios(criterio).stream().map(FilaSocio::desde).toList())
                .ifPresent(filas -> {
                    pantalla.mostrarSocios(filas);
                    if (filas.isEmpty()) {
                        pantalla.mostrarMensaje("No hay socios que coincidan con la búsqueda.");
                    }
                });
    }

    /**
     * Abre la ficha del socio elegido (P-08).
     *
     * <p>Casos de uso: CU-02, CU-04
     * @param idSocio socio elegido
     */
    public void alSeleccionar(Long idSocio) {
        pantalla.navegarA("socios/ficha/" + idSocio);
    }

    /**
     * Abre el formulario de alta (P-07).
     *
     * <p>Casos de uso: CU-01
     */
    public void alNuevo() {
        pantalla.navegarA("socios/formulario");
    }
}

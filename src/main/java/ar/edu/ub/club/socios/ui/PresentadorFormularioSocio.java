package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.DatosSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;

/**
 * Presentador de la pantalla P-07: Formulario de alta y modificación de socio.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-16
 */
public class PresentadorFormularioSocio extends PresentadorBase<PantallaFormularioSocio> {

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /** Atributo idSocio. */
    private Long idSocio;

    /**
     * {@code ServicioSocios.listarCategorias()} (→ {@code OpcionSeleccion}); con id: {@code
     * ServicioSocios.consultarSocio(idSocio): FichaSocio} → {@code DatosSocio.desde(ficha.socio)}
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @param idSocio valor de {@code idSocio}
     */
    public void cargar(Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * alta {@code ServicioSocios.registrarSocio(datos): Socio}; modificación {@code
     * ServicioSocios.modificarSocio(idSocio, datos): Socio}; luego {@code navegarA("socios/ficha/" + id)}
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @param datos valor de {@code datos}
     */
    public void alGuardar(DatosSocio datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * vuelve a la ficha o a {@code socios}
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     */
    public void alCancelar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import java.util.List;

/**
 * Presentador de la pantalla P-08: Ficha del socio con su grupo, cuotas y reservas futuras; acciones modificar,
 * ver grupo, asociar a otro grupo y desactivar.
 *
 * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
 */
public class PresentadorFichaSocio extends PresentadorBase<PantallaFichaSocio> {

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /** Atributo idSocio. */
    private Long idSocio;

    /** Atributo idGrupo. */
    private Long idGrupo;

    /** Atributo candidatosTitular. */
    private List<OpcionSeleccion> candidatosTitular;

    /**
     * {@code ServicioSocios.consultarSocio(idSocio): FichaSocio} → {@code DetalleSocio.desde(ficha)}
     *
     * <p>Casos de uso: CU-03, CU-04
     * @param idSocio valor de {@code idSocio}
     */
    public void cargar(Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("socios/formulario/" + id)}
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04
     */
    public void alModificar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("cuentas/formulario/" + id)} (CU-15)
     *
     * <p>Casos de uso: CU-03, CU-04
     */
    public void alModificarAcceso() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("grupos/" + idGrupo)}
     *
     * <p>Casos de uso: CU-03, CU-04
     */
    public void alVerGrupo() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code pantalla.abrirDesactivacion(idSocio, nombre, dni, candidatosTitular)} (P-09)
     *
     * <p>Casos de uso: CU-03, CU-04
     */
    public void alDesactivar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code pantalla.abrirAsociacion(idSocio, nombre, dni, idGrupo, grupoActual, null)} (P-12; {@code grupoActual}
     * es el texto con el titular y el DNI del grupo vigente)
     *
     * <p>Casos de uso: CU-03, CU-04, CU-07
     */
    public void alAsociarAGrupo() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.configuracion.aplicacion.ServicioConfiguracionClub;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;

/**
 * Presentador de la pantalla P-29: Vista imprimible de un comprobante.
 *
 * <p>Casos de uso: todos
 */
public class PresentadorComprobante extends PresentadorBase<PantallaComprobante> {

    /** Atributo servicioPagos. */
    private ServicioPagos servicioPagos;

    /** Atributo servicioConfiguracionClub. */
    private ServicioConfiguracionClub servicioConfiguracionClub;

    /** Atributo usuarioActual. */
    private UsuarioActual usuarioActual;

    /**
     * {@code ServicioPagos.obtenerComprobante(usuarioActual.obtener(), idComprobante): Comprobante} y {@code
     * ServicioConfiguracionClub.consultar(): ConfiguracionClub} → {@code DetalleComprobante.desde(comprobante,
     * club)}
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param idComprobante valor de {@code idComprobante}
     */
    public void cargar(Long idComprobante) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code pantalla.imprimir()}
     *
     * <p>Casos de uso: CU-37
     */
    public void alImprimir() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

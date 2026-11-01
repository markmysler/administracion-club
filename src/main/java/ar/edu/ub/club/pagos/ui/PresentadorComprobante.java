package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.configuracion.aplicacion.ServicioConfiguracionClub;
import ar.edu.ub.club.configuracion.dominio.ConfiguracionClub;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla imprimible P-29 Comprobante (MVP Vista Pasiva; no usa tipos de Vaadin). Pide el
 * comprobante a {@code ServicioPagos.obtenerComprobante} con la cuenta de la sesión (la fachada verifica el alcance,
 * RN-20) y los datos del club a {@code ServicioConfiguracionClub}. Diagrama sec_cu37_p3.
 *
 * <p>Casos de uso: CU-05, CU-36, CU-37
 */
@SpringComponent
@UIScope
public class PresentadorComprobante extends PresentadorBase<PantallaComprobante> {

    /** Fachada de pagos. */
    private final ServicioPagos servicioPagos;

    /** Fachada de la configuración del club (encabezado institucional). */
    private final ServicioConfiguracionClub servicioConfiguracionClub;

    /** Cuenta de la sesión. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea el presentador con sus dependencias.
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     * @param servicioPagos fachada de pagos
     * @param servicioConfiguracionClub fachada de configuración del club
     * @param usuarioActual cuenta de la sesión
     */
    public PresentadorComprobante(ServicioPagos servicioPagos, ServicioConfiguracionClub servicioConfiguracionClub,
                                  UsuarioActual usuarioActual) {
        this.servicioPagos = servicioPagos;
        this.servicioConfiguracionClub = servicioConfiguracionClub;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Carga el comprobante (pasos 9 a 11): {@code ServicioPagos.obtenerComprobante(usuarioActual.obtener(),
     * idComprobante)} y {@code ServicioConfiguracionClub.consultar()} → {@code DetalleComprobante.desde(comprobante,
     * club)} → {@code pantalla.mostrarComprobante}. Fuera del alcance, {@code consultar(...)} muestra «No tiene
     * permiso para realizar esta operación.» sin datos (FA3).
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     * @param idComprobante id del comprobante
     */
    public void cargar(Long idComprobante) {
        consultar(() -> {
            Comprobante comprobante = servicioPagos.obtenerComprobante(usuarioActual.obtener(), idComprobante);
            ConfiguracionClub club = servicioConfiguracionClub.consultar();
            return DetalleComprobante.desde(comprobante, club);
        }).ifPresent(pantalla::mostrarComprobante);
    }

    /**
     * Imprime: {@code pantalla.imprimir()}.
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     */
    public void alImprimir() {
        pantalla.imprimir();
    }
}

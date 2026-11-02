package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.pagos.aplicacion.ConfiguracionPagos;
import ar.edu.ub.club.pagos.aplicacion.DatosConfiguracionPagos;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import ar.edu.ub.club.pagos.dominio.ModoPasarela;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-31 Medios de pago y pasarela (MVP Vista Pasiva; no usa tipos de Vaadin): consulta y
 * guarda la configuración con {@code ServicioPagos} y compara lo pedido con lo guardado para elegir el mensaje.
 * Diagramas sec_cu34_p1 y sec_cu34_p2.
 *
 * <p>Casos de uso: CU-34
 */
@SpringComponent
@UIScope
public class PresentadorMediosPago extends PresentadorBase<PantallaMediosPago> {

    /** Fachada de pagos. */
    private final ServicioPagos servicioPagos;

    /**
     * Crea el presentador con su fachada.
     *
     * <p>Casos de uso: CU-34
     * @param servicioPagos fachada de pagos
     */
    public PresentadorMediosPago(ServicioPagos servicioPagos) {
        this.servicioPagos = servicioPagos;
    }

    /**
     * Carga la configuración (pasos 1 y 2): {@code ServicioPagos.consultarMediosYPasarela()} → {@code
     * DatosConfiguracionPagos.desde(config)} y {@code ResumenPasarela.desde(config.pasarela)} → {@code
     * pantalla.mostrarConfiguracion}.
     *
     * <p>Casos de uso: CU-34
     */
    public void cargar() {
        consultar(() -> servicioPagos.consultarMediosYPasarela()).ifPresent(this::mostrar);
    }

    /**
     * Guarda la configuración (pasos 3 a 11): {@code ServicioPagos.configurarMediosYPasarela(datos)}; como la
     * operación no devuelve la configuración, la vuelve a consultar, la muestra y elige el mensaje: habilitada,
     * deshabilitada a pedido (FA3), Mercado Pago sin credenciales (FA4) o rechazada por la pasarela (FA5). Un medio
     * repetido o un dato inválido llegan como {@code ErrorNegocio} con campo y se marcan en el formulario (FA1, FA2).
     *
     * <p>Casos de uso: CU-34
     * @param datos configuración pedida
     */
    public void alGuardar(DatosConfiguracionPagos datos) {
        boolean pidioHabilitada = datos.getHabilitada();
        if (!ejecutar(() -> servicioPagos.configurarMediosYPasarela(datos))) {
            return;
        }
        consultar(() -> servicioPagos.consultarMediosYPasarela()).ifPresent(config -> {
            mostrar(config);
            ResumenPasarela pasarela = ResumenPasarela.desde(config.getPasarela());
            if (pasarela.habilitada()) {
                pantalla.mostrarMensaje("Configuración guardada. Pagos online habilitados");
            } else if (!pidioHabilitada) {
                pantalla.mostrarMensaje("Configuración guardada. El pago online no se ofrecerá");
            } else if (ModoPasarela.MERCADO_PAGO.name().equals(pasarela.modo())
                    && !pasarela.credencialesConfiguradas()) {
                pantalla.mostrarError("Las credenciales deben cargarse en la configuración protegida del servidor:"
                        + " la pasarela quedó deshabilitada");
            } else {
                pantalla.mostrarError("La pasarela rechazó la configuración: quedó deshabilitada."
                        + " Los medios manuales se guardaron");
            }
        });
    }

    /**
     * Convierte la configuración en los modelos de la pantalla y la muestra.
     *
     * <p>Casos de uso: CU-34
     * @param config configuración de pagos
     */
    private void mostrar(ConfiguracionPagos config) {
        pantalla.mostrarConfiguracion(DatosConfiguracionPagos.desde(config), ResumenPasarela.desde(config.getPasarela()));
    }
}

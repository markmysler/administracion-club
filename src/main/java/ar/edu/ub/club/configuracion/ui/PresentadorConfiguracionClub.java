package ar.edu.ub.club.configuracion.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.configuracion.aplicacion.DatosConfiguracionClub;
import ar.edu.ub.club.configuracion.aplicacion.ServicioConfiguracionClub;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-34: Formulario de datos institucionales del club (MVP, Vista Pasiva). No usa tipos
 * de Vaadin: llama a la fachada dentro de {@code consultar(...)} y le pasa a la pantalla modelos de vista.
 *
 * <p>Casos de uso: CU-41
 */
@SpringComponent
@UIScope
public class PresentadorConfiguracionClub extends PresentadorBase<PantallaConfiguracionClub> {

    /** Fachada de configuración del club. */
    private final ServicioConfiguracionClub servicioConfiguracionClub;

    /**
     * Crea el presentador.
     *
     * <p>Casos de uso: CU-41
     * @param servicioConfiguracionClub fachada de configuración
     */
    public PresentadorConfiguracionClub(ServicioConfiguracionClub servicioConfiguracionClub) {
        this.servicioConfiguracionClub = servicioConfiguracionClub;
    }

    /**
     * {@code ServicioConfiguracionClub.consultar(): ConfiguracionClub} → {@code DatosConfiguracionClub.desde(c)}
     * → {@code pantalla.mostrarDatos} (CU-41 pasos 1-2).
     *
     * <p>Casos de uso: CU-41
     */
    public void cargar() {
        consultar(servicioConfiguracionClub::consultar)
                .map(DatosConfiguracionClub::desde)
                .ifPresent(pantalla::mostrarDatos);
    }

    /**
     * {@code ServicioConfiguracionClub.modificar(datos)}; si se guardó, muestra los datos guardados y el mensaje
     * (CU-41 pasos 3-7); si no, {@code PresentadorBase} marca el campo con el motivo (FA2).
     *
     * <p>Casos de uso: CU-41
     * @param datos datos leídos del formulario
     */
    public void alGuardar(DatosConfiguracionClub datos) {
        consultar(() -> servicioConfiguracionClub.modificar(datos)).ifPresent(c -> {
            pantalla.mostrarDatos(DatosConfiguracionClub.desde(c));
            pantalla.mostrarMensaje("Datos guardados.");
        });
    }
}

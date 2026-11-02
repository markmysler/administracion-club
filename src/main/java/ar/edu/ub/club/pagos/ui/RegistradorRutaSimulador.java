package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.pagos.integracion.PasarelaPago;
import ar.edu.ub.club.pagos.integracion.PasarelaSimuladaAdapter;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.startup.ApplicationRouteRegistry;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.aop.support.AopUtils;

/**
 * Registra la ruta pública {@code simulador-pasarela/:idPreferencia} de {@code VistaSimuladorPasarela} (P-27) al
 * iniciar el servicio de Vaadin, solo si el adaptador activo de la pasarela es {@code PasarelaSimuladaAdapter}; con
 * Mercado Pago la ruta no existe y nadie puede simular un pago (RNF-05). Solo consulta qué adaptador está activo
 * (07B, paquete pagos.ui).
 *
 * <p>Casos de uso: CU-35
 */
@SpringComponent
public class RegistradorRutaSimulador implements VaadinServiceInitListener {

    /** Ruta de la vista del simulador. */
    static final String RUTA = "simulador-pasarela/:idPreferencia";

    /** Adaptador activo de la pasarela. */
    private final PasarelaPago pasarela;

    /**
     * Crea el registrador con el adaptador activo.
     *
     * <p>Casos de uso: CU-35
     * @param pasarela adaptador activo de la pasarela
     */
    public RegistradorRutaSimulador(PasarelaPago pasarela) {
        this.pasarela = pasarela;
    }

    /**
     * Registra la ruta de {@code VistaSimuladorPasarela} con {@code RouteConfiguration} (sobre el registro de rutas de
     * la aplicación) solo si el adaptador activo es {@code PasarelaSimuladaAdapter}; se usa la clase real del bean
     * por si Spring lo envuelve en un proxy.
     *
     * <p>Casos de uso: CU-35
     * @param evento evento de inicio del servicio de Vaadin
     */
    @Override
    public void serviceInit(ServiceInitEvent evento) {
        if (PasarelaSimuladaAdapter.class.isAssignableFrom(AopUtils.getTargetClass(pasarela))) {
            RouteConfiguration rutas = RouteConfiguration.forRegistry(
                    ApplicationRouteRegistry.getInstance(evento.getSource().getContext()));
            if (!rutas.isRouteRegistered(VistaSimuladorPasarela.class)) {
                rutas.setRoute(RUTA, VistaSimuladorPasarela.class);
            }
        }
    }
}

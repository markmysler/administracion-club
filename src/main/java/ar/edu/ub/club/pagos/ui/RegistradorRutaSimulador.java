package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.pagos.integracion.PasarelaPago;
import com.vaadin.flow.server.ServiceInitEvent;

/**
 * Vista de la pantalla P-27: Página del simulador de pasarela (solo modo SIMULADA): muestra concepto e importe y
 * ofrece aprobar o rechazar.
 *
 * <p>Casos de uso: CU-35
 */
public class RegistradorRutaSimulador {

    /** Adaptador activo de la pasarela. */
    private PasarelaPago pasarela;

    /**
     * Registra la ruta {@code simulador-pasarela/:idPreferencia} de {@code VistaSimuladorPasarela} con {@code
     * RouteConfiguration} solo si el adaptador activo es {@code PasarelaSimuladaAdapter}; con Mercado Pago la ruta
     * no existe.
     *
     * <p>Casos de uso: CU-35
     * @param evento Evento de inicio del servicio de Vaadin.
     */
    public void serviceInit(ServiceInitEvent evento) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

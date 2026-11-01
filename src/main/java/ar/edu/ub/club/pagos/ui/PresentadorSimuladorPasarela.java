package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.pagos.integracion.PasarelaSimuladaAdapter;

/**
 * Presentador de la pantalla P-27: Página del simulador de pasarela (solo modo SIMULADA): muestra concepto e
 * importe y ofrece aprobar o rechazar.
 *
 * <p>Casos de uso: CU-35
 */
public class PresentadorSimuladorPasarela extends PresentadorBase<PantallaSimuladorPasarela> {

    /** Atributo pasarelaSimulada. */
    private PasarelaSimuladaAdapter pasarelaSimulada;

    /** Atributo idPreferencia. */
    private String idPreferencia;

    /**
     * {@code PasarelaSimuladaAdapter.obtenerIntencion(idPreferencia): IntencionSimulada} → {@code
     * ResumenIntencionSimulada.desde(i)}
     *
     * <p>Casos de uso: CU-35
     * @param idPreferencia valor de {@code idPreferencia}
     */
    public void cargar(String idPreferencia) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code PasarelaSimuladaAdapter.resolverPago(idPreferencia, aprobar): String} (envía la notificación al
     * webhook) → {@code pantalla.redirigirA(urlRetorno)}
     *
     * <p>Casos de uso: CU-35
     * @param aprobar valor de {@code aprobar}
     */
    public void alResolver(boolean aprobar) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

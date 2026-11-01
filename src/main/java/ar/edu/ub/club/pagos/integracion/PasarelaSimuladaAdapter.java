package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import java.util.Map;

/**
 * Adaptador (patrón Adapter) que implementa {@code PasarelaPago} en modo SIMULADA. Cumple el papel de la
 * pasarela en el proyecto académico: guarda en memoria las intenciones de pago creadas, ofrece la página P-27
 * ({@code VistaSimuladorPasarela} y {@code PresentadorSimuladorPasarela}) y, al resolverse, envía al webhook del
 * sistema la misma notificación que enviaría Mercado Pago. No mueve dinero.
 *
 * <p>Casos de uso: CU-35
 */
public class PasarelaSimuladaAdapter implements PasarelaPago {

    /** Intenciones creadas, por idPreferencia. */
    private Map<String, IntencionSimulada> intenciones;

    /** URL del webhook del sistema. */
    private String urlWebhook;

    /**
     * Registra la intención y devuelve como {@code urlCheckout} la ruta de P-27.
     *
     * <p>Casos de uso: CU-35
     * @param solicitud Referencia externa, concepto e importe.
     * @return {@code IntencionPago}.
     */
    public IntencionPago crearIntencionPago(SolicitudPagoOnline solicitud) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve el resultado elegido en el simulador.
     *
     * <p>Casos de uso: CU-35
     * @param idPagoPasarela Identificador generado al resolver.
     * @return {@code ResultadoPasarela}.
     */
    public ResultadoPasarela obtenerResultado(String idPagoPasarela) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * En modo simulado siempre es válida.
     *
     * <p>Casos de uso: CU-35
     * @param config Configuración.
     * @return true.
     */
    public boolean validarConfiguracion(ConfiguracionPasarela config) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve la intención para mostrarla en P-27 (concepto, importe y vencimiento; {@code
     * PresentadorSimuladorPasarela} arma {@code ResumenIntencionSimulada}).
     *
     * <p>Casos de uso: CU-35
     * @param idPreferencia Intención.
     * @return {@code IntencionSimulada} (idPreferencia, referenciaExterna, concepto, importe, venceEn, urlRetorno).
     */
    public IntencionSimulada obtenerIntencion(String idPreferencia) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Registra el resultado, envía la notificación al webhook y devuelve la URL de retorno
     * (/pagos/retorno?referencia=(uuid}, la misma que usa Mercado Pago), que lleva a P-26.
     *
     * <p>Casos de uso: CU-35
     * @param idPreferencia Intención.
     * @param aprobado Resultado elegido.
     * @return URL de retorno.
     */
    public String resolverPago(String idPreferencia, boolean aprobado) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Adaptador (patrón Adapter) que implementa {@code PasarelaPago} en modo SIMULADA. Cumple el papel de la
 * pasarela en el proyecto académico: guarda en memoria las intenciones de pago creadas, ofrece la página P-27
 * ({@code VistaSimuladorPasarela} y {@code PresentadorSimuladorPasarela}) y, al resolverse, notifica al sistema
 * el resultado igual que el webhook de Mercado Pago. No mueve dinero.
 *
 * <p>Es el bean {@code @Primary} de {@code PasarelaPago} y está siempre disponible (P-27 lo usa directamente);
 * {@code ServicioPagos} lo usa cuando {@code ConfiguracionPasarela.modo} es SIMULADA (valor por omisión). Las
 * intenciones viven en memoria: se pierden al reiniciar la aplicación.
 *
 * <p>Casos de uso: CU-34, CU-35; tareas programadas: vencimiento de pagos online pendientes
 */
@Component
@Primary
public class PasarelaSimuladaAdapter implements PasarelaPago {

    /** Intenciones creadas, por idPreferencia. */
    private final Map<String, IntencionSimulada> intenciones = new ConcurrentHashMap<>();

    /** Agregado en la implementación: resultados elegidos en P-27, por idPagoPasarela. */
    private final Map<String, ResultadoPasarela> resultados = new ConcurrentHashMap<>();

    /** Agregado en la implementación: idPagoPasarela de cada intención ya resuelta (un segundo clic no vuelve a notificar). */
    private final Map<String, String> resueltas = new ConcurrentHashMap<>();

    /** Agregado en la implementación: URL pública del sistema ({@code club.url-base}). */
    private final String urlBase;

    /** Agregado en la implementación: fachada de pagos, obtenida en diferido para evitar el ciclo de dependencias. */
    private final ObjectProvider<ServicioPagos> servicioPagos;

    /**
     * Crea el adaptador.
     *
     * <p>Casos de uso: CU-35
     * @param urlBase URL pública del sistema
     * @param servicioPagos proveedor diferido de la fachada de pagos
     */
    public PasarelaSimuladaAdapter(@Value("${club.url-base:http://localhost:8080}") String urlBase,
            ObjectProvider<ServicioPagos> servicioPagos) {
        this.urlBase = urlBase.endsWith("/") ? urlBase.substring(0, urlBase.length() - 1) : urlBase;
        this.servicioPagos = servicioPagos;
    }

    /**
     * Registra la intención y devuelve como {@code urlCheckout} la ruta de P-27
     * ({@code <club.url-base>/simulador-pasarela/<idPreferencia>}).
     *
     * <p>Casos de uso: CU-35
     * @param solicitud Referencia externa, concepto e importe.
     * @return {@code IntencionPago}.
     */
    @Override
    public IntencionPago crearIntencionPago(SolicitudPagoOnline solicitud) {
        String idPreferencia = "PREF-SIM-" + UUID.randomUUID();
        intenciones.put(idPreferencia, new IntencionSimulada(idPreferencia, solicitud.getReferenciaExterna(),
                solicitud.getConcepto(), solicitud.getImporte(), solicitud.getVencimiento(), solicitud.getUrlRetorno()));
        return new IntencionPago(idPreferencia, urlBase + "/simulador-pasarela/" + idPreferencia);
    }

    /**
     * Devuelve el resultado elegido en el simulador.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param idPagoPasarela Identificador generado al resolver.
     * @return {@code ResultadoPasarela}, o null si el simulador no lo conoce (por ejemplo, después de reiniciar).
     */
    @Override
    public ResultadoPasarela obtenerResultado(String idPagoPasarela) {
        return idPagoPasarela == null ? null : resultados.get(idPagoPasarela);
    }

    /**
     * En modo simulado siempre es válida.
     *
     * <p>Casos de uso: CU-34
     * @param config Configuración.
     * @return true.
     */
    @Override
    public boolean validarConfiguracion(ConfiguracionPasarela config) {
        return true;
    }

    /**
     * Devuelve la intención para mostrarla en P-27 (concepto, importe y vencimiento; {@code
     * PresentadorSimuladorPasarela} arma {@code ResumenIntencionSimulada}).
     *
     * <p>Casos de uso: CU-35
     * @param idPreferencia Intención.
     * @return {@code IntencionSimulada} (idPreferencia, referenciaExterna, concepto, importe, venceEn, urlRetorno).
     * @throws ErrorNegocio si la intención no existe
     */
    public IntencionSimulada obtenerIntencion(String idPreferencia) {
        IntencionSimulada intencion = idPreferencia == null ? null : intenciones.get(idPreferencia);
        if (intencion == null) {
            throw new ErrorNegocio("La intención de pago no existe o ya no está disponible.");
        }
        return intencion;
    }

    /**
     * Registra el resultado, notifica al sistema con {@code ServicioPagos.confirmarResultadoPasarela(idPagoPasarela)}
     * (la misma confirmación que dispara el webhook) y devuelve la URL de retorno ({@code
     * /pagos/retorno?referencia=(uuid)}, la misma que usa Mercado Pago), que lleva a P-26. Si la intención ya se
     * resolvió solo devuelve la URL.
     *
     * <p>Casos de uso: CU-35
     * @param idPreferencia Intención.
     * @param aprobado Resultado elegido.
     * @return URL de retorno.
     * @throws ErrorNegocio si la intención no existe
     */
    public String resolverPago(String idPreferencia, boolean aprobado) {
        IntencionSimulada intencion = obtenerIntencion(idPreferencia);
        if (resueltas.containsKey(idPreferencia)) {
            return intencion.getUrlRetorno();
        }
        String idPagoPasarela = "SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        resultados.put(idPagoPasarela, new ResultadoPasarela(idPagoPasarela, intencion.getReferenciaExterna(),
                aprobado ? ResultadoPasarela.APROBADO : ResultadoPasarela.RECHAZADO, intencion.getImporte(),
                aprobado ? null : "Rechazado en el simulador de pasarela"));
        servicioPagos.getObject().confirmarResultadoPasarela(idPagoPasarela);
        resueltas.put(idPreferencia, idPagoPasarela);
        return intencion.getUrlRetorno();
    }

}

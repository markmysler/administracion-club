package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;

/**
 * Puerto (interfaz objetivo del patrón Adapter, Protected Variations) hacia la pasarela de pago AC-03. Lo
 * implementan {@code PasarelaSimuladaAdapter} (modo SIMULADA, sin dinero real) y {@code MercadoPagoAdapter}
 * (Checkout Pro); {@code ServicioPagos} elige el adaptador según {@code ConfiguracionPasarela.modo}.
 *
 * <p>Casos de uso: CU-34, CU-35; tareas programadas: vencimiento de pagos online pendientes
 */
public interface PasarelaPago {

    /**
     * Crea la intención de pago (preferencia) que vence a las 24 horas.
     *
     * <p>Casos de uso: CU-35
     * @param solicitud Referencia, concepto, importe, retorno, notificación y vencimiento.
     * @return identificador de la preferencia y página de pago
     * @throws IllegalStateException si la pasarela no puede crear la intención (CU-35 FA6)
     */
    IntencionPago crearIntencionPago(SolicitudPagoOnline solicitud);

    /**
     * Consulta del lado del servidor el resultado de un pago (RN-17: nunca se confía en la notificación).
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param idPagoPasarela Identificador del pago en la pasarela.
     * @return resultado, o null si la pasarela no conoce el pago
     */
    ResultadoPasarela obtenerResultado(String idPagoPasarela);

    /**
     * Valida la configuración antes de habilitar la pasarela (CU-34, paso 7).
     *
     * <p>Casos de uso: CU-34
     * @param c Configuración a validar.
     * @return true si la pasarela acepta la configuración
     */
    boolean validarConfiguracion(ConfiguracionPasarela c);
}

package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Adaptador concreto del patrón Adapter (GoF).
 *
 * <p>Casos de uso: PENDIENTE
 */
public class MercadoPagoAdapter implements PasarelaPago {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param solicitud valor de {@code solicitud}
     * @return el resultado
     */
    public IntencionPago crearIntencionPago(SolicitudPagoOnline solicitud) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param idPagoPasarela valor de {@code idPagoPasarela}
     * @return el resultado
     */
    public ResultadoPasarela obtenerResultado(String idPagoPasarela) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param config valor de {@code config}
     * @return el resultado
     */
    public boolean validarConfiguracion(ConfiguracionPasarela config) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

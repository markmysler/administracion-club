package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Casos de uso: CU-34, CU-35; tareas programadas: vencimiento de pagos online pendientes
 */
public interface PasarelaPago {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param solicitud valor de {@code solicitud}
     * @return el resultado
     */
    IntencionPago crearIntencionPago(SolicitudPagoOnline solicitud);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param idPagoPasarela valor de {@code idPagoPasarela}
     * @return el resultado
     */
    ResultadoPasarela obtenerResultado(String idPagoPasarela);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-34
     * @param c valor de {@code c}
     * @return el resultado
     */
    boolean validarConfiguracion(ConfiguracionPasarela c);
}

package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;

/**
 * Servicio de dominio (pagos.dominio) que emite el comprobante de un pago aprobado o la constancia de un pago
 * acreditado, en la misma transacción.
 *
 * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas
 */
public class GeneradorComprobante {

    /** Número correlativo y guardado del comprobante. */
    private FachadaPersistencia fachadaPersistencia;

    /**
     * Obtiene el número con {@code fachadaPersistencia.buscarUno(SiguienteNumeroComprobante())}, crea el {@code
     * Comprobante}, lo asocia con {@code p.asociarComprobante(c)} y lo guarda con {@code
     * fachadaPersistencia.put(c)}.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas
     * @param p valor de {@code p}
     * @return Comprobante emitido.
     */
    public Comprobante generar(Pago p) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

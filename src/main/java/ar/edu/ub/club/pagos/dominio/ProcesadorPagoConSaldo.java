package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.cuotas.dominio.Cuota;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Creador concreto del patrón Factory Method (GoF).
 *
 * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
 */
public class ProcesadorPagoConSaldo extends ProcesadorPago {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param cuota valor de {@code cuota}
     * @param datos valor de {@code datos}
     * @return el resultado
     */
    protected Pago crearPago(Cuota cuota, DatosPago datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

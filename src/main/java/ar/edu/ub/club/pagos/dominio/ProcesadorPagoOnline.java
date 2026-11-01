package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.cuotas.dominio.Cuota;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Creador concreto del patrón Factory Method (GoF).
 *
 * <p>Casos de uso: CU-35
 */
public class ProcesadorPagoOnline extends ProcesadorPago {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param cuota valor de {@code cuota}
     * @param datos valor de {@code datos}
     * @return el resultado
     */
    protected Pago crearPago(Cuota cuota, DatosPago datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

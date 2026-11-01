package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.pagos.dominio.Pago;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Estado concreto del patrón State.
 *
 * <p>Casos de uso: tareas programadas: vencimiento de cuotas
 */
public class EstadoCuotaVencida extends EstadoCuota {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @param cuota valor de {@code cuota}
     * @param pago valor de {@code pago}
     */
    public void registrarPagoAprobado(Cuota cuota, Pago pago) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @return el resultado
     */
    public boolean admitePago() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @return el resultado
     */
    public String codigo() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

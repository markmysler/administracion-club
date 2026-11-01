package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.time.LocalDateTime;

/**
 * Pago que registra la cancelación de una cuota con el saldo a favor del grupo (RN-21). Lo crea {@code
 * ProcesadorPagoConSaldo} ya aprobado.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
 */
public class PagoConSaldo extends Pago {

    /**
     * Constructor que usa {@code ProcesadorPagoConSaldo}.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param cuota valor de {@code cuota}
     * @param importe valor de {@code importe}
     */
    public PagoConSaldo(Cuota cuota, Dinero importe) {
        // PENDIENTE
    }

    /**
     * Devuelve "CON_SALDO".
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @return el resultado
     */
    public String origen() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve "Saldo a favor".
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @return el resultado
     */
    public String descripcionMedio() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param id valor de {@code id}
     * @param fecha valor de {@code fecha}
     * @param importe valor de {@code importe}
     * @param estado valor de {@code estado}
     * @param motivoRechazo valor de {@code motivoRechazo}
     * @return el resultado
     */
    public static PagoConSaldo reconstituir(Long id, LocalDateTime fecha, Dinero importe, EstadoPago estado, String motivoRechazo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param cuota valor de {@code cuota}
     * @param comprobante valor de {@code comprobante}
     */
    public void restaurarAsociaciones(Cuota cuota, Comprobante comprobante) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

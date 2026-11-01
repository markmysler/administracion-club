package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDateTime;

/**
 * Comprobante de un pago aprobado o constancia de acreditación de un pago acreditado, con numeración
 * correlativa (C-00000001). Extiende {@code EntidadPersistente}; es inmutable. Lo crea {@code
 * GeneradorComprobante}.
 *
 * <p>Casos de uso: CU-35, CU-36, CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class Comprobante extends EntidadPersistente {

    /** Pago que respalda. */
    private Pago pago;

    /** Número correlativo. */
    private String numero;

    /** Momento de emisión. */
    private LocalDateTime fechaEmision;

    /** Importe pagado. */
    private Dinero importe;

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: CU-37
     */
    private Comprobante() {
    }

    /**
     * Agregado en la implementación: crea el comprobante (lo llama {@code GeneradorComprobante.generar}).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param pago Pago aprobado o acreditado.
     * @param numero Número correlativo.
     * @param fechaEmision Momento de emisión.
     * @param importe Importe del pago.
     */
    public Comprobante(Pago pago, String numero, LocalDateTime fechaEmision, Dinero importe) {
        this.pago = pago;
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.importe = importe;
    }

    /**
     * Reconstituye un comprobante leído de {@code comprobante} (operación «persistencia»).
     *
     * <p>Casos de uso: CU-37
     * @param id Identificador.
     * @param numero Número.
     * @param fechaEmision Momento de emisión.
     * @param importe Importe.
     * @return el comprobante sin el pago
     */
    public static Comprobante reconstituir(Long id, String numero, LocalDateTime fechaEmision, Dinero importe) {
        Comprobante c = new Comprobante();
        c.asignarId(id);
        c.numero = numero;
        c.fechaEmision = fechaEmision;
        c.importe = importe;
        return c;
    }

    /**
     * Entrega el pago resuelto por {@code BrokerComprobante.cargarAsociaciones}.
     *
     * <p>Casos de uso: CU-37
     * @param pago Pago que respalda.
     */
    public void restaurarAsociaciones(Pago pago) {
        this.pago = pago;
    }

    /**
     * Indica si es una constancia de acreditación (el pago quedó ACREDITADO como saldo a favor) en lugar de un
     * comprobante de pago. Agregado en la implementación (lo puede usar {@code DetalleComprobante}).
     *
     * <p>Casos de uso: CU-37
     * @return true si el pago está acreditado
     */
    public boolean esConstanciaAcreditacion() {
        return pago != null && pago.getEstado() instanceof EstadoPagoAcreditado;
    }

    /**
     * Devuelve {@code pago}: Pago que respalda.
     *
     * <p>Casos de uso: CU-37
     * @return el valor del atributo
     */
    public Pago getPago() {
        return pago;
    }

    /**
     * Devuelve {@code numero}: Número correlativo.
     *
     * <p>Casos de uso: CU-37
     * @return el valor del atributo
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Devuelve {@code fechaEmision}: Momento de emisión.
     *
     * <p>Casos de uso: CU-37
     * @return el valor del atributo
     */
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Devuelve {@code importe}: Importe pagado.
     *
     * <p>Casos de uso: CU-37
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }
}

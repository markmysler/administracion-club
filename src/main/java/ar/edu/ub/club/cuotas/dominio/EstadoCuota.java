package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.time.LocalDate;

/**
 * Clase abstracta del patrón State para {@code Cuota}, con subclases {@code EstadoCuotaPendiente}, {@code
 * EstadoCuotaVencida} y {@code EstadoCuotaPagada}. Las operaciones tienen un comportamiento por omisión que
 * lanza {@code TransicionInvalidaException}; cada estado redefine solo las transiciones que admite
 * (diagrama de estados {@code est_cuota}: Pendiente → Pagada | Vencida; Vencida → Pagada).
 *
 * <p>Los estados no tienen datos propios: cada subclase tiene una instancia única ({@code INSTANCIA}) y en la
 * base se guarda solo su {@code codigo()}.
 *
 * <p>Casos de uso: CU-05, CU-35, CU-36, CU-39; tareas programadas: emisión mensual de cuotas, vencimiento de cuotas
 */
public abstract class EstadoCuota {

    /**
     * Transición a Pagada. Por omisión la transición no está permitida (una cuota ya pagada no se vuelve a pagar,
     * RN-18).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param cuota Contexto del patrón State (entrada-salida).
     * @param pago Pago aprobado que cancela la cuota.
     * @throws TransicionInvalidaException si el estado actual no admite el pago
     */
    public void registrarPagoAprobado(Cuota cuota, Pago pago) {
        throw new TransicionInvalidaException("La cuota del período " + cuota.getPeriodo()
                + " no admite registrar un pago en estado " + codigo() + ".");
    }

    /**
     * Transición a Vencida. Por omisión la transición no está permitida (solo una cuota pendiente vence).
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @param cuota Contexto del patrón State (entrada-salida).
     * @param hoy Fecha de proceso.
     * @throws TransicionInvalidaException si el estado actual no puede vencer
     */
    public void vencer(Cuota cuota, LocalDate hoy) {
        throw new TransicionInvalidaException("La cuota del período " + cuota.getPeriodo()
                + " no puede vencer en estado " + codigo() + ".");
    }

    /**
     * Indica si la cuota admite un pago: true en Pendiente y Vencida (las subclases lo redefinen); por omisión
     * false (RN-18).
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return true si admite pago.
     */
    public boolean admitePago() {
        return false;
    }

    /**
     * Código persistido en {@code cuota.estado}.
     *
     * <p>Casos de uso: todos
     * @return Pendiente, VENCIDA o PAGADA.
     */
    public abstract String codigo();

    /**
     * Devuelve la instancia única del estado del código; un código desconocido lanza {@code IllegalStateException}.
     * La usa {@code BrokerCuota.crearObjeto}.
     *
     * <p>Casos de uso: todos
     * @param codigo Código guardado en la base.
     * @return Estado concreto (instancia única).
     * @throws IllegalStateException si el código no corresponde a ningún estado
     */
    public static EstadoCuota desdeCodigo(String codigo) {
        String limpio = codigo == null ? "" : codigo.trim();
        return switch (limpio) {
            case EstadoCuotaPendiente.CODIGO -> EstadoCuotaPendiente.INSTANCIA;
            case EstadoCuotaVencida.CODIGO -> EstadoCuotaVencida.INSTANCIA;
            case EstadoCuotaPagada.CODIGO -> EstadoCuotaPagada.INSTANCIA;
            default -> throw new IllegalStateException("Estado de cuota desconocido: " + codigo);
        };
    }

    /**
     * Representación legible del estado (su código), útil en la bitácora y en las pruebas.
     *
     * <p>Casos de uso: todos
     * @return el código del estado
     */
    @Override
    public String toString() {
        return codigo();
    }
}

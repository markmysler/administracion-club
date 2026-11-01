package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.pagos.dominio.Pago;
import java.time.LocalDate;

/**
 * Clase abstracta del patrón State para {@code Cuota}, con subclases {@code EstadoCuotaPendiente}, {@code
 * EstadoCuotaVencida} y {@code EstadoCuotaPagada}. Las operaciones tienen un comportamiento por omisión que
 * lanza {@code TransicionInvalidaException}; cada estado redefine solo las transiciones que admite.
 *
 * <p>Casos de uso: PENDIENTE
 */
public abstract class EstadoCuota {

    /**
     * Transición a Pagada.
     *
     * <p>Casos de uso: PENDIENTE
     * @param cuota valor de {@code cuota}
     * @param pago Pago.
     */
    public void registrarPagoAprobado(Cuota cuota, Pago pago) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Transición a Vencida.
     *
     * <p>Casos de uso: PENDIENTE
     * @param cuota valor de {@code cuota}
     * @param hoy Fecha.
     */
    public void vencer(Cuota cuota, LocalDate hoy) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * true en Pendiente y Vencida.
     *
     * <p>Casos de uso: PENDIENTE
     * @return true si admite pago.
     */
    public boolean admitePago() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Código persistido.
     *
     * <p>Casos de uso: PENDIENTE
     * @return PENDIENTE, VENCIDA o PAGADA.
     */
    public abstract String codigo();

    /**
     * Devuelve la instancia única del estado del código; un código desconocido lanza {@code IllegalStateException}.
     * La usa {@code BrokerCuota.crearObjeto}.
     *
     * <p>Casos de uso: PENDIENTE
     * @param codigo valor de {@code codigo}
     * @return el resultado
     */
    public static EstadoCuota desdeCodigo(String codigo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

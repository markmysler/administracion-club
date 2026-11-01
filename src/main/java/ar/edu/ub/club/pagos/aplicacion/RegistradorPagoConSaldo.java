package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.CuotaCubiertaConSaldoEvento;
import ar.edu.ub.club.pagos.dominio.DatosPago;
import ar.edu.ub.club.pagos.dominio.GeneradorComprobante;
import ar.edu.ub.club.pagos.dominio.Pago;
import ar.edu.ub.club.pagos.dominio.ProcesadorPagoConSaldo;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Observador (patrón Observer) de {@code CuotaCubiertaConSaldoEvento}, publicado por {@code ServicioCuotas} al
 * emitir una cuota cubierta completamente por el saldo a favor. Se ejecuta de forma sincrónica dentro de la
 * transacción de emisión ({@code &#64;EventListener}). Gracias a él, el módulo cuotas no depende de pagos.
 *
 * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
@Component
public class RegistradorPagoConSaldo {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code CuotasImpagasDeGrupo}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Creador de pagos con saldo (Factory Method). */
    private final ProcesadorPagoConSaldo procesadorConSaldo;

    /** Comprobante de la cuota cancelada con saldo. */
    private final GeneradorComprobante generadorComprobante;

    /**
     * Crea el observador.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param fachadaPersistencia fachada de persistencia
     * @param procesadorConSaldo creador de pagos con saldo
     * @param generadorComprobante generador de comprobantes
     */
    public RegistradorPagoConSaldo(FachadaPersistencia fachadaPersistencia, ProcesadorPagoConSaldo procesadorConSaldo,
            GeneradorComprobante generadorComprobante) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.procesadorConSaldo = procesadorConSaldo;
        this.generadorComprobante = generadorComprobante;
    }

    /**
     * {@code procesadorConSaldo.registrar(cuota, datos)} crea el {@code PagoConSaldo} aprobado, {@code
     * cuota.registrarPagoAprobado(pago)} deja la cuota PAGADA y {@code generadorComprobante.generar(pago)} emite el
     * comprobante (sec_tmp_emision_p2).
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param evento Cuota cubierta, grupo e importe.
     */
    @EventListener
    public void alCubrirCuotaConSaldo(CuotaCubiertaConSaldoEvento evento) {
        Cuota cuota = fachadaPersistencia.get(evento.getIdCuota(), Cuota.class);
        Pago pago = procesadorConSaldo.registrar(cuota,
                new DatosPago(evento.getImporte(), evento.getFechaHora(), null, null, null));
        cuota.registrarPagoAprobado(pago);
        fachadaPersistencia.put(cuota);
        generadorComprobante.generar(pago);
    }
}

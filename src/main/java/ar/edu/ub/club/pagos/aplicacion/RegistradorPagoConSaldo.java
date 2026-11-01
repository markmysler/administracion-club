package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.cuotas.dominio.CuotaCubiertaConSaldoEvento;
import ar.edu.ub.club.pagos.dominio.GeneradorComprobante;
import ar.edu.ub.club.pagos.dominio.ProcesadorPagoConSaldo;

/**
 * Observador (patrón Observer) de {@code CuotaCubiertaConSaldoEvento}, publicado por {@code ServicioCuotas} al
 * emitir una cuota cubierta completamente por el saldo a favor. Se ejecuta de forma sincrónica dentro de la
 * transacción de emisión ({@code &#64;EventListener}). Gracias a él, el módulo cuotas no depende de pagos.
 *
 * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
 */
public class RegistradorPagoConSaldo {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code CuotasImpagasDeGrupo}). */
    private FachadaPersistencia fachadaPersistencia;

    /** Creador de pagos con saldo (Factory Method). */
    private ProcesadorPagoConSaldo procesadorConSaldo;

    /** Comprobante de la cuota cancelada con saldo. */
    private GeneradorComprobante generadorComprobante;

    /**
     * {@code procesadorConSaldo.registrar(cuota, datos)} crea el {@code PagoConSaldo} aprobado, {@code
     * cuota.registrarPagoAprobado(pago)} deja la cuota PAGADA y {@code generadorComprobante.generar(pago)} emite el
     * comprobante.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param evento Cuota cubierta, grupo e importe.
     */
    public void alCubrirCuotaConSaldo(CuotaCubiertaConSaldoEvento evento) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

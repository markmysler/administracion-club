package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.cuotas.dominio.Cuota;

/**
 * Creador abstracto del patrón Factory Method (pagos.dominio). Junto con {@code GeneradorComprobante} es la
 * única clase de dominio con acceso a {@code FachadaPersistencia}: el método plantilla {@code registrar} crea y
 * guarda el pago en un solo paso, para que ninguna fachada pueda crear un pago sin guardarlo. Creadores
 * concretos {@code ProcesadorPagoOnline} (crea {@code PagoOnline} PENDIENTE), {@code ProcesadorPagoManual} (crea
 * {@code PagoManual} aprobado) y {@code ProcesadorPagoConSaldo} (crea {@code PagoConSaldo} aprobado).
 *
 * <p>Casos de uso: PENDIENTE
 */
public abstract class ProcesadorPago {

    /** Único punto de acceso a la persistencia («Singleton»): guarda el pago creado con {@code put(pago)}. */
    private FachadaPersistencia fachadaPersistencia;

    /**
     * Método plantilla: verifica {@code cuota.admitePago()}, llama al método fábrica {@code crearPago} y guarda el
     * pago.
     *
     * <p>Casos de uso: PENDIENTE
     * @param cuota Cuota.
     * @param datos Datos del pago.
     * @return Pago creado.
     */
    public Pago registrar(Cuota cuota, DatosPago datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Método fábrica abstracto protegido que redefine cada creador concreto.
     *
     * <p>Casos de uso: PENDIENTE
     * @param cuota Cuota.
     * @param datos Datos.
     * @return Pago de la subclase concreta.
     */
    protected abstract Pago crearPago(Cuota cuota, DatosPago datos);
}

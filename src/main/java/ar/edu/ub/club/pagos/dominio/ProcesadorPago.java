package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.cuotas.dominio.Cuota;

/**
 * Creador abstracto del patrón Factory Method (pagos.dominio). Junto con {@code GeneradorComprobante} es la
 * única clase de dominio con acceso a {@code FachadaPersistencia}: el método plantilla {@code registrar} crea y
 * guarda el pago en un solo paso, para que ninguna fachada pueda crear un pago sin guardarlo. Creadores
 * concretos {@code ProcesadorPagoOnline} (crea {@code PagoOnline} Pendiente), {@code ProcesadorPagoManual} (crea
 * {@code PagoManual} aprobado) y {@code ProcesadorPagoConSaldo} (crea {@code PagoConSaldo} aprobado).
 *
 * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public abstract class ProcesadorPago {

    /** Único punto de acceso a la persistencia («Singleton»): guarda el pago creado con {@code put(pago)}. */
    private final FachadaPersistencia fachadaPersistencia;

    /**
     * Constructor común de los creadores concretos.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param fachadaPersistencia fachada de persistencia
     */
    protected ProcesadorPago(FachadaPersistencia fachadaPersistencia) {
        this.fachadaPersistencia = fachadaPersistencia;
    }

    /**
     * Método plantilla: verifica {@code cuota.admitePago()}, llama al método fábrica {@code crearPago} y guarda el
     * pago (sec_cu35_p4).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param cuota Cuota.
     * @param datos Datos del pago.
     * @return Pago creado y guardado.
     * @throws ErrorNegocio si la cuota ya está pagada (RN-18)
     */
    public final Pago registrar(Cuota cuota, DatosPago datos) {
        if (!cuota.admitePago()) {
            throw new ErrorNegocio("La cuota ya fue pagada.");
        }
        Pago pago = crearPago(cuota, datos);
        fachadaPersistencia.put(pago);
        return pago;
    }

    /**
     * Método fábrica abstracto protegido que redefine cada creador concreto.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param cuota Cuota.
     * @param datos Datos.
     * @return Pago de la subclase concreta.
     */
    protected abstract Pago crearPago(Cuota cuota, DatosPago datos);
}

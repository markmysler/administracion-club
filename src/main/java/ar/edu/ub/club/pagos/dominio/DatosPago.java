package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;

/**
 * Objeto de datos que reciben los creadores concretos ({@code ProcesadorPago.registrar(cuota, datos)}). Cada
 * creador usa solo los datos que le corresponden: medio y administrador el manual, titular el online.
 *
 * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class DatosPago {

    /** Importe. */
    private final Dinero importe;

    /** Fecha del pago. */
    private final LocalDateTime fecha;

    /** Medio (pago manual). */
    private final MedioPago medio;

    /** Administrador (pago manual). */
    private final Administrador registradoPor;

    /** Titular (pago online). */
    private final Socio iniciadoPor;

    /**
     * Agregado en la implementación (el modelo no lista el constructor): crea los datos del pago.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param importe Importe.
     * @param fecha Momento.
     * @param medio Medio (solo pago manual) o null.
     * @param registradoPor Administrador (solo pago manual) o null.
     * @param iniciadoPor Titular (solo pago online) o null.
     */
    public DatosPago(Dinero importe, LocalDateTime fecha, MedioPago medio, Administrador registradoPor, Socio iniciadoPor) {
        this.importe = importe;
        this.fecha = fecha;
        this.medio = medio;
        this.registradoPor = registradoPor;
        this.iniciadoPor = iniciadoPor;
    }

    /**
     * Devuelve {@code importe}: Importe.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code fecha}: Fecha del pago.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return el valor del atributo
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Devuelve {@code medio}: Medio (pago manual).
     *
     * <p>Casos de uso: CU-36
     * @return el valor del atributo
     */
    public MedioPago getMedio() {
        return medio;
    }

    /**
     * Devuelve {@code registradoPor}: Administrador (pago manual).
     *
     * <p>Casos de uso: CU-36
     * @return el valor del atributo
     */
    public Administrador getRegistradoPor() {
        return registradoPor;
    }

    /**
     * Devuelve {@code iniciadoPor}: Titular (pago online).
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public Socio getIniciadoPor() {
        return iniciadoPor;
    }
}

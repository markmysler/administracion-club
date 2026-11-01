package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;

/**
 * Objeto de datos que reciben los creadores concretos.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class DatosPago {

    /** Importe. */
    private Dinero importe;

    /** Fecha del pago. */
    private LocalDateTime fecha;

    /** Medio (pago manual). */
    private MedioPago medio;

    /** Administrador (pago manual). */
    private Administrador registradoPor;

    /** Titular (pago online). */
    private Socio iniciadoPor;

    /**
     * Devuelve {@code importe}: Importe.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code fecha}: Fecha del pago.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Devuelve {@code medio}: Medio (pago manual).
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public MedioPago getMedio() {
        return medio;
    }

    /**
     * Devuelve {@code registradoPor}: Administrador (pago manual).
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Administrador getRegistradoPor() {
        return registradoPor;
    }

    /**
     * Devuelve {@code iniciadoPor}: Titular (pago online).
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Socio getIniciadoPor() {
        return iniciadoPor;
    }
}

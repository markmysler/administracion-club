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
}

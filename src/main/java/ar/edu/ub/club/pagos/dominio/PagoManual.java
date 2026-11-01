package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.time.LocalDateTime;

/**
 * Pago registrado por un administrador con un medio manual (CU-36). Lo crea {@code ProcesadorPagoManual} ya
 * aprobado.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: CU-36
 */
public class PagoManual extends Pago {

    /** Medio utilizado. */
    private MedioPago medio;

    /** Administrador que registró el pago. */
    private Administrador registradoPor;

    /**
     * Devuelve "MANUAL".
     *
     * <p>Casos de uso: CU-36
     * @return el resultado
     */
    public String origen() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code medio.nombre}.
     *
     * <p>Casos de uso: CU-36
     * @return el resultado
     */
    public String descripcionMedio() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @param id valor de {@code id}
     * @param fecha valor de {@code fecha}
     * @param importe valor de {@code importe}
     * @param estado valor de {@code estado}
     * @param motivoRechazo valor de {@code motivoRechazo}
     * @return el resultado
     */
    public static PagoManual reconstituir(Long id, LocalDateTime fecha, Dinero importe, EstadoPago estado, String motivoRechazo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @param cuota valor de {@code cuota}
     * @param medio valor de {@code medio}
     * @param registradoPor valor de {@code registradoPor}
     * @param comprobante valor de {@code comprobante}
     */
    public void restaurarAsociaciones(Cuota cuota, MedioPago medio, Administrador registradoPor, Comprobante comprobante) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code medio}: Medio utilizado.
     *
     * <p>Casos de uso: CU-36
     * @return el valor del atributo
     */
    public MedioPago getMedio() {
        return medio;
    }

    /**
     * Devuelve {@code registradoPor}: Administrador que registró el pago.
     *
     * <p>Casos de uso: CU-36
     * @return el valor del atributo
     */
    public Administrador getRegistradoPor() {
        return registradoPor;
    }
}

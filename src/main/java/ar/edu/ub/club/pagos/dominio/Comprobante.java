package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDateTime;

/**
 * Comprobante de un pago aprobado o constancia de acreditación de un pago acreditado, con numeración
 * correlativa. Extiende {@code EntidadPersistente}; es inmutable.
 *
 * <p>Casos de uso: todos
 */
public class Comprobante extends EntidadPersistente {

    /** Pago que respalda. */
    private Pago pago;

    /** Número correlativo. */
    private String numero;

    /** Momento de emisión. */
    private LocalDateTime fechaEmision;

    /** Importe pagado. */
    private Dinero importe;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param numero valor de {@code numero}
     * @param fechaEmision valor de {@code fechaEmision}
     * @param importe valor de {@code importe}
     * @return el resultado
     */
    public static Comprobante reconstituir(Long id, String numero, LocalDateTime fechaEmision, Dinero importe) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param pago valor de {@code pago}
     */
    public void restaurarAsociaciones(Pago pago) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

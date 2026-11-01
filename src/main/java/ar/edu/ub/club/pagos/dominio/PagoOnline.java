package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.pagos.integracion.ResultadoPasarela;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Pago iniciado por el titular a través de la pasarela (CU-35).
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
 */
public class PagoOnline extends Pago {

    /** Referencia enviada a la pasarela. */
    private UUID referenciaExterna;

    /** Identificador informado por la pasarela. */
    private String idPagoPasarela;

    /** Resultado consultado (JSON, sin datos de tarjeta). */
    private String respuestaPasarela;

    /** Titular que inició el pago (el titular del grupo puede cambiar después). */
    private Socio iniciadoPor;

    /**
     * Crea una instancia de {@code PagoOnline}.
     *
     * <p>Casos de uso: CU-35
     * @param cuota valor de {@code cuota}
     * @param importe valor de {@code importe}
     * @param referenciaExterna valor de {@code referenciaExterna}
     * @param fecha valor de {@code fecha}
     */
    public PagoOnline(Cuota cuota, Dinero importe, UUID referenciaExterna, LocalDateTime fecha) {
        // PENDIENTE
    }

    /**
     * Guarda {@code idPagoPasarela} y {@code respuestaPasarela}.
     *
     * <p>Casos de uso: CU-35
     * @param resultado Resultado consultado a la pasarela.
     */
    public void registrarRespuestaPasarela(ResultadoPasarela resultado) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve "ONLINE".
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @return el resultado
     */
    public String origen() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve "Mercado Pago".
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @return el resultado
     */
    public String descripcionMedio() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param id valor de {@code id}
     * @param fecha valor de {@code fecha}
     * @param importe valor de {@code importe}
     * @param estado valor de {@code estado}
     * @param motivoRechazo valor de {@code motivoRechazo}
     * @param referenciaExterna valor de {@code referenciaExterna}
     * @param idPagoPasarela valor de {@code idPagoPasarela}
     * @param respuestaPasarela valor de {@code respuestaPasarela}
     * @return el resultado
     */
    public static PagoOnline reconstituir(Long id, LocalDateTime fecha, Dinero importe, EstadoPago estado, String motivoRechazo, UUID referenciaExterna, String idPagoPasarela, String respuestaPasarela) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param cuota valor de {@code cuota}
     * @param iniciadoPor valor de {@code iniciadoPor}
     * @param comprobante valor de {@code comprobante}
     */
    public void restaurarAsociaciones(Cuota cuota, Socio iniciadoPor, Comprobante comprobante) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

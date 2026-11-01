package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.pagos.integracion.ResultadoPasarela;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Pago iniciado por el titular a través de la pasarela (CU-35). Nace Pendiente con una referencia externa única
 * (UUID) que viaja a la pasarela y vuelve en el resultado y en {@code /pagos/retorno}.
 *
 * <p>Entidad del dominio; la persiste su broker ({@code BrokerPagoOnline}: tablas {@code pago} + {@code pago_online}).
 *
 * <p>Casos de uso: CU-35, CU-37; tareas programadas: vencimiento de pagos online pendientes
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
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     */
    private PagoOnline() {
    }

    /**
     * Crea el pago online Pendiente (lo llama {@code ProcesadorPagoOnline.crearPago}).
     *
     * <p>Casos de uso: CU-35
     * @param cuota Cuota que se paga.
     * @param importe Importe a pagar de la cuota.
     * @param referenciaExterna UUID nuevo.
     * @param fecha Momento de inicio.
     */
    public PagoOnline(Cuota cuota, Dinero importe, UUID referenciaExterna, LocalDateTime fecha) {
        super(cuota, importe, fecha, EstadoPagoPendiente.INSTANCIA);
        this.referenciaExterna = referenciaExterna;
    }

    /**
     * Guarda {@code idPagoPasarela} y {@code respuestaPasarela} (JSON con el identificador, el estado y el importe
     * informados; nunca datos de tarjeta, RNF-05).
     *
     * <p>Casos de uso: CU-35
     * @param resultado Resultado consultado a la pasarela.
     */
    public void registrarRespuestaPasarela(ResultadoPasarela resultado) {
        if (resultado.getIdPagoPasarela() != null) {
            this.idPagoPasarela = resultado.getIdPagoPasarela();
        }
        this.respuestaPasarela = "{\"idPagoPasarela\": " + json(resultado.getIdPagoPasarela())
                + ", \"estado\": " + json(resultado.getEstado())
                + ", \"importe\": " + (resultado.getImporte() == null ? "null" : resultado.getImporte().getMonto().toPlainString())
                + ", \"motivo\": " + json(resultado.getMotivo()) + "}";
    }

    /**
     * Devuelve "ONLINE".
     *
     * <p>Casos de uso: CU-37
     * @return "ONLINE"
     */
    @Override
    public String origen() {
        return "ONLINE";
    }

    /**
     * Devuelve "Mercado Pago".
     *
     * <p>Casos de uso: CU-37
     * @return "Mercado Pago"
     */
    @Override
    public String descripcionMedio() {
        return "Mercado Pago";
    }

    /**
     * Reconstituye un pago online leído de {@code pago} + {@code pago_online} (operación «persistencia»).
     *
     * <p>Casos de uso: CU-35, CU-37
     * @param id Identificador.
     * @param fecha Momento.
     * @param importe Importe.
     * @param estado Estado.
     * @param motivoRechazo Motivo.
     * @param referenciaExterna Referencia.
     * @param idPagoPasarela Identificador en la pasarela.
     * @param respuestaPasarela JSON de la respuesta.
     * @return el pago sin asociaciones
     */
    public static PagoOnline reconstituir(Long id, LocalDateTime fecha, Dinero importe, EstadoPago estado, String motivoRechazo, UUID referenciaExterna, String idPagoPasarela, String respuestaPasarela) {
        PagoOnline p = new PagoOnline();
        p.restaurarDatos(id, fecha, importe, estado, motivoRechazo);
        p.referenciaExterna = referenciaExterna;
        p.idPagoPasarela = idPagoPasarela;
        p.respuestaPasarela = respuestaPasarela;
        return p;
    }

    /**
     * Entrega las asociaciones resueltas por {@code BrokerPagoOnline}.
     *
     * <p>Casos de uso: CU-35, CU-37
     * @param cuota Cuota.
     * @param iniciadoPor Titular que inició el pago.
     * @param comprobante Comprobante o null.
     */
    public void restaurarAsociaciones(Cuota cuota, Socio iniciadoPor, Comprobante comprobante) {
        restaurarCuotaYComprobante(cuota, comprobante);
        this.iniciadoPor = iniciadoPor;
    }

    /**
     * Registra el titular que inicia el pago (lo llama {@code ProcesadorPagoOnline.crearPago}).
     *
     * <p>Casos de uso: CU-35
     * @param titular Titular de la sesión.
     */
    void asignarIniciadoPor(Socio titular) {
        this.iniciadoPor = titular;
    }

    /**
     * Texto como valor JSON (entre comillas y con escapes) o {@code null}.
     *
     * <p>Casos de uso: CU-35
     * @param texto Texto.
     * @return valor JSON
     */
    private static String json(String texto) {
        if (texto == null) {
            return "null";
        }
        return "\"" + texto.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    /**
     * Devuelve {@code referenciaExterna}: Referencia enviada a la pasarela.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public UUID getReferenciaExterna() {
        return referenciaExterna;
    }

    /**
     * Devuelve {@code idPagoPasarela}: Identificador informado por la pasarela.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getIdPagoPasarela() {
        return idPagoPasarela;
    }

    /**
     * Devuelve {@code respuestaPasarela}: Resultado consultado (JSON, sin datos de tarjeta).
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getRespuestaPasarela() {
        return respuestaPasarela;
    }

    /**
     * Devuelve {@code iniciadoPor}: Titular que inició el pago (el titular del grupo puede cambiar después).
     *
     * <p>Casos de uso: CU-35, CU-37
     * @return el valor del atributo
     */
    public Socio getIniciadoPor() {
        return iniciadoPor;
    }
}

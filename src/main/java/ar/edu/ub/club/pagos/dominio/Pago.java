package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.time.LocalDateTime;

/**
 * Clase abstracta especializada en {@code PagoOnline}, {@code PagoManual} y {@code PagoConSaldo}, creada por los
 * {@code ProcesadorPago} (Factory Method). Contexto del patrón State: los estados concretos cambian su estado
 * con {@code ~cambiarEstado(nuevo)}.
 *
 * <p>Entidad del dominio; la persiste su broker ({@code BrokerPago} y sus subclases, herencia JOINED).
 *
 * <p>Casos de uso: CU-35, CU-36, CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor), vencimiento de pagos online pendientes
 */
public abstract class Pago extends EntidadPersistente {

    /** Longitud máxima del motivo ({@code VARCHAR(100)}). */
    private static final int LARGO_MOTIVO = 100;

    /** Cuota que se paga. */
    private Cuota cuota;

    /** Momento del pago. */
    private LocalDateTime fecha;

    /** Importe. */
    private Dinero importe;

    /** Estado actual. */
    private EstadoPago estado;

    /** Motivo del rechazo informado por la pasarela o «Abandonado» del vencimiento (RN-22). */
    private String motivoRechazo;

    /** Comprobante emitido; lo asigna {@code GeneradorComprobante.generar} con {@code asociarComprobante(c)}. */
    private Comprobante comprobante;

    /**
     * Agregado en la implementación: constructor vacío para el {@code reconstituir} de las subclases.
     *
     * <p>Casos de uso: todos
     */
    protected Pago() {
    }

    /**
     * Agregado en la implementación: constructor común de las subclases para un pago nuevo.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param cuota Cuota que se paga.
     * @param importe Importe (mayor que 0).
     * @param fecha Momento del pago.
     * @param estadoInicial Pendiente (online) o Aprobado (manual y con saldo).
     * @throws IllegalArgumentException si el importe no es positivo
     */
    protected Pago(Cuota cuota, Dinero importe, LocalDateTime fecha, EstadoPago estadoInicial) {
        if (importe == null || !importe.esPositivo()) {
            throw new IllegalArgumentException("El importe del pago debe ser mayor que 0");
        }
        this.cuota = cuota;
        this.importe = importe;
        this.fecha = fecha;
        this.estado = estadoInicial;
    }

    /**
     * Delega en el estado: Pendiente o Vencido → Aprobado.
     *
     * <p>Casos de uso: CU-35
     * @param ahora Momento de la aprobación.
     */
    public void aprobar(LocalDateTime ahora) {
        estado.aprobar(this, ahora);
    }

    /**
     * Delega en el estado: Pendiente → Rechazado.
     *
     * <p>Casos de uso: CU-35
     * @param motivo Motivo informado por la pasarela.
     */
    public void rechazar(String motivo) {
        estado.rechazar(this, motivo);
    }

    /**
     * Delega en el estado: Pendiente o Vencido → Acreditado, cuando el pago se aprueba pero la cuota ya está pagada
     * (RN-21).
     *
     * <p>Casos de uso: CU-35
     * @param ahora Momento de la acreditación.
     */
    public void acreditar(LocalDateTime ahora) {
        estado.acreditar(this, ahora);
    }

    /**
     * Operación abstracta: {@code PagoOnline} devuelve "ONLINE", {@code PagoManual} "MANUAL" y {@code PagoConSaldo}
     * "CON_SALDO". La usan {@code FilaPago} y {@code DetalleComprobante} sin preguntar por la clase.
     *
     * <p>Casos de uso: CU-37
     * @return Origen del pago.
     */
    public abstract String origen();

    /**
     * Medio para mostrar: "Mercado Pago" en {@code PagoOnline}, "Saldo a favor" en {@code PagoConSaldo} y el nombre
     * del medio en {@code PagoManual}. Cada subclase lo redefine; aquí se devuelve el origen como valor por omisión.
     *
     * <p>Casos de uso: CU-37
     * @return Descripción del medio.
     */
    public String descripcionMedio() {
        return origen();
    }

    /**
     * Asigna el comprobante del pago (lo llama {@code GeneradorComprobante.generar}).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param c Comprobante emitido.
     */
    public void asociarComprobante(Comprobante c) {
        this.comprobante = c;
    }

    /**
     * Devuelve el comprobante del pago.
     *
     * <p>Casos de uso: CU-35, CU-36, CU-37
     * @return Comprobante o null si no tiene (pendiente, rechazado o vencido).
     */
    public Comprobante getComprobante() {
        return comprobante;
    }

    /**
     * Reemplaza el objeto estado (patrón State). Solo lo invocan los estados concretos.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param nuevo Estado destino.
     */
    void cambiarEstado(EstadoPago nuevo) {
        this.estado = nuevo;
    }

    /**
     * Delega en el estado: Pendiente → Vencido con el motivo recibido («Abandonado», RN-22).
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param motivo Motivo del vencimiento; se guarda en {@code motivoRechazo}.
     */
    public void vencer(String motivo) {
        estado.vencer(this, motivo);
    }

    /**
     * Guarda el motivo del rechazo o del vencimiento (lo usan los estados concretos), recortado a 100 caracteres.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param motivo Motivo o null para borrarlo.
     */
    void asignarMotivo(String motivo) {
        this.motivoRechazo = motivo == null || motivo.length() <= LARGO_MOTIVO ? motivo : motivo.substring(0, LARGO_MOTIVO);
    }

    /**
     * Registra el momento de la aprobación o acreditación como fecha del pago (lo usan los estados concretos).
     *
     * <p>Casos de uso: CU-35
     * @param ahora Momento.
     */
    void registrarMomento(LocalDateTime ahora) {
        if (ahora != null) {
            this.fecha = ahora;
        }
    }

    /**
     * Agregado en la implementación: carga los datos comunes al reconstituir una subclase (operación
     * «persistencia» que usan {@code PagoOnline.reconstituir}, {@code PagoManual.reconstituir} y {@code
     * PagoConSaldo.reconstituir}).
     *
     * <p>Casos de uso: todos
     * @param id Identificador.
     * @param fecha Momento.
     * @param importe Importe.
     * @param estado Estado.
     * @param motivoRechazo Motivo.
     */
    protected void restaurarDatos(Long id, LocalDateTime fecha, Dinero importe, EstadoPago estado, String motivoRechazo) {
        asignarId(id);
        this.fecha = fecha;
        this.importe = importe;
        this.estado = estado;
        this.motivoRechazo = motivoRechazo;
    }

    /**
     * Agregado en la implementación: entrega la cuota y el comprobante resueltos por el broker (lo usan los
     * {@code restaurarAsociaciones} de las subclases).
     *
     * <p>Casos de uso: todos
     * @param cuota Cuota del pago.
     * @param comprobante Comprobante o null.
     */
    protected void restaurarCuotaYComprobante(Cuota cuota, Comprobante comprobante) {
        this.cuota = cuota;
        this.comprobante = comprobante;
    }

    /**
     * Devuelve {@code cuota}: Cuota que se paga.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Cuota getCuota() {
        return cuota;
    }

    /**
     * Devuelve {@code fecha}: Momento del pago.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Devuelve {@code importe}: Importe.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code estado}: Estado actual.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public EstadoPago getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code motivoRechazo}: Motivo del rechazo informado por la pasarela o «Abandonado» del vencimiento (RN-22).
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getMotivoRechazo() {
        return motivoRechazo;
    }
}

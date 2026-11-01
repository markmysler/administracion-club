package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDateTime;

/**
 * Entidad que registra un crédito (por un pago ACREDITADO) o un débito (al aplicar saldo a una cuota) del saldo
 * a favor de un grupo.
 *
 * <p>Casos de uso: todos
 */
public class MovimientoSaldo extends EntidadPersistente {

    /** Grupo dueño del saldo (referencia por id). */
    private Long idGrupo;

    /** Momento del movimiento. */
    private LocalDateTime fecha;

    /** Crédito o débito. */
    private TipoMovimientoSaldo tipo;

    /** Importe del movimiento. */
    private Dinero importe;

    /** Descripción. */
    private String motivo;

    /** Identificador del pago que origina el crédito (referencia por id: cuotas no depende de pagos). */
    private Long idPago;

    /** Cuota a la que se aplica el débito. */
    private Cuota cuota;

    /**
     * Crea una instancia de {@code MovimientoSaldo}.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @param tipo valor de {@code tipo}
     * @param importe valor de {@code importe}
     * @param cuota valor de {@code cuota}
     */
    public MovimientoSaldo(TipoMovimientoSaldo tipo, Dinero importe, Cuota cuota) {
        this.tipo = tipo;
        this.importe = importe;
        this.cuota = cuota;
    }

    /**
     * Crea una instancia de {@code MovimientoSaldo}.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @param tipo valor de {@code tipo}
     * @param importe valor de {@code importe}
     * @param idPago valor de {@code idPago}
     */
    public MovimientoSaldo(TipoMovimientoSaldo tipo, Dinero importe, Long idPago) {
        this.tipo = tipo;
        this.importe = importe;
        this.idPago = idPago;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param idGrupo valor de {@code idGrupo}
     * @param fecha valor de {@code fecha}
     * @param tipo valor de {@code tipo}
     * @param importe valor de {@code importe}
     * @param motivo valor de {@code motivo}
     * @param idPago valor de {@code idPago}
     * @return el resultado
     */
    public static MovimientoSaldo reconstituir(Long id, Long idGrupo, LocalDateTime fecha, TipoMovimientoSaldo tipo, Dinero importe, String motivo, Long idPago) {
        MovimientoSaldo m = new MovimientoSaldo(tipo, importe, idPago);
        m.asignarId(id);
        m.idGrupo = idGrupo;
        m.fecha = fecha;
        m.motivo = motivo;
        return m;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param cuota valor de {@code cuota}
     */
    public void restaurarAsociaciones(Cuota cuota) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code idGrupo}: Grupo dueño del saldo (referencia por id).
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Long getIdGrupo() {
        return idGrupo;
    }

    /**
     * Devuelve {@code fecha}: Momento del movimiento.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Devuelve {@code tipo}: Crédito o débito.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public TipoMovimientoSaldo getTipo() {
        return tipo;
    }

    /**
     * Devuelve {@code importe}: Importe del movimiento.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code motivo}: Descripción.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Devuelve {@code idPago}: Identificador del pago que origina el crédito (referencia por id: cuotas no depende de pagos).
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Long getIdPago() {
        return idPago;
    }

    /**
     * Devuelve {@code cuota}: Cuota a la que se aplica el débito.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Cuota getCuota() {
        return cuota;
    }
}

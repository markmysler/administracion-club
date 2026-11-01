package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDateTime;

/**
 * Entidad que registra un crédito (por un pago ACREDITADO) o un débito (al aplicar saldo a una cuota) del saldo
 * a favor de un grupo. El saldo nunca se guarda: es la suma de créditos menos la de débitos (RN-21).
 *
 * <p>Casos de uso: CU-05, CU-35, CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class MovimientoSaldo extends EntidadPersistente {

    /** Longitud máxima del motivo ({@code VARCHAR(100)}). */
    private static final int LARGO_MOTIVO = 100;

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
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     */
    private MovimientoSaldo() {
    }

    /**
     * Crea el DEBITO del saldo aplicado a una cuota (en la emisión o en {@code aplicarSaldoAPendientes}); el grupo
     * es el de la cuota y la fecha, el momento del registro en la zona del club.
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param tipo Debe ser DEBITO (el CHECK de la base exige cuota en el débito).
     * @param importe Importe aplicado (mayor que 0).
     * @param cuota Cuota a la que se aplica.
     * @throws IllegalArgumentException si el tipo no es DEBITO o el importe no es positivo
     */
    public MovimientoSaldo(TipoMovimientoSaldo tipo, Dinero importe, Cuota cuota) {
        validar(tipo, TipoMovimientoSaldo.DEBITO, importe);
        this.tipo = tipo;
        this.importe = importe;
        this.cuota = cuota;
        this.idGrupo = cuota.getIdGrupo();
        this.fecha = LocalDateTime.now(RelojSistema.ZONA_CLUB);
        this.motivo = recortar("Saldo a favor aplicado a la cuota " + cuota.getPeriodo());
    }

    /**
     * Crea el CREDITO de un pago ACREDITADO (RN-21); el grupo lo asigna {@code ServicioCuotas.registrarCredito}
     * con {@code asignarGrupo}.
     *
     * <p>Casos de uso: CU-35
     * @param tipo Debe ser CREDITO (el CHECK de la base exige pago en el crédito).
     * @param importe Importe acreditado (mayor que 0).
     * @param idPago Pago que origina el crédito.
     * @throws IllegalArgumentException si el tipo no es CREDITO o el importe no es positivo
     */
    public MovimientoSaldo(TipoMovimientoSaldo tipo, Dinero importe, Long idPago) {
        validar(tipo, TipoMovimientoSaldo.CREDITO, importe);
        this.tipo = tipo;
        this.importe = importe;
        this.idPago = idPago;
        this.fecha = LocalDateTime.now(RelojSistema.ZONA_CLUB);
        this.motivo = recortar("Pago acreditado N° " + idPago + " (cuota ya pagada)");
    }

    /**
     * Reconstituye un movimiento leído de la base (operación «persistencia» de {@code BrokerMovimientoSaldo}).
     *
     * <p>Casos de uso: CU-05, CU-35, CU-37
     * @param id Identificador.
     * @param idGrupo Grupo dueño.
     * @param fecha Momento.
     * @param tipo Crédito o débito.
     * @param importe Importe.
     * @param motivo Descripción.
     * @param idPago Pago del crédito (null en el débito).
     * @return el movimiento sin la cuota (llega con {@code restaurarAsociaciones})
     */
    public static MovimientoSaldo reconstituir(Long id, Long idGrupo, LocalDateTime fecha, TipoMovimientoSaldo tipo, Dinero importe, String motivo, Long idPago) {
        MovimientoSaldo m = new MovimientoSaldo();
        m.asignarId(id);
        m.idGrupo = idGrupo;
        m.fecha = fecha;
        m.tipo = tipo;
        m.importe = importe;
        m.motivo = motivo;
        m.idPago = idPago;
        return m;
    }

    /**
     * Entrega la cuota del débito resuelta por {@code BrokerMovimientoSaldo.cargarAsociaciones}.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-37
     * @param cuota Cuota del débito o null en el crédito.
     */
    public void restaurarAsociaciones(Cuota cuota) {
        this.cuota = cuota;
    }

    /**
     * Agregado en la implementación: asigna el grupo dueño del CREDITO (el constructor del crédito solo recibe el
     * pago, que cuotas no conoce). Lo llama {@code ServicioCuotas.registrarCredito}.
     *
     * <p>Casos de uso: CU-35
     * @param idGrupo Grupo beneficiario.
     */
    public void asignarGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * Verifica el tipo y el importe de un movimiento nuevo (reglas del CHECK {@code ck_movimiento_origen} e
     * importe mayor que 0).
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param tipo Tipo recibido.
     * @param esperado Tipo que corresponde al constructor.
     * @param importe Importe recibido.
     * @throws IllegalArgumentException si no se cumple
     */
    private static void validar(TipoMovimientoSaldo tipo, TipoMovimientoSaldo esperado, Dinero importe) {
        if (tipo != esperado) {
            throw new IllegalArgumentException("El movimiento " + tipo + " no corresponde a este constructor");
        }
        if (importe == null || !importe.esPositivo()) {
            throw new IllegalArgumentException("El importe del movimiento de saldo debe ser mayor que 0");
        }
    }

    /**
     * Recorta el motivo al largo de la columna.
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param texto Motivo.
     * @return el motivo de hasta 100 caracteres
     */
    private static String recortar(String texto) {
        return texto.length() <= LARGO_MOTIVO ? texto : texto.substring(0, LARGO_MOTIVO);
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

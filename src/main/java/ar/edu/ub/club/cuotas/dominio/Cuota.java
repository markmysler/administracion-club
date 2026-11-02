package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad compuesta por {@code ItemCuota} (composición 1 a 0..*; la colección {@code items} se carga en el
 * primer acceso con un {@code ProxyListaVirtual} y la consulta {@code ItemsDeCuota}). Contexto del patrón State:
 * los estados concretos cambian su estado con {@code ~cambiarEstado(nuevo)}.
 *
 * <p>La crea {@code GrupoFamiliar.emitirCuota} (Creator: el grupo contiene sus cuotas y tiene los datos para
 * calcularlas) y la guarda {@code ServicioCuotas} con {@code FachadaPersistencia.put}.
 *
 * <p>Casos de uso: todos
 */
public class Cuota extends EntidadPersistente {

    /** Grupo al que se emitió (referencia por id; la asigna {@code GrupoFamiliar.emitirCuota}). */
    private Long idGrupo;

    /** Año y mes. */
    private String periodo;

    /** Total de la cuota. */
    private Dinero importe;

    /** Saldo a favor descontado al emitir la cuota (dato de la emisión). */
    private Dinero creditoAplicado;

    /** Emisión. */
    private LocalDate fechaEmision;

    /** Vencimiento. */
    private LocalDate fechaVencimiento;

    /** Estado actual. */
    private EstadoCuota estado;

    /** Aportes de cada integrante (Virtual Proxy). */
    private List<ItemCuota> items;

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     */
    private Cuota() {
    }

    /**
     * Crea una cuota nueva del período en estado Pendiente, sin crédito aplicado y sin ítems (los agrega
     * {@code GrupoFamiliar.emitirCuota} con {@code agregarItem}).
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param periodo Año y mes (AAAA-MM).
     * @param importe Total de la cuota (suma de los ítems).
     * @param fechas Emisión y vencimiento.
     */
    public Cuota(String periodo, Dinero importe, FechasCuota fechas) {
        this.periodo = periodo;
        this.importe = importe;
        this.creditoAplicado = Dinero.cero();
        this.fechaEmision = fechas.getEmision();
        this.fechaVencimiento = fechas.getVencimiento();
        this.estado = EstadoCuotaPendiente.INSTANCIA;
        this.items = new ArrayList<>();
    }

    /**
     * Atributo derivado: importe menos crédito aplicado.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return Importe a pagar.
     */
    public Dinero importeAPagar() {
        return importe.restar(creditoAplicado);
    }

    /**
     * Delega en {@code estado.registrarPagoAprobado(this, pago)}: Pendiente o Vencida → Pagada.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param pago Pago aprobado.
     * @throws ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException si la cuota ya está pagada
     */
    public void registrarPagoAprobado(Pago pago) {
        estado.registrarPagoAprobado(this, pago);
    }

    /**
     * Delega en {@code estado.vencer(this, hoy)}: Pendiente → Vencida si hoy es posterior al vencimiento.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @param hoy Fecha de proceso.
     * @throws ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException si la cuota no está pendiente
     */
    public void vencer(LocalDate hoy) {
        estado.vencer(this, hoy);
    }

    /**
     * Indica si el estado es Vencida (lo usa {@code GrupoFamiliar.tieneCuotaVencida}, RN-11).
     *
     * <p>Casos de uso: CU-04, CU-27, CU-28
     * @return true si está vencida.
     */
    public boolean estaVencida() {
        return estado instanceof EstadoCuotaVencida;
    }

    /**
     * Delega en el estado: Pendiente o Vencida (RN-18).
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return true si se puede pagar.
     */
    public boolean admitePago() {
        return estado.admitePago();
    }

    /**
     * Reconstituye una cuota leída de la base (operación «persistencia» que usa {@code BrokerCuota.crearObjeto}).
     *
     * <p>Casos de uso: todos
     * @param id Identificador.
     * @param idGrupo Grupo familiar.
     * @param periodo Año y mes.
     * @param importe Total.
     * @param creditoAplicado Saldo a favor descontado al emitir.
     * @param fechaEmision Emisión.
     * @param fechaVencimiento Vencimiento.
     * @param estado Estado (instancia única de {@code EstadoCuota.desdeCodigo}).
     * @param items Ítems ({@code ProxyListaVirtual} con {@code ItemsDeCuota}).
     * @return la cuota
     */
    public static Cuota reconstituir(Long id, Long idGrupo, String periodo, Dinero importe, Dinero creditoAplicado, LocalDate fechaEmision, LocalDate fechaVencimiento, EstadoCuota estado, List<ItemCuota> items) {
        Cuota c = new Cuota();
        c.asignarId(id);
        c.idGrupo = idGrupo;
        c.periodo = periodo;
        c.importe = importe;
        c.creditoAplicado = creditoAplicado == null ? Dinero.cero() : creditoAplicado;
        c.fechaEmision = fechaEmision;
        c.fechaVencimiento = fechaVencimiento;
        c.estado = estado;
        c.items = items == null ? new ArrayList<>() : items;
        return c;
    }

    /**
     * Agregado en la implementación: asigna el grupo al que se emite la cuota. Lo llama {@code
     * GrupoFamiliar.emitirCuota} (el diseño dice «la asigna GrupoFamiliar.emitirCuota» sin nombrar la operación).
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param idGrupo Grupo familiar.
     */
    public void asignarGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * Agregado en la implementación: agrega el aporte de un integrante (mensaje «agrega ItemCuota(socio,
     * categoria, importe)» de {@code sec_tmp_emision_p1}). Lo llama {@code GrupoFamiliar.emitirCuota}.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param item Aporte del integrante.
     */
    public void agregarItem(ItemCuota item) {
        items.add(item);
    }

    /**
     * Agregado en la implementación: {@code creditoAplicado = min(saldo, importe)} (mensaje de {@code
     * sec_tmp_emision_p1}, RN-21). Un saldo nulo o negativo no aplica crédito.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param saldo Saldo a favor disponible del grupo.
     */
    public void aplicarCredito(Dinero saldo) {
        if (saldo == null || !saldo.esPositivo()) {
            this.creditoAplicado = Dinero.cero();
        } else {
            this.creditoAplicado = saldo.minimo(importe);
        }
    }

    /**
     * Reemplaza el objeto estado (patrón State). Solo lo invocan los estados concretos.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: vencimiento de cuotas, emisión mensual de cuotas
     * @param nuevo Estado destino.
     */
    void cambiarEstado(EstadoCuota nuevo) {
        this.estado = nuevo;
    }

    /**
     * Devuelve {@code idGrupo}: Grupo al que se emitió (referencia por id; la asigna {@code GrupoFamiliar.emitirCuota}).
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Long getIdGrupo() {
        return idGrupo;
    }

    /**
     * Devuelve {@code periodo}: Año y mes.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getPeriodo() {
        return periodo;
    }

    /**
     * Devuelve {@code importe}: Total de la cuota.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code creditoAplicado}: Saldo a favor descontado al emitir la cuota (dato de la emisión).
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Dinero getCreditoAplicado() {
        return creditoAplicado;
    }

    /**
     * Devuelve {@code fechaEmision}: Emisión.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Devuelve {@code fechaVencimiento}: Vencimiento.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * Devuelve {@code estado}: Estado actual.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public EstadoCuota getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code items}: Aportes de cada integrante (Virtual Proxy).
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public List<ItemCuota> getItems() {
        return items;
    }
}

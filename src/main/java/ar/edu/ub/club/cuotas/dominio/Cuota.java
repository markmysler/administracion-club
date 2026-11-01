package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.time.LocalDate;
import java.util.List;

/**
 * Entidad compuesta por {@code ItemCuota} (composición 1 a 0..*; la colección {@code items} se carga en el
 * primer acceso con un {@code ProxyListaVirtual} y la consulta {@code ItemsDeCuota}). Contexto del patrón State:
 * los estados concretos cambian su estado con {@code ~cambiarEstado(nuevo)}.
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
     * Crea una instancia de {@code Cuota}.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param periodo valor de {@code periodo}
     * @param importe valor de {@code importe}
     * @param fechas valor de {@code fechas}
     */
    public Cuota(String periodo, Dinero importe, FechasCuota fechas) {
        this.periodo = periodo;
        this.importe = importe;
        this.creditoAplicado = Dinero.cero();
        this.fechaEmision = fechas.getEmision();
        this.fechaVencimiento = fechas.getVencimiento();
        this.estado = new EstadoCuotaPendiente();
        this.items = new java.util.ArrayList<>();
    }

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     * @param idGrupo grupo
     */
    public void asignarGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     * @param item ítem
     */
    public void agregarItem(ItemCuota item) {
        items.add(item);
    }

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     * @param saldo saldo
     */
    public void aplicarCredito(Dinero saldo) {
        this.creditoAplicado = saldo.minimo(importe);
    }

    /**
     * Atributo derivado: importe menos crédito aplicado.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return Importe a pagar.
     */
    public Dinero importeAPagar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Delega en {@code estado.registrarPagoAprobado(this, pago)}: Pendiente o Vencida → Pagada.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas
     * @param pago Pago aprobado.
     */
    public void registrarPagoAprobado(Pago pago) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Delega en {@code estado.vencer(this, hoy)}: Pendiente → Vencida si hoy es posterior al vencimiento.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @param hoy Fecha de proceso.
     */
    public void vencer(LocalDate hoy) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Indica si el estado es Vencida.
     *
     * <p>Casos de uso: todos
     * @return true si está vencida.
     */
    public boolean estaVencida() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Delega en el estado: Pendiente o Vencida.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return true si se puede pagar.
     */
    public boolean admitePago() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param idGrupo valor de {@code idGrupo}
     * @param periodo valor de {@code periodo}
     * @param importe valor de {@code importe}
     * @param creditoAplicado valor de {@code creditoAplicado}
     * @param fechaEmision valor de {@code fechaEmision}
     * @param fechaVencimiento valor de {@code fechaVencimiento}
     * @param estado valor de {@code estado}
     * @param items valor de {@code items}
     * @return el resultado
     */
    public static Cuota reconstituir(Long id, Long idGrupo, String periodo, Dinero importe, Dinero creditoAplicado, LocalDate fechaEmision, LocalDate fechaVencimiento, EstadoCuota estado, List<ItemCuota> items) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Reemplaza el objeto estado (patrón State). Solo lo invocan los estados concretos.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @param nuevo Estado destino.
     */
     void cambiarEstado(EstadoCuota nuevo) {
        throw new UnsupportedOperationException("PENDIENTE");
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

package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.time.LocalDateTime;

/**
 * Evento de dominio ({@code cuotas.dominio}) que publica {@code ServicioCuotas} cuando el crédito aplicado cubre
 * todo el importe de la cuota emitida (o cuando un crédito posterior cubre una cuota impaga). Lo atiende {@code
 * RegistradorPagoConSaldo} (módulo pagos, patrón Observer) dentro de la misma transacción: así cuotas no depende
 * de pagos.
 *
 * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class CuotaCubiertaConSaldoEvento {

    /** Cuota cubierta. */
    private final Long idCuota;

    /** Grupo familiar. */
    private final Long idGrupo;

    /** Importe cubierto con saldo. */
    private final Dinero importe;

    /** Momento de la emisión. */
    private final LocalDateTime fechaHora;

    /**
     * Agregado en la implementación (el modelo no lista el constructor de este objeto de datos): crea el evento
     * con todos sus datos.
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param idCuota Cuota cubierta.
     * @param idGrupo Grupo familiar.
     * @param importe Importe cubierto.
     * @param fechaHora Momento de la emisión o del crédito.
     */
    public CuotaCubiertaConSaldoEvento(Long idCuota, Long idGrupo, Dinero importe, LocalDateTime fechaHora) {
        this.idCuota = idCuota;
        this.idGrupo = idGrupo;
        this.importe = importe;
        this.fechaHora = fechaHora;
    }

    /**
     * Devuelve {@code idCuota}: Cuota cubierta.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el valor del atributo
     */
    public Long getIdCuota() {
        return idCuota;
    }

    /**
     * Devuelve {@code idGrupo}: Grupo familiar.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el valor del atributo
     */
    public Long getIdGrupo() {
        return idGrupo;
    }

    /**
     * Devuelve {@code importe}: Importe cubierto con saldo.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code fechaHora}: Momento de la emisión.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el valor del atributo
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}

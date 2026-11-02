package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.util.UUID;

/**
 * Resultado de un pago consultado a la pasarela del lado del servidor ({@code PasarelaPago.obtenerResultado}).
 * Cada adaptador traduce el estado propio de la pasarela a uno de los tres valores del sistema: {@code
 * APROBADO}, {@code RECHAZADO} o {@code Pendiente} (Adapter). Nunca contiene datos de tarjeta (RNF-05).
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
 */
public class ResultadoPasarela {

    /** Estado del sistema: la pasarela aprobó el pago. */
    public static final String APROBADO = "APROBADO";

    /** Estado del sistema: la pasarela rechazó el pago. */
    public static final String RECHAZADO = "RECHAZADO";

    /** Estado del sistema: la pasarela todavía no resolvió el pago. */
    public static final String PENDIENTE = "PENDIENTE";

    /** Identificador del pago en la pasarela. */
    private final String idPagoPasarela;

    /** Referencia externa que el sistema envió al crear la intención. */
    private final UUID referenciaExterna;

    /** APROBADO, RECHAZADO o Pendiente. */
    private final String estado;

    /** Importe informado por la pasarela. */
    private final Dinero importe;

    /** Agregado en la implementación: detalle del rechazo informado por la pasarela (null si no hay). */
    private final String motivo;

    /**
     * Agregado en la implementación (el modelo no lista el constructor): crea el resultado.
     *
     * <p>Casos de uso: CU-35
     * @param idPagoPasarela Identificador en la pasarela.
     * @param referenciaExterna Referencia del sistema.
     * @param estado APROBADO, RECHAZADO o Pendiente.
     * @param importe Importe informado.
     * @param motivo Detalle del rechazo o null.
     */
    public ResultadoPasarela(String idPagoPasarela, UUID referenciaExterna, String estado, Dinero importe, String motivo) {
        this.idPagoPasarela = idPagoPasarela;
        this.referenciaExterna = referenciaExterna;
        this.estado = estado;
        this.importe = importe;
        this.motivo = motivo;
    }

    /**
     * Indica si la pasarela aprobó el pago.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @return true si el estado es APROBADO
     */
    public boolean esAprobado() {
        return APROBADO.equals(estado);
    }

    /**
     * Indica si la pasarela rechazó el pago.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @return true si el estado es RECHAZADO
     */
    public boolean esRechazado() {
        return RECHAZADO.equals(estado);
    }

    /**
     * Devuelve {@code idPagoPasarela}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getIdPagoPasarela() {
        return idPagoPasarela;
    }

    /**
     * Devuelve {@code referenciaExterna}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public UUID getReferenciaExterna() {
        return referenciaExterna;
    }

    /**
     * Devuelve {@code estado}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code importe}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code motivo}: detalle del rechazo.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getMotivo() {
        return motivo;
    }
}

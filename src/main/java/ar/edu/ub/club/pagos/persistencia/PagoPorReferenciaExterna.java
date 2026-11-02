package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

/**
 * Pago online por su referencia externa (UNIQUE). Con bloqueo ({@code FOR UPDATE}) la usa {@code
 * ServicioPagos.confirmarResultadoPasarela}: dos notificaciones simultáneas del mismo pago se procesan en serie
 * (idempotencia). Sin bloqueo la usa {@code consultarPagoOnline}, que corre en una transacción de solo lectura
 * (PostgreSQL no admite {@code FOR UPDATE} en ella). Consulta 47 de la Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-35
 */
public class PagoPorReferenciaExterna extends ConsultaEntidades<PagoOnline> {

    /** Referencia externa. */
    private final UUID referencia;

    /** Agregado en la implementación: si se toma el bloqueo de fila. */
    private final boolean bloquear;

    /**
     * Crea la consulta con bloqueo de fila ({@code FOR UPDATE}), como la define el diseño.
     *
     * <p>Casos de uso: CU-35
     * @param referencia Referencia externa.
     */
    public PagoPorReferenciaExterna(UUID referencia) {
        this(referencia, true);
    }

    /**
     * Agregado en la implementación: crea la consulta indicando si se bloquea la fila.
     *
     * <p>Casos de uso: CU-35
     * @param referencia Referencia externa.
     * @param bloquear true para {@code FOR UPDATE}.
     */
    public PagoPorReferenciaExterna(UUID referencia, boolean bloquear) {
        this.referencia = Objects.requireNonNull(referencia, "referencia");
        this.bloquear = bloquear;
    }

    /**
     * Clase del resultado: {@code PagoOnline}.
     *
     * <p>Casos de uso: CU-35
     * @return PagoOnline.class
     */
    @Override
    public Class<PagoOnline> claseResultado() {
        return PagoOnline.class;
    }

    /**
     * Condición (y bloqueo) que se agrega al SELECT con JOIN de {@code BrokerPagoOnline}.
     *
     * <p>Casos de uso: CU-35
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE pago_online.referencia_externa = ?" + (bloquear ? " FOR UPDATE" : "");
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-35
     * @return [referencia]
     */
    @Override
    public List<Object> parametros() {
        return List.of(referencia);
    }
}

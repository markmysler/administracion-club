package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Pagos online pendientes creados antes del límite (ahora − 24 h; índice {@code ix_pago_estado_fecha}). La usa
 * {@code ServicioPagos.vencerPagosPendientes} (RN-22). Consulta 50 de la Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
 */
public class PagosOnlinePendientesAnterioresA extends ConsultaEntidades<PagoOnline> {

    /** Momento límite. */
    private final LocalDateTime limite;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param limite Momento límite.
     */
    public PagosOnlinePendientesAnterioresA(LocalDateTime limite) {
        this.limite = limite;
    }

    /**
     * Clase del resultado: {@code PagoOnline}.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @return PagoOnline.class
     */
    @Override
    public Class<PagoOnline> claseResultado() {
        return PagoOnline.class;
    }

    /**
     * Condición y orden.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE pago.estado = 'PENDIENTE' AND pago.fecha < ? ORDER BY pago.fecha";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @return [limite]
     */
    @Override
    public List<Object> parametros() {
        return List.of(limite);
    }
}

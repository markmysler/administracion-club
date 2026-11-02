package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.time.LocalDate;
import java.util.List;

/**
 * Cuotas pendientes cuya fecha de vencimiento es anterior a la fecha de proceso (usa el índice {@code
 * ix_cuota_estado_vencimiento}). La usa {@code ServicioCuotas.marcarCuotasVencidas}. Consulta 42 de la Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: tareas programadas: vencimiento de cuotas
 */
public class CuotasPendientesVencidas extends ConsultaEntidades<Cuota> {

    /** Fecha de proceso. */
    private final LocalDate hoy;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @param hoy Fecha de proceso.
     */
    public CuotasPendientesVencidas(LocalDate hoy) {
        this.hoy = hoy;
    }

    /**
     * Clase del resultado: {@code Cuota}.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @return Cuota.class
     */
    @Override
    public Class<Cuota> claseResultado() {
        return Cuota.class;
    }

    /**
     * Condición y orden que se agregan a {@code SELECT * FROM cuota}.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE cuota.estado = 'PENDIENTE' AND cuota.fecha_vencimiento < ? ORDER BY cuota.id";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @return [hoy]
     */
    @Override
    public List<Object> parametros() {
        return List.of(hoy);
    }
}

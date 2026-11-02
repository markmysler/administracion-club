package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.util.List;

/**
 * Objeto consulta de proyección n.º 62 (Entrega 7C): cantidad de cuotas en un estado (Pendiente o VENCIDA para
 * el indicador «Cuotas impagas» de P-04, paso 6 de CU-40). Tabla: cuota.
 *
 * <p>Casos de uso: CU-40
 */
public class CuotasEnEstado extends ConsultaProyeccion<Long> {

    /** Código del estado de la cuota (Pendiente, VENCIDA o PAGADA). */
    private String codigoEstado;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-40
     * @param codigoEstado código del estado
     */
    public CuotasEnEstado(String codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    /**
     * Devuelve la clase del resultado.
     *
     * <p>Casos de uso: CU-40
     * @return {@code Long.class}
     */
    @Override
    public Class<Long> claseResultado() {
        return Long.class;
    }

    /**
     * Lee la cantidad.
     *
     * <p>Casos de uso: CU-40
     * @param fila fila del {@code ResultSet}
     * @return cantidad de cuotas
     */
    @Override
    public Long leerFila(FilaResultado fila) {
        return fila.largo("cantidad");
    }

    /**
     * Sentencia SQL.
     *
     * <p>Casos de uso: CU-40
     * @return SQL
     */
    @Override
    public String sql() {
        return "SELECT COUNT(*) AS cantidad FROM cuota WHERE cuota.estado = ?";
    }

    /**
     * Parámetro: código del estado.
     *
     * <p>Casos de uso: CU-40
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(codigoEstado);
    }
}

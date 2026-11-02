package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.time.LocalDate;
import java.util.List;

/**
 * Objeto consulta de proyección n.º 58 (Entrega 7C): cantidad de reservas CONFIRMADAS o FINALIZADAS con fecha
 * entre dos días (indicadores «Reservas de hoy» y «Semana» de P-04, paso 3 de CU-40). Tabla: reserva.
 *
 * <p>Casos de uso: CU-40
 */
public class ReservasEntre extends ConsultaProyeccion<Long> {

    /** Fecha inicial (inclusive). */
    private LocalDate desde;

    /** Fecha final (inclusive). */
    private LocalDate hasta;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-40
     * @param desde fecha inicial
     * @param hasta fecha final
     */
    public ReservasEntre(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
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
     * @return cantidad de reservas
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
        return "SELECT COUNT(*) AS cantidad FROM reserva WHERE reserva.estado IN ('CONFIRMADA', 'FINALIZADA')"
                + " AND reserva.fecha >= ? AND reserva.fecha <= ?";
    }

    /**
     * Parámetros: desde y hasta.
     *
     * <p>Casos de uso: CU-40
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(desde, hasta);
    }
}

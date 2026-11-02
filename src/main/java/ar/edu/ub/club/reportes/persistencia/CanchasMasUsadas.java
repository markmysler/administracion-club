package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.reportes.aplicacion.FilaUsoCancha;
import java.time.LocalDate;
import java.util.List;

/**
 * Objeto consulta de proyección n.º 63 (Entrega 7C): las {@code limite} canchas con más reservas CONFIRMADAS o
 * FINALIZADAS entre dos fechas, con sus horas y su ocupación (indicador «Canchas más utilizadas» de P-04, paso 7 de
 * CU-40). Tablas: reserva, cancha, deporte, disponibilidad_club, dia_atencion, feriado.
 *
 * <p>Casos de uso: CU-40
 */
public class CanchasMasUsadas extends ConsultaProyeccion<FilaUsoCancha> {

    /** Fecha inicial (inclusive). */
    private LocalDate desde;

    /** Fecha final (inclusive). */
    private LocalDate hasta;

    /** Cantidad máxima de canchas. */
    private Integer limite;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-40
     * @param desde fecha inicial
     * @param hasta fecha final
     * @param limite cantidad máxima de filas
     */
    public CanchasMasUsadas(LocalDate desde, LocalDate hasta, Integer limite) {
        this.desde = desde;
        this.hasta = hasta;
        this.limite = limite;
    }

    /**
     * Devuelve la clase de las filas resultado.
     *
     * <p>Casos de uso: CU-40
     * @return {@code FilaUsoCancha.class}
     */
    @Override
    public Class<FilaUsoCancha> claseResultado() {
        return FilaUsoCancha.class;
    }

    /**
     * Lee una fila del resultado.
     *
     * <p>Casos de uso: CU-40
     * @param fila fila del {@code ResultSet}
     * @return la fila de uso de la cancha
     */
    @Override
    public FilaUsoCancha leerFila(FilaResultado fila) {
        return new FilaUsoCancha(fila.texto("deporte"), fila.texto("cancha"), fila.largo("cantidad_reservas"),
                fila.decimal("horas_reservadas"), fila.decimal("porcentaje_ocupacion"));
    }

    /**
     * Sentencia SQL: agrupa las reservas por cancha y ordena por cantidad descendente.
     *
     * <p>Casos de uso: CU-40
     * @return SQL
     */
    @Override
    public String sql() {
        return UsoDeCanchas.ATENCION
                + "SELECT deporte.nombre AS deporte, cancha.nombre AS cancha, COUNT(reserva.id) AS cantidad_reservas,"
                + " ROUND(" + UsoDeCanchas.HORAS_RESERVA + ", 2) AS horas_reservadas,"
                + " CASE WHEN MAX(atencion.horas) > 0 THEN ROUND(100 * " + UsoDeCanchas.HORAS_RESERVA
                + " / MAX(atencion.horas), 2) ELSE 0 END AS porcentaje_ocupacion"
                + " FROM reserva JOIN cancha ON cancha.id = reserva.cancha_id"
                + " JOIN deporte ON deporte.id = cancha.deporte_id CROSS JOIN atencion"
                + " WHERE reserva.estado IN ('CONFIRMADA', 'FINALIZADA')"
                + " AND reserva.fecha >= ? AND reserva.fecha <= ?"
                + " GROUP BY deporte.nombre, cancha.id, cancha.nombre"
                + " ORDER BY COUNT(reserva.id) DESC, cancha.nombre LIMIT ?";
    }

    /**
     * Parámetros en el orden del SQL.
     *
     * <p>Casos de uso: CU-40
     * @return desde, hasta (atención), desde, hasta (reservas) y límite
     */
    @Override
    public List<Object> parametros() {
        return List.of(desde, hasta, desde, hasta, limite);
    }
}

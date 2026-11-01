package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.reportes.aplicacion.FilaUsoCancha;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto consulta de proyección n.º 64 (MODELO 11.8.4): por deporte y cancha, cantidad de reservas CONFIRMADAS y
 * FINALIZADAS del período, horas reservadas y porcentaje de ocupación sobre las horas de atención del club en el
 * período (días de {@code dia_atencion} que no son {@code feriado}, por la duración de {@code disponibilidad_club}).
 * Incluye las canchas activas sin reservas (en cero, FA2) y las inactivas que tuvieron reservas. Filtros opcionales de
 * deporte y cancha agregados al WHERE solo si no son nulos (convención de persistencia 5). Tablas: reserva, cancha,
 * deporte, disponibilidad_club, dia_atencion, feriado.
 *
 * <p>Casos de uso: CU-38
 */
public class UsoDeCanchas extends ConsultaProyeccion<FilaUsoCancha> {

    /** Expresión de las horas de una reserva. */
    static final String HORAS_RESERVA =
            "COALESCE(SUM(EXTRACT(EPOCH FROM (reserva.hora_fin - reserva.hora_inicio)) / 3600), 0)";

    /**
     * Subconsulta común con las horas de atención del club entre dos fechas (dos parámetros: desde y hasta). La usan
     * {@code UsoDeCanchas} y {@code CanchasMasUsadas}.
     */
    static final String ATENCION = "WITH atencion AS ("
            + "SELECT COALESCE(SUM(EXTRACT(EPOCH FROM (disponibilidad_club.hora_cierre"
            + " - disponibilidad_club.hora_apertura)) / 3600), 0) AS horas"
            + " FROM generate_series(CAST(? AS date), CAST(? AS date), INTERVAL '1 day') AS dia(fecha)"
            + " JOIN disponibilidad_club ON disponibilidad_club.id = 1"
            + " JOIN dia_atencion ON dia_atencion.disponibilidad_id = disponibilidad_club.id"
            + " AND dia_atencion.dia = EXTRACT(ISODOW FROM dia.fecha)"
            + " WHERE NOT EXISTS (SELECT 1 FROM feriado WHERE feriado.fecha = CAST(dia.fecha AS date))) ";

    /** Filtro del reporte (período obligatorio; deporte y cancha opcionales). */
    private FiltroReporte filtro;

    /**
     * Crea la consulta para un filtro ya validado.
     *
     * <p>Casos de uso: CU-38
     * @param filtro filtro del reporte
     */
    public UsoDeCanchas(FiltroReporte filtro) {
        this.filtro = filtro;
    }

    /**
     * Devuelve la clase de las filas resultado.
     *
     * <p>Casos de uso: CU-38
     * @return {@code FilaUsoCancha.class}
     */
    @Override
    public Class<FilaUsoCancha> claseResultado() {
        return FilaUsoCancha.class;
    }

    /**
     * Lee una fila del resultado.
     *
     * <p>Casos de uso: CU-38
     * @param fila fila del {@code ResultSet}
     * @return la fila del reporte
     */
    @Override
    public FilaUsoCancha leerFila(FilaResultado fila) {
        return new FilaUsoCancha(fila.texto("deporte"), fila.texto("cancha"), fila.largo("cantidad_reservas"),
                fila.decimal("horas_reservadas"), fila.decimal("porcentaje_ocupacion"));
    }

    /**
     * Sentencia SQL con los filtros no nulos.
     *
     * <p>Casos de uso: CU-38
     * @return SQL
     */
    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder(ATENCION)
                .append("SELECT deporte.nombre AS deporte, cancha.nombre AS cancha,")
                .append(" COUNT(reserva.id) AS cantidad_reservas,")
                .append(" ROUND(").append(HORAS_RESERVA).append(", 2) AS horas_reservadas,")
                .append(" CASE WHEN MAX(atencion.horas) > 0 THEN ROUND(100 * ").append(HORAS_RESERVA)
                .append(" / MAX(atencion.horas), 2) ELSE 0 END AS porcentaje_ocupacion")
                .append(" FROM cancha JOIN deporte ON deporte.id = cancha.deporte_id CROSS JOIN atencion")
                .append(" LEFT JOIN reserva ON reserva.cancha_id = cancha.id")
                .append(" AND reserva.estado IN ('CONFIRMADA', 'FINALIZADA')")
                .append(" AND reserva.fecha >= ? AND reserva.fecha <= ?");
        List<String> condiciones = new ArrayList<>();
        if (filtro.getIdDeporte() != null) {
            condiciones.add("deporte.id = ?");
        }
        if (filtro.getIdCancha() != null) {
            condiciones.add("cancha.id = ?");
        }
        if (!condiciones.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", condiciones));
        }
        sql.append(" GROUP BY deporte.id, deporte.nombre, cancha.id, cancha.nombre, cancha.activa")
                .append(" HAVING cancha.activa OR COUNT(reserva.id) > 0")
                .append(" ORDER BY deporte.nombre, cancha.nombre");
        return sql.toString();
    }

    /**
     * Parámetros en el orden del SQL: período de atención, período de reservas, deporte y cancha si no son nulos.
     *
     * <p>Casos de uso: CU-38
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        List<Object> parametros = new ArrayList<>(List.of(filtro.getDesde(), filtro.getHasta(),
                filtro.getDesde(), filtro.getHasta()));
        if (filtro.getIdDeporte() != null) {
            parametros.add(filtro.getIdDeporte());
        }
        if (filtro.getIdCancha() != null) {
            parametros.add(filtro.getIdCancha());
        }
        return parametros;
    }
}

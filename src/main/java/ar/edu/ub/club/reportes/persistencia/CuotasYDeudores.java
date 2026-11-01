package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.reportes.aplicacion.FilaCuotaDeudor;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import java.util.List;

/**
 * Objeto consulta de proyección n.º 65 (MODELO 11.8.4): cuotas emitidas en el período ({@code cuota.fecha_emision}
 * entre desde y hasta) agrupadas por grupo familiar, con el titular, las cuotas adeudadas (Pendiente y VENCIDA) y su
 * deuda con el importe a pagar ({@code importe - credito_aplicado}, RN-21), y además las cobradas (PAGADA) y las
 * vencidas para los totales. Orden por deuda descendente. Tablas: cuota, grupo_familiar, socio.
 *
 * <p>Casos de uso: CU-39
 */
public class CuotasYDeudores extends ConsultaProyeccion<FilaCuotaDeudor> {

    /** Filtro del reporte (período de emisión). */
    private FiltroReporte filtro;

    /**
     * Crea la consulta para un filtro ya validado.
     *
     * <p>Casos de uso: CU-39
     * @param filtro filtro del reporte
     */
    public CuotasYDeudores(FiltroReporte filtro) {
        this.filtro = filtro;
    }

    /**
     * Devuelve la clase de las filas resultado.
     *
     * <p>Casos de uso: CU-39
     * @return {@code FilaCuotaDeudor.class}
     */
    @Override
    public Class<FilaCuotaDeudor> claseResultado() {
        return FilaCuotaDeudor.class;
    }

    /**
     * Lee una fila del resultado.
     *
     * <p>Casos de uso: CU-39
     * @param fila fila del {@code ResultSet}
     * @return la fila del grupo
     */
    @Override
    public FilaCuotaDeudor leerFila(FilaResultado fila) {
        return new FilaCuotaDeudor(fila.largo("id_grupo"), fila.texto("titular"), fila.texto("dni_titular"),
                fila.entero("cuotas_adeudadas"), fila.dinero("deuda_total"), fila.entero("cuotas_cobradas"),
                fila.dinero("importe_cobrado"), fila.entero("cuotas_vencidas"), fila.dinero("importe_vencido"));
    }

    /**
     * Sentencia SQL.
     *
     * <p>Casos de uso: CU-39
     * @return SQL
     */
    @Override
    public String sql() {
        return "SELECT grupo_familiar.id AS id_grupo,"
                + " socio.apellido || ', ' || socio.nombre AS titular, socio.dni AS dni_titular,"
                + " COUNT(cuota.id) FILTER (WHERE cuota.estado IN ('PENDIENTE', 'VENCIDA')) AS cuotas_adeudadas,"
                + " COALESCE(SUM(cuota.importe - cuota.credito_aplicado)"
                + " FILTER (WHERE cuota.estado IN ('PENDIENTE', 'VENCIDA')), 0) AS deuda_total,"
                + " COUNT(cuota.id) FILTER (WHERE cuota.estado = 'PAGADA') AS cuotas_cobradas,"
                + " COALESCE(SUM(cuota.importe) FILTER (WHERE cuota.estado = 'PAGADA'), 0) AS importe_cobrado,"
                + " COUNT(cuota.id) FILTER (WHERE cuota.estado = 'VENCIDA') AS cuotas_vencidas,"
                + " COALESCE(SUM(cuota.importe - cuota.credito_aplicado)"
                + " FILTER (WHERE cuota.estado = 'VENCIDA'), 0) AS importe_vencido"
                + " FROM cuota JOIN grupo_familiar ON grupo_familiar.id = cuota.grupo_familiar_id"
                + " LEFT JOIN socio ON socio.id = grupo_familiar.titular_id"
                + " WHERE cuota.fecha_emision >= ? AND cuota.fecha_emision <= ?"
                + " GROUP BY grupo_familiar.id, socio.apellido, socio.nombre, socio.dni"
                + " ORDER BY deuda_total DESC, grupo_familiar.id";
    }

    /**
     * Parámetros: desde y hasta del período de emisión.
     *
     * <p>Casos de uso: CU-39
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(filtro.getDesde(), filtro.getHasta());
    }
}

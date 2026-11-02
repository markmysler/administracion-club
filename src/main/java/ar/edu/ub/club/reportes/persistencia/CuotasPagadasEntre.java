package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.time.LocalDate;
import java.util.List;

/**
 * Objeto consulta de proyección n.º 60 (Entrega 7C): cantidad de cuotas distintas con un pago APROBADO cuya fecha
 * (hora local del club) está entre dos días (indicador «cuotas pagadas en el mes» de P-04, paso 4 de CU-40). Los
 * pagos ACREDITADOS no cuentan: no pagan una cuota, generan saldo a favor (RN-21). Tabla: pago.
 *
 * <p>Casos de uso: CU-40
 */
public class CuotasPagadasEntre extends ConsultaProyeccion<Long> {

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
    public CuotasPagadasEntre(LocalDate desde, LocalDate hasta) {
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
     * @return cantidad de cuotas pagadas
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
        return "SELECT COUNT(DISTINCT pago.cuota_id) AS cantidad FROM pago WHERE pago.estado = 'APROBADO'"
                + " AND CAST(pago.fecha AT TIME ZONE ? AS date) >= ? AND CAST(pago.fecha AT TIME ZONE ? AS date) <= ?";
    }

    /**
     * Parámetros: zona del club, desde, zona del club, hasta.
     *
     * <p>Casos de uso: CU-40
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        String zona = RelojSistema.ZONA_CLUB.getId();
        return List.of(zona, desde, zona, hasta);
    }
}

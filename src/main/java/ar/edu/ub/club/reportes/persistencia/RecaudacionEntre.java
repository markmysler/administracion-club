package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.time.LocalDate;
import java.util.List;

/**
 * Objeto consulta de proyección n.º 67 (MODELO 11.8.4): suma del importe de los pagos APROBADOS cuya fecha (en la
 * hora local del club) está entre dos días (indicador «Cobrado en el mes» de P-04, paso 4 de CU-40). Tabla: pago.
 *
 * <p>Casos de uso: CU-40
 */
public class RecaudacionEntre extends ConsultaProyeccion<Dinero> {

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
    public RecaudacionEntre(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    /**
     * Devuelve la clase del resultado.
     *
     * <p>Casos de uso: CU-40
     * @return {@code Dinero.class}
     */
    @Override
    public Class<Dinero> claseResultado() {
        return Dinero.class;
    }

    /**
     * Lee el importe recaudado.
     *
     * <p>Casos de uso: CU-40
     * @param fila fila del {@code ResultSet}
     * @return importe (cero si no hubo pagos)
     */
    @Override
    public Dinero leerFila(FilaResultado fila) {
        return fila.dinero("recaudacion");
    }

    /**
     * Sentencia SQL; la fecha del pago (TIMESTAMPTZ) se pasa a la fecha local del club.
     *
     * <p>Casos de uso: CU-40
     * @return SQL
     */
    @Override
    public String sql() {
        return "SELECT COALESCE(SUM(pago.importe), 0) AS recaudacion FROM pago WHERE pago.estado = 'APROBADO'"
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

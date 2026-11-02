package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.aplicacion.FiltroPagos;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Pagos del club que cumplen el filtro (rango de fechas, período de la cuota, estado y grupo), del más reciente
 * al más antiguo. Consulta polimórfica sobre {@code Pago}: se ejecuta en el broker de cada subclase y la unión se
 * ordena con {@code comparador()}. La usa {@code ServicioPagos.consultarPagos} con CONSULTAR_PAGOS_CLUB. El
 * WHERE se arma solo con los filtros no nulos. Consulta 48 de la Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-37
 */
public class PagosPorFiltro extends ConsultaEntidades<Pago> {

    /** Filtro de la consulta. */
    private final FiltroPagos filtro;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-37
     * @param filtro Filtro (puede tener todos los campos nulos).
     */
    public PagosPorFiltro(FiltroPagos filtro) {
        this.filtro = filtro == null ? new FiltroPagos() : filtro;
    }

    /**
     * Clase del resultado: {@code Pago} (abstracta, consulta polimórfica).
     *
     * <p>Casos de uso: CU-37
     * @return Pago.class
     */
    @Override
    public Class<Pago> claseResultado() {
        return Pago.class;
    }

    /**
     * Condición y orden que se agregan al SELECT con JOIN de cada broker.
     *
     * <p>Casos de uso: CU-37
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return armarSql(condiciones(filtro, filtro.getIdGrupo()));
    }

    /**
     * Parámetros en el mismo orden que las condiciones.
     *
     * <p>Casos de uso: CU-37
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return parametrosDe(filtro, filtro.getIdGrupo());
    }

    /**
     * Orden de la unión de los tres brokers: fecha descendente.
     *
     * <p>Casos de uso: CU-37
     * @return comparador
     */
    @Override
    public Comparator<Pago> comparador() {
        return porFechaDescendente();
    }

    /**
     * Comparador por fecha descendente compartido con {@code PagosDeGrupo}.
     *
     * <p>Casos de uso: CU-37
     * @return comparador
     */
    static Comparator<Pago> porFechaDescendente() {
        return Comparator.comparing(Pago::getFecha, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Pago::getId, Comparator.nullsLast(Comparator.reverseOrder()));
    }

    /**
     * Condiciones SQL de los filtros no nulos (compartido con {@code PagosDeGrupo}).
     *
     * <p>Casos de uso: CU-37
     * @param filtro Filtro.
     * @param idGrupo Grupo (del filtro o forzado por el alcance del socio).
     * @return condiciones
     */
    static List<String> condiciones(FiltroPagos filtro, Long idGrupo) {
        List<String> c = new ArrayList<>();
        if (filtro.getDesde() != null) {
            c.add("pago.fecha >= ?");
        }
        if (filtro.getHasta() != null) {
            c.add("pago.fecha < ?");
        }
        if (filtro.getEstado() != null && !filtro.getEstado().isBlank()) {
            c.add("pago.estado = ?");
        }
        if (filtro.getPeriodo() != null && !filtro.getPeriodo().isBlank()) {
            c.add("pago.cuota_id IN (SELECT cuota.id FROM cuota WHERE cuota.periodo = ?)");
        }
        if (idGrupo != null) {
            c.add("pago.cuota_id IN (SELECT cuota.id FROM cuota WHERE cuota.grupo_familiar_id = ?)");
        }
        return c;
    }

    /**
     * Parámetros de los filtros no nulos, en el orden de {@code condiciones}. Las fechas se convierten en
     * momentos del día en la zona del club (hasta es inclusivo: menor que el día siguiente).
     *
     * <p>Casos de uso: CU-37
     * @param filtro Filtro.
     * @param idGrupo Grupo.
     * @return parámetros
     */
    static List<Object> parametrosDe(FiltroPagos filtro, Long idGrupo) {
        List<Object> p = new ArrayList<>();
        if (filtro.getDesde() != null) {
            p.add(filtro.getDesde().atTime(LocalTime.MIDNIGHT));
        }
        if (filtro.getHasta() != null) {
            p.add(filtro.getHasta().plusDays(1).atTime(LocalTime.MIDNIGHT));
        }
        if (filtro.getEstado() != null && !filtro.getEstado().isBlank()) {
            p.add(filtro.getEstado().trim());
        }
        if (filtro.getPeriodo() != null && !filtro.getPeriodo().isBlank()) {
            p.add(filtro.getPeriodo().trim());
        }
        if (idGrupo != null) {
            p.add(idGrupo);
        }
        return p;
    }

    /**
     * Arma {@code WHERE ... ORDER BY pago.fecha DESC}.
     *
     * <p>Casos de uso: CU-37
     * @param condiciones condiciones
     * @return fragmento SQL
     */
    static String armarSql(List<String> condiciones) {
        String donde = condiciones.isEmpty() ? "" : "WHERE " + String.join(" AND ", condiciones) + " ";
        return donde + "ORDER BY pago.fecha DESC, pago.id DESC";
    }
}

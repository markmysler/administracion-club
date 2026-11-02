package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.aplicacion.FiltroPagos;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.util.Comparator;
import java.util.List;

/**
 * Pagos de las cuotas de un grupo con el filtro (el grupo es el del socio de la sesión, RN-20: se ignora el
 * {@code idGrupo} del filtro), del más reciente al más antiguo. Consulta polimórfica sobre {@code Pago}. La usa
 * {@code ServicioPagos.consultarPagos} con CONSULTAR_PAGOS_PROPIOS. Consulta 49 de la Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-37
 */
public class PagosDeGrupo extends ConsultaEntidades<Pago> {

    /** Grupo del socio. */
    private final Long idGrupo;

    /** Rango, período y estado. */
    private final FiltroPagos filtro;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-37
     * @param idGrupo Grupo del socio.
     * @param filtro Filtro.
     */
    public PagosDeGrupo(Long idGrupo, FiltroPagos filtro) {
        this.idGrupo = idGrupo;
        this.filtro = filtro == null ? new FiltroPagos() : filtro;
    }

    /**
     * Clase del resultado: {@code Pago}.
     *
     * <p>Casos de uso: CU-37
     * @return Pago.class
     */
    @Override
    public Class<Pago> claseResultado() {
        return Pago.class;
    }

    /**
     * Condición y orden.
     *
     * <p>Casos de uso: CU-37
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return PagosPorFiltro.armarSql(PagosPorFiltro.condiciones(filtro, idGrupo));
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-37
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return PagosPorFiltro.parametrosDe(filtro, idGrupo);
    }

    /**
     * Orden de la unión: fecha descendente.
     *
     * <p>Casos de uso: CU-37
     * @return comparador
     */
    @Override
    public Comparator<Pago> comparador() {
        return PagosPorFiltro.porFechaDescendente();
    }
}

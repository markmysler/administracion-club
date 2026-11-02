package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.cuotas.dominio.ItemCuota;
import java.util.List;

/**
 * Ítems de una cuota; la ejecuta el {@code ProxyListaVirtual} de {@code Cuota.items} en el primer acceso
 * (Virtual Proxy). Consulta 44 de la Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-05, CU-35, CU-36
 */
public class ItemsDeCuota extends ConsultaEntidades<ItemCuota> {

    /** Cuota. */
    private final Long idCuota;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @param idCuota Cuota.
     */
    public ItemsDeCuota(Long idCuota) {
        this.idCuota = idCuota;
    }

    /**
     * Clase del resultado: {@code ItemCuota}.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @return ItemCuota.class
     */
    @Override
    public Class<ItemCuota> claseResultado() {
        return ItemCuota.class;
    }

    /**
     * Condición y orden que se agregan a {@code SELECT * FROM item_cuota}.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE item_cuota.cuota_id = ? ORDER BY item_cuota.id";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @return [idCuota]
     */
    @Override
    public List<Object> parametros() {
        return List.of(idCuota);
    }
}

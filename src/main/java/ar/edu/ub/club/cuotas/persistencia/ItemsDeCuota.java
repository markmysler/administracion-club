package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.cuotas.dominio.ItemCuota;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ItemsDeCuota extends ConsultaEntidades<ItemCuota> {

    /** Atributo idCuota. */
    private Long idCuota;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    public Class<ItemCuota> claseResultado() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    public String sql() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

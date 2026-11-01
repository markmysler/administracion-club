package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: todos
 */
public class MovimientosDeGrupo extends ConsultaEntidades<MovimientoSaldo> {

    /** Atributo idGrupo. */
    private final Long idGrupo;

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     * @param idGrupo grupo
     */
    public MovimientosDeGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    public Class<MovimientoSaldo> claseResultado() {
        return MovimientoSaldo.class;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    @Override
    public String sql() {
        return "WHERE movimiento_saldo.grupo_familiar_id = ? ORDER BY movimiento_saldo.fecha";
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        return List.of(idGrupo);
    }
}

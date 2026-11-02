package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import java.util.List;

/**
 * Movimientos de saldo a favor de un grupo, del más reciente al más antiguo. La usan {@code
 * ServicioCuotas.consultarSaldo} y el proxy {@code GrupoFamiliar.movimientos} de {@code BrokerGrupoFamiliar}.
 * Consulta 45 de la Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-05, CU-37
 */
public class MovimientosDeGrupo extends ConsultaEntidades<MovimientoSaldo> {

    /** Grupo familiar. */
    private final Long idGrupo;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param idGrupo Grupo familiar.
     */
    public MovimientosDeGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * Clase del resultado: {@code MovimientoSaldo}.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @return MovimientoSaldo.class
     */
    @Override
    public Class<MovimientoSaldo> claseResultado() {
        return MovimientoSaldo.class;
    }

    /**
     * Condición y orden que se agregan a {@code SELECT * FROM movimiento_saldo}.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE movimiento_saldo.grupo_familiar_id = ? ORDER BY movimiento_saldo.fecha DESC, movimiento_saldo.id DESC";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @return [idGrupo]
     */
    @Override
    public List<Object> parametros() {
        return List.of(idGrupo);
    }
}

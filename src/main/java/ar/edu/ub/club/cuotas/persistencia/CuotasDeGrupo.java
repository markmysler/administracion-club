package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.util.List;

/**
 * Cuotas de un grupo familiar, de la más reciente a la más antigua. La usan {@code
 * ServicioCuotas.consultarCuotasDeGrupo} (P-25, P-30) y el proxy {@code GrupoFamiliar.cuotas} de {@code
 * BrokerGrupoFamiliar}. Consulta 41 de la Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-04, CU-05, CU-10, CU-35, CU-36
 */
public class CuotasDeGrupo extends ConsultaEntidades<Cuota> {

    /** Grupo familiar. */
    private final Long idGrupo;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-04, CU-05, CU-10, CU-35, CU-36
     * @param idGrupo Grupo familiar.
     */
    public CuotasDeGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * Clase del resultado: {@code Cuota} (la resuelve {@code BrokerCuota}).
     *
     * <p>Casos de uso: CU-04, CU-05, CU-10, CU-35, CU-36
     * @return Cuota.class
     */
    @Override
    public Class<Cuota> claseResultado() {
        return Cuota.class;
    }

    /**
     * Condición y orden que se agregan a {@code SELECT * FROM cuota}.
     *
     * <p>Casos de uso: CU-04, CU-05, CU-10, CU-35, CU-36
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE cuota.grupo_familiar_id = ? ORDER BY cuota.periodo DESC";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-04, CU-05, CU-10, CU-35, CU-36
     * @return [idGrupo]
     */
    @Override
    public List<Object> parametros() {
        return List.of(idGrupo);
    }
}

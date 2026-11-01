package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.util.List;

/**
 * Cuotas impagas (Pendiente o VENCIDA) de un grupo, de la más antigua a la más reciente. La usa {@code
 * ServicioCuotas.aplicarSaldoAPendientes} para cancelar con saldo a favor la cuota impaga más antigua (RN-21).
 * Consulta 50 de MODELO §11.8.4.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-35, CU-36
 */
public class CuotasImpagasDeGrupo extends ConsultaEntidades<Cuota> {

    /** Grupo familiar. */
    private final Long idGrupo;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @param idGrupo Grupo familiar.
     */
    public CuotasImpagasDeGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * Clase del resultado: {@code Cuota}.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return Cuota.class
     */
    @Override
    public Class<Cuota> claseResultado() {
        return Cuota.class;
    }

    /**
     * Condición y orden que se agregan a {@code SELECT * FROM cuota}.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE cuota.grupo_familiar_id = ? AND cuota.estado IN ('PENDIENTE', 'VENCIDA')"
                + " ORDER BY cuota.periodo, cuota.id";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return [idGrupo]
     */
    @Override
    public List<Object> parametros() {
        return List.of(idGrupo);
    }
}

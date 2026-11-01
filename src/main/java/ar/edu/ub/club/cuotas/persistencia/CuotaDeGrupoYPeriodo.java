package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.util.List;

/**
 * Cuota de un grupo en un período (a lo sumo una por el UNIQUE {@code uk_cuota_grupo_periodo}). La usa el perfil
 * del socio para la cuota vigente ({@code ServicioSocios.consultarPerfilPropio}). Consulta 48 de MODELO §11.8.4.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-05
 */
public class CuotaDeGrupoYPeriodo extends ConsultaEntidades<Cuota> {

    /** Grupo familiar. */
    private final Long idGrupo;

    /** Período AAAA-MM. */
    private final String periodo;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-05
     * @param idGrupo Grupo familiar.
     * @param periodo Período AAAA-MM.
     */
    public CuotaDeGrupoYPeriodo(Long idGrupo, String periodo) {
        this.idGrupo = idGrupo;
        this.periodo = periodo;
    }

    /**
     * Clase del resultado: {@code Cuota}.
     *
     * <p>Casos de uso: CU-05
     * @return Cuota.class
     */
    @Override
    public Class<Cuota> claseResultado() {
        return Cuota.class;
    }

    /**
     * Condición que se agrega a {@code SELECT * FROM cuota}.
     *
     * <p>Casos de uso: CU-05
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE cuota.grupo_familiar_id = ? AND cuota.periodo = ?";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-05
     * @return [idGrupo, periodo]
     */
    @Override
    public List<Object> parametros() {
        return List.of(idGrupo, periodo);
    }
}

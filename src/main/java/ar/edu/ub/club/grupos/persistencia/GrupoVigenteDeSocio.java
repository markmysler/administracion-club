package ar.edu.ub.club.grupos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import java.util.List;

/**
 * Grupo familiar vigente del socio: el grupo al que pertenece hoy ({@code socio.grupo_familiar_id}). Un socio
 * siempre integra exactamente un grupo vigente (RN-02, RN-07), así que devuelve a lo sumo uno. La usan
 * {@code ServicioSocios} (CU-03, CU-04, CU-05), {@code ServicioGruposFamiliares.asociarSocio} (CU-07) y el módulo
 * cuotas.
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code GrupoFamiliar} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-35, CU-37
 */
public class GrupoVigenteDeSocio extends ConsultaEntidades<GrupoFamiliar> {

    /** Socio. */
    private final Long idSocio;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-35, CU-37
     * @param idSocio socio
     */
    public GrupoVigenteDeSocio(Long idSocio) {
        this.idSocio = idSocio;
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code GrupoFamiliar}.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-35, CU-37
     * @return {@code GrupoFamiliar.class}
     */
    @Override
    public Class<GrupoFamiliar> claseResultado() {
        return GrupoFamiliar.class;
    }

    /**
     * Condición con subconsulta sobre {@code socio}.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-35, CU-37
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE grupo_familiar.id = (SELECT socio.grupo_familiar_id FROM socio WHERE socio.id = ?)";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-35, CU-37
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(idSocio);
    }
}

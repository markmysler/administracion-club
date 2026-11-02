package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.List;

/**
 * Integrantes de un grupo familiar (socios cuyo {@code grupo_familiar_id} es el grupo). Es la consulta del
 * {@code ProxyListaVirtual} de {@code GrupoFamiliar.integrantes} (Virtual Proxy): se ejecuta en el primer acceso.
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code Socio} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-08, CU-09, CU-10
 */
public class SociosDeGrupo extends ConsultaEntidades<Socio> {

    /** Grupo familiar. */
    private final Long idGrupo;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-08, CU-09, CU-10
     * @param idGrupo grupo familiar
     */
    public SociosDeGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code Socio}.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-08, CU-09, CU-10
     * @return {@code Socio.class}
     */
    @Override
    public Class<Socio> claseResultado() {
        return Socio.class;
    }

    /**
     * Condición por grupo, ordenada por apellido y nombre.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-08, CU-09, CU-10
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE socio.grupo_familiar_id = ? ORDER BY socio.apellido, socio.nombre";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-08, CU-09, CU-10
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(idGrupo);
    }
}

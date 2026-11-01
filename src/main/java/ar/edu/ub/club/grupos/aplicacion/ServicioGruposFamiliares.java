package ar.edu.ub.club.grupos.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.List;

/**
 * Fachada del módulo grupos. Delega en {@code GrupoFamiliar} (Information Expert) las reglas de composición y
 * titularidad.
 *
 * <p>Casos de uso: CU-01, CU-07, CU-08, CU-09, CU-10, CU-36, CU-37
 */
public class ServicioGruposFamiliares {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code GruposPorCriterio}, {@code GrupoVigenteDeSocio}). */
    private FachadaPersistencia fachadaPersistencia;

    /** Verifica permisos. */
    private ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private UsuarioActual usuarioActual;

    /** Auditoría. */
    private ServicioAuditoria servicioAuditoria;

    /**
     * Quita al socio de su grupo actual (rechaza si es titular con otros integrantes activos) y lo agrega al grupo
     * destino con {@code agregarIntegrante}. Un grupo de origen que queda sin integrantes deja de estar vigente.
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}.
     *
     * <p>Casos de uso: CU-07
     * @param idSocio Socio a asociar.
     * @param idGrupoDestino Grupo destino.
     * @return {@code DetalleGrupo} del grupo afectado (destino en {@code asociarSocio}, origen en {@code quitarSocio}), para que el presentador muestre el resultado con una sola llamada.
     */
    public DetalleGrupo asociarSocio(Long idSocio, Long idGrupoDestino) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Invoca {@code cambiarTitular(socio)}, que exige que sea integrante activo.
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}.
     *
     * <p>Casos de uso: CU-08
     * @param idGrupo Grupo.
     * @param idNuevoTitular Integrante que pasa a ser titular.
     * @return {@code DetalleGrupo} del grupo con el nuevo titular, para que el presentador muestre el resultado con una sola llamada.
     */
    public DetalleGrupo cambiarTitular(Long idGrupo, Long idNuevoTitular) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Invoca {@code quitarIntegrante(socio)} y crea para el socio un grupo unipersonal del que es titular.
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}.
     *
     * <p>Casos de uso: CU-09
     * @param idGrupo Grupo de origen.
     * @param idSocio Integrante.
     * @return {@code DetalleGrupo} del grupo de origen, para que el presentador muestre el resultado con una sola llamada.
     */
    public DetalleGrupo quitarSocio(Long idGrupo, Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Busca grupos por DNI o nombre de un integrante o del titular.
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @param criterio Texto y vigencia.
     * @return Lista de {@code ResumenGrupo}.
     */
    public List<ResumenGrupo> buscarGrupos(CriterioBusquedaGrupo criterio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Arma el detalle con integrantes activos y {@code calcularImporteCuota()}.
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}.
     *
     * <p>Casos de uso: CU-10
     * @param idGrupo Grupo.
     * @return {@code DetalleGrupo}.
     */
    public DetalleGrupo consultarGrupo(Long idGrupo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Crea un grupo vigente con el socio como único integrante y titular. Se ejecuta dentro de la transacción del
     * llamador.
     *
     * <p>Casos de uso: CU-01, CU-09
     * @param socio Socio titular.
     * @return {@code GrupoFamiliar} creado.
     */
    public GrupoFamiliar crearGrupoUnipersonal(Socio socio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

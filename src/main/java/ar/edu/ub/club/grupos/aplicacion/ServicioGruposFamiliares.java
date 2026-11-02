package ar.edu.ub.club.grupos.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.grupos.persistencia.GrupoVigenteDeSocio;
import ar.edu.ub.club.grupos.persistencia.GruposPorCriterio;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada del módulo grupos (patrón Facade, Controlador de casos de uso de GRASP). Delega en {@code GrupoFamiliar}
 * (Information Expert) las reglas de composición y titularidad (RN-05, RN-06, RN-07) y guarda los cambios con
 * {@code FachadaPersistencia}. La primera instrucción de cada método con permiso es
 * {@code controlAcceso.verificar(usuario, permiso)}.
 *
 * <p>Cada método público es una transacción ({@code @Transactional}; las consultas con {@code readOnly = true}).
 * {@code crearGrupoUnipersonal} no abre una propia: se une a la del llamador ({@code ServicioSocios.registrarSocio}
 * o {@code quitarSocio}).
 *
 * <p>Casos de uso: CU-01, CU-07, CU-08, CU-09, CU-10, CU-36, CU-37
 */
@Service
@Transactional
public class ServicioGruposFamiliares {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code GruposPorCriterio}, {@code GrupoVigenteDeSocio}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private final UsuarioActual usuarioActual;

    /** Auditoría. */
    private final ServicioAuditoria servicioAuditoria;

    /**
     * Crea la fachada con sus colaboradores (inyección por constructor de Spring).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param fachadaPersistencia fachada de persistencia
     * @param controlAcceso verificador de permisos
     * @param usuarioActual cuenta de la sesión
     * @param servicioAuditoria auditoría
     */
    public ServicioGruposFamiliares(FachadaPersistencia fachadaPersistencia, ControlAcceso controlAcceso,
            UsuarioActual usuarioActual, ServicioAuditoria servicioAuditoria) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
        this.servicioAuditoria = servicioAuditoria;
    }

    /**
     * Quita al socio de su grupo actual (rechaza si es titular con otros integrantes activos) y lo agrega al grupo
     * destino con {@code agregarIntegrante}. Un grupo de origen que queda sin integrantes deja de estar vigente
     * (RN-07). Ambos grupos se guardan en la misma transacción ({@code sec_cu07_p3}).
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}.
     *
     * <p>Casos de uso: CU-07
     * @param idSocio Socio a asociar.
     * @param idGrupoDestino Grupo destino.
     * @return {@code DetalleGrupo} del grupo afectado (destino en {@code asociarSocio}, origen en {@code quitarSocio}), para que el presentador muestre el resultado con una sola llamada.
     * @throws ErrorNegocio si el socio o el titular destino están inactivos, el destino no es válido (FA2) o el
     *     socio es titular con otros integrantes activos (FA3)
     */
    public DetalleGrupo asociarSocio(Long idSocio, Long idGrupoDestino) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_GRUPOS);
        Socio socio = fachadaPersistencia.get(idSocio, Socio.class);
        GrupoFamiliar origen = fachadaPersistencia.buscarUno(new GrupoVigenteDeSocio(idSocio))
                .orElse(socio.getGrupoFamiliar());
        GrupoFamiliar destino = fachadaPersistencia.get(idGrupoDestino, GrupoFamiliar.class);
        if (!socio.estaActiva()) {
            throw new ErrorNegocio("El socio no está activo.");
        }
        if (!Boolean.TRUE.equals(destino.getVigente())) {
            throw new ErrorNegocio("El grupo destino no está vigente.");
        }
        if (destino.getTitular() == null || !destino.getTitular().estaActiva()) {
            throw new ErrorNegocio("El titular del grupo destino no está activo.");
        }
        if (destino.equals(origen)) {
            throw new ErrorNegocio("El grupo destino es el grupo actual del socio.");
        }
        origen.integrantesActivos();
        if (origen.esTitular(socio) && origen.tieneOtrosIntegrantesActivos(socio)) {
            throw new ErrorNegocio("El socio es titular de un grupo con otros integrantes activos: "
                    + "primero designe otro titular con CU-08.");
        }
        origen.quitarIntegrante(socio);
        destino.agregarIntegrante(socio);
        fachadaPersistencia.put(origen);
        fachadaPersistencia.put(destino);
        servicioAuditoria.registrar(usuario, "MODIFICACION", "GrupoFamiliar", destino.getId(),
                "{\"operacion\":\"asociarSocio\",\"idSocio\":" + idSocio + ",\"grupoOrigen\":" + origen.getId()
                        + ",\"origenVigente\":" + origen.getVigente() + "}");
        return detalle(destino);
    }

    /**
     * Invoca {@code cambiarTitular(socio)}, que exige que sea integrante activo (RN-05).
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}.
     *
     * <p>Casos de uso: CU-08
     * @param idGrupo Grupo.
     * @param idNuevoTitular Integrante que pasa a ser titular.
     * @return {@code DetalleGrupo} del grupo con el nuevo titular, para que el presentador muestre el resultado con una sola llamada.
     * @throws ErrorNegocio si el grupo no está vigente o el integrante no es válido (FA2)
     */
    public DetalleGrupo cambiarTitular(Long idGrupo, Long idNuevoTitular) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_GRUPOS);
        GrupoFamiliar grupo = fachadaPersistencia.get(idGrupo, GrupoFamiliar.class);
        if (!Boolean.TRUE.equals(grupo.getVigente())) {
            throw new ErrorNegocio("El grupo familiar no está vigente.");
        }
        Socio nuevo;
        try {
            nuevo = fachadaPersistencia.get(idNuevoTitular, Socio.class);
        } catch (RecursoNoEncontradoException e) {
            throw new ErrorNegocio("El integrante elegido no puede ser titular.");
        }
        Long anterior = grupo.getTitular() == null ? null : grupo.getTitular().getId();
        grupo.cambiarTitular(nuevo);
        fachadaPersistencia.put(grupo);
        servicioAuditoria.registrar(usuario, "MODIFICACION", "GrupoFamiliar", idGrupo,
                "{\"operacion\":\"cambiarTitular\",\"titularAnterior\":" + anterior + ",\"titularNuevo\":"
                        + nuevo.getId() + "}");
        return detalle(grupo);
    }

    /**
     * Invoca {@code quitarIntegrante(socio)} y crea para el socio un grupo unipersonal del que es titular (RN-07);
     * ambos grupos se guardan en la misma transacción ({@code sec_cu09_p2}).
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}.
     *
     * <p>Casos de uso: CU-09
     * @param idGrupo Grupo de origen.
     * @param idSocio Integrante.
     * @return {@code DetalleGrupo} del grupo de origen, para que el presentador muestre el resultado con una sola llamada.
     * @throws ErrorNegocio si el grupo es unipersonal, el socio no pertenece a él (FA1) o es el titular (FA2)
     */
    public DetalleGrupo quitarSocio(Long idGrupo, Long idSocio) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_GRUPOS);
        GrupoFamiliar grupo = fachadaPersistencia.get(idGrupo, GrupoFamiliar.class);
        Socio socio = fachadaPersistencia.get(idSocio, Socio.class);
        grupo.integrantesActivos();
        if (!grupo.getIntegrantes().contains(socio) || grupo.getIntegrantes().size() <= 1) {
            throw new ErrorNegocio("No corresponde quitar al socio: el grupo tiene un solo integrante o el socio no "
                    + "pertenece a él.");
        }
        if (grupo.esTitular(socio)) {
            throw new ErrorNegocio("El socio es el titular del grupo: primero designe otro titular con CU-08.");
        }
        grupo.quitarIntegrante(socio);
        GrupoFamiliar nuevo = crearGrupoUnipersonal(socio);
        fachadaPersistencia.put(grupo);
        servicioAuditoria.registrar(usuario, "MODIFICACION", "GrupoFamiliar", idGrupo,
                "{\"operacion\":\"quitarSocio\",\"idSocio\":" + idSocio + ",\"grupoNuevo\":" + nuevo.getId() + "}");
        return detalle(grupo);
    }

    /**
     * Busca grupos por DNI o nombre de un integrante o del titular ({@code GruposPorCriterio}) y arma un
     * {@code ResumenGrupo} por grupo.
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}, {@code REGISTRAR_PAGO_MANUAL} o {@code CONSULTAR_PAGOS_CLUB}.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @param criterio Texto y vigencia.
     * @return Lista de {@code ResumenGrupo}.
     * @throws AccesoDenegadoException si la cuenta no tiene ninguno de los tres permisos
     */
    @Transactional(readOnly = true)
    public List<ResumenGrupo> buscarGrupos(CriterioBusquedaGrupo criterio) {
        Usuario usuario = usuarioActual.obtener();
        if (!controlAcceso.tienePermiso(usuario, Permiso.GESTIONAR_GRUPOS)
                && !controlAcceso.tienePermiso(usuario, Permiso.REGISTRAR_PAGO_MANUAL)
                && !controlAcceso.tienePermiso(usuario, Permiso.CONSULTAR_PAGOS_CLUB)) {
            throw new AccesoDenegadoException("Falta el permiso " + Permiso.GESTIONAR_GRUPOS);
        }
        List<ResumenGrupo> resumenes = new ArrayList<>();
        for (GrupoFamiliar grupo : fachadaPersistencia.buscar(
                new GruposPorCriterio(criterio.getTexto(), criterio.getVigente()))) {
            Socio titular = grupo.getTitular();
            resumenes.add(new ResumenGrupo(grupo.getId(), titular == null ? "" : titular.nombreCompleto(),
                    titular == null ? "" : titular.getDni(), grupo.getIntegrantes().size(), grupo.getVigente()));
        }
        return resumenes;
    }

    /**
     * Arma el detalle con integrantes activos y {@code calcularImporteCuota()}.
     *
     * <p>Permiso: {@code GESTIONAR_GRUPOS}.
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param idGrupo Grupo.
     * @return {@code DetalleGrupo}.
     * @throws RecursoNoEncontradoException si el grupo no existe
     */
    @Transactional(readOnly = true)
    public DetalleGrupo consultarGrupo(Long idGrupo) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_GRUPOS);
        GrupoFamiliar grupo = fachadaPersistencia.get(idGrupo, GrupoFamiliar.class);
        return detalle(grupo);
    }

    /**
     * Crea un grupo vigente con el socio como único integrante y titular (RN-02, RN-07). Se ejecuta dentro de la
     * transacción del llamador. Si el socio es nuevo (CU-01), {@code put(g)} también lo inserta (ver
     * {@code BrokerGrupoFamiliar}).
     *
     * <p>Permiso: ninguno (lo verifica el llamador).
     *
     * <p>Casos de uso: CU-01, CU-09
     * @param socio Socio titular.
     * @return {@code GrupoFamiliar} creado.
     */
    public GrupoFamiliar crearGrupoUnipersonal(Socio socio) {
        GrupoFamiliar grupo = new GrupoFamiliar(LocalDate.now(RelojSistema.ZONA_CLUB));
        grupo.agregarIntegrante(socio);
        grupo.cambiarTitular(socio);
        fachadaPersistencia.put(grupo);
        return grupo;
    }

    /**
     * Arma el {@code DetalleGrupo} cargando los integrantes dentro de la transacción (el presentador los recorre
     * después) y calculando el importe de la próxima cuota.
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     * @param grupo grupo familiar
     * @return detalle del grupo
     */
    private static DetalleGrupo detalle(GrupoFamiliar grupo) {
        grupo.getIntegrantes().size();
        return new DetalleGrupo(grupo, grupo.calcularImporteCuota());
    }
}

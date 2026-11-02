package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.GestorSesiones;
import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.CodificadorClave;
import ar.edu.ub.club.accesos.dominio.PoliticaContrasenas;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.accesos.persistencia.UsuarioPorEmail;
import ar.edu.ub.club.accesos.persistencia.UsuarioPorUsername;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.ConsultaReservasFuturas;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.dominio.ResultadoBaja;
import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.grupos.persistencia.GrupoVigenteDeSocio;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import ar.edu.ub.club.socios.dominio.Socio;
import ar.edu.ub.club.socios.dominio.SocioDesactivadoEvento;
import ar.edu.ub.club.socios.persistencia.CategoriaPorNombre;
import ar.edu.ub.club.socios.persistencia.CategoriasActivas;
import ar.edu.ub.club.socios.persistencia.CategoriasSocio;
import ar.edu.ub.club.socios.persistencia.SocioPorDni;
import ar.edu.ub.club.socios.persistencia.SociosActivosDeCategoria;
import ar.edu.ub.club.socios.persistencia.SociosPorCriterio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada del módulo socios (patrón Facade, Controlador de casos de uso de GRASP). Coordina el alta atómica de
 * socio, grupo y credenciales (CU-01), la modificación (CU-02), la baja lógica en dos pasos con sus controles
 * (CU-03), las consultas de la ficha (CU-04) y del perfil propio (CU-05) y la gestión de categorías (CU-06).
 * Delega las reglas en el dominio ({@code Socio}, {@code CategoriaSocio}, {@code GrupoFamiliar}, Information
 * Expert) y la persistencia en {@code FachadaPersistencia}. La primera instrucción de cada método con permiso es
 * {@code controlAcceso.verificar(usuario, permiso)} (RNF-04).
 *
 * <p>Cada método público es una transacción ({@code @Transactional}; las consultas con {@code readOnly = true}):
 * si se lanza una excepción no controlada se revierte completa (RNF-13).
 *
 * <p>Casos de uso: CU-01, CU-02, CU-03, CU-04, CU-05, CU-06, CU-16, CU-28, CU-35
 */
@Service
@Transactional
public class ServicioSocios {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code SocioPorDni}, {@code SociosPorCriterio}, {@code SociosActivosDeCategoria}, {@code CategoriasSocio}, {@code CategoriaPorNombre}, {@code UsuarioPorUsername}, {@code UsuarioPorEmail}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Fachada de grupos (creación del grupo unipersonal). */
    private final ServicioGruposFamiliares servicioGrupos;

    /** Consulta invertida de reservas futuras del socio (la implementa {@code ConsultaReservasFuturasReservas}). */
    private final ConsultaReservasFuturas consultaReservasFuturas;

    /** Publica {@code SocioDesactivadoEvento}. */
    private final ApplicationEventPublisher publicadorEventos;

    /** Hash de la clave inicial. */
    private final CodificadorClave codificadorClave;

    /** Valida la contraseña inicial (RNF-03). */
    private final PoliticaContrasenas politicaContrasenas;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private final UsuarioActual usuarioActual;

    /** Auditoría de cambios. */
    private final ServicioAuditoria servicioAuditoria;

    /** Revoca tokens persistentes e invalida sesiones del socio dado de baja. */
    private final GestorSesiones gestorSesiones;

    /**
     * Crea la fachada con sus colaboradores (inyección por constructor de Spring).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param fachadaPersistencia fachada de persistencia
     * @param servicioGrupos fachada de grupos
     * @param consultaReservasFuturas consulta invertida de reservas futuras
     * @param publicadorEventos publicador de eventos de Spring
     * @param codificadorClave codificador BCrypt
     * @param politicaContrasenas política de contraseñas
     * @param controlAcceso verificador de permisos
     * @param usuarioActual cuenta de la sesión
     * @param servicioAuditoria auditoría
     * @param gestorSesiones gestor de sesiones y tokens
     */
    public ServicioSocios(FachadaPersistencia fachadaPersistencia, ServicioGruposFamiliares servicioGrupos,
            ConsultaReservasFuturas consultaReservasFuturas, ApplicationEventPublisher publicadorEventos,
            CodificadorClave codificadorClave, PoliticaContrasenas politicaContrasenas, ControlAcceso controlAcceso,
            UsuarioActual usuarioActual, ServicioAuditoria servicioAuditoria, GestorSesiones gestorSesiones) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.servicioGrupos = servicioGrupos;
        this.consultaReservasFuturas = consultaReservasFuturas;
        this.publicadorEventos = publicadorEventos;
        this.codificadorClave = codificadorClave;
        this.politicaContrasenas = politicaContrasenas;
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
        this.servicioAuditoria = servicioAuditoria;
        this.gestorSesiones = gestorSesiones;
    }

    /**
     * Verifica DNI, usuario y correo únicos y categoría activa; aplica {@code politicaContrasenas.validar(clave,
     * username)} (longitud de 15 a 64 caracteres, no figura en la lista de contraseñas prohibidas, distinta del
     * nombre de usuario) a la contraseña inicial que elige el socio; instancia {@code Socio} (tipo de cuenta Socio,
     * CU-16) con la clave codificada; pide a {@code ServicioGruposFamiliares.crearGrupoUnipersonal} el grupo del que
     * será titular; guarda todo en una única transacción y audita.
     *
     * <p>El {@code put(s)} del diagrama ({@code sec_cu01_p3}, mensaje 1.10) lo realiza {@code put(g)} dentro de
     * {@code crearGrupoUnipersonal}: {@code BrokerGrupoFamiliar} inserta el grupo sin titular, inserta al socio
     * (que necesita {@code grupo_familiar_id}) y completa titular y vigencia, por la restricción no diferible
     * {@code ck_grupo_titular_vigente}.
     *
     * <p>Permiso: {@code GESTIONAR_SOCIOS}.
     *
     * <p>Casos de uso: CU-01, CU-16
     * @param datos Datos personales, categoría y credenciales iniciales.
     * @return {@code Socio} creado.
     * @throws ErrorNegocio con el campo a corregir (CU-01 FA3 a FA7)
     */
    public Socio registrarSocio(DatosSocio datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_SOCIOS);
        exigir(datos.getUsername(), "username", "El nombre de usuario es obligatorio.");
        exigir(datos.getEmail(), "email", "El correo electrónico es obligatorio.");
        exigir(datos.getClaveInicial(), "claveInicial", "La contraseña inicial es obligatoria.");
        politicaContrasenas.validar(datos.getClaveInicial(), datos.getUsername());
        if (fachadaPersistencia.contar(new SocioPorDni(recortar(datos.getDni()))) > 0) {
            throw new ErrorNegocio("dni", "El DNI ya pertenece a un socio.");
        }
        CategoriaSocio categoria = categoriaAsignable(datos.getIdCategoria());
        if (fachadaPersistencia.contar(new UsuarioPorUsername(datos.getUsername().trim())) > 0) {
            throw new ErrorNegocio("username", "El nombre de usuario ya está en uso.");
        }
        if (fachadaPersistencia.contar(new UsuarioPorEmail(datos.getEmail().trim())) > 0) {
            throw new ErrorNegocio("email", "El correo electrónico ya está en uso.");
        }
        datos.setUsername(datos.getUsername().trim());
        datos.setEmail(datos.getEmail().trim());
        String hash = codificadorClave.codificar(datos.getClaveInicial());
        Socio socio = new Socio(datos, categoria, hash);
        servicioGrupos.crearGrupoUnipersonal(socio);
        servicioAuditoria.registrar(usuario, "ALTA", "Socio", socio.getId(), resumenSocio(socio));
        return socio;
    }

    /**
     * Verifica unicidad de DNI; {@code socio.actualizarDatos(datos, categoria)} (Information Expert) y audita los
     * valores anteriores y los nuevos (RNF-12).
     *
     * <p>Permiso: {@code GESTIONAR_SOCIOS}.
     *
     * <p>Casos de uso: CU-02
     * @param id Socio.
     * @param datos Datos modificados.
     * @return {@code Socio} modificado.
     * @throws ErrorNegocio si el DNI está en uso o la categoría está inactiva (CU-02 FA4, FA5)
     * @throws RecursoNoEncontradoException si el socio no existe
     */
    public Socio modificarSocio(Long id, DatosSocio datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_SOCIOS);
        Socio socio = fachadaPersistencia.get(id, Socio.class);
        String nuevoDni = recortar(datos.getDni());
        if (!Objects.equals(nuevoDni, socio.getDni())
                && fachadaPersistencia.contar(new SocioPorDni(nuevoDni)) > 0) {
            throw new ErrorNegocio("dni", "El DNI ya está en uso por otro socio.");
        }
        CategoriaSocio categoria = socio.getCategoria();
        if (!Objects.equals(datos.getIdCategoria(), categoria.getId())) {
            categoria = categoriaAsignable(datos.getIdCategoria());
        }
        String anterior = resumenSocio(socio);
        socio.actualizarDatos(datos, categoria);
        fachadaPersistencia.put(socio);
        servicioAuditoria.registrar(usuario, "MODIFICACION", "Socio", socio.getId(),
                "{\"anterior\":" + anterior + ",\"nuevo\":" + resumenSocio(socio) + "}");
        return socio;
    }

    /**
     * Si el socio es titular de un grupo con otros integrantes activos exige {@code idNuevoTitular} (integrante
     * activo) y cambia el titular; sin él lanza {@code ErrorNegocio}. Obtiene las reservas afectadas con {@code
     * consultaReservasFuturas.buscarFuturasDeSocio(id)}; si no se confirmó devuelve {@code
     * ResultadoBaja(false, reservasAfectadas)} sin cambios (primer paso del diálogo P-09, aunque la lista esté
     * vacía, como en {@code sec_cu03_p3}). Si se confirma: {@code desactivar()}, publica
     * {@code SocioDesactivadoEvento}, {@code put(s)}, {@code gestorSesiones.revocarTokensDe(socio)} e
     * {@code invalidarSesionesDe(socio)} y audita. {@code CanceladorReservasPorBaja} cancela las reservas futuras
     * en la misma transacción (RN-16): si una cancelación falla, la baja se revierte.
     *
     * <p>Permiso: {@code GESTIONAR_SOCIOS}.
     *
     * <p>Casos de uso: CU-03
     * @param id Socio.
     * @param idNuevoTitular Nuevo titular del grupo (nulo si no corresponde).
     * @param confirmar Confirmación del administrador tras ver las reservas afectadas.
     * @return {@code ResultadoBaja} (realizada, reservasAfectadas).
     * @throws ErrorNegocio si el socio ya está inactivo (FA1) o falta o no es válido el nuevo titular (FA5, FA6)
     */
    public ResultadoBaja desactivarSocio(Long id, Long idNuevoTitular, boolean confirmar) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_SOCIOS);
        Socio socio = fachadaPersistencia.get(id, Socio.class);
        if (!socio.estaActiva()) {
            throw new ErrorNegocio("El socio ya está dado de baja.");
        }
        GrupoFamiliar grupo = grupoDe(socio);
        boolean titularConOtros = grupo.esTitular(socio) && grupo.tieneOtrosIntegrantesActivos(socio);
        List<ResumenReserva> reservasAfectadas = consultaReservasFuturas.buscarFuturasDeSocio(id);
        if (!confirmar) {
            return ResultadoBaja.pendiente(reservasAfectadas);
        }
        if (titularConOtros) {
            if (idNuevoTitular == null) {
                throw new ErrorNegocio("idNuevoTitular", "Debe designarse un nuevo titular antes de la baja.");
            }
            Socio nuevo = socioOError(idNuevoTitular, "El integrante elegido ya no puede ser titular.");
            if (nuevo.equals(socio)) {
                throw new ErrorNegocio("idNuevoTitular", "El nuevo titular debe ser otro integrante del grupo.");
            }
            grupo.cambiarTitular(nuevo);
            fachadaPersistencia.put(grupo);
        }
        socio.desactivar();
        LocalDateTime ahora = LocalDateTime.now(RelojSistema.ZONA_CLUB);
        publicadorEventos.publishEvent(new SocioDesactivadoEvento(id, usuario, ahora));
        fachadaPersistencia.put(socio);
        gestorSesiones.revocarTokensDe(socio);
        gestorSesiones.invalidarSesionesDe(socio);
        servicioAuditoria.registrar(usuario, "BAJA", "Socio", id, "{\"dni\":" + texto(socio.getDni())
                + ",\"reservasCanceladas\":" + reservasAfectadas.size() + ",\"nuevoTitular\":"
                + (titularConOtros ? idNuevoTitular : "null") + "}");
        return ResultadoBaja.realizada(reservasAfectadas);
    }

    /**
     * Arma la ficha del socio con su grupo (integrantes y titular), sus reservas futuras y el estado de cuenta del
     * grupo (sus cuotas). Las colecciones que recorre la vista se cargan aquí, dentro de la transacción.
     *
     * <p>Permiso: {@code GESTIONAR_SOCIOS}.
     *
     * <p>Casos de uso: CU-02, CU-04, CU-28
     * @param id Socio.
     * @return {@code FichaSocio}.
     * @throws RecursoNoEncontradoException si el socio no existe (CU-04 FA2)
     */
    @Transactional(readOnly = true)
    public FichaSocio consultarSocio(Long id) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_SOCIOS);
        Socio socio = fachadaPersistencia.get(id, Socio.class);
        GrupoFamiliar grupo = grupoDe(socio);
        grupo.integrantesActivos();
        List<ResumenReserva> reservas = consultaReservasFuturas.buscarFuturasDeSocio(id);
        List<Cuota> estadoCuenta = cuotasCargadas(grupo);
        grupo.saldoAFavor();
        return new FichaSocio(socio, grupo, reservas, estadoCuenta);
    }

    /**
     * Busca por DNI, nombre o apellido y estado ({@code SociosPorCriterio}).
     *
     * <p>Permiso: {@code GESTIONAR_SOCIOS}.
     *
     * <p>Casos de uso: CU-04, CU-07, CU-10, CU-28
     * @param criterio Criterio de búsqueda.
     * @return Lista de {@code Socio}.
     */
    @Transactional(readOnly = true)
    public List<Socio> buscarSocios(CriterioBusquedaSocio criterio) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_SOCIOS);
        return fachadaPersistencia.buscar(new SociosPorCriterio(criterio.getTexto(), criterio.getEstado()));
    }

    /**
     * Arma el perfil del socio autenticado: datos, grupo con sus integrantes, cuota vigente (la última emitida) y
     * próximas reservas. Los comprobantes y los movimientos de saldo los consulta el presentador con las
     * fachadas de pagos y cuotas.
     *
     * <p>Permiso: {@code CONSULTAR_PERFIL_PROPIO}.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param socio Socio de la sesión.
     * @return {@code PerfilSocio}.
     */
    @Transactional(readOnly = true)
    public PerfilSocio consultarPerfilPropio(Socio socio) {
        controlAcceso.verificar(socio, Permiso.CONSULTAR_PERFIL_PROPIO);
        GrupoFamiliar grupo = fachadaPersistencia.buscarUno(new GrupoVigenteDeSocio(socio.getId()))
                .orElseThrow(() -> new RecursoNoEncontradoException(GrupoFamiliar.class, socio.getId()));
        grupo.integrantesActivos();
        Cuota cuotaVigente = cuotasCargadas(grupo).stream()
                .max(Comparator.comparing(Cuota::getPeriodo)).orElse(null);
        List<ResumenReserva> proximasReservas = consultaReservasFuturas.buscarFuturasDeSocio(socio.getId());
        return new PerfilSocio(socio, grupo, cuotaVigente, proximasReservas);
    }

    /**
     * Devuelve las categorías ordenadas por nombre (activas e inactivas: P-10 las muestra todas y P-07 las filtra
     * para el selector).
     *
     * <p>Permiso: ninguno (lo usan P-07 y P-10, que ya verificaron el permiso de la vista).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06
     * @return Lista de {@code CategoriaSocio}.
     */
    @Transactional(readOnly = true)
    public List<CategoriaSocio> listarCategorias() {
        return fachadaPersistencia.buscar(new CategoriasSocio());
    }

    /**
     * Devuelve las categorías que pueden asignarse a socios ({@code fachadaPersistencia.buscar(CategoriasActivas())}),
     * ordenadas por nombre: el selector del formulario de socio (P-07) solo ofrece categorías activas.
     *
     * <p>Permiso: ninguno (lo usa P-07, que ya verificó GESTIONAR_SOCIOS en la vista; las operaciones de alta y
     * modificación lo verifican).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return lista de {@code CategoriaSocio} activas
     */
    @Transactional(readOnly = true)
    public List<CategoriaSocio> listarCategoriasActivas() {
        return fachadaPersistencia.buscar(new CategoriasActivas());
    }

    /**
     * Crea una categoría con nombre único e importe no negativo y audita.
     *
     * <p>Permiso: {@code GESTIONAR_CATEGORIAS}.
     *
     * <p>Casos de uso: CU-06
     * @param datos Nombre, importe y estado.
     * @return {@code CategoriaSocio}.
     * @throws ErrorNegocio si el nombre está repetido o los datos son inválidos (CU-06 FA2, FA3)
     */
    public CategoriaSocio registrarCategoria(DatosCategoria datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_CATEGORIAS);
        verificarNombreLibre(datos.getNombre(), null);
        CategoriaSocio categoria = new CategoriaSocio(datos.getNombre(), datos.getImporte(), datos.getActiva());
        fachadaPersistencia.put(categoria);
        servicioAuditoria.registrar(usuario, "ALTA", "CategoriaSocio", categoria.getId(), resumenCategoria(categoria));
        return categoria;
    }

    /**
     * Si se desactiva, verifica con {@code fachadaPersistencia.contar(SociosActivosDeCategoria(id))} que no esté
     * asignada a socios activos ({@code ErrorNegocio}); {@code categoria.modificar(nombre, importe, activa)} y
     * audita. No altera cuotas ya emitidas (RN-08).
     *
     * <p>Permiso: {@code GESTIONAR_CATEGORIAS}.
     *
     * <p>Casos de uso: CU-06
     * @param id Categoría.
     * @param datos Datos modificados.
     * @return {@code CategoriaSocio}.
     * @throws ErrorNegocio si el nombre está repetido o la categoría está en uso (CU-06 FA3, FA4)
     */
    public CategoriaSocio modificarCategoria(Long id, DatosCategoria datos) {
        Usuario usuario = usuarioActual.obtener();
        controlAcceso.verificar(usuario, Permiso.GESTIONAR_CATEGORIAS);
        verificarNombreLibre(datos.getNombre(), id);
        CategoriaSocio categoria = fachadaPersistencia.get(id, CategoriaSocio.class);
        if (categoria.estaActiva() && !datos.getActiva()) {
            long enUso = fachadaPersistencia.contar(new SociosActivosDeCategoria(id));
            if (enUso > 0) {
                throw new ErrorNegocio("activa", "La categoría está asignada a " + enUso
                        + " socio(s) activo(s): primero cambie su categoría (CU-02).");
            }
        }
        String anterior = resumenCategoria(categoria);
        categoria.modificar(datos.getNombre(), datos.getImporte(), datos.getActiva());
        fachadaPersistencia.put(categoria);
        servicioAuditoria.registrar(usuario, "MODIFICACION", "CategoriaSocio", id,
                "{\"anterior\":" + anterior + ",\"nuevo\":" + resumenCategoria(categoria) + "}");
        return categoria;
    }

    // ------------------------------------------------------------------ auxiliares privados

    /**
     * Obtiene la categoría elegida y verifica que exista y esté activa (CU-01 paso 7, CU-02 paso 8).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param idCategoria categoría elegida
     * @return la categoría activa
     * @throws ErrorNegocio en el campo {@code idCategoria} si falta, no existe o está inactiva (FA6 / FA5)
     */
    private CategoriaSocio categoriaAsignable(Long idCategoria) {
        if (idCategoria == null) {
            throw new ErrorNegocio("idCategoria", "Debe elegir una categoría.");
        }
        CategoriaSocio categoria;
        try {
            categoria = fachadaPersistencia.get(idCategoria, CategoriaSocio.class);
        } catch (RecursoNoEncontradoException e) {
            throw new ErrorNegocio("idCategoria", "La categoría no está disponible.");
        }
        if (!categoria.estaActiva()) {
            throw new ErrorNegocio("idCategoria", "La categoría no está disponible.");
        }
        return categoria;
    }

    /**
     * Grupo vigente del socio ({@code buscarUno(GrupoVigenteDeSocio(id))}); si la consulta no devuelve nada usa
     * la asociación ya materializada del socio.
     *
     * <p>Casos de uso: CU-03, CU-04
     * @param socio socio
     * @return su grupo familiar
     */
    private GrupoFamiliar grupoDe(Socio socio) {
        return fachadaPersistencia.buscarUno(new GrupoVigenteDeSocio(socio.getId()))
                .orElse(socio.getGrupoFamiliar());
    }

    /**
     * Obtiene un socio o traduce su inexistencia a {@code ErrorNegocio} en el campo {@code idNuevoTitular}.
     *
     * <p>Casos de uso: CU-03
     * @param id socio
     * @param mensaje mensaje si no existe
     * @return el socio
     */
    private Socio socioOError(Long id, String mensaje) {
        try {
            return fachadaPersistencia.get(id, Socio.class);
        } catch (RecursoNoEncontradoException e) {
            throw new ErrorNegocio("idNuevoTitular", mensaje);
        }
    }

    /**
     * Carga, dentro de la transacción, las cuotas del grupo y sus ítems (los recorre {@code FilaCuota.desde} en
     * el presentador, fuera de la transacción) y devuelve una copia ordenada por período descendente.
     *
     * <p>Casos de uso: CU-04, CU-05
     * @param grupo grupo familiar
     * @return cuotas del grupo, de la más reciente a la más antigua
     */
    private static List<Cuota> cuotasCargadas(GrupoFamiliar grupo) {
        List<Cuota> cuotas = new ArrayList<>(grupo.getCuotas());
        for (Cuota cuota : cuotas) {
            cuota.getItems().size();
        }
        cuotas.sort(Comparator.comparing(Cuota::getPeriodo).reversed());
        return cuotas;
    }

    /**
     * Verifica que no exista otra categoría con el mismo nombre (CU-06 paso 7).
     *
     * <p>Casos de uso: CU-06
     * @param nombre nombre propuesto
     * @param idPropio categoría que se modifica ({@code null} en el alta)
     * @throws ErrorNegocio en el campo {@code nombre} si está repetido (FA3)
     */
    private void verificarNombreLibre(String nombre, Long idPropio) {
        for (CategoriaSocio otra : fachadaPersistencia.buscar(new CategoriaPorNombre(nombre))) {
            if (!otra.getId().equals(idPropio)) {
                throw new ErrorNegocio("nombre", "Ya existe una categoría con ese nombre.");
            }
        }
    }

    /**
     * Exige un texto obligatorio del formulario.
     *
     * <p>Casos de uso: CU-01
     * @param valor valor ingresado
     * @param campo propiedad de {@code DatosSocio}
     * @param mensaje mensaje del error
     * @throws ErrorNegocio si está vacío
     */
    private static void exigir(String valor, String campo, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new ErrorNegocio(campo, mensaje);
        }
    }

    /**
     * Recorta un texto opcional.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param valor texto
     * @return texto recortado o {@code null}
     */
    private static String recortar(String valor) {
        return valor == null ? null : valor.trim();
    }

    /**
     * Detalle JSON del socio para la auditoría (datos personales y categoría; nunca la clave, RNF-06).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param socio socio
     * @return objeto JSON
     */
    private static String resumenSocio(Socio socio) {
        return "{\"dni\":" + texto(socio.getDni()) + ",\"nombre\":" + texto(socio.getNombre()) + ",\"apellido\":"
                + texto(socio.getApellido()) + ",\"fechaNacimiento\":" + texto(String.valueOf(socio.getFechaNacimiento()))
                + ",\"telefono\":" + texto(socio.getTelefono()) + ",\"direccion\":" + texto(socio.getDireccion())
                + ",\"categoria\":" + texto(socio.getCategoria() == null ? null : socio.getCategoria().getNombre()) + "}";
    }

    /**
     * Detalle JSON de la categoría para la auditoría.
     *
     * <p>Casos de uso: CU-06
     * @param categoria categoría
     * @return objeto JSON
     */
    private static String resumenCategoria(CategoriaSocio categoria) {
        return "{\"nombre\":" + texto(categoria.getNombre()) + ",\"importe\":" + categoria.getImporte().getMonto()
                + ",\"activa\":" + categoria.getActiva() + "}";
    }

    /**
     * Literal JSON de un texto (con comillas y caracteres escapados) o {@code null}.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-06
     * @param valor texto
     * @return literal JSON
     */
    private static String texto(String valor) {
        if (valor == null) {
            return "null";
        }
        return "\"" + valor.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
}

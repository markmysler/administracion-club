package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.accesos.dominio.CodificadorClave;
import ar.edu.ub.club.accesos.dominio.PoliticaContrasenas;
import ar.edu.ub.club.accesos.dominio.TokenRecuperacion;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.accesos.persistencia.Administradores;
import ar.edu.ub.club.accesos.persistencia.AdministradoresActivos;
import ar.edu.ub.club.accesos.persistencia.CuentasPorCriterio;
import ar.edu.ub.club.accesos.persistencia.TokenRecuperacionPorHash;
import ar.edu.ub.club.accesos.persistencia.TokensRecuperacionVigentes;
import ar.edu.ub.club.accesos.persistencia.UsuarioPorEmail;
import ar.edu.ub.club.accesos.persistencia.UsuarioPorUsername;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.notificaciones.integracion.MensajeCorreo;
import ar.edu.ub.club.notificaciones.integracion.ResultadoEnvio;
import ar.edu.ub.club.notificaciones.integracion.ServicioCorreo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada (GoF Facade) y controlador de casos de uso del módulo accesos. Coordina {@code Usuario}, {@code
 * TokenRecuperacion}, {@code PoliticaBloqueo}, el codificador de claves y el servicio de correo. Concentra las
 * reglas de bloqueo por intentos (5 intentos, 15 minutos), de recuperación con token de un solo uso y de
 * protección del último administrador activo.
 *
 * <p>Cada método público es una transacción ({@code @Transactional} en la clase; las consultas con
 * {@code readOnly = true}); la primera instrucción de cada operación con permiso es
 * {@code controlAcceso.verificar(usuarioActual.obtener(), permiso)} ({@code sec_comun_autorizar.puml}).
 *
 * <p>Casos de uso: CU-11, CU-12, CU-13, CU-14, CU-15, CU-16, CU-17
 */
@Service
@Transactional
public class ServicioAccesos {

    /** Bitácora técnica (nunca registra contraseñas ni tokens). */
    private static final Logger BITACORA = LoggerFactory.getLogger(ServicioAccesos.class);

    /** Mensaje único para usuario inexistente o clave incorrecta (CU-11 FA4, FA6: no revela cuál falló). */
    private static final String CREDENCIALES_INVALIDAS = "Usuario o contraseña incorrectos.";

    /** Mensaje del enlace de recuperación inexistente, usado o vencido (CU-13 FA4). */
    private static final String ENLACE_INVALIDO = "El enlace no es válido o ya venció. Solicite uno nuevo.";

    /** Vigencia del token de recuperación (RN-05: 30 minutos). */
    private static final Duration VIGENCIA_RECUPERACION = Duration.ofMinutes(30);

    /** Generador de tokens de recuperación. */
    private static final SecureRandom ALEATORIO = new SecureRandom();

    /** Serializa el detalle JSON de la auditoría. */
    private static final ObjectMapper JSON = new ObjectMapper();

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code UsuarioPorUsername}, {@code UsuarioPorEmail}, {@code CuentasPorCriterio}, {@code Administradores}, {@code AdministradoresActivos}, {@code TokenRecuperacionPorHash}, {@code TokensRecuperacionVigentes}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Calcula y verifica hashes BCrypt. */
    private final CodificadorClave codificadorClave;

    /** Puerto de envío del enlace de recuperación. */
    private final ServicioCorreo servicioCorreo;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private final UsuarioActual usuarioActual;

    /** Registro de altas, bajas y modificaciones de cuentas. */
    private final ServicioAuditoria servicioAuditoria;

    /** Emite y revoca tokens persistentes e invalida sesiones. */
    private final GestorSesiones gestorSesiones;

    /** Valida las contraseñas nuevas (RNF-03). */
    private final PoliticaContrasenas politicaContrasenas;

    /** Fecha y hora actual. */
    private final Reloj reloj;

    /** URL pública del sistema para armar el enlace de P-03 ({@code club.url-base}). Agregado en la implementación. */
    private final String urlBase;

    /**
     * Crea la fachada con sus colaboradores (inyectados por Spring).
     *
     * <p>Casos de uso: CU-11, CU-12, CU-13, CU-14, CU-15, CU-16, CU-17
     * @param fachadaPersistencia fachada de persistencia
     * @param codificadorClave codificador BCrypt
     * @param servicioCorreo puerto de correo (adaptador SMTP o simulado)
     * @param controlAcceso verificación de permisos
     * @param usuarioActual cuenta de la sesión
     * @param servicioAuditoria auditoría
     * @param gestorSesiones sesiones y tokens persistentes
     * @param politicaContrasenas política de contraseñas
     * @param reloj reloj del sistema
     * @param urlBase URL pública del sistema (propiedad {@code club.url-base})
     */
    public ServicioAccesos(FachadaPersistencia fachadaPersistencia, CodificadorClave codificadorClave,
                           ServicioCorreo servicioCorreo, ControlAcceso controlAcceso, UsuarioActual usuarioActual,
                           ServicioAuditoria servicioAuditoria, GestorSesiones gestorSesiones,
                           PoliticaContrasenas politicaContrasenas, Reloj reloj,
                           @Value("${club.url-base:http://localhost:8080}") String urlBase) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.codificadorClave = codificadorClave;
        this.servicioCorreo = servicioCorreo;
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
        this.servicioAuditoria = servicioAuditoria;
        this.gestorSesiones = gestorSesiones;
        this.politicaContrasenas = politicaContrasenas;
        this.reloj = reloj;
        this.urlBase = urlBase;
    }

    /**
     * Busca la cuenta por usuario, rechaza si está inactiva o bloqueada ({@code estaBloqueada(ahora)}), verifica la
     * clave con {@code autenticar}. Si falla, {@code registrarIntentoFallido(ahora)} (al quinto intento fija {@code
     * bloqueadoHasta}). Si es correcta, {@code reiniciarIntentos()} y arma la sesión con {@code obtenerPermisos()},
     * resuelto por polimorfismo según la subclase. Si {@code mantenerSesion} es verdadero y {@code
     * usuario.admiteSesionPersistente()} (verdadero para Socio, falso para Administrador, sin preguntar por el
     * tipo), {@code gestorSesiones.emitirTokenPersistente(usuario)} emite el token de 30 días. El método se declara
     * {@code &#64;Transactional(noRollbackFor = ErrorNegocio.class)} para que el intento fallido y el bloqueo queden
     * guardados aunque se rechace el ingreso.
     *
     * <p>Orden de las verificaciones ({@code sec_cu11_p2.puml}, {@code sec_cu11_p3.puml}): existencia (FA4),
     * bloqueo (FA5), clave (FA6/FA7), estado activo (FA8).
     *
     * <p>Casos de uso: CU-11
     * @param username Nombre de usuario.
     * @param clave Contraseña en texto plano; no se almacena.
     * @param mantenerSesion Casilla «Mantener la sesión iniciada» de P-01.
     * @return {@code SesionUsuario} (idUsuario, nombre visible, permisos y, si corresponde, token persistente).
     * @throws ErrorNegocio con el motivo del rechazo (RN-01, RN-02)
     */
    @Transactional(noRollbackFor = ErrorNegocio.class)
    public SesionUsuario iniciarSesion(String username, String clave, boolean mantenerSesion) {
        LocalDateTime ahora = reloj.ahora();
        if (username == null || username.isBlank() || clave == null || clave.isEmpty()) {
            throw new ErrorNegocio(CREDENCIALES_INVALIDAS);
        }
        Usuario usuario = fachadaPersistencia.buscarUno(new UsuarioPorUsername(username.strip()))
                .orElseThrow(() -> new ErrorNegocio(CREDENCIALES_INVALIDAS));
        if (usuario.estaBloqueada(ahora)) {
            throw new ErrorNegocio(mensajeBloqueo(usuario, ahora));
        }
        if (!usuario.autenticar(clave, codificadorClave)) {
            usuario.registrarIntentoFallido(ahora);
            fachadaPersistencia.put(usuario);
            if (usuario.estaBloqueada(ahora)) {
                BITACORA.warn("Cuenta {} bloqueada por intentos fallidos", usuario.getId());
                throw new ErrorNegocio(mensajeBloqueo(usuario, ahora));
            }
            throw new ErrorNegocio(CREDENCIALES_INVALIDAS);
        }
        if (!usuario.estaActiva()) {
            throw new ErrorNegocio("La cuenta no está habilitada. Comuníquese con la administración del club.");
        }
        usuario.reiniciarIntentos();
        fachadaPersistencia.put(usuario);
        SesionUsuario sesion = new SesionUsuario(usuario.getId(), usuario.nombreCompleto(), usuario.obtenerPermisos());
        if (mantenerSesion && usuario.admiteSesionPersistente()) {
            sesion.agregarTokenPersistente(gestorSesiones.emitirTokenPersistente(usuario));
        }
        BITACORA.info("Inicio de sesión de la cuenta {}", usuario.getId());
        return sesion;
    }

    /**
     * Revoca los tokens persistentes de la cuenta ({@code gestorSesiones.revocarTokensDe(usuario)}) y registra el
     * cierre en la bitácora técnica. La invalidación de la sesión HTTP la hace {@code RegistroSesionWeb.cerrar()},
     * que llama {@code PresentadorCierreSesion}.
     *
     * <p>Casos de uso: CU-12
     * @param sesion Sesión que se cierra.
     */
    public void cerrarSesion(SesionUsuario sesion) {
        Usuario usuario = fachadaPersistencia.get(sesion.getIdUsuario(), Usuario.class);
        gestorSesiones.revocarTokensDe(usuario);
        BITACORA.info("Cierre de sesión de la cuenta {}", usuario.getId());
    }

    /**
     * Reconstruye la {@code SesionUsuario} de la cuenta autenticada.
     *
     * <p>Casos de uso: CU-11, CU-12
     * @return {@code SesionUsuario}.
     * @throws ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException si la sesión ya expiró (CU-12 FA1)
     */
    @Transactional(readOnly = true)
    public SesionUsuario consultarSesionActual() {
        Usuario usuario = usuarioActual.obtener();
        return new SesionUsuario(usuario.getId(), usuario.nombreCompleto(), usuario.obtenerPermisos());
    }

    /**
     * Si existe una cuenta activa con ese correo, invalida los tokens anteriores, crea un {@code TokenRecuperacion}
     * (se guarda solo su hash, vence a los 30 minutos) y envía el enlace por {@code ServicioCorreo}. Si no existe,
     * no hace nada visible. Una falla del envío se registra en la bitácora técnica sin informarla (CU-13 FA3).
     *
     * <p>Casos de uso: CU-13
     * @param email Correo de la cuenta.
     */
    public void solicitarRecuperacion(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        LocalDateTime ahora = reloj.ahora();
        Optional<Usuario> encontrado = fachadaPersistencia.buscarUno(new UsuarioPorEmail(email.strip()));
        if (encontrado.isEmpty() || !encontrado.get().estaActiva()) {
            BITACORA.info("Solicitud de recuperación sin cuenta activa asociada");
            return;
        }
        Usuario usuario = encontrado.get();
        for (TokenRecuperacion vigente : fachadaPersistencia.buscar(new TokensRecuperacionVigentes(usuario.getId(), ahora))) {
            vigente.marcarUsado();
            fachadaPersistencia.put(vigente);
        }
        String token = tokenAleatorio();
        TokenRecuperacion t = new TokenRecuperacion(usuario, GestorSesiones.hashSha256(token),
                ahora.plus(VIGENCIA_RECUPERACION));
        fachadaPersistencia.put(t);
        enviarEnlace(usuario, urlBase + "/restablecer-clave?token=" + token);
    }

    /**
     * Busca el token por su hash, verifica {@code esValido(ahora)}, aplica {@code politicaContrasenas.validar(clave,
     * username)} (longitud de 15 a 64 caracteres, no figura en la lista de contraseñas prohibidas, distinta del
     * nombre de usuario), obtiene la cuenta por la asociación {@code token.usuario}, {@code
     * usuario.cambiarClave(codificadorClave.codificar(nuevaClave))}, {@code marcarUsado()}, reinicia los intentos
     * fallidos y cierra las demás sesiones y tokens persistentes ({@code revocarTokensDe}, {@code
     * invalidarSesionesDe}).
     *
     * <p>Casos de uso: CU-13
     * @param token Token recibido en el enlace.
     * @param nuevaClave Nueva contraseña.
     * @throws ErrorNegocio enlace no válido (FA4) o clave fuera de la política (FA5, campo {@code nuevaClave})
     */
    public void restablecerClave(String token, String nuevaClave) {
        if (token == null || token.isBlank()) {
            throw new ErrorNegocio(ENLACE_INVALIDO);
        }
        TokenRecuperacion t = fachadaPersistencia.buscarUno(new TokenRecuperacionPorHash(GestorSesiones.hashSha256(token)))
                .orElseThrow(() -> new ErrorNegocio(ENLACE_INVALIDO));
        if (!t.esValido(reloj.ahora())) {
            throw new ErrorNegocio(ENLACE_INVALIDO);
        }
        Usuario usuario = t.getUsuario();
        validarClave("nuevaClave", nuevaClave, usuario.getUsername());
        usuario.cambiarClave(codificadorClave.codificar(nuevaClave));
        t.marcarUsado();
        usuario.reiniciarIntentos();
        fachadaPersistencia.put(usuario);
        fachadaPersistencia.put(t);
        gestorSesiones.revocarTokensDe(usuario);
        gestorSesiones.invalidarSesionesDe(usuario);
        BITACORA.info("Contraseña restablecida de la cuenta {}", usuario.getId());
    }

    /**
     * Verifica unicidad de usuario y correo, aplica {@code politicaContrasenas.validar(clave, username)} (longitud
     * de 15 a 64 caracteres, no figura en la lista de contraseñas prohibidas, distinta del nombre de usuario),
     * instancia {@code Administrador} (asignación del tipo de cuenta, CU-16), codifica la clave, guarda y audita.
     *
     * <p>Orden ({@code sec_cu14_p2.puml}): autorizar, política (FA3), unicidad (FA4, CU-16), codificar,
     * {@code new Administrador}, {@code put}, {@code registrar("ALTA")}.
     *
     * <p>Permiso: {@code GESTIONAR_CUENTAS}.
     *
     * <p>Casos de uso: CU-14, CU-16
     * @param datos Nombre, apellido, usuario, correo y clave inicial.
     * @return {@code Administrador} creado.
     * @throws ErrorNegocio si falta un dato, la clave no cumple la política o el usuario o correo ya existen
     */
    public Administrador crearAdministrador(DatosAdministrador datos) {
        Usuario actor = usuarioActual.obtener();
        controlAcceso.verificar(actor, Permiso.GESTIONAR_CUENTAS);
        String nombre = obligatorio("nombre", datos.getNombre(), "Ingrese el nombre.");
        String apellido = obligatorio("apellido", datos.getApellido(), "Ingrese el apellido.");
        String username = validarUsername(datos.getUsername());
        String email = validarEmail(datos.getEmail());
        validarClave("claveInicial", datos.getClaveInicial(), username);
        verificarUsernameLibre(username);
        verificarEmailLibre(email);
        String hash = codificadorClave.codificar(datos.getClaveInicial());
        Administrador administrador = new Administrador(nombre, apellido, username, email, hash);
        fachadaPersistencia.put(administrador);
        Map<String, Object> detalle = new LinkedHashMap<>();
        detalle.put("username", username);
        detalle.put("email", email);
        detalle.put("nombre", nombre);
        detalle.put("apellido", apellido);
        servicioAuditoria.registrar(actor, "ALTA", "Administrador", administrador.getId(), json(detalle));
        return administrador;
    }

    /**
     * Busca cuentas de cualquier tipo por nombre de usuario, correo o nombre.
     *
     * <p>Permiso: {@code GESTIONAR_CUENTAS}.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @param criterio Texto de búsqueda.
     * @return Lista de {@code ResumenCuenta} (con el tipo de cuenta derivado de la subclase).
     */
    @Transactional(readOnly = true)
    public List<ResumenCuenta> buscarCuentas(CriterioBusquedaCuenta criterio) {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.GESTIONAR_CUENTAS);
        LocalDateTime ahora = reloj.ahora();
        String texto = criterio == null ? null : criterio.getTexto();
        return fachadaPersistencia.buscar(new CuentasPorCriterio(texto)).stream()
                .map(u -> ResumenCuenta.desde(u, ahora))
                .toList();
    }

    /**
     * Devuelve las cuentas de administrador.
     *
     * <p>Permiso: {@code GESTIONAR_CUENTAS}.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @return Lista de {@code Administrador}.
     */
    @Transactional(readOnly = true)
    public List<Administrador> listarAdministradores() {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.GESTIONAR_CUENTAS);
        return fachadaPersistencia.buscar(new Administradores());
    }

    /**
     * Devuelve una cuenta de cualquier tipo.
     *
     * <p>Permiso: {@code GESTIONAR_CUENTAS}.
     *
     * <p>Casos de uso: CU-15
     * @param idUsuario Identificador de la cuenta.
     * @return {@code Usuario}.
     */
    @Transactional(readOnly = true)
    public Usuario consultarCuenta(Long idUsuario) {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.GESTIONAR_CUENTAS);
        return fachadaPersistencia.get(idUsuario, Usuario.class);
    }

    /**
     * Verifica unicidad de usuario y correo ({@code fachadaPersistencia.contar(UsuarioPorUsername(username)) &gt; 0}
     * y {@code contar(UsuarioPorEmail(email)) &gt; 0}); {@code usuario.actualizarDatosAcceso(username, email)}; una
     * clave nueva pasa por {@code politicaContrasenas.validar(clave, username)} y se guarda con {@code
     * usuario.cambiarClave(codificadorClave.codificar(clave))}. Revoca los tokens persistentes ({@code
     * revocarTokensDe}) y, si cambió la clave, {@code invalidarSesionesDe(usuario)}. Audita el cambio.
     *
     * <p>Orden ({@code sec_cu15_p3.puml}): autorizar, cuenta activa (FA2), política (FA4), unicidad solo de los
     * datos que cambiaron (FA5), actualizar, {@code put}, tokens, sesiones, auditoría.
     *
     * <p>Permiso: {@code GESTIONAR_CUENTAS}.
     *
     * <p>Casos de uso: CU-15
     * @param idUsuario Cuenta a modificar.
     * @param datos Nuevos datos de acceso.
     * @throws ErrorNegocio si la cuenta está dada de baja, la clave no cumple la política o el dato está en uso
     */
    public void modificarDatosAcceso(Long idUsuario, DatosAcceso datos) {
        Usuario actor = usuarioActual.obtener();
        controlAcceso.verificar(actor, Permiso.GESTIONAR_CUENTAS);
        Usuario usuario = fachadaPersistencia.get(idUsuario, Usuario.class);
        if (!usuario.estaActiva()) {
            throw new ErrorNegocio("La cuenta está dada de baja: no se pueden modificar sus datos de acceso.");
        }
        String username = validarUsername(datos.getUsername());
        String email = validarEmail(datos.getEmail());
        boolean cambiaClave = datos.getNuevaClave() != null && !datos.getNuevaClave().isEmpty();
        if (cambiaClave) {
            validarClave("nuevaClave", datos.getNuevaClave(), username);
        }
        boolean cambiaUsername = !username.equals(usuario.getUsername());
        boolean cambiaEmail = !email.equalsIgnoreCase(usuario.getEmail());
        if (cambiaUsername) {
            verificarUsernameLibre(username);
        }
        if (cambiaEmail) {
            verificarEmailLibre(email);
        }
        usuario.actualizarDatosAcceso(username, email);
        if (cambiaClave) {
            usuario.cambiarClave(codificadorClave.codificar(datos.getNuevaClave()));
        }
        fachadaPersistencia.put(usuario);
        gestorSesiones.revocarTokensDe(usuario);
        if (cambiaClave) {
            gestorSesiones.invalidarSesionesDe(usuario);
        }
        Map<String, Object> detalle = new LinkedHashMap<>();
        detalle.put("username", username);
        detalle.put("email", email);
        detalle.put("claveCambiada", cambiaClave);
        servicioAuditoria.registrar(actor, "MODIFICACION", "Usuario", usuario.getId(), json(detalle));
    }

    /**
     * Solo admite cuentas de administrador (la obtiene con {@code fachadaPersistencia.get(idUsuario,
     * Administrador)}; la de un socio se desactiva con CU-03). Si es el último administrador activo ({@code
     * fachadaPersistencia.contar(AdministradoresActivos()) = 1}) lanza {@code ErrorNegocio}; si no, {@code
     * desactivar()}, {@code gestorSesiones.revocarTokensDe(cuenta)}, {@code invalidarSesionesDe(cuenta)} y audita.
     * El conteo bloquea las filas ({@code FOR UPDATE}) para que dos desactivaciones simultáneas no dejen al club sin
     * administradores.
     *
     * <p>Permiso: {@code GESTIONAR_CUENTAS}.
     *
     * <p>Casos de uso: CU-17
     * @param idUsuario Cuenta a desactivar.
     * @throws ErrorNegocio si no es de administrador (FA2), ya está inactiva (FA1) o es el último activo (FA3)
     */
    public void desactivarCuenta(Long idUsuario) {
        Usuario actor = usuarioActual.obtener();
        controlAcceso.verificar(actor, Permiso.GESTIONAR_CUENTAS);
        Administrador administrador;
        try {
            administrador = fachadaPersistencia.get(idUsuario, Administrador.class);
        } catch (RecursoNoEncontradoException e) {
            throw new ErrorNegocio("La cuenta no es de administrador: las cuentas de socio se desactivan con la baja del socio.");
        }
        if (!administrador.estaActiva()) {
            throw new ErrorNegocio("La cuenta ya está inactiva.");
        }
        if (fachadaPersistencia.contar(new AdministradoresActivos()) <= 1) {
            throw new ErrorNegocio("No se puede desactivar el último administrador activo del club.");
        }
        administrador.desactivar();
        fachadaPersistencia.put(administrador);
        gestorSesiones.revocarTokensDe(administrador);
        gestorSesiones.invalidarSesionesDe(administrador);
        Map<String, Object> detalle = new LinkedHashMap<>();
        detalle.put("username", administrador.getUsername());
        detalle.put("estado", administrador.getEstado().name());
        servicioAuditoria.registrar(actor, "BAJA", "Administrador", administrador.getId(), json(detalle));
    }

    // ------------------------------------------------------------------ apoyo privado

    /**
     * Mensaje de cuenta bloqueada con los minutos restantes (CU-11 FA5, FA7).
     *
     * <p>Casos de uso: CU-11
     * @param usuario cuenta bloqueada
     * @param ahora momento actual
     * @return mensaje
     */
    private static String mensajeBloqueo(Usuario usuario, LocalDateTime ahora) {
        long segundos = Duration.between(ahora, usuario.getBloqueadoHasta()).getSeconds();
        long minutos = Math.max(1, (segundos + 59) / 60);
        return "La cuenta está bloqueada temporalmente por intentos fallidos. Intente nuevamente en "
                + minutos + (minutos == 1 ? " minuto." : " minutos.");
    }

    /**
     * Arma y envía el correo con el enlace de recuperación (CU-13 paso 7); una falla queda solo en la bitácora.
     *
     * <p>Casos de uso: CU-13
     * @param usuario cuenta destinataria
     * @param enlace enlace a P-03 con el token
     */
    private void enviarEnlace(Usuario usuario, String enlace) {
        MensajeCorreo mensaje = new MensajeCorreo(usuario.getEmail(), "Recuperación de contraseña",
                "Hola " + usuario.nombreCompleto() + ":\n\n"
                        + "Recibimos un pedido para restablecer la contraseña de su cuenta. Para elegir una nueva, "
                        + "abra este enlace (vence en 30 minutos y se puede usar una sola vez):\n\n" + enlace + "\n\n"
                        + "Si no pidió el cambio, ignore este mensaje: su contraseña actual sigue vigente.");
        try {
            ResultadoEnvio resultado = servicioCorreo.enviar(mensaje);
            if (resultado == null || !resultado.isEnviado()) {
                BITACORA.warn("No se pudo enviar el correo de recuperación de la cuenta {}: {}", usuario.getId(),
                        resultado == null ? "sin resultado" : resultado.getDetalle());
            }
        } catch (RuntimeException e) {
            BITACORA.warn("Falla del envío del correo de recuperación de la cuenta {}", usuario.getId(), e);
        }
    }

    /**
     * Token de recuperación aleatorio de 32 bytes en Base64 apto para URL.
     *
     * <p>Casos de uso: CU-13
     * @return token en claro (solo viaja en el correo)
     */
    private static String tokenAleatorio() {
        byte[] bytes = new byte[32];
        ALEATORIO.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Aplica la política de contraseñas y, si falla, vuelve a lanzar el error asociado al campo del formulario.
     *
     * <p>Casos de uso: CU-13, CU-14, CU-15
     * @param campo campo del formulario
     * @param clave clave propuesta
     * @param username nombre de usuario de la cuenta
     * @throws ErrorNegocio con el campo y el requisito incumplido
     */
    private void validarClave(String campo, String clave, String username) {
        try {
            politicaContrasenas.validar(clave, username);
        } catch (ErrorNegocio e) {
            throw new ErrorNegocio(campo, e.getMensaje());
        }
    }

    /**
     * Verifica que un dato obligatorio no esté vacío.
     *
     * <p>Casos de uso: CU-14
     * @param campo campo del formulario
     * @param valor valor ingresado
     * @param mensaje mensaje si falta
     * @return valor sin espacios en los extremos
     * @throws ErrorNegocio si está vacío
     */
    private static String obligatorio(String campo, String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new ErrorNegocio(campo, mensaje);
        }
        return valor.strip();
    }

    /**
     * Valida el nombre de usuario: obligatorio, de 4 a 50 caracteres y sin espacios.
     *
     * <p>Casos de uso: CU-14, CU-15
     * @param username nombre de usuario ingresado
     * @return nombre de usuario normalizado
     * @throws ErrorNegocio si no cumple el formato
     */
    private static String validarUsername(String username) {
        String valor = obligatorio("username", username, "Ingrese el nombre de usuario.");
        if (valor.length() < 4 || valor.length() > 50 || valor.chars().anyMatch(Character::isWhitespace)) {
            throw new ErrorNegocio("username", "El nombre de usuario debe tener de 4 a 50 caracteres, sin espacios.");
        }
        return valor;
    }

    /**
     * Valida el formato básico del correo.
     *
     * <p>Casos de uso: CU-14, CU-15
     * @param email correo ingresado
     * @return correo normalizado
     * @throws ErrorNegocio si no tiene formato de correo
     */
    private static String validarEmail(String email) {
        String valor = obligatorio("email", email, "Ingrese el correo electrónico.");
        if (valor.length() > 120 || !valor.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ErrorNegocio("email", "El correo electrónico no tiene un formato válido.");
        }
        return valor;
    }

    /**
     * Rechaza un nombre de usuario que ya usa otra cuenta ({@code contar(UsuarioPorUsername) > 0}).
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param username nombre de usuario
     * @throws ErrorNegocio si está en uso
     */
    private void verificarUsernameLibre(String username) {
        if (fachadaPersistencia.contar(new UsuarioPorUsername(username)) > 0) {
            throw new ErrorNegocio("username", "El nombre de usuario ya está en uso.");
        }
    }

    /**
     * Rechaza un correo que ya usa otra cuenta ({@code contar(UsuarioPorEmail) > 0}).
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param email correo
     * @throws ErrorNegocio si está en uso
     */
    private void verificarEmailLibre(String email) {
        if (fachadaPersistencia.contar(new UsuarioPorEmail(email)) > 0) {
            throw new ErrorNegocio("email", "El correo electrónico ya está en uso.");
        }
    }

    /**
     * Serializa el detalle de auditoría como JSON (sin credenciales).
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param detalle pares nombre → valor
     * @return texto JSON
     */
    private static String json(Map<String, Object> detalle) {
        try {
            return JSON.writeValueAsString(detalle);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No se pudo serializar el detalle de auditoría", e);
        }
    }
}

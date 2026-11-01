package ar.edu.ub.club.accesos.dominio;

import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.compartido.seguridad.Autorizable;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Clase abstracta que representa una cuenta de acceso; se especializa en {@code Socio} y {@code Administrador} e
 * implementa la interfaz {@code Autorizable}. Es Information Expert de sus intentos fallidos, de su bloqueo y de
 * sus datos de acceso.
 *
 * <p>Polimorfismo (GRASP Polymorphism): {@code obtenerPermisos()}, {@code admiteSesionPersistente()} y
 * {@code tipoCuenta()} se redefinen en cada subclase, por eso no hay atributo «rol» ni preguntas por el tipo.
 * Implementa además la política de bloqueo (PoliticaBloqueo, RN-02): al quinto intento fallido consecutivo la
 * cuenta queda bloqueada 15 minutos. Diagrama de estados: {@code est_cuenta.puml} (Activa ⇄ Bloqueada
 * temporalmente, Activa → Inactiva final).
 *
 * <p>Entidad del dominio; la persiste {@code BrokerUsuario} (herencia por tabla de subclase, JOINED).
 *
 * <p>Casos de uso: todos
 */
public abstract class Usuario extends EntidadPersistente implements Autorizable {

    /** PoliticaBloqueo: cantidad de intentos fallidos consecutivos que bloquean la cuenta (RN-02). */
    public static final int INTENTOS_MAXIMOS = 5;

    /** PoliticaBloqueo: duración del bloqueo temporal (RN-02). */
    public static final Duration DURACION_BLOQUEO = Duration.ofMinutes(15);

    /** Nombre de usuario. */
    private String username;

    /** Correo. */
    private String email;

    /** Hash BCrypt. */
    private String passwordHash;

    /** Estado de la cuenta. */
    private EstadoCuenta estado;

    /** Intentos fallidos consecutivos. */
    private Integer intentosFallidos;

    /** Fin del bloqueo temporal. */
    private LocalDateTime bloqueadoHasta;

    /** Alta de la cuenta. */
    private LocalDateTime fechaCreacion;

    /**
     * Constructor vacío para que las subclases se reconstituyan desde la base ({@code reconstituir}); luego
     * llaman a {@code restaurarDatosAcceso}.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructores y los atributos son privados.
     *
     * <p>Casos de uso: todos
     */
    protected Usuario() {
        // los atributos los completa restaurarDatosAcceso
    }

    /**
     * Constructor del alta de una cuenta (lo usan {@code new Administrador(...)} en CU-14 y {@code new Socio(...)}
     * en CU-01): la cuenta nace ACTIVA, con 0 intentos fallidos y sin bloqueo (CU-16: el tipo lo fija la subclase
     * instanciada).
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructores y los atributos son privados.
     *
     * <p>Casos de uso: CU-01, CU-14, CU-16
     * @param username nombre de usuario (único; la unicidad la verifica la fachada)
     * @param email correo (único)
     * @param passwordHash hash ya calculado por {@code CodificadorClave}
     */
    protected Usuario(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.estado = EstadoCuenta.ACTIVA;
        this.intentosFallidos = 0;
        this.bloqueadoHasta = null;
        this.fechaCreacion = LocalDateTime.now(RelojSistema.ZONA_CLUB);
    }

    /**
     * Restaura los atributos comunes leídos de la tabla {@code usuario}; lo llaman los {@code reconstituir} de las
     * subclases (desmaterialización del Database Broker).
     *
     * <p>Agregado en la implementación: los atributos son privados y las subclases están en otros paquetes.
     *
     * <p>Casos de uso: todos
     * @param id identificador
     * @param username nombre de usuario
     * @param email correo
     * @param passwordHash hash BCrypt
     * @param estado estado de la cuenta
     * @param intentosFallidos intentos fallidos consecutivos
     * @param bloqueadoHasta fin del bloqueo o {@code null}
     * @param fechaCreacion alta de la cuenta
     */
    protected void restaurarDatosAcceso(Long id, String username, String email, String passwordHash,
                                        EstadoCuenta estado, Integer intentosFallidos, LocalDateTime bloqueadoHasta,
                                        LocalDateTime fechaCreacion) {
        asignarId(id);
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.estado = estado;
        this.intentosFallidos = intentosFallidos == null ? 0 : intentosFallidos;
        this.bloqueadoHasta = bloqueadoHasta;
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Verifica la clave con {@code codificador.coincide(clave, passwordHash)} (CU-11 paso 6). La cuenta no conoce
     * BCrypt: lo encapsula el codificador (Protected Variations).
     *
     * <p>Casos de uso: CU-11
     * @param clave Clave ingresada.
     * @param codificador Codificador BCrypt.
     * @return true si coincide.
     */
    public boolean autenticar(String clave, CodificadorClave codificador) {
        return clave != null && codificador.coincide(clave, passwordHash);
    }

    /**
     * Incrementa los intentos; al quinto fija {@code bloqueadoHasta = ahora + 15 minutos} (PoliticaBloqueo,
     * RN-02, CU-11 FA6/FA7). El contador no supera 5 (restricción {@code ck_usuario_intentos}): si un bloqueo ya
     * venció y se vuelve a fallar, el contador recomienza en 1.
     *
     * <p>Casos de uso: CU-11
     * @param ahora Momento actual.
     */
    public void registrarIntentoFallido(LocalDateTime ahora) {
        if (intentosFallidos >= INTENTOS_MAXIMOS) {
            // el bloqueo anterior ya venció (si no, la fachada no habría llegado aquí): nueva serie de intentos
            intentosFallidos = 0;
            bloqueadoHasta = null;
        }
        intentosFallidos++;
        if (intentosFallidos >= INTENTOS_MAXIMOS) {
            bloqueadoHasta = ahora.plus(DURACION_BLOQUEO);
        }
    }

    /**
     * Indica si {@code bloqueadoHasta} es posterior a ahora (estado «Bloqueada temporalmente»; al vencer los 15
     * minutos se desbloquea sola).
     *
     * <p>Casos de uso: CU-11
     * @param ahora Momento actual.
     * @return true si está bloqueada.
     */
    public boolean estaBloqueada(LocalDateTime ahora) {
        return bloqueadoHasta != null && bloqueadoHasta.isAfter(ahora);
    }

    /**
     * Pone los intentos en 0 y borra el bloqueo (ingreso exitoso, CU-11 paso 8, o contraseña restablecida, CU-13).
     *
     * <p>Casos de uso: CU-11, CU-13
     */
    public void reiniciarIntentos() {
        intentosFallidos = 0;
        bloqueadoHasta = null;
    }

    /**
     * Indica si el estado es ACTIVA.
     *
     * <p>Casos de uso: CU-11, CU-13, CU-15, CU-17
     * @return true si está activa.
     */
    public boolean estaActiva() {
        return estado == EstadoCuenta.ACTIVA;
    }

    /**
     * Reemplaza el hash de la contraseña (CU-13, CU-15). La validación de la política la hace la fachada antes.
     *
     * <p>Casos de uso: CU-13, CU-15
     * @param hash Hash ya calculado por {@code CodificadorClave}.
     */
    public void cambiarClave(String hash) {
        this.passwordHash = hash;
    }

    /**
     * Modifica usuario y correo (CU-15); la unicidad la verifica la fachada.
     *
     * <p>Casos de uso: CU-15
     * @param username Nuevo usuario.
     * @param email Nuevo correo.
     */
    public void actualizarDatosAcceso(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Pasa la cuenta a INACTIVA (estado final: conserva el historial, RN-03). La protección del último
     * administrador activo la verifica {@code ServicioAccesos.desactivarCuenta}.
     *
     * <p>Casos de uso: CU-03, CU-17
     */
    public void desactivar() {
        this.estado = EstadoCuenta.INACTIVA;
    }

    /**
     * Operación abstracta de {@code Autorizable}; cada subclase devuelve sus permisos.
     *
     * <p>Casos de uso: todos
     * @return Conjunto de permisos.
     */
    @Override
    public abstract Set<Permiso> obtenerPermisos();

    /**
     * Operación abstracta: {@code Socio} devuelve verdadero y {@code Administrador} falso. Decide, sin preguntar por
     * el tipo, si se emite el token de «Mantener la sesión iniciada».
     *
     * <p>Casos de uso: CU-11
     * @return true si la cuenta admite sesión persistente.
     */
    public abstract boolean admiteSesionPersistente();

    /**
     * Apellido y nombre de la cuenta ({@code Socio} y {@code Administrador} tienen sus propios {@code nombre} y
     * {@code apellido} y lo redefinen). La usan {@code DetalleCuenta}, {@code FilaCuenta} y la sesión
     * ({@code SesionUsuario.nombreVisible}) sin preguntar por la clase.
     *
     * <p>Decisión de implementación: queda concreta (devuelve el nombre de usuario) porque el esqueleto de
     * {@code Socio} no la declaraba; las subclases la redefinen.
     *
     * <p>Casos de uso: CU-11, CU-15, CU-17
     * @return Apellido y nombre.
     */
    public String nombreCompleto() {
        return username;
    }

    /**
     * Operación abstracta: {@code Socio} devuelve "Socio" y {@code Administrador} "Administrador". La usan los
     * modelos de vista {@code FilaCuenta} y {@code DetalleCuenta} para mostrar el tipo de cuenta sin preguntar por
     * la clase.
     *
     * <p>Casos de uso: CU-15, CU-16, CU-17
     * @return Tipo de cuenta para mostrar.
     */
    public abstract String tipoCuenta();

    /**
     * Devuelve {@code username}: Nombre de usuario.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getUsername() {
        return username;
    }

    /**
     * Devuelve {@code email}: Correo.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Devuelve {@code passwordHash}: Hash BCrypt.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Devuelve {@code estado}: Estado de la cuenta.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public EstadoCuenta getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code intentosFallidos}: Intentos fallidos consecutivos.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Integer getIntentosFallidos() {
        return intentosFallidos;
    }

    /**
     * Devuelve {@code bloqueadoHasta}: Fin del bloqueo temporal.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDateTime getBloqueadoHasta() {
        return bloqueadoHasta;
    }

    /**
     * Devuelve {@code fechaCreacion}: Alta de la cuenta.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}

package ar.edu.ub.club.accesos.dominio;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Subclase de {@code Usuario} (accesos.dominio) para las cuentas de administrador. Redefine las operaciones
 * polimórficas de {@code Usuario} (GRASP Polymorphism, {@code pat_polimorfismo.puml}): sus permisos son los de
 * administración, no admite sesión persistente y su tipo de cuenta es "Administrador". No hay atributo «rol»: el
 * tipo de cuenta es la clase instanciada (CU-16, RN-04).
 *
 * <p>Entidad del dominio; la persiste {@code BrokerAdministrador} (tablas {@code usuario} + {@code administrador}).
 *
 * <p>Casos de uso: CU-11, CU-14, CU-15, CU-16, CU-17
 */
public class Administrador extends Usuario {

    /** Permisos de toda cuenta de administrador (07B, clase {@code Permiso}); conjunto inmutable. */
    private static final Set<Permiso> PERMISOS_ADMINISTRADOR = Collections.unmodifiableSet(EnumSet.of(
            Permiso.GESTIONAR_SOCIOS, Permiso.GESTIONAR_CATEGORIAS, Permiso.GESTIONAR_GRUPOS,
            Permiso.GESTIONAR_CUENTAS, Permiso.GESTIONAR_RECURSOS, Permiso.CONFIGURAR_AGENDA,
            Permiso.CONSULTAR_DISPONIBILIDAD, Permiso.GESTIONAR_RESERVAS_CLUB, Permiso.CONFIGURAR_PAGOS,
            Permiso.REGISTRAR_PAGO_MANUAL, Permiso.CONSULTAR_PAGOS_CLUB, Permiso.GENERAR_REPORTES,
            Permiso.CONSULTAR_DASHBOARD, Permiso.CONFIGURAR_CLUB));

    /** Nombre. */
    private String nombre;

    /** Apellido. */
    private String apellido;

    /**
     * Constructor que usa {@code ServicioAccesos.crearAdministrador} (CU-14, CU-16): la cuenta nace ACTIVA y con 0
     * intentos fallidos (constructor protegido de {@code Usuario}).
     *
     * <p>Casos de uso: CU-14, CU-16
     * @param nombre Nombre.
     * @param apellido Apellido.
     * @param username Nombre de usuario (único).
     * @param email Correo (único).
     * @param passwordHash Hash ya calculado por {@code CodificadorClave}.
     */
    public Administrador(String nombre, String apellido, String username, String email, String passwordHash) {
        super(username, email, passwordHash);
        this.nombre = nombre;
        this.apellido = apellido;
    }

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     */
    private Administrador() {
        super();
    }

    /**
     * Permisos de administrador (despacho dinámico desde {@code ControlAcceso}, sin preguntar por el tipo).
     *
     * <p>Casos de uso: todos
     * @return Conjunto de permisos de administrador.
     */
    @Override
    public Set<Permiso> obtenerPermisos() {
        return PERMISOS_ADMINISTRADOR;
    }

    /**
     * Devuelve falso: los administradores siempre inician sesión con usuario y contraseña (CU-11 FA9; su sesión
     * vence a los 30 minutos de inactividad).
     *
     * <p>Casos de uso: CU-11
     * @return false.
     */
    @Override
    public boolean admiteSesionPersistente() {
        return false;
    }

    /**
     * Devuelve "Administrador".
     *
     * <p>Casos de uso: CU-15, CU-16, CU-17
     * @return "Administrador".
     */
    @Override
    public String tipoCuenta() {
        return "Administrador";
    }

    /**
     * Apellido y nombre: "Apellido, Nombre".
     *
     * <p>Casos de uso: CU-11, CU-15, CU-17
     * @return apellido y nombre
     */
    @Override
    public String nombreCompleto() {
        return apellido + ", " + nombre;
    }

    /**
     * Reconstituye un administrador con los valores de las tablas {@code usuario} y {@code administrador}
     * (desmaterialización; lo llama {@code BrokerAdministrador.crearObjeto}).
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
     * @param nombre nombre
     * @param apellido apellido
     * @return el administrador reconstituido
     */
    public static Administrador reconstituir(Long id, String username, String email, String passwordHash, EstadoCuenta estado, Integer intentosFallidos, LocalDateTime bloqueadoHasta, LocalDateTime fechaCreacion, String nombre, String apellido) {
        Administrador a = new Administrador();
        a.restaurarDatosAcceso(id, username, email, passwordHash, estado, intentosFallidos, bloqueadoHasta,
                fechaCreacion);
        a.nombre = nombre;
        a.apellido = apellido;
        return a;
    }

    /**
     * Devuelve {@code nombre}: Nombre.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve {@code apellido}: Apellido.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getApellido() {
        return apellido;
    }
}

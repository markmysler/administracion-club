package ar.edu.ub.club.socios.dominio;

import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.socios.aplicacion.DatosSocio;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

/**
 * Subclase de {@code Usuario} con los datos personales, la categoría y el grupo familiar vigente. El socio es a la
 * vez su cuenta de acceso de tipo Socio (CU-16): hereda de {@code Usuario} el nombre de usuario, el correo, la
 * clave y el estado, y redefine por polimorfismo {@code obtenerPermisos()}, {@code admiteSesionPersistente()},
 * {@code tipoCuenta()} y {@code nombreCompleto()} (ninguna clase pregunta por el tipo de cuenta, RNF-04).
 *
 * <p>Entidad del dominio; la persiste {@code BrokerSocio} (herencia JOINED: tablas {@code usuario} y
 * {@code socio}). Information Expert de sus datos personales.
 *
 * <p>Casos de uso: todos
 */
public class Socio extends Usuario {

    /** Permisos de toda cuenta de socio (los devuelve {@code obtenerPermisos()}). */
    private static final Set<Permiso> PERMISOS_SOCIO = EnumSet.of(Permiso.CONSULTAR_PERFIL_PROPIO,
            Permiso.CONSULTAR_DISPONIBILIDAD, Permiso.RESERVAR_ONLINE, Permiso.GESTIONAR_RESERVAS_PROPIAS,
            Permiso.PAGAR_CUOTA_ONLINE, Permiso.CONSULTAR_PAGOS_PROPIOS);

    /** Documento. */
    private String dni;

    /** Nombre. */
    private String nombre;

    /** Apellido. */
    private String apellido;

    /** Nacimiento. */
    private LocalDate fechaNacimiento;

    /** Teléfono. */
    private String telefono;

    /** Domicilio. */
    private String direccion;

    /** Alta. */
    private LocalDate fechaAlta;

    /** Categoría vigente. */
    private CategoriaSocio categoria;

    /** Grupo vigente. */
    private GrupoFamiliar grupoFamiliar;

    /**
     * Crea el socio del alta (CU-01) como cuenta activa de tipo Socio (CU-16) con la clave ya codificada; el
     * constructor protegido de {@code Usuario} fija estado ACTIVA, 0 intentos y la fecha de creación. Valida los
     * datos personales (Information Expert) y fija {@code fechaAlta} en la fecha del club. El grupo familiar lo
     * asigna después {@code ServicioGruposFamiliares.crearGrupoUnipersonal} (RN-02).
     *
     * <p>Casos de uso: CU-01, CU-16
     * @param datos datos personales, correo y nombre de usuario
     * @param categoria categoría activa elegida
     * @param passwordHash hash BCrypt de la contraseña inicial (RNF-03)
     * @throws ErrorNegocio si un dato personal obligatorio falta o tiene formato inválido (CU-01 FA3)
     */
    public Socio(DatosSocio datos, CategoriaSocio categoria, String passwordHash) {
        super(datos.getUsername(), datos.getEmail(), passwordHash);
        this.fechaAlta = LocalDate.now(RelojSistema.ZONA_CLUB);
        aplicarDatos(datos);
        this.categoria = categoria;
    }

    /**
     * Constructor vacío que usa {@code reconstituir} (encadena con el constructor protegido sin argumentos de
     * {@code Usuario}).
     *
     * <p>Casos de uso: todos
     */
    private Socio() {
        super();
    }

    /**
     * Aplica los datos personales y la categoría modificados (CU-02, Information Expert). Los datos de acceso
     * (usuario, correo, clave) no cambian aquí: se modifican con CU-15.
     *
     * <p>Casos de uso: CU-02
     * @param datos Datos personales.
     * @param categoria Categoría elegida.
     * @throws ErrorNegocio si un dato obligatorio falta o es inválido (CU-02 FA3)
     */
    public void actualizarDatos(DatosSocio datos, CategoriaSocio categoria) {
        aplicarDatos(datos);
        this.categoria = categoria;
    }

    /**
     * Permisos del socio (polimorfismo de {@code Autorizable}): perfil propio, disponibilidad, reservas online y
     * propias, pago online y pagos propios.
     *
     * <p>Casos de uso: todos
     * @return Conjunto de permisos de socio.
     */
    @Override
    public Set<Permiso> obtenerPermisos() {
        return PERMISOS_SOCIO;
    }

    /**
     * Devuelve verdadero: el socio puede mantener la sesión iniciada (CU-11, cookie remember-me).
     *
     * <p>Casos de uso: todos
     * @return true.
     */
    @Override
    public boolean admiteSesionPersistente() {
        return true;
    }

    /**
     * Devuelve "Socio" (tipo de cuenta por polimorfismo, CU-16).
     *
     * <p>Casos de uso: todos
     * @return "Socio".
     */
    @Override
    public String tipoCuenta() {
        return "Socio";
    }

    /**
     * Redefine {@code Usuario.nombreCompleto()} con el formato «Apellido, Nombre» que se muestra en las fichas,
     * listados y reservas.
     *
     * <p>Casos de uso: todos
     * @return apellido y nombre del socio
     */
    @Override
    public String nombreCompleto() {
        return apellido + ", " + nombre;
    }

    /**
     * Asigna el grupo familiar vigente del socio. Lo llama {@code GrupoFamiliar.agregarIntegrante} para que la
     * asociación quede navegable en ambos sentidos y {@code BrokerSocio} guarde {@code socio.grupo_familiar_id}.
     *
     * <p>Agregado en la implementación: el diseño no tenía un modo de cambiar el grupo del socio fuera del broker.
     *
     * <p>Casos de uso: CU-01, CU-07, CU-09
     * @param grupo nuevo grupo vigente
     */
    public void asignarGrupoFamiliar(GrupoFamiliar grupo) {
        this.grupoFamiliar = grupo;
    }

    /**
     * Reconstituye el socio desde la fila de {@code usuario JOIN socio} (lo usa solo {@code BrokerSocio},
     * estereotipo «persistencia»). Los datos comunes se restauran con {@code restaurarDatosAcceso} de
     * {@code Usuario}; la categoría y el grupo llegan después con {@code restaurarAsociaciones}.
     *
     * <p>Casos de uso: todos
     * @param id identificador (el mismo en usuario y socio)
     * @param username nombre de usuario
     * @param email correo
     * @param passwordHash hash de la clave
     * @param estado estado de la cuenta
     * @param intentosFallidos intentos fallidos consecutivos
     * @param bloqueadoHasta fin del bloqueo temporal o {@code null}
     * @param fechaCreacion alta de la cuenta
     * @param dni documento
     * @param nombre nombre
     * @param apellido apellido
     * @param fechaNacimiento nacimiento
     * @param telefono teléfono o {@code null}
     * @param direccion domicilio o {@code null}
     * @param fechaAlta fecha de alta como socio
     * @return el socio rehidratado sin asociaciones
     */
    public static Socio reconstituir(Long id, String username, String email, String passwordHash, EstadoCuenta estado, Integer intentosFallidos, LocalDateTime bloqueadoHasta, LocalDateTime fechaCreacion, String dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono, String direccion, LocalDate fechaAlta) {
        Socio socio = new Socio();
        socio.restaurarDatosAcceso(id, username, email, passwordHash, estado, intentosFallidos, bloqueadoHasta,
                fechaCreacion);
        socio.dni = dni;
        socio.nombre = nombre;
        socio.apellido = apellido;
        socio.fechaNacimiento = fechaNacimiento;
        socio.telefono = telefono;
        socio.direccion = direccion;
        socio.fechaAlta = fechaAlta;
        return socio;
    }

    /**
     * Entrega la categoría y el grupo familiar resueltos por {@code BrokerSocio.cargarAsociaciones} con
     * {@code fachada().get(...)} (estereotipo «persistencia»).
     *
     * <p>Casos de uso: todos
     * @param categoria categoría vigente
     * @param grupoFamiliar grupo vigente
     */
    public void restaurarAsociaciones(CategoriaSocio categoria, GrupoFamiliar grupoFamiliar) {
        this.categoria = categoria;
        this.grupoFamiliar = grupoFamiliar;
    }

    /**
     * Valida y copia los datos personales comunes al alta y a la modificación (CU-01 paso 4, CU-02 paso 6). Los
     * nombres de campo de los errores son los de {@code DatosSocio} para que la vista marque el campo.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param datos datos personales
     * @throws ErrorNegocio si falta un dato obligatorio, el DNI no tiene 7 u 8 dígitos o la fecha de nacimiento
     *     no es anterior al alta
     */
    private void aplicarDatos(DatosSocio datos) {
        String nuevoDni = datos.getDni() == null ? null : datos.getDni().trim();
        if (nuevoDni == null || !nuevoDni.matches("[0-9]{7,8}")) {
            throw new ErrorNegocio("dni", "El DNI debe tener 7 u 8 dígitos.");
        }
        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new ErrorNegocio("nombre", "El nombre es obligatorio.");
        }
        if (datos.getApellido() == null || datos.getApellido().isBlank()) {
            throw new ErrorNegocio("apellido", "El apellido es obligatorio.");
        }
        if (datos.getFechaNacimiento() == null || !datos.getFechaNacimiento().isBefore(fechaAlta)) {
            throw new ErrorNegocio("fechaNacimiento", "La fecha de nacimiento debe ser anterior a la fecha de alta.");
        }
        this.dni = nuevoDni;
        this.nombre = datos.getNombre().trim();
        this.apellido = datos.getApellido().trim();
        this.fechaNacimiento = datos.getFechaNacimiento();
        this.telefono = vacioANulo(datos.getTelefono());
        this.direccion = vacioANulo(datos.getDireccion());
    }

    /**
     * Normaliza un texto opcional: los espacios en blanco se guardan como {@code null}.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param texto texto ingresado
     * @return texto recortado o {@code null}
     */
    private static String vacioANulo(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    /**
     * Devuelve {@code dni}: Documento.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getDni() {
        return dni;
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

    /**
     * Devuelve {@code fechaNacimiento}: Nacimiento.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Devuelve {@code telefono}: Teléfono.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Devuelve {@code direccion}: Domicilio.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Devuelve {@code fechaAlta}: Alta.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Devuelve {@code categoria}: Categoría vigente.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public CategoriaSocio getCategoria() {
        return categoria;
    }

    /**
     * Devuelve {@code grupoFamiliar}: Grupo vigente.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public GrupoFamiliar getGrupoFamiliar() {
        return grupoFamiliar;
    }
}

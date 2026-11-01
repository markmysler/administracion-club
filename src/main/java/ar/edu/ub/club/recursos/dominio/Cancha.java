package ar.edu.ub.club.recursos.dominio;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Cancha de un deporte. Information Expert de su disponibilidad para reservar: admite reservas nuevas solo si
 * ella y su deporte están activos ({@code estaDisponibleParaReservar}, RN-11, lo usan
 * {@code ValidadorRecursoActivo} y {@code ServicioAgenda.consultarDisponibilidad}).
 *
 * <p>Entidad del dominio; la persiste {@code BrokerCancha} (tabla {@code cancha}; el deporte se resuelve por
 * {@code get}).
 *
 * <p>Casos de uso: CU-21, CU-22, CU-23, CU-26, CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33, CU-38
 */
public class Cancha extends EntidadPersistente {

    /** Longitud máxima del nombre ({@code cancha.nombre VARCHAR(40)}). */
    private static final int LARGO_NOMBRE = 40;

    /** Longitud máxima de la ubicación ({@code cancha.ubicacion VARCHAR(100)}). */
    private static final int LARGO_UBICACION = 100;

    /** Longitud máxima de las características ({@code cancha.caracteristicas VARCHAR(200)}). */
    private static final int LARGO_CARACTERISTICAS = 200;

    /** Nombre. */
    private String nombre;

    /** Deporte de la cancha. */
    private Deporte deporte;

    /** Ubicación. */
    private String ubicacion;

    /** Características. */
    private String caracteristicas;

    /** Admite reservas nuevas. */
    private Boolean activa;

    /**
     * Crea una cancha activa asociada al deporte (CU-21 paso 7). Que el deporte esté activo y que el nombre sea
     * único lo verifica {@code ServicioRecursos.registrarCancha}.
     *
     * <p>Casos de uso: CU-21
     * @param nombre nombre (1 a 40 caracteres)
     * @param deporte deporte de la cancha
     * @param ubicacion ubicación opcional (hasta 100 caracteres)
     * @param caracteristicas características opcionales (hasta 200 caracteres)
     * @throws ErrorNegocio con el campo a corregir si un dato es inválido (CU-21 FA3)
     */
    public Cancha(String nombre, Deporte deporte, String ubicacion, String caracteristicas) {
        asignarDatos(nombre, deporte, ubicacion, caracteristicas);
        this.activa = Boolean.TRUE;
    }

    /**
     * Constructor de materialización: no valida (los datos vienen de la base).
     *
     * <p>Casos de uso: todos
     */
    private Cancha() {
    }

    /**
     * Aplica los cambios de CU-22 (nombre, deporte, ubicación y características); las reservas de la cancha se
     * conservan.
     *
     * <p>Casos de uso: CU-22
     * @param nombre Nombre.
     * @param deporte Deporte.
     * @param ubicacion Ubicación.
     * @param caracteristicas Características.
     * @throws ErrorNegocio con el campo a corregir si un dato es inválido (CU-22 FA3)
     */
    public void modificar(String nombre, Deporte deporte, String ubicacion, String caracteristicas) {
        asignarDatos(nombre, deporte, ubicacion, caracteristicas);
    }

    /**
     * Valida y asigna los datos editables (textos sin espacios sobrantes; opcionales vacíos → null).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param nombre nombre
     * @param deporte deporte
     * @param ubicacion ubicación
     * @param caracteristicas características
     * @throws ErrorNegocio si falta el nombre o el deporte, o un texto excede su longitud
     */
    private void asignarDatos(String nombre, Deporte deporte, String ubicacion, String caracteristicas) {
        String limpio = nombre == null ? "" : nombre.trim();
        if (limpio.isEmpty() || limpio.length() > LARGO_NOMBRE) {
            throw new ErrorNegocio("nombre", "Ingrese un nombre de hasta " + LARGO_NOMBRE + " caracteres.");
        }
        if (deporte == null) {
            throw new ErrorNegocio("idDeporte", "Elija el deporte de la cancha.");
        }
        this.nombre = limpio;
        this.deporte = deporte;
        this.ubicacion = opcional("ubicacion", ubicacion, LARGO_UBICACION);
        this.caracteristicas = opcional("caracteristicas", caracteristicas, LARGO_CARACTERISTICAS);
    }

    /**
     * Normaliza un texto opcional y valida su longitud.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param campo propiedad de {@code DatosCancha}
     * @param valor texto ingresado
     * @param largo longitud máxima
     * @return el texto sin espacios sobrantes, o null si está vacío
     * @throws ErrorNegocio con ese campo si excede la longitud
     */
    private static String opcional(String campo, String valor, int largo) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String texto = valor.trim();
        if (texto.length() > largo) {
            throw new ErrorNegocio(campo, "Admite hasta " + largo + " caracteres.");
        }
        return texto;
    }

    /**
     * Reactiva la cancha (CU-22 FA1); exige que su deporte esté activo ({@code ErrorNegocio}, paso 3.2).
     *
     * <p>Casos de uso: CU-22
     * @throws ErrorNegocio si el deporte de la cancha está inactivo
     */
    public void activar() {
        if (deporte == null || !deporte.estaActivo()) {
            throw new ErrorNegocio("No se puede activar la cancha: su deporte está inactivo.");
        }
        this.activa = Boolean.TRUE;
    }

    /**
     * La desactiva (CU-23 paso 6): deja de ofrecerse para reservas nuevas (RN-11).
     *
     * <p>Casos de uso: CU-23
     * @throws ErrorNegocio si ya estaba inactiva (CU-23 FA1)
     */
    public void desactivar() {
        if (!estaActivo()) {
            throw new ErrorNegocio("La cancha ya está inactiva.");
        }
        this.activa = Boolean.FALSE;
    }

    /**
     * Indica si está activa.
     *
     * <p>Casos de uso: CU-22, CU-23
     * @return true si está activa.
     */
    public boolean estaActivo() {
        return Boolean.TRUE.equals(activa);
    }

    /**
     * Cancha y deporte activos (ValidadorRecursoActivo, RN-11; filtro de canchas de la grilla de CU-26).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @return true si admite reservas.
     */
    public boolean estaDisponibleParaReservar() {
        return estaActivo() && deporte != null && deporte.estaActivo();
    }

    /**
     * Desmaterialización (estereotipo «persistencia», solo para {@code BrokerCancha}): reconstituye la cancha con
     * los valores de la fila; el deporte lo entrega luego {@code restaurarAsociaciones}.
     *
     * <p>Casos de uso: todos
     * @param id identificador
     * @param nombre nombre
     * @param ubicacion ubicación
     * @param caracteristicas características
     * @param activa si admite reservas nuevas
     * @return la cancha reconstituida (sin deporte)
     */
    public static Cancha reconstituir(Long id, String nombre, String ubicacion, String caracteristicas, Boolean activa) {
        Cancha cancha = new Cancha();
        cancha.asignarId(id);
        cancha.nombre = nombre;
        cancha.ubicacion = ubicacion;
        cancha.caracteristicas = caracteristicas;
        cancha.activa = activa;
        return cancha;
    }

    /**
     * Recibe el deporte que resuelve {@code BrokerCancha.cargarAsociaciones} con {@code fachada().get(idDeporte,
     * Deporte)} (asociación a uno).
     *
     * <p>Casos de uso: todos
     * @param deporte deporte de la cancha
     */
    public void restaurarAsociaciones(Deporte deporte) {
        this.deporte = deporte;
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
     * Devuelve {@code deporte}: Deporte de la cancha.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Deporte getDeporte() {
        return deporte;
    }

    /**
     * Devuelve {@code ubicacion}: Ubicación.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return el valor del atributo
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Devuelve {@code caracteristicas}: Características.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return el valor del atributo
     */
    public String getCaracteristicas() {
        return caracteristicas;
    }

    /**
     * Devuelve {@code activa}: Admite reservas nuevas.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @return el valor del atributo
     */
    public Boolean getActiva() {
        return activa;
    }
}

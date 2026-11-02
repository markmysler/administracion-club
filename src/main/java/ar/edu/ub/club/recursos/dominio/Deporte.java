package ar.edu.ub.club.recursos.dominio;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Deporte o disciplina del club. Mientras está activo sus canchas activas se ofrecen para reservas nuevas
 * (RN-11); al desactivarlo (CU-20) sus canchas dejan de ofrecerse y la fachada publica
 * {@code DeporteDesactivadoEvento}. Information Expert de sus propios datos y de su estado activo.
 *
 * <p>Entidad del dominio; la persiste {@code BrokerDeporte} (tabla {@code deporte}).
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-26, CU-27, CU-28, CU-32, CU-38
 */
public class Deporte extends EntidadPersistente {

    /** Longitud máxima del nombre ({@code deporte.nombre VARCHAR(40)}). */
    private static final int LARGO_NOMBRE = 40;

    /** Longitud máxima de la descripción ({@code deporte.descripcion VARCHAR(200)}). */
    private static final int LARGO_DESCRIPCION = 200;

    /** Nombre. */
    private String nombre;

    /** Descripción. */
    private String descripcion;

    /** Admite reservas nuevas. */
    private Boolean activo;

    /**
     * Crea un deporte activo (CU-18 paso 6) con el nombre y la descripción validados. La unicidad del nombre la
     * verifica {@code ServicioRecursos} con {@code DeportePorNombre}.
     *
     * <p>Casos de uso: CU-18
     * @param nombre nombre (1 a 40 caracteres)
     * @param descripcion descripción opcional (hasta 200 caracteres)
     * @throws ErrorNegocio con campo {@code nombre} o {@code descripcion} si no cumplen la longitud (CU-18 FA2)
     */
    public Deporte(String nombre, String descripcion) {
        asignarDatos(nombre, descripcion);
        this.activo = Boolean.TRUE;
    }

    /**
     * Constructor de materialización: no valida (los datos vienen de la base).
     *
     * <p>Casos de uso: todos
     */
    private Deporte() {
    }

    /**
     * Aplica los cambios de CU-19 (nombre y descripción); conserva las canchas asociadas y el estado.
     *
     * <p>Casos de uso: CU-19
     * @param nombre Nombre.
     * @param descripcion Descripción.
     * @throws ErrorNegocio con campo {@code nombre} o {@code descripcion} si no cumplen la longitud (CU-19 FA3)
     */
    public void modificar(String nombre, String descripcion) {
        asignarDatos(nombre, descripcion);
    }

    /**
     * Valida y asigna nombre y descripción (el nombre sin espacios sobrantes; descripción vacía → null).
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param nombre nombre ingresado
     * @param descripcion descripción ingresada
     * @throws ErrorNegocio si el nombre falta o excede 40 caracteres, o la descripción excede 200
     */
    private void asignarDatos(String nombre, String descripcion) {
        String limpio = nombre == null ? "" : nombre.trim();
        if (limpio.isEmpty() || limpio.length() > LARGO_NOMBRE) {
            throw new ErrorNegocio("nombre", "Ingrese un nombre de hasta " + LARGO_NOMBRE + " caracteres.");
        }
        String texto = descripcion == null || descripcion.isBlank() ? null : descripcion.trim();
        if (texto != null && texto.length() > LARGO_DESCRIPCION) {
            throw new ErrorNegocio("descripcion", "La descripción admite hasta " + LARGO_DESCRIPCION + " caracteres.");
        }
        this.nombre = limpio;
        this.descripcion = texto;
    }

    /**
     * Reactiva el deporte (CU-19 FA1): sus canchas activas vuelven a ofrecerse para reservas nuevas. Si ya está
     * activo no cambia nada.
     *
     * <p>Casos de uso: CU-19
     */
    public void activar() {
        this.activo = Boolean.TRUE;
    }

    /**
     * Lo desactiva (CU-20 paso 6): sus canchas dejan de ofrecerse para reservas nuevas (RN-11).
     *
     * <p>Casos de uso: CU-20
     * @throws ErrorNegocio si ya estaba inactivo (CU-20 FA1)
     */
    public void desactivar() {
        if (!estaActivo()) {
            throw new ErrorNegocio("El deporte ya está inactivo.");
        }
        this.activo = Boolean.FALSE;
    }

    /**
     * Indica si está activo.
     *
     * <p>Casos de uso: CU-20, CU-21, CU-22, CU-26, CU-27, CU-28, CU-32
     * @return true si está activo.
     */
    public boolean estaActivo() {
        return Boolean.TRUE.equals(activo);
    }

    /**
     * Desmaterialización (estereotipo «persistencia», solo para {@code BrokerDeporte}): reconstituye el deporte
     * con los valores de la fila.
     *
     * <p>Casos de uso: todos
     * @param id identificador
     * @param nombre nombre
     * @param descripcion descripción
     * @param activo si admite reservas nuevas
     * @return el deporte reconstituido
     */
    public static Deporte reconstituir(Long id, String nombre, String descripcion, Boolean activo) {
        Deporte deporte = new Deporte();
        deporte.asignarId(id);
        deporte.nombre = nombre;
        deporte.descripcion = descripcion;
        deporte.activo = activo;
        return deporte;
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
     * Devuelve {@code descripcion}: Descripción.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @return el valor del atributo
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Devuelve {@code activo}: Admite reservas nuevas.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @return el valor del atributo
     */
    public Boolean getActivo() {
        return activo;
    }
}

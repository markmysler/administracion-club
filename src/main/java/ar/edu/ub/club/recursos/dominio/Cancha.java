package ar.edu.ub.club.recursos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Cancha de un deporte.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: todos
 */
public class Cancha extends EntidadPersistente {

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
     * Crea una instancia de {@code Cancha}.
     *
     * <p>Casos de uso: CU-21
     * @param nombre valor de {@code nombre}
     * @param deporte valor de {@code deporte}
     * @param ubicacion valor de {@code ubicacion}
     * @param caracteristicas valor de {@code caracteristicas}
     */
    public Cancha(String nombre, Deporte deporte, String ubicacion, String caracteristicas) {
        // PENDIENTE
    }

    /**
     * Aplica los cambios de CU-22.
     *
     * <p>Casos de uso: CU-22
     * @param nombre Nombre.
     * @param deporte Deporte.
     * @param ubicacion Ubicación.
     * @param caracteristicas Características.
     */
    public void modificar(String nombre, Deporte deporte, String ubicacion, String caracteristicas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Reactiva la cancha; exige que su deporte esté activo ({@code ErrorNegocio}).
     *
     * <p>Casos de uso: CU-22
     */
    public void activar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * La desactiva (CU-23).
     *
     * <p>Casos de uso: CU-23
     */
    public void desactivar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Indica si está activa.
     *
     * <p>Casos de uso: CU-23
     * @return true si está activa.
     */
    public boolean estaActivo() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Cancha y deporte activos (ValidadorRecursoActivo).
     *
     * <p>Casos de uso: todos
     * @return true si admite reservas.
     */
    public boolean estaDisponibleParaReservar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param nombre valor de {@code nombre}
     * @param ubicacion valor de {@code ubicacion}
     * @param caracteristicas valor de {@code caracteristicas}
     * @param activa valor de {@code activa}
     * @return el resultado
     */
    public static Cancha reconstituir(Long id, String nombre, String ubicacion, String caracteristicas, Boolean activa) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param deporte valor de {@code deporte}
     */
    public void restaurarAsociaciones(Deporte deporte) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

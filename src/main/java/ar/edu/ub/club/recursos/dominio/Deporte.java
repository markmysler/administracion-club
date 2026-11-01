package ar.edu.ub.club.recursos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Deporte del club.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: todos
 */
public class Deporte extends EntidadPersistente {

    /** Nombre. */
    private String nombre;

    /** Descripción. */
    private String descripcion;

    /** Admite reservas nuevas. */
    private Boolean activo;

    /**
     * Crea una instancia de {@code Deporte}.
     *
     * <p>Casos de uso: CU-18
     * @param nombre valor de {@code nombre}
     * @param descripcion valor de {@code descripcion}
     */
    public Deporte(String nombre, String descripcion) {
        // PENDIENTE
    }

    /**
     * Aplica los cambios de CU-19.
     *
     * <p>Casos de uso: CU-19
     * @param nombre Nombre.
     * @param descripcion Descripción.
     */
    public void modificar(String nombre, String descripcion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Reactiva el deporte.
     *
     * <p>Casos de uso: CU-19
     */
    public void activar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Lo desactiva (CU-20).
     *
     * <p>Casos de uso: CU-20
     */
    public void desactivar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Indica si está activo.
     *
     * <p>Casos de uso: CU-20, CU-21, CU-22
     * @return true si está activo.
     */
    public boolean estaActivo() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param nombre valor de {@code nombre}
     * @param descripcion valor de {@code descripcion}
     * @param activo valor de {@code activo}
     * @return el resultado
     */
    public static Deporte reconstituir(Long id, String nombre, String descripcion, Boolean activo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

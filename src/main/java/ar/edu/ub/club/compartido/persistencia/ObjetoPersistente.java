package ar.edu.ub.club.compartido.persistencia;

/**
 * Interfaz de todo objeto que guarda y recupera {@code FachadaPersistencia}: identidad ({@code id}) y versión
 * para el control de concurrencia optimista. La implementa {@code EntidadPersistente}, superclase de las
 * entidades del dominio.
 *
 * <p>Casos de uso: todos
 */
public interface ObjetoPersistente {

    /**
     * Identificador asignado por la base; {@code null} si el objeto todavía no se insertó.
     *
     * <p>Casos de uso: todos
     * @return identificador o {@code null}
     */
    Long getId();

    /**
     * Asigna el identificador generado por la base ({@code INSERT ... RETURNING id}); lo llama el broker.
     *
     * <p>Casos de uso: todos
     * @param id identificador generado
     */
    void asignarId(Long id);

    /**
     * Versión del control optimista; {@code null} si la clase no usa control optimista (solo {@code Reserva}
     * lo usa).
     *
     * <p>Casos de uso: todos
     * @return versión o {@code null}
     */
    Integer getVersion();

    /**
     * Asigna la versión después de insertar o actualizar; lo llama el broker.
     *
     * <p>Casos de uso: todos
     * @param version nueva versión
     */
    void asignarVersion(Integer version);

    /**
     * Indica si el objeto todavía no está en la base: {@code FachadaPersistencia.put} inserta o actualiza según
     * este valor.
     *
     * <p>Casos de uso: todos
     * @return true si es nuevo
     */
    boolean esNuevo();
}

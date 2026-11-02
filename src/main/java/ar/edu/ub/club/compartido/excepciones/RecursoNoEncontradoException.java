package ar.edu.ub.club.compartido.excepciones;

/**
 * Excepción que lanza {@code FachadaPersistencia.get} cuando no existe la fila pedida (reemplaza al
 * {@code Optional} vacío de los repositorios de 5.0). {@code PresentadorBase} la traduce a «El elemento ya no
 * existe.».
 *
 * <p>Casos de uso: todos
 */
public class RecursoNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepción indicando la clase y el identificador buscados.
     *
     * <p>Casos de uso: todos
     * @param clase clase buscada
     * @param id identificador buscado
     */
    public RecursoNoEncontradoException(Class<?> clase, Object id) {
        super("No existe " + clase.getSimpleName() + " con id " + id);
    }

    /**
     * Crea la excepción con un mensaje propio.
     *
     * <p>Casos de uso: todos
     * @param mensaje detalle del recurso no encontrado
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

package ar.edu.ub.club.compartido.excepciones;

/**
 * Excepción que lanza {@code ControlAcceso.verificar} cuando la cuenta de la sesión no tiene el permiso de la
 * operación, y las fachadas cuando el recurso no pertenece al socio de la sesión (RN-20). {@code PresentadorBase}
 * la traduce a «No tiene permiso para realizar esta operación.».
 *
 * <p>Casos de uso: todos
 */
public class AccesoDenegadoException extends RuntimeException {

    /**
     * Crea la excepción con el mensaje por omisión.
     *
     * <p>Casos de uso: todos
     */
    public AccesoDenegadoException() {
        super("No tiene permiso para realizar esta operación.");
    }

    /**
     * Crea la excepción con un mensaje técnico (no se muestra al usuario).
     *
     * <p>Casos de uso: todos
     * @param mensaje detalle para la bitácora
     */
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}

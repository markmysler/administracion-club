package ar.edu.ub.club.compartido.excepciones;

/**
 * Excepción que lanzan los estados concretos del patrón State ({@code EstadoReserva}, {@code EstadoCuota},
 * {@code EstadoPago}) cuando se pide una transición que el estado actual no admite (por ejemplo, cancelar una
 * reserva ya finalizada). {@code PresentadorBase} muestra su mensaje.
 *
 * <p>Casos de uso: CU-30, CU-32, CU-33, CU-35, CU-36; tareas programadas: finalización de reservas cumplidas,
 * vencimiento de cuotas, vencimiento de pagos online pendientes
 */
public class TransicionInvalidaException extends RuntimeException {

    /**
     * Crea la excepción con el mensaje que verá el usuario.
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33, CU-35, CU-36
     * @param mensaje descripción de la transición rechazada
     */
    public TransicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}

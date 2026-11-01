package ar.edu.ub.club.reservas.dominio;

/**
 * Clase abstracta que implementa {@code ValidadorReserva} y mantiene el enlace al siguiente eslabón (Chain of
 * Responsibility). {@code validar} es un método plantilla (Template Method): llama a {@code verificar} y, si no
 * lanzó {@code ErrorNegocio}, al siguiente.
 *
 * <p>Casos de uso: PENDIENTE
 */
public abstract class ValidadorReservaEncadenado implements ValidadorReserva {

    /** Siguiente eslabón. */
    private ValidadorReserva siguiente;

    /**
     * Fija el siguiente y lo devuelve para encadenar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param siguiente Validador siguiente.
     * @return El validador recibido.
     */
    public ValidadorReserva enlazar(ValidadorReserva siguiente) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Método plantilla final.
     *
     * <p>Casos de uso: PENDIENTE
     * @param solicitud Solicitud.
     */
    public void validar(SolicitudReserva solicitud) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Operación abstracta protegida que redefine cada validador concreto; lanza {@code ErrorNegocio} con el motivo.
     *
     * <p>Casos de uso: PENDIENTE
     * @param solicitud Solicitud.
     */
    protected abstract void verificar(SolicitudReserva solicitud);
}

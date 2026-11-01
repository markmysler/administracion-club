package ar.edu.ub.club.reservas.dominio;

/**
 * Clase abstracta que implementa {@code ValidadorReserva} y mantiene el enlace al siguiente eslabón (Chain of
 * Responsibility). {@code validar} es un método plantilla (Template Method) final: llama a {@code verificar} y, si
 * no lanzó {@code ErrorNegocio}, al siguiente. Así cada validador concreto solo implementa su regla.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public abstract class ValidadorReservaEncadenado implements ValidadorReserva {

    /** Siguiente eslabón ({@code null} en el último). */
    private ValidadorReserva siguiente;

    /**
     * Fija el siguiente eslabón y lo devuelve para encadenar.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param siguiente Validador siguiente.
     * @return El validador recibido.
     */
    public ValidadorReserva enlazar(ValidadorReserva siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    /**
     * Método plantilla final: {@code verificar(solicitud)}; si no hubo excepción y hay siguiente, {@code
     * siguiente.validar(solicitud)}. El primer eslabón que no cumple corta la cadena.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param solicitud Solicitud.
     */
    @Override
    public final void validar(SolicitudReserva solicitud) {
        verificar(solicitud);
        if (siguiente != null) {
            siguiente.validar(solicitud);
        }
    }

    /**
     * Operación abstracta protegida que redefine cada validador concreto; lanza {@code ErrorNegocio} con el motivo.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param solicitud Solicitud.
     */
    protected abstract void verificar(SolicitudReserva solicitud);
}

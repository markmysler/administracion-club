package ar.edu.ub.club.reservas.dominio;

/**
 * Interfaz común de los eslabones de la cadena de validadores (GoF Chain of Responsibility). El cliente ({@code
 * ServicioReservas}) solo conoce el primer eslabón y no elige entre validadores: todos se ejecutan en secuencia
 * hasta que uno rechaza la solicitud (RN-11 a RN-15).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public interface ValidadorReserva {

    /**
     * Termina sin resultado si la solicitud cumple; lanza {@code ErrorNegocio} con el motivo si no cumple.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param solicitud solicitud a validar
     */
    void validar(SolicitudReserva solicitud);
}

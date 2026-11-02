package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;

/**
 * Eslabón concreto de la Cadena de Responsabilidad (posición 2) de RN-11: la cancha y su deporte deben estar
 * activos. Sin atributos.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorRecursoActivo extends ValidadorReservaEncadenado {

    /**
     * Crea el validador (no necesita datos: la cancha viaja en la solicitud).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     */
    public ValidadorRecursoActivo() {
        super();
    }

    /**
     * Si {@code s.cancha.estaDisponibleParaReservar()} es false (cancha o deporte inactivos) lanza {@code
     * ErrorNegocio} («La cancha no está disponible para reservas»).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s solicitud
     * @throws ErrorNegocio si la cancha no admite reservas
     */
    @Override
    protected void verificar(SolicitudReserva s) {
        if (s.getCancha() == null || !s.getCancha().estaDisponibleParaReservar()) {
            throw new ErrorNegocio("La cancha no está disponible para reservas.");
        }
    }
}

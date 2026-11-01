package ar.edu.ub.club.reservas.dominio;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Eslabón concreto de la Cadena de Responsabilidad (GoF Chain of Responsibility).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorRecursoActivo extends ValidadorReservaEncadenado {

    /**
     * Crea una instancia de {@code ValidadorRecursoActivo}.
     *
     * <p>Casos de uso: CU-27
     */
    public ValidadorRecursoActivo() {
        // PENDIENTE
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s valor de {@code s}
     */
    protected void verificar(SolicitudReserva s) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

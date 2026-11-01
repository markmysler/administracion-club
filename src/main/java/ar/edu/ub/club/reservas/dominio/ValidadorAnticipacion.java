package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.ReglaReserva;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Eslabón concreto de la Cadena de Responsabilidad (GoF Chain of Responsibility).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorAnticipacion extends ValidadorReservaEncadenado {

    /** Atributo regla. */
    private ReglaReserva regla;

    /**
     * Crea una instancia de {@code ValidadorAnticipacion}.
     *
     * <p>Casos de uso: CU-27
     * @param regla valor de {@code regla}
     */
    public ValidadorAnticipacion(ReglaReserva regla) {
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

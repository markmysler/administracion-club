package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Eslabón concreto de la Cadena de Responsabilidad (GoF Chain of Responsibility).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorLimiteReservas extends ValidadorReservaEncadenado {

    /** Atributo regla. */
    private ReglaReserva regla;

    /** Atributo vigentes. */
    private List<Reserva> vigentes;

    /**
     * Crea una instancia de {@code ValidadorLimiteReservas}.
     *
     * <p>Casos de uso: CU-27
     * @param regla valor de {@code regla}
     * @param vigentes valor de {@code vigentes}
     */
    public ValidadorLimiteReservas(ReglaReserva regla, List<Reserva> vigentes) {
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

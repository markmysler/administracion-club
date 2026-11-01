package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import ar.edu.ub.club.agenda.dominio.Feriado;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Eslabón concreto de la Cadena de Responsabilidad (GoF Chain of Responsibility).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorHorarioClub extends ValidadorReservaEncadenado {

    /** Atributo disponibilidad. */
    private DisponibilidadClub disponibilidad;

    /** Atributo feriados. */
    private List<Feriado> feriados;

    /**
     * Crea una instancia de {@code ValidadorHorarioClub}.
     *
     * <p>Casos de uso: CU-27
     * @param disponibilidad valor de {@code disponibilidad}
     * @param feriados valor de {@code feriados}
     */
    public ValidadorHorarioClub(DisponibilidadClub disponibilidad, List<Feriado> feriados) {
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

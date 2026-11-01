package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.Turno;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Eslabón concreto de la Cadena de Responsabilidad (GoF Chain of Responsibility).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorTurnoLibre extends ValidadorReservaEncadenado {

    /** Atributo ocupados. */
    private List<Turno> ocupados;

    /**
     * Crea una instancia de {@code ValidadorTurnoLibre}.
     *
     * <p>Casos de uso: CU-27
     * @param ocupados valor de {@code ocupados}
     */
    public ValidadorTurnoLibre(List<Turno> ocupados) {
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

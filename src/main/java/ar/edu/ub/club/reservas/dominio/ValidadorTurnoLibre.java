package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.util.List;

/**
 * Eslabón concreto de la Cadena de Responsabilidad (posición 7) de RN-12: el turno no debe superponerse con otra
 * reserva confirmada de la cancha. Es la verificación en la aplicación; la base de datos la garantiza con la
 * restricción de exclusión (D-05).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorTurnoLibre extends ValidadorReservaEncadenado {

    /** Turnos de otras reservas confirmadas de la cancha en la fecha. */
    private final List<Turno> ocupados;

    /**
     * Crea el validador con los turnos ocupados que obtuvo la fábrica.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param ocupados turnos ocupados de la cancha en la fecha
     */
    public ValidadorTurnoLibre(List<Turno> ocupados) {
        this.ocupados = ocupados == null ? List.of() : List.copyOf(ocupados);
    }

    /**
     * Si algún turno de {@code ocupados} cumple {@code seSuperponeCon(s.turno)} lanza {@code ErrorNegocio} («El
     * turno ya fue reservado; elija otro turno»).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s solicitud
     * @throws ErrorNegocio si el turno está ocupado
     */
    @Override
    protected void verificar(SolicitudReserva s) {
        for (Turno ocupado : ocupados) {
            if (ocupado.seSuperponeCon(s.getTurno())) {
                throw new ErrorNegocio("El turno ya fue reservado; elija otro turno.");
            }
        }
    }
}

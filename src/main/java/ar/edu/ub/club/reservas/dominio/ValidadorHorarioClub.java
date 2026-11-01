package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import ar.edu.ub.club.agenda.dominio.Feriado;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.util.List;

/**
 * Eslabón concreto de la Cadena de Responsabilidad (posición 3) de RN-13: día de atención, horario y feriados.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorHorarioClub extends ValidadorReservaEncadenado {

    /** Días y horario de atención del club. */
    private final DisponibilidadClub disponibilidad;

    /** Feriados de la fecha del turno (lista vacía o con un elemento). */
    private final List<Feriado> feriados;

    /**
     * Crea el validador con los datos que obtuvo la fábrica.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param disponibilidad días y horario de atención
     * @param feriados feriados de la fecha del turno
     */
    public ValidadorHorarioClub(DisponibilidadClub disponibilidad, List<Feriado> feriados) {
        this.disponibilidad = disponibilidad;
        this.feriados = feriados == null ? List.of() : List.copyOf(feriados);
    }

    /**
     * Lanza {@code ErrorNegocio} si la lista de feriados no está vacía («La fecha elegida es feriado»), si {@code
     * disponibilidad.atiende(fecha)} es false («El club no atiende ese día») o si {@code
     * disponibilidad.dentroDelHorario(turno)} es false («El turno está fuera del horario de atención»).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s solicitud
     * @throws ErrorNegocio si el turno no cae en un horario de atención
     */
    @Override
    protected void verificar(SolicitudReserva s) {
        if (!feriados.isEmpty()) {
            throw new ErrorNegocio("La fecha elegida es feriado.");
        }
        if (!disponibilidad.atiende(s.getTurno().getFecha())) {
            throw new ErrorNegocio("El club no atiende ese día.");
        }
        if (!disponibilidad.dentroDelHorario(s.getTurno())) {
            throw new ErrorNegocio("El turno está fuera del horario de atención.");
        }
    }
}

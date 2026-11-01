package ar.edu.ub.club.agenda.aplicacion;

import ar.edu.ub.club.agenda.dominio.DiaSemana;
import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import java.time.LocalTime;
import java.util.Set;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class DatosDisponibilidad {

    /** Atributo diasAtencion. */
    private Set<DiaSemana> diasAtencion;

    /** Atributo horaApertura. */
    private LocalTime horaApertura;

    /** Atributo horaCierre. */
    private LocalTime horaCierre;

    /** Atributo confirmar. */
    private boolean confirmar;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param disponibilidad valor de {@code disponibilidad}
     * @return el resultado
     */
    public static DatosDisponibilidad desde(DisponibilidadClub disponibilidad) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

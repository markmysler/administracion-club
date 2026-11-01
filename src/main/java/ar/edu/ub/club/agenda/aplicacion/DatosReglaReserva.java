package ar.edu.ub.club.agenda.aplicacion;

import ar.edu.ub.club.agenda.dominio.ReglaReserva;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class DatosReglaReserva {

    /** Atributo anticipacionMinimaHoras. */
    private Integer anticipacionMinimaHoras;

    /** Atributo anticipacionMaximaDias. */
    private Integer anticipacionMaximaDias;

    /** Atributo duracionTurnoMinutos. */
    private Integer duracionTurnoMinutos;

    /** Atributo limiteReservasVigentesPorSocio. */
    private Integer limiteReservasVigentesPorSocio;

    /** Atributo anticipacionMinimaCancelacionHoras. */
    private Integer anticipacionMinimaCancelacionHoras;

    /** Atributo anticipacionMinimaModificacionHoras. */
    private Integer anticipacionMinimaModificacionHoras;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param regla valor de {@code regla}
     * @return el resultado
     */
    public static DatosReglaReserva desde(ReglaReserva regla) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

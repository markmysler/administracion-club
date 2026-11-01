package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.agenda.aplicacion.DatosReglaReserva;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Parámetros de reserva (fila única). Information Expert de las anticipaciones.
 *
 * <p>Casos de uso: todos
 */
public class ReglaReserva extends EntidadPersistente {

    /** Anticipación mínima. */
    private Integer anticipacionMinimaHoras;

    /** Anticipación máxima. */
    private Integer anticipacionMaximaDias;

    /** Duración del turno. */
    private Integer duracionTurnoMinutos;

    /** Límite de reservas vigentes. */
    private Integer limiteReservasVigentesPorSocio;

    /** Anticipación de cancelación. */
    private Integer anticipacionMinimaCancelacionHoras;

    /** Anticipación de modificación. */
    private Integer anticipacionMinimaModificacionHoras;

    /** Identificador de la única fila. */
    private Long ID_UNICO;

    /**
     * Aplica y valida los seis parámetros (CU-25).
     *
     * <p>Casos de uso: CU-25
     * @param valores Parámetros.
     * @param disponibilidad Horario para validar la duración.
     */
    public void modificar(DatosReglaReserva valores, DisponibilidadClub disponibilidad) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * La fecha está entre hoy y la anticipación máxima (CU-26).
     *
     * <p>Casos de uso: CU-26
     * @param fecha Fecha.
     * @param hoy Fecha actual.
     * @return true si se puede consultar.
     */
    public boolean fechaConsultable(LocalDate fecha, LocalDate hoy) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Inicio del turno menos ahora ≥ anticipación mínima.
     *
     * <p>Casos de uso: CU-26
     * @param turno Turno.
     * @param ahora Momento actual.
     * @return true si cumple.
     */
    public boolean cumpleAnticipacionMinima(Turno turno, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Inicio del turno menos ahora ≥ anticipación mínima de cancelación (RN-14).
     *
     * <p>Casos de uso: CU-30
     * @param turno Turno.
     * @param ahora Momento actual.
     * @return true si se puede cancelar.
     */
    public boolean admiteCancelacion(Turno turno, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve la duración del turno; {@code CalendarioTurnos} la usa para armar la grilla de turnos (CU-26).
     *
     * <p>Casos de uso: CU-26
     * @return Duración en minutos.
     */
    public Integer getDuracionTurnoMinutos() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param anticipacionMinimaHoras valor de {@code anticipacionMinimaHoras}
     * @param anticipacionMaximaDias valor de {@code anticipacionMaximaDias}
     * @param duracionTurnoMinutos valor de {@code duracionTurnoMinutos}
     * @param limiteReservasVigentesPorSocio valor de {@code limiteReservasVigentesPorSocio}
     * @param anticipacionMinimaCancelacionHoras valor de {@code anticipacionMinimaCancelacionHoras}
     * @param anticipacionMinimaModificacionHoras valor de {@code anticipacionMinimaModificacionHoras}
     * @return el resultado
     */
    public static ReglaReserva reconstituir(Long id, Integer anticipacionMinimaHoras, Integer anticipacionMaximaDias, Integer duracionTurnoMinutos, Integer limiteReservasVigentesPorSocio, Integer anticipacionMinimaCancelacionHoras, Integer anticipacionMinimaModificacionHoras) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code anticipacionMinimaHoras}: Anticipación mínima.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Integer getAnticipacionMinimaHoras() {
        return anticipacionMinimaHoras;
    }

    /**
     * Devuelve {@code anticipacionMaximaDias}: Anticipación máxima.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Integer getAnticipacionMaximaDias() {
        return anticipacionMaximaDias;
    }

    /**
     * Devuelve {@code limiteReservasVigentesPorSocio}: Límite de reservas vigentes.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Integer getLimiteReservasVigentesPorSocio() {
        return limiteReservasVigentesPorSocio;
    }

    /**
     * Devuelve {@code anticipacionMinimaCancelacionHoras}: Anticipación de cancelación.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Integer getAnticipacionMinimaCancelacionHoras() {
        return anticipacionMinimaCancelacionHoras;
    }

    /**
     * Devuelve {@code anticipacionMinimaModificacionHoras}: Anticipación de modificación.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Integer getAnticipacionMinimaModificacionHoras() {
        return anticipacionMinimaModificacionHoras;
    }

    /**
     * Devuelve {@code ID_UNICO}: Identificador de la única fila.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Long getID_UNICO() {
        return ID_UNICO;
    }
}

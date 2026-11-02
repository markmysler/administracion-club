package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.agenda.aplicacion.DatosReglaReserva;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Parámetros de reserva (fila única {@code ID_UNICO}). Information Expert de las anticipaciones: decide si una
 * fecha se puede consultar (RN-13, CU-26), si un turno cumple la anticipación mínima para reservar (RN-13) y si
 * una reserva admite cancelación (RN-14). La usan {@code CalendarioTurnos} y los validadores de la cadena de
 * reservas ({@code ValidadorAnticipacion}, {@code ValidadorDuracionTurno}, {@code ValidadorLimiteReservas}).
 *
 * <p>Entidad del dominio; la persiste {@code BrokerReglaReserva} (tabla {@code regla_reserva}).
 *
 * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-30, CU-32
 */
public class ReglaReserva extends EntidadPersistente {

    /** Identificador de la única fila ({@code regla_reserva.id = 1}, CHECK en la base). */
    public static final Long ID_UNICO = 1L;

    /** Máximo que admite una columna SMALLINT de {@code regla_reserva}. */
    private static final int MAXIMO_SMALLINT = Short.MAX_VALUE;

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

    /**
     * Constructor de materialización; la fila única la crea la migración V6 y solo se modifica (CU-25).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-30, CU-32
     */
    private ReglaReserva() {
    }

    /**
     * Aplica y valida los seis parámetros (CU-25): todos enteros mayores que cero (paso 4, FA2), la anticipación
     * mínima para reservar menor que la máxima expresada en horas y la duración del turno no mayor que el horario
     * diario del club (paso 5, FA3). Si algo falla lanza {@code ErrorNegocio} y no cambia nada.
     *
     * <p>Casos de uso: CU-25
     * @param valores Parámetros.
     * @param disponibilidad Horario para validar la duración.
     * @throws ErrorNegocio con el campo de {@code DatosReglaReserva} que hay que corregir
     */
    public void modificar(DatosReglaReserva valores, DisponibilidadClub disponibilidad) {
        int minima = positivo("anticipacionMinimaHoras", valores.getAnticipacionMinimaHoras());
        int maxima = positivo("anticipacionMaximaDias", valores.getAnticipacionMaximaDias());
        int duracion = positivo("duracionTurnoMinutos", valores.getDuracionTurnoMinutos());
        int limite = positivo("limiteReservasVigentesPorSocio", valores.getLimiteReservasVigentesPorSocio());
        int cancelacion = positivo("anticipacionMinimaCancelacionHoras", valores.getAnticipacionMinimaCancelacionHoras());
        int modificacion = positivo("anticipacionMinimaModificacionHoras", valores.getAnticipacionMinimaModificacionHoras());
        if (minima >= maxima * 24L) {
            throw new ErrorNegocio("anticipacionMinimaHoras", "La anticipación mínima para reservar debe ser menor que la máxima ("
                    + (maxima * 24L) + " horas).");
        }
        if (disponibilidad != null) {
            long horario = Duration.between(disponibilidad.getHoraApertura(), disponibilidad.getHoraCierre()).toMinutes();
            if (duracion > horario) {
                throw new ErrorNegocio("duracionTurnoMinutos", "La duración del turno no puede superar el horario diario del club ("
                        + horario + " minutos).");
            }
        }
        this.anticipacionMinimaHoras = minima;
        this.anticipacionMaximaDias = maxima;
        this.duracionTurnoMinutos = duracion;
        this.limiteReservasVigentesPorSocio = limite;
        this.anticipacionMinimaCancelacionHoras = cancelacion;
        this.anticipacionMinimaModificacionHoras = modificacion;
    }

    /**
     * Valida que un parámetro sea un entero mayor que cero y que entre en la columna SMALLINT (CU-25 FA2).
     *
     * <p>Casos de uso: CU-25
     * @param campo propiedad de {@code DatosReglaReserva}
     * @param valor valor ingresado
     * @return el valor validado
     * @throws ErrorNegocio con ese campo si el valor falta, no es positivo o es demasiado grande
     */
    private static int positivo(String campo, Integer valor) {
        if (valor == null || valor <= 0) {
            throw new ErrorNegocio(campo, "Ingrese un número entero mayor que cero.");
        }
        if (valor > MAXIMO_SMALLINT) {
            throw new ErrorNegocio(campo, "El valor no puede superar " + MAXIMO_SMALLINT + ".");
        }
        return valor;
    }

    /**
     * La fecha está entre hoy y la anticipación máxima, ambos inclusive (RN-13; CU-26 paso 4).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param fecha Fecha.
     * @param hoy Fecha actual.
     * @return true si se puede consultar.
     */
    public boolean fechaConsultable(LocalDate fecha, LocalDate hoy) {
        return !fecha.isBefore(hoy) && !fecha.isAfter(hoy.plusDays(anticipacionMaximaDias));
    }

    /**
     * Inicio del turno menos ahora ≥ anticipación mínima para reservar (RN-13; CU-26 paso 9).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param turno Turno.
     * @param ahora Momento actual.
     * @return true si cumple.
     */
    public boolean cumpleAnticipacionMinima(Turno turno, LocalDateTime ahora) {
        return !turno.inicio().isBefore(ahora.plusHours(anticipacionMinimaHoras));
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
        return !turno.inicio().isBefore(ahora.plusHours(anticipacionMinimaCancelacionHoras));
    }

    /**
     * Devuelve la duración del turno; {@code CalendarioTurnos} la usa para armar la grilla de turnos (CU-26).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return Duración en minutos.
     */
    public Integer getDuracionTurnoMinutos() {
        return duracionTurnoMinutos;
    }

    /**
     * Desmaterialización (estereotipo «persistencia», solo para {@code BrokerReglaReserva}): reconstituye la fila
     * única con sus seis parámetros.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-30, CU-32
     * @param id identificador (siempre {@code ID_UNICO})
     * @param anticipacionMinimaHoras anticipación mínima para reservar
     * @param anticipacionMaximaDias anticipación máxima para reservar
     * @param duracionTurnoMinutos duración del turno
     * @param limiteReservasVigentesPorSocio límite de reservas vigentes por socio
     * @param anticipacionMinimaCancelacionHoras anticipación mínima para cancelar
     * @param anticipacionMinimaModificacionHoras anticipación mínima para modificar
     * @return la regla reconstituida
     */
    public static ReglaReserva reconstituir(Long id, Integer anticipacionMinimaHoras, Integer anticipacionMaximaDias, Integer duracionTurnoMinutos, Integer limiteReservasVigentesPorSocio, Integer anticipacionMinimaCancelacionHoras, Integer anticipacionMinimaModificacionHoras) {
        ReglaReserva regla = new ReglaReserva();
        regla.asignarId(id);
        regla.anticipacionMinimaHoras = anticipacionMinimaHoras;
        regla.anticipacionMaximaDias = anticipacionMaximaDias;
        regla.duracionTurnoMinutos = duracionTurnoMinutos;
        regla.limiteReservasVigentesPorSocio = limiteReservasVigentesPorSocio;
        regla.anticipacionMinimaCancelacionHoras = anticipacionMinimaCancelacionHoras;
        regla.anticipacionMinimaModificacionHoras = anticipacionMinimaModificacionHoras;
        return regla;
    }

    /**
     * Devuelve {@code anticipacionMinimaHoras}: Anticipación mínima.
     *
     * <p>Casos de uso: CU-25, CU-26, CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public Integer getAnticipacionMinimaHoras() {
        return anticipacionMinimaHoras;
    }

    /**
     * Devuelve {@code anticipacionMaximaDias}: Anticipación máxima.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public Integer getAnticipacionMaximaDias() {
        return anticipacionMaximaDias;
    }

    /**
     * Devuelve {@code limiteReservasVigentesPorSocio}: Límite de reservas vigentes.
     *
     * <p>Casos de uso: CU-25, CU-27, CU-28
     * @return el valor del atributo
     */
    public Integer getLimiteReservasVigentesPorSocio() {
        return limiteReservasVigentesPorSocio;
    }

    /**
     * Devuelve {@code anticipacionMinimaCancelacionHoras}: Anticipación de cancelación.
     *
     * <p>Casos de uso: CU-25, CU-30
     * @return el valor del atributo
     */
    public Integer getAnticipacionMinimaCancelacionHoras() {
        return anticipacionMinimaCancelacionHoras;
    }

    /**
     * Devuelve {@code anticipacionMinimaModificacionHoras}: Anticipación de modificación.
     *
     * <p>Casos de uso: CU-25, CU-32
     * @return el valor del atributo
     */
    public Integer getAnticipacionMinimaModificacionHoras() {
        return anticipacionMinimaModificacionHoras;
    }

    /**
     * Devuelve {@code ID_UNICO}: Identificador de la única fila (equivale a la constante de clase).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26
     * @return 1
     */
    public Long getID_UNICO() {
        return ID_UNICO;
    }
}

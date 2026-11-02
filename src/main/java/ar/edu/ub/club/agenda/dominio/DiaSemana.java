package ar.edu.ub.club.agenda.dominio;

import java.time.LocalDate;

/**
 * Enumeración de los días de la semana en los que puede atender el club ({@code DisponibilidadClub.diasAtencion}).
 * El orden de los valores sigue la norma ISO 8601 (1 = lunes … 7 = domingo), que es el número que se guarda en la
 * columna {@code dia_atencion.dia} (07C). Se usa para decidir si el club atiende en una fecha (RN-13).
 *
 * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
 */
public enum DiaSemana {

    /** Lunes (ISO 1). */
    LUNES,
    /** Martes (ISO 2). */
    MARTES,
    /** Miércoles (ISO 3). */
    MIERCOLES,
    /** Jueves (ISO 4). */
    JUEVES,
    /** Viernes (ISO 5). */
    VIERNES,
    /** Sábado (ISO 6). */
    SABADO,
    /** Domingo (ISO 7). */
    DOMINGO;

    /**
     * Agregado en la implementación: número ISO 8601 del día (1 = lunes … 7 = domingo), el valor que
     * {@code BrokerDisponibilidadClub} guarda en {@code dia_atencion.dia}.
     *
     * <p>Casos de uso: CU-24
     * @return número de 1 a 7
     */
    public int numeroIso() {
        return ordinal() + 1;
    }

    /**
     * Agregado en la implementación: convierte el número ISO leído de {@code dia_atencion.dia} (consulta
     * {@code DiasAtencion}) en el valor de la enumeración.
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @param numero número ISO de 1 a 7
     * @return día correspondiente
     * @throws IllegalArgumentException si el número no está entre 1 y 7 (la base lo impide con un CHECK)
     */
    public static DiaSemana desdeNumeroIso(int numero) {
        if (numero < 1 || numero > 7) {
            throw new IllegalArgumentException("Día de la semana inválido: " + numero);
        }
        return values()[numero - 1];
    }

    /**
     * Agregado en la implementación: día de la semana de una fecha; lo usa {@code DisponibilidadClub.atiende}.
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @param fecha fecha
     * @return día de la semana de la fecha
     */
    public static DiaSemana de(LocalDate fecha) {
        return desdeNumeroIso(fecha.getDayOfWeek().getValue());
    }
}

package ar.edu.ub.club.compartido.ui;

import java.time.LocalDate;

/**
 * Modelo de vista común (record inmutable) con un rango de fechas; lo devuelve {@code FiltroFechas}.
 *
 * <p>Casos de uso: CU-29, CU-31, CU-37, CU-38, CU-39
 *
 * @param desde fecha inicial
 * @param hasta fecha final
 */
public record RangoFechas(LocalDate desde, LocalDate hasta) {
}

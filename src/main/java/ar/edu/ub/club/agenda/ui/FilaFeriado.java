package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.dominio.Feriado;
import java.time.LocalDate;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: PENDIENTE
 */
public record FilaFeriado(Long id, LocalDate fecha, String descripcion) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param feriado valor de {@code feriado}
     * @return el resultado
     */
    public static FilaFeriado desde(Feriado feriado) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

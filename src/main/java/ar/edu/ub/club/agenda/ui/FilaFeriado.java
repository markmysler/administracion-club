package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.dominio.Feriado;
import java.time.LocalDate;

/**
 * Fila de la grilla de feriados de P-17: fecha y descripción, con el id para eliminarlo (CU-24 FA2).
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-24
 *
 * @param id identificador del feriado
 * @param fecha fecha
 * @param descripcion descripción
 */
public record FilaFeriado(Long id, LocalDate fecha, String descripcion) {

    /**
     * Arma la fila a partir del feriado.
     *
     * <p>Casos de uso: CU-24
     * @param feriado feriado
     * @return la fila
     */
    public static FilaFeriado desde(Feriado feriado) {
        return new FilaFeriado(feriado.getId(), feriado.getFecha(), feriado.getDescripcion());
    }
}

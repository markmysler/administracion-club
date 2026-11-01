package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.reportes.aplicacion.ResultadoReporte;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-38, CU-39
 */
public record TablaReporte(String titulo, List<String> columnas, List<List<String>> filas, List<String> totales) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param resultado valor de {@code resultado}
     * @return el resultado
     */
    public static TablaReporte desde(ResultadoReporte resultado) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

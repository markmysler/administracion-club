package ar.edu.ub.club.reportes.aplicacion;

import java.util.List;

/**
 * Subclases concretas de {@code Reporte}. Atributo común: {@code - consultas: ConsultasReportes} (inyectada por
 * constructor).
 *
 * <p>Casos de uso: CU-38
 */
public class ReporteUsoCanchas extends Reporte {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param filtro valor de {@code filtro}
     * @return el resultado
     */
    protected List obtenerDatos(FiltroReporte filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param filas valor de {@code filas}
     * @return el resultado
     */
    protected Totales calcularTotales(List filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

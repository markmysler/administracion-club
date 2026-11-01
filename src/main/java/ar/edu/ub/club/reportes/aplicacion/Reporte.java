package ar.edu.ub.club.reportes.aplicacion;

import java.util.List;

/**
 * Clase abstracta de {@code reportes.aplicacion} (Template Method) que usa {@code ServicioReportes}: {@code
 * generar} fija los pasos y cada subclase completa la obtención de datos y los totales. Es un bean sin estado
 * compartido en {@code ServicioReportes.reportes}, por eso los pasos reciben sus datos por parámetro.
 *
 * <p>Casos de uso: PENDIENTE
 */
public abstract class Reporte {

    /**
     * Método plantilla: {@code validarFiltro(filtro)}; {@code filas = obtenerDatos(filtro)}; {@code totales =
     * calcularTotales(filas)}; {@code return armarResultado(filas, totales)}.
     *
     * <p>Casos de uso: PENDIENTE
     * @param filtro Período, deporte y cancha.
     * @return {@code ResultadoReporte}.
     */
    public final ResultadoReporte generar(FiltroReporte filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Lanza {@code ErrorNegocio} si el período está incompleto o {@code desde &gt; hasta}.
     *
     * <p>Casos de uso: PENDIENTE
     * @param filtro valor de {@code filtro}
     */
    protected void validarFiltro(FiltroReporte filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Filas del reporte, obtenidas de {@code ConsultasReportes}.
     *
     * <p>Casos de uso: PENDIENTE
     * @param filtro valor de {@code filtro}
     * @return el resultado
     */
    protected abstract List obtenerDatos(FiltroReporte filtro);

    /**
     * Totales del reporte.
     *
     * <p>Casos de uso: PENDIENTE
     * @param filas valor de {@code filas}
     * @return el resultado
     */
    protected abstract Totales calcularTotales(List filas);

    /**
     * Arma el {@code ResultadoReporte} con título, columnas, filas y totales; las subclases pueden redefinirlo.
     *
     * <p>Casos de uso: PENDIENTE
     * @param filas valor de {@code filas}
     * @param totales valor de {@code totales}
     * @return el resultado
     */
    protected ResultadoReporte armarResultado(List filas, Totales totales) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

package ar.edu.ub.club.reportes.aplicacion;

import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ResultadoReporte {

    /** Atributo titulo. */
    private String titulo;

    /** Atributo filtro. */
    private FiltroReporte filtro;

    /** Atributo columnas. */
    private List<String> columnas;

    /** Atributo filas. */
    private List<List<Object>> filas;

    /** Atributo totales. */
    private Totales totales;

    /**
     * Devuelve {@code titulo}: titulo.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Devuelve {@code filtro}: filtro.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public FiltroReporte getFiltro() {
        return filtro;
    }

    /**
     * Devuelve {@code columnas}: columnas.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public List<String> getColumnas() {
        return columnas;
    }

    /**
     * Devuelve {@code filas}: filas.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public List<List<Object>> getFilas() {
        return filas;
    }

    /**
     * Devuelve {@code totales}: totales.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Totales getTotales() {
        return totales;
    }
}

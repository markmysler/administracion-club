package ar.edu.ub.club.reportes.aplicacion;

import java.util.List;

/**
 * Resultado de un reporte: título, filtro aplicado, columnas, filas (cada celda con su tipo del dominio: texto, número,
 * {@code Dinero}) y totales. Lo crea el paso {@code armarResultado} del método plantilla {@code Reporte.generar}; lo
 * consumen el presentador ({@code TablaReporte.desde}) y las estrategias {@code ExportadorReporte}.
 *
 * <p>Objeto de datos de salida de la capa de aplicación (inmutable).
 *
 * <p>Casos de uso: CU-38, CU-39
 */
public class ResultadoReporte {

    /** Título del reporte. */
    private String titulo;

    /** Filtro con que se generó (período y, en CU-38, deporte y cancha). */
    private FiltroReporte filtro;

    /** Encabezados de las columnas del detalle. */
    private List<String> columnas;

    /** Filas del detalle; cada una tiene una celda por columna. */
    private List<List<Object>> filas;

    /** Totales del reporte. */
    private Totales totales;

    /**
     * Crea el resultado con todos sus datos.
     *
     * <p>Agregado en la implementación: constructor con los atributos del diseño.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param titulo título del reporte
     * @param filtro filtro con que se generó
     * @param columnas encabezados de columna
     * @param filas filas del detalle
     * @param totales totales calculados
     */
    public ResultadoReporte(String titulo, FiltroReporte filtro, List<String> columnas, List<List<Object>> filas,
                            Totales totales) {
        this.titulo = titulo;
        this.filtro = filtro;
        this.columnas = List.copyOf(columnas);
        this.filas = List.copyOf(filas);
        this.totales = totales;
    }

    /**
     * Devuelve el título del reporte.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Devuelve el filtro con que se generó el reporte.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return filtro aplicado
     */
    public FiltroReporte getFiltro() {
        return filtro;
    }

    /**
     * Devuelve los encabezados de las columnas.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return columnas
     */
    public List<String> getColumnas() {
        return columnas;
    }

    /**
     * Devuelve las filas del detalle.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return filas, una lista de celdas por fila
     */
    public List<List<Object>> getFilas() {
        return filas;
    }

    /**
     * Devuelve los totales del reporte.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return totales
     */
    public Totales getTotales() {
        return totales;
    }
}

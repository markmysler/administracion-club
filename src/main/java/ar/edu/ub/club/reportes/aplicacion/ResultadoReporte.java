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
}

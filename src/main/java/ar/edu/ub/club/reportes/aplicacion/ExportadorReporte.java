package ar.edu.ub.club.reportes.aplicacion;

/**
 * {@code ExportadorReporte} es la interfaz del patrón Strategy (reportes.aplicacion) con la operación {@code +
 * exportar(resultado: ResultadoReporte): ArchivoExportado}. La implementan {@code ExportadorExcel} (Apache POI;
 * tipo de contenido de hoja de cálculo, extensión .xlsx) y {@code ExportadorPdf} (OpenPDF; {@code
 * application/pdf}). {@code ServicioReportes.exportadores: Map&lt;FormatoExportacion, ExportadorReporte&gt;}
 * elige la estrategia por el formato pedido, sin preguntar por el tipo.
 *
 * <p>Casos de uso: CU-38
 */
public interface ExportadorReporte {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param r valor de {@code r}
     * @return el resultado
     */
    ArchivoExportado exportar(ResultadoReporte r);
}

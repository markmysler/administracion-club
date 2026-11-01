package ar.edu.ub.club.reportes.aplicacion;

/**
 * {@code ExportadorReporte} es la interfaz del patrón Strategy (reportes.aplicacion) con la operación {@code +
 * exportar(resultado: ResultadoReporte): ArchivoExportado}. La implementan {@code ExportadorExcel} (Apache POI;
 * tipo de contenido de hoja de cálculo, extensión .xlsx) y {@code ExportadorPdf} (OpenPDF; {@code
 * application/pdf}). {@code ServicioReportes.exportadores: Map&lt;FormatoExportacion, ExportadorReporte&gt;}
 * elige la estrategia por el formato pedido, sin preguntar por el tipo.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ExportadorExcel implements ExportadorReporte {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param resultado valor de {@code resultado}
     * @return el resultado
     */
    public ArchivoExportado exportar(ResultadoReporte resultado) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

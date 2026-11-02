package ar.edu.ub.club.reportes.aplicacion;

/**
 * Interfaz del patrón <b>Strategy</b> (reportes.aplicacion) con la operación {@code exportar(resultado):
 * ArchivoExportado}. La implementan {@code ExportadorExcel} (Apache POI; hoja de cálculo .xlsx) y {@code
 * ExportadorPdf} (OpenPDF; {@code application/pdf}). {@code ServicioReportes.exportadores: Map<FormatoExportacion,
 * ExportadorReporte>} elige la estrategia por el formato pedido, sin preguntar por el tipo; agregar un formato es
 * agregar una estrategia al mapa (sec_cu38_p4).
 *
 * <p>Casos de uso: CU-38, CU-39
 */
public interface ExportadorReporte {

    /**
     * Genera el archivo del reporte en el formato de la estrategia (paso 9 de CU-38, paso 10 de CU-39).
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param r resultado del reporte ya generado
     * @return archivo con nombre, tipo MIME y bytes
     * @throws ar.edu.ub.club.compartido.excepciones.ErrorNegocio «El archivo no pudo generarse» si falla la
     *     biblioteca (FA3, RNF-16)
     */
    ArchivoExportado exportar(ResultadoReporte r);
}

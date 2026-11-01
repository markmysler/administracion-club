package ar.edu.ub.club.reportes.aplicacion;

/**
 * Formatos de archivo en que se puede exportar un reporte. Es la clave del mapa {@code
 * ServicioReportes.exportadores}: cada valor tiene su estrategia concreta de {@code ExportadorReporte} (patrón
 * Strategy), así la fachada elige el exportador sin preguntar por el tipo.
 *
 * <p>Casos de uso: CU-38, CU-39
 */
public enum FormatoExportacion {

    /** Libro de Excel (.xlsx), lo genera {@code ExportadorExcel} con Apache POI. */
    EXCEL,
    /** Documento PDF, lo genera {@code ExportadorPdf} con OpenPDF. */
    PDF;
}

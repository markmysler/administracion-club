package ar.edu.ub.club.reportes.aplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de las estrategias concretas del patrón Strategy {@code ExportadorReporte}: {@code ExportadorExcel} y {@code
 * ExportadorPdf} generan bytes no vacíos, con el tipo MIME y la extensión correctos y con los encabezados del reporte.
 *
 * <p>Casos de uso: CU-38, CU-39
 */
class ExportadoresTest {

    /**
     * Arma un resultado de reporte de prueba.
     *
     * <p>Casos de uso: CU-38
     * @return resultado con dos filas y totales
     */
    private ResultadoReporte resultado() {
        Map<String, Object> totales = new LinkedHashMap<>();
        totales.put("Reservas", 3L);
        totales.put("Importe", Dinero.de("1500.50"));
        return new ResultadoReporte("Uso de canchas (horas reservadas)",
                new FiltroReporte(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30)),
                List.of("Deporte", "Cancha", "Reservas", "Horas reservadas"),
                List.of(List.of("Tenis", "Tenis 1", 2L, new BigDecimal("2.00")),
                        List.of("Pádel", "Pádel 1", 1L, new BigDecimal("1.50"))),
                new Totales(totales));
    }

    /**
     * El libro de Excel tiene contenido, tipo de hoja de cálculo, extensión .xlsx y la fila de encabezados.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @throws Exception si no puede leerse el libro generado
     */
    @Test
    void excelGeneraLibroConEncabezados() throws Exception {
        ArchivoExportado archivo = new ExportadorExcel().exportar(resultado());
        assertTrue(archivo.getContenido().length > 0);
        assertEquals(ExportadorExcel.TIPO_CONTENIDO, archivo.getTipoContenido());
        assertEquals("reporte-uso-de-canchas-horas-reservadas-2026-11-01_2026-11-30.xlsx", archivo.getNombre());
        try (XSSFWorkbook libro = new XSSFWorkbook(new ByteArrayInputStream(archivo.getContenido()))) {
            Sheet hoja = libro.getSheetAt(0);
            assertEquals("Uso de canchas (horas reservadas)", hoja.getRow(0).getCell(0).getStringCellValue());
            Row encabezado = hoja.getRow(3);
            assertEquals("Deporte", encabezado.getCell(0).getStringCellValue());
            assertEquals("Horas reservadas", encabezado.getCell(3).getStringCellValue());
            assertEquals(2.0, hoja.getRow(4).getCell(2).getNumericCellValue());
        }
    }

    /**
     * El PDF tiene contenido, tipo {@code application/pdf}, extensión .pdf y la firma {@code %PDF}.
     *
     * <p>Casos de uso: CU-38, CU-39
     */
    @Test
    void pdfGeneraDocumento() {
        ArchivoExportado archivo = new ExportadorPdf().exportar(resultado());
        assertTrue(archivo.getContenido().length > 0);
        assertEquals("application/pdf", archivo.getTipoContenido());
        assertTrue(archivo.getNombre().endsWith(".pdf"));
        assertTrue(new String(archivo.getContenido(), 0, 4, StandardCharsets.ISO_8859_1).startsWith("%PDF"));
    }

    /**
     * Un reporte sin filas también se exporta (FA2: totales en cero).
     *
     * <p>Casos de uso: CU-38, CU-39
     */
    @Test
    void reporteVacioSeExporta() {
        ResultadoReporte vacio = new ResultadoReporte("Cuotas y grupos deudores",
                new FiltroReporte(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)),
                List.of("Grupo", "Titular"), List.of(), new Totales(Map.of("Grupos deudores", 0L)));
        assertTrue(new ExportadorExcel().exportar(vacio).getContenido().length > 0);
        assertTrue(new ExportadorPdf().exportar(vacio).getContenido().length > 0);
    }
}

package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta del patrón <b>Strategy</b> {@code ExportadorReporte} que genera un libro de Excel (.xlsx) con
 * Apache POI: título, período, encabezados en negrita, una fila por fila del reporte (números e importes como
 * celdas numéricas) y la sección de totales. Tipo de contenido de hoja de cálculo OOXML.
 *
 * <p>Casos de uso: CU-38, CU-39
 */
@Component
public class ExportadorExcel implements ExportadorReporte {

    /** Tipo MIME de un libro .xlsx. */
    public static final String TIPO_CONTENIDO =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /** Formato de fecha de los archivos exportados. */
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Ancho fijo de columna (24 caracteres); no se usa autoSizeColumn porque requiere fuentes AWT en el servidor. */
    private static final int ANCHO_COLUMNA = 24 * 256;

    /**
     * Crea la estrategia (sin estado).
     *
     * <p>Casos de uso: CU-38, CU-39
     */
    public ExportadorExcel() {
    }

    /**
     * Arma el libro .xlsx del reporte (paso 9 de CU-38, paso 10 de CU-39).
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param resultado resultado del reporte
     * @return archivo .xlsx
     * @throws ErrorNegocio «El archivo no pudo generarse» si POI falla (FA3, RNF-16)
     */
    @Override
    public ArchivoExportado exportar(ResultadoReporte resultado) {
        try (Workbook libro = new XSSFWorkbook(); ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
            Sheet hoja = libro.createSheet("Reporte");
            CellStyle negrita = estiloNegrita(libro);
            int fila = 0;
            Cell titulo = hoja.createRow(fila++).createCell(0);
            titulo.setCellValue(resultado.getTitulo());
            titulo.setCellStyle(negrita);
            hoja.createRow(fila++).createCell(0).setCellValue(periodo(resultado.getFiltro()));
            fila++;
            Row encabezado = hoja.createRow(fila++);
            List<String> columnas = resultado.getColumnas();
            for (int c = 0; c < columnas.size(); c++) {
                Cell celda = encabezado.createCell(c);
                celda.setCellValue(columnas.get(c));
                celda.setCellStyle(negrita);
            }
            for (List<Object> datos : resultado.getFilas()) {
                Row r = hoja.createRow(fila++);
                for (int c = 0; c < datos.size(); c++) {
                    escribir(r.createCell(c), datos.get(c));
                }
            }
            fila++;
            Cell tituloTotales = hoja.createRow(fila++).createCell(0);
            tituloTotales.setCellValue("Totales");
            tituloTotales.setCellStyle(negrita);
            for (Map.Entry<String, Object> total : resultado.getTotales().getValores().entrySet()) {
                Row r = hoja.createRow(fila++);
                r.createCell(0).setCellValue(total.getKey());
                escribir(r.createCell(1), total.getValue());
            }
            for (int c = 0; c < Math.max(columnas.size(), 2); c++) {
                hoja.setColumnWidth(c, ANCHO_COLUMNA);
            }
            libro.write(salida);
            return new ArchivoExportado(nombreArchivo(resultado, ".xlsx"), TIPO_CONTENIDO, salida.toByteArray());
        } catch (IOException | RuntimeException e) {
            throw new ErrorNegocio("El archivo no pudo generarse");
        }
    }

    /**
     * Crea el estilo en negrita para el título y los encabezados.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param libro libro en construcción
     * @return estilo en negrita
     */
    private CellStyle estiloNegrita(Workbook libro) {
        Font fuente = libro.createFont();
        fuente.setBold(true);
        CellStyle estilo = libro.createCellStyle();
        estilo.setFont(fuente);
        return estilo;
    }

    /**
     * Escribe una celda según el tipo del valor: números e importes como numéricos, el resto como texto.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param celda celda destino
     * @param valor valor de la celda (puede ser {@code null})
     */
    private void escribir(Cell celda, Object valor) {
        if (valor instanceof Number n) {
            celda.setCellValue(n.doubleValue());
        } else if (valor instanceof Dinero d) {
            celda.setCellValue(d.getMonto().doubleValue());
        } else if (valor instanceof LocalDate f) {
            celda.setCellValue(FECHA.format(f));
        } else {
            celda.setCellValue(valor == null ? "" : valor.toString());
        }
    }

    /**
     * Texto del período del reporte («Período: dd/MM/aaaa al dd/MM/aaaa»).
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param filtro filtro del reporte (puede ser {@code null})
     * @return texto del período
     */
    private String periodo(FiltroReporte filtro) {
        if (filtro == null || filtro.getDesde() == null || filtro.getHasta() == null) {
            return "";
        }
        return "Período: " + FECHA.format(filtro.getDesde()) + " al " + FECHA.format(filtro.getHasta());
    }

    /**
     * Arma el nombre del archivo a partir del título y el período: {@code reporte-<titulo>-<desde>_<hasta><ext>}, en
     * minúsculas, sin tildes ni espacios.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param resultado resultado del reporte
     * @param extension extensión con el punto
     * @return nombre del archivo
     */
    private String nombreArchivo(ResultadoReporte resultado, String extension) {
        String base = Normalizer.normalize(resultado.getTitulo(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        FiltroReporte f = resultado.getFiltro();
        String periodo = f == null || f.getDesde() == null ? "" : "-" + f.getDesde() + "_" + f.getHasta();
        return "reporte-" + base + periodo + extension;
    }
}

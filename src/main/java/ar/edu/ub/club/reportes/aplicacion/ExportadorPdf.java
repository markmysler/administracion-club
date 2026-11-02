package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta del patrón <b>Strategy</b> {@code ExportadorReporte} que genera un documento PDF con OpenPDF
 * (paquetes {@code com.lowagie.text}): título, período, tabla con encabezados en negrita y la tabla de totales, en A4
 * apaisado. Tipo de contenido {@code application/pdf}.
 *
 * <p>Casos de uso: CU-38, CU-39
 */
@Component
public class ExportadorPdf implements ExportadorReporte {

    /** Tipo MIME de un documento PDF. */
    public static final String TIPO_CONTENIDO = "application/pdf";

    /** Formato de fecha de los archivos exportados. */
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Configuración regional de los importes (es-AR). */
    private static final Locale ES_AR = Locale.forLanguageTag("es-AR");

    /**
     * Crea la estrategia (sin estado).
     *
     * <p>Casos de uso: CU-38, CU-39
     */
    public ExportadorPdf() {
    }

    /**
     * Arma el documento PDF del reporte (paso 9 de CU-38, paso 10 de CU-39).
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param resultado resultado del reporte
     * @return archivo .pdf
     * @throws ErrorNegocio «El archivo no pudo generarse» si OpenPDF falla (FA3, RNF-16)
     */
    @Override
    public ArchivoExportado exportar(ResultadoReporte resultado) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(documento, salida);
            documento.open();
            Font fuenteTitulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font negrita = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 10, Font.NORMAL);
            documento.add(new Paragraph(resultado.getTitulo(), fuenteTitulo));
            documento.add(new Paragraph(periodo(resultado.getFiltro()), normal));
            documento.add(new Paragraph(" "));
            List<String> columnas = resultado.getColumnas();
            PdfPTable tabla = new PdfPTable(columnas.size());
            tabla.setWidthPercentage(100);
            for (String columna : columnas) {
                tabla.addCell(new PdfPCell(new Phrase(columna, negrita)));
            }
            for (List<Object> fila : resultado.getFilas()) {
                for (Object valor : fila) {
                    PdfPCell celda = new PdfPCell(new Phrase(texto(valor), normal));
                    if (valor instanceof Number || valor instanceof Dinero) {
                        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    }
                    tabla.addCell(celda);
                }
            }
            if (resultado.getFilas().isEmpty()) {
                PdfPCell vacia = new PdfPCell(new Phrase("Sin datos en el período", normal));
                vacia.setColspan(columnas.size());
                tabla.addCell(vacia);
            }
            documento.add(tabla);
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Totales", negrita));
            PdfPTable totales = new PdfPTable(2);
            totales.setWidthPercentage(50);
            totales.setHorizontalAlignment(Element.ALIGN_LEFT);
            for (Map.Entry<String, Object> total : resultado.getTotales().getValores().entrySet()) {
                totales.addCell(new PdfPCell(new Phrase(total.getKey(), negrita)));
                PdfPCell valor = new PdfPCell(new Phrase(texto(total.getValue()), normal));
                valor.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totales.addCell(valor);
            }
            documento.add(totales);
            documento.close();
            return new ArchivoExportado(nombreArchivo(resultado, ".pdf"), TIPO_CONTENIDO, salida.toByteArray());
        } catch (RuntimeException e) {
            throw new ErrorNegocio("El archivo no pudo generarse");
        }
    }

    /**
     * Convierte un valor de celda en texto: importes con «$» y dos decimales (es-AR), decimales con dos decimales,
     * fechas dd/MM/aaaa y {@code null} como texto vacío.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param valor valor de la celda
     * @return texto a mostrar
     */
    private String texto(Object valor) {
        DecimalFormat formato = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(ES_AR));
        if (valor == null) {
            return "";
        }
        if (valor instanceof Dinero d) {
            return "$ " + formato.format(d.getMonto());
        }
        if (valor instanceof BigDecimal b) {
            return formato.format(b);
        }
        if (valor instanceof LocalDate f) {
            return FECHA.format(f);
        }
        return valor.toString();
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

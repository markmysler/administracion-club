package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import ar.edu.ub.club.reportes.aplicacion.ResultadoReporte;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Modelo de vista (record inmutable) de un reporte ya formateado como texto: título con el período, columnas, filas
 * y totales («nombre: valor»). Un mismo record sirve para P-32 y P-33 (MODELO 11.6.1).
 *
 * <p>Casos de uso: CU-38, CU-39
 * @param titulo título del reporte con su período
 * @param columnas encabezados
 * @param filas celdas formateadas
 * @param totales totales formateados
 */
public record TablaReporte(String titulo, List<String> columnas, List<List<String>> filas, List<String> totales) {

    /**
     * Fábrica: convierte un {@code ResultadoReporte} en texto con {@code FormatoUI} (importes con $, fechas
     * dd/MM/aaaa, decimales con dos decimales en es-AR).
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param resultado resultado del reporte
     * @return el modelo de vista
     */
    public static TablaReporte desde(ResultadoReporte resultado) {
        List<List<String>> filas = new ArrayList<>();
        for (List<Object> fila : resultado.getFilas()) {
            List<String> celdas = new ArrayList<>();
            for (Object valor : fila) {
                celdas.add(texto(valor));
            }
            filas.add(List.copyOf(celdas));
        }
        List<String> totales = new ArrayList<>();
        for (Map.Entry<String, Object> total : resultado.getTotales().getValores().entrySet()) {
            totales.add(total.getKey() + ": " + texto(total.getValue()));
        }
        FiltroReporte f = resultado.getFiltro();
        String titulo = f == null ? resultado.getTitulo()
                : resultado.getTitulo() + " del " + FormatoUI.fecha(f.getDesde()) + " al " + FormatoUI.fecha(f.getHasta());
        return new TablaReporte(titulo, List.copyOf(resultado.getColumnas()), List.copyOf(filas),
                List.copyOf(totales));
    }

    /**
     * Formatea una celda según su tipo.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param valor valor de la celda
     * @return texto
     */
    private static String texto(Object valor) {
        if (valor == null) {
            return "";
        }
        if (valor instanceof Dinero d) {
            return FormatoUI.dinero(d);
        }
        if (valor instanceof BigDecimal b) {
            return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(FormatoUI.ES_AR)).format(b);
        }
        if (valor instanceof LocalDate fecha) {
            return FormatoUI.fecha(fecha);
        }
        return valor.toString();
    }
}

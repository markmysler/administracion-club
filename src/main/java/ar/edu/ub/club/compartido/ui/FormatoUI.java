package ar.edu.ub.club.compartido.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Clase utilitaria final con métodos estáticos que formatean los tipos del dominio para mostrarlos en es-AR. Los
 * modelos de vista conservan los tipos del dominio; las vistas los formatean con esta clase.
 *
 * <p>Casos de uso: todos
 */
public final class FormatoUI {

    /** Configuración regional del club. */
    public static final Locale ES_AR = Locale.forLanguageTag("es-AR");

    /** Formato dd/mm/aaaa. */
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Formato hh:mm. */
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    /** Formato dd/mm/aaaa hh:mm. */
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * No se instancia.
     *
     * <p>Casos de uso: todos
     */
    private FormatoUI() {
    }

    /**
     * Formato dd/mm/aaaa.
     *
     * <p>Casos de uso: todos
     * @param valor fecha (puede ser {@code null})
     * @return texto o vacío
     */
    public static String fecha(LocalDate valor) {
        return valor == null ? "" : FECHA.format(valor);
    }

    /**
     * Formato hh:mm.
     *
     * <p>Casos de uso: todos
     * @param valor hora (puede ser {@code null})
     * @return texto o vacío
     */
    public static String hora(LocalTime valor) {
        return valor == null ? "" : HORA.format(valor);
    }

    /**
     * Formato dd/mm/aaaa hh:mm.
     *
     * <p>Casos de uso: todos
     * @param valor fecha y hora (puede ser {@code null})
     * @return texto o vacío
     */
    public static String fechaHora(LocalDateTime valor) {
        return valor == null ? "" : FECHA_HORA.format(valor);
    }

    /**
     * Formato «$ 1.234,50» (es-AR).
     *
     * <p>Casos de uso: todos
     * @param valor importe (puede ser {@code null})
     * @return texto o vacío
     */
    public static String dinero(Dinero valor) {
        if (valor == null) {
            return "";
        }
        DecimalFormat formato = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(ES_AR));
        return "$ " + formato.format(valor.getMonto());
    }
}

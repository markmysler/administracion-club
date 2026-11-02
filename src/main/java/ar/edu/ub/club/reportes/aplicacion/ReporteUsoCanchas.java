package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.reportes.persistencia.ConsultasReportes;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Subclase concreta del patrón <b>Template Method</b> {@code Reporte} para el reporte de uso de canchas (P-32):
 * redefine {@code obtenerDatos} (reservas CONFIRMADAS y FINALIZADAS del período por deporte y cancha, con {@code
 * ConsultasReportes.usoCanchas}) y {@code calcularTotales} (reservas, horas reservadas y porcentaje de ocupación).
 * Diagrama sec_cu38_p3.
 *
 * <p>Casos de uso: CU-38
 */
@Component
public class ReporteUsoCanchas extends Reporte {

    /** Nombre del total de reservas. */
    public static final String TOTAL_RESERVAS = "Reservas";

    /** Nombre del total de horas reservadas. */
    public static final String TOTAL_HORAS = "Horas reservadas";

    /** Nombre de la ocupación promedio. */
    public static final String TOTAL_OCUPACION = "Ocupación (%)";

    /** Consultas de lectura de reportes (inyectada por constructor). */
    private final ConsultasReportes consultas;

    /**
     * Crea el reporte con sus consultas de lectura.
     *
     * <p>Casos de uso: CU-38
     * @param consultas consultas de reportes
     */
    public ReporteUsoCanchas(ConsultasReportes consultas) {
        this.consultas = consultas;
    }

    /**
     * Paso 5 de CU-38: {@code consultas.usoCanchas(filtro)}.
     *
     * <p>Casos de uso: CU-38
     * @param filtro período, deporte y cancha
     * @return lista de {@code FilaUsoCancha}
     */
    @Override
    @SuppressWarnings("rawtypes")
    protected List obtenerDatos(FiltroReporte filtro) {
        return consultas.usoCanchas(filtro);
    }

    /**
     * Paso 6 de CU-38: total de reservas, total de horas reservadas y ocupación del conjunto. Como todas las canchas
     * se miden contra las mismas horas de atención del club, la ocupación del conjunto es el promedio de los
     * porcentajes de cada cancha. Sin filas todo queda en cero (FA2).
     *
     * <p>Casos de uso: CU-38
     * @param filas filas {@code FilaUsoCancha}
     * @return totales del reporte
     */
    @Override
    @SuppressWarnings("rawtypes")
    protected Totales calcularTotales(List filas) {
        long reservas = 0;
        BigDecimal horas = BigDecimal.ZERO;
        BigDecimal porcentajes = BigDecimal.ZERO;
        for (Object o : filas) {
            FilaUsoCancha fila = (FilaUsoCancha) o;
            reservas += fila.getCantidadReservas();
            horas = horas.add(fila.getHorasReservadas());
            porcentajes = porcentajes.add(fila.getPorcentajeOcupacion());
        }
        BigDecimal ocupacion = filas.isEmpty() ? BigDecimal.ZERO
                : porcentajes.divide(BigDecimal.valueOf(filas.size()), 2, RoundingMode.HALF_UP);
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put(TOTAL_RESERVAS, reservas);
        valores.put(TOTAL_HORAS, horas.setScale(2, RoundingMode.HALF_UP));
        valores.put(TOTAL_OCUPACION, ocupacion);
        return new Totales(valores);
    }

    /**
     * Título del reporte; aclara que el uso reservado no equivale a la presencia física (07A, P-32).
     *
     * <p>Casos de uso: CU-38
     * @return título
     */
    @Override
    protected String titulo() {
        return "Uso de canchas (horas reservadas)";
    }

    /**
     * Columnas del detalle: deporte, cancha, cantidad de reservas, horas reservadas y porcentaje de ocupación.
     *
     * <p>Casos de uso: CU-38
     * @return encabezados
     */
    @Override
    protected List<String> columnas() {
        return List.of("Deporte", "Cancha", "Reservas", "Horas reservadas", "Ocupación (%)");
    }

    /**
     * Celdas de una {@code FilaUsoCancha}, en el orden de {@code columnas()}.
     *
     * <p>Casos de uso: CU-38
     * @param fila la fila
     * @return celdas
     */
    @Override
    protected List<Object> celdas(Object fila) {
        FilaUsoCancha f = (FilaUsoCancha) fila;
        return List.of(f.getDeporte(), f.getCancha(), f.getCantidadReservas(), f.getHorasReservadas(),
                f.getPorcentajeOcupacion());
    }
}

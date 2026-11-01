package ar.edu.ub.club.cuotas.dominio;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Objeto de valor inmutable con las fechas de emisión y de vencimiento de una cuota. Lo arma {@code
 * ServicioCuotas.emitirCuotasDelMes} con {@code FechasCuota.delPeriodo(periodo)} (emisión el día 1, vencimiento
 * el día 10 del período) y lo recibe {@code GrupoFamiliar.emitirCuota}, que lo pasa al constructor de
 * {@code Cuota}.
 *
 * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
 */
public class FechasCuota {

    /** Día del mes en que se emite la cuota. */
    public static final int DIA_EMISION = 1;

    /** Día del mes en que vence la cuota (se marca vencida el día 11). */
    public static final int DIA_VENCIMIENTO = 10;

    /** Fecha de emisión. */
    private final LocalDate emision;

    /** Fecha de vencimiento (posterior o igual a la emisión). */
    private final LocalDate vencimiento;

    /**
     * Crea las fechas de una cuota.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param emision Fecha de emisión.
     * @param vencimiento Fecha de vencimiento.
     * @throws IllegalArgumentException si falta una fecha o el vencimiento es anterior a la emisión
     */
    public FechasCuota(LocalDate emision, LocalDate vencimiento) {
        if (emision == null || vencimiento == null || vencimiento.isBefore(emision)) {
            throw new IllegalArgumentException("El vencimiento de la cuota debe ser posterior o igual a la emisión.");
        }
        this.emision = emision;
        this.vencimiento = vencimiento;
    }

    /**
     * Fechas por omisión de la cuota de un período: emisión el día 1 y vencimiento el día 10.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param periodo Año y mes.
     * @return Fechas de la cuota del período.
     */
    public static FechasCuota delPeriodo(YearMonth periodo) {
        return new FechasCuota(periodo.atDay(DIA_EMISION), periodo.atDay(DIA_VENCIMIENTO));
    }

    /**
     * Devuelve la fecha de emisión.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @return emisión
     */
    public LocalDate getEmision() {
        return emision;
    }

    /**
     * Devuelve la fecha de vencimiento.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @return vencimiento
     */
    public LocalDate getVencimiento() {
        return vencimiento;
    }
}

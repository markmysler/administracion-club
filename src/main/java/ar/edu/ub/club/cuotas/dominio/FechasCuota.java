package ar.edu.ub.club.cuotas.dominio;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * TEMPORAL (API acordada de cuotas): fechas de emisión y vencimiento.
 *
 * <p>Casos de uso: todos
 */
public class FechasCuota {

    /** Emisión. */
    private final LocalDate emision;

    /** Vencimiento. */
    private final LocalDate vencimiento;

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     * @param emision emisión
     * @param vencimiento vencimiento
     */
    public FechasCuota(LocalDate emision, LocalDate vencimiento) {
        this.emision = emision;
        this.vencimiento = vencimiento;
    }

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     * @param periodo período
     * @return fechas
     */
    public static FechasCuota delPeriodo(YearMonth periodo) {
        return new FechasCuota(periodo.atDay(1), periodo.atDay(10));
    }

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     * @return emisión
     */
    public LocalDate getEmision() {
        return emision;
    }

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     * @return vencimiento
     */
    public LocalDate getVencimiento() {
        return vencimiento;
    }
}

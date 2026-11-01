package ar.edu.ub.club.cuotas.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del patrón State de {@code Cuota} (Pendiente → Pagada | Vencida; Vencida → Pagada; transiciones
 * inválidas) y del crédito aplicado (RN-21).
 *
 * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas, vencimiento de cuotas
 */
class CuotaTest {

    /** Período de prueba. */
    private static final YearMonth PERIODO = YearMonth.of(2026, 11);

    /**
     * Cuota nueva de $ 50.000 del período de prueba.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @return cuota pendiente
     */
    private Cuota nueva() {
        return new Cuota(PERIODO.toString(), Dinero.de("50000"), FechasCuota.delPeriodo(PERIODO));
    }

    /**
     * Una cuota nueva está pendiente, vence el día 10 y admite pago.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     */
    @Test
    void cuotaNuevaPendiente() {
        Cuota c = nueva();
        assertEquals("PENDIENTE", c.getEstado().codigo());
        assertEquals(LocalDate.of(2026, 11, 10), c.getFechaVencimiento());
        assertTrue(c.admitePago());
        assertFalse(c.estaVencida());
        assertEquals(Dinero.de("50000"), c.importeAPagar());
    }

    /**
     * Pendiente → Pagada con un pago aprobado; una cuota pagada no admite otro pago ni vence.
     *
     * <p>Casos de uso: CU-35, CU-36
     */
    @Test
    void pendienteAPagadaYTransicionesInvalidas() {
        Cuota c = nueva();
        c.registrarPagoAprobado(null);
        assertEquals("PAGADA", c.getEstado().codigo());
        assertFalse(c.admitePago());
        assertThrows(TransicionInvalidaException.class, () -> c.registrarPagoAprobado(null));
        assertThrows(TransicionInvalidaException.class, () -> c.vencer(LocalDate.of(2026, 12, 1)));
    }

    /**
     * Pendiente → Vencida solo después de la fecha de vencimiento; Vencida → Pagada; Vencida no vuelve a vencer.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de cuotas
     */
    @Test
    void vencimiento() {
        Cuota c = nueva();
        c.vencer(LocalDate.of(2026, 11, 10));
        assertEquals("PENDIENTE", c.getEstado().codigo());
        c.vencer(LocalDate.of(2026, 11, 11));
        assertTrue(c.estaVencida());
        assertTrue(c.admitePago());
        assertThrows(TransicionInvalidaException.class, () -> c.vencer(LocalDate.of(2026, 11, 12)));
        c.registrarPagoAprobado(null);
        assertEquals("PAGADA", c.getEstado().codigo());
    }

    /**
     * El crédito aplicado es el mínimo entre saldo e importe (RN-21) y descuenta el importe a pagar.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     */
    @Test
    void creditoAplicado() {
        Cuota parcial = nueva();
        parcial.aplicarCredito(Dinero.de("20000"));
        assertEquals(Dinero.de("30000"), parcial.importeAPagar());
        Cuota total = nueva();
        total.aplicarCredito(Dinero.de("80000"));
        assertEquals(Dinero.de("50000"), total.getCreditoAplicado());
        assertTrue(total.importeAPagar().esCero());
    }

    /**
     * {@code desdeCodigo} devuelve instancias únicas y rechaza códigos desconocidos.
     *
     * <p>Casos de uso: todos
     */
    @Test
    void desdeCodigo() {
        assertEquals(EstadoCuota.desdeCodigo("VENCIDA"), EstadoCuota.desdeCodigo("VENCIDA"));
        assertThrows(IllegalStateException.class, () -> EstadoCuota.desdeCodigo("OTRO"));
    }
}

package ar.edu.ub.club.pagos.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.FechasCuota;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del patrón State de {@code Pago} (diagrama est_pago), incluidas las transiciones inválidas.
 *
 * <p>Casos de uso: CU-35, CU-36; tareas programadas: vencimiento de pagos online pendientes
 */
class PagoTest {

    /** Momento de prueba. */
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 5, 10, 0);

    /**
     * Pago online pendiente de una cuota de $ 50.000.
     *
     * <p>Casos de uso: CU-35
     * @return pago pendiente
     */
    private PagoOnline pendiente() {
        Cuota c = new Cuota("2026-11", Dinero.de("50000"), FechasCuota.delPeriodo(YearMonth.of(2026, 11)));
        return new PagoOnline(c, Dinero.de("50000"), UUID.randomUUID(), AHORA.minusHours(1));
    }

    /**
     * Pendiente → Aprobado; Aprobado es final.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void pendienteAAprobado() {
        PagoOnline p = pendiente();
        assertFalse(p.getEstado().esFinal());
        p.aprobar(AHORA);
        assertEquals("APROBADO", p.getEstado().codigo());
        assertEquals(AHORA, p.getFecha());
        assertTrue(p.getEstado().esFinal());
        assertThrows(TransicionInvalidaException.class, () -> p.rechazar("x"));
        assertThrows(TransicionInvalidaException.class, () -> p.acreditar(AHORA));
        assertThrows(TransicionInvalidaException.class, () -> p.vencer("Abandonado"));
        assertThrows(TransicionInvalidaException.class, () -> p.aprobar(AHORA));
    }

    /**
     * Pendiente → Rechazado con motivo; Rechazado es final.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void pendienteARechazado() {
        PagoOnline p = pendiente();
        p.rechazar("Fondos insuficientes");
        assertEquals("RECHAZADO", p.getEstado().codigo());
        assertEquals("Fondos insuficientes", p.getMotivoRechazo());
        assertThrows(TransicionInvalidaException.class, () -> p.aprobar(AHORA));
    }

    /**
     * Pendiente → Vencido («Abandonado», RN-22); Vencido admite aprobar o acreditar, no rechazar ni volver a vencer.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     */
    @Test
    void vencidoNoEsFinal() {
        PagoOnline p = pendiente();
        p.vencer("Abandonado");
        assertEquals("VENCIDO", p.getEstado().codigo());
        assertEquals("Abandonado", p.getMotivoRechazo());
        assertFalse(p.getEstado().esFinal());
        assertThrows(TransicionInvalidaException.class, () -> p.rechazar("x"));
        assertThrows(TransicionInvalidaException.class, () -> p.vencer("Abandonado"));
        p.aprobar(AHORA);
        assertEquals("APROBADO", p.getEstado().codigo());
        assertNull(p.getMotivoRechazo());

        PagoOnline q = pendiente();
        q.vencer("Abandonado");
        q.acreditar(AHORA);
        assertEquals("ACREDITADO", q.getEstado().codigo());
    }

    /**
     * Pendiente → Acreditado (cuota ya pagada, RN-21); Acreditado es final.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void pendienteAAcreditado() {
        PagoOnline p = pendiente();
        p.acreditar(AHORA);
        assertEquals("ACREDITADO", p.getEstado().codigo());
        assertTrue(p.getEstado().esFinal());
        assertThrows(TransicionInvalidaException.class, () -> p.aprobar(AHORA));
    }

    /**
     * Los pagos manual y con saldo nacen aprobados y describen su medio sin preguntar por la clase.
     *
     * <p>Casos de uso: CU-36, CU-37
     */
    @Test
    void origenYMedio() {
        Cuota c = new Cuota("2026-11", Dinero.de("10"), FechasCuota.delPeriodo(YearMonth.of(2026, 11)));
        Pago manual = new PagoManual(c, Dinero.de("10"), MedioPago.reconstituir(1L, "Efectivo", true), null, AHORA);
        Pago saldo = new PagoConSaldo(c, Dinero.de("10"));
        assertEquals("APROBADO", manual.getEstado().codigo());
        assertEquals("Efectivo", manual.descripcionMedio());
        assertEquals("CON_SALDO", saldo.origen());
        assertEquals("Saldo a favor", saldo.descripcionMedio());
        assertEquals("Mercado Pago", pendiente().descripcionMedio());
        assertThrows(IllegalStateException.class, () -> EstadoPago.desdeCodigo("X"));
        assertEquals("C-00000042", GeneradorComprobante.formatear(42));
    }
}

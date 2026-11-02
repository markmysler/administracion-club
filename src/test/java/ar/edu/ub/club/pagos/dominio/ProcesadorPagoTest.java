package ar.edu.ub.club.pagos.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.FechasCuota;
import java.time.LocalDateTime;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del Factory Method de los creadores de pagos ({@code ProcesadorPago.registrar} y los tres {@code
 * crearPago}).
 *
 * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
class ProcesadorPagoTest {

    /** Momento de prueba. */
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 5, 10, 0);

    /** Fachada simulada. */
    private final FachadaPersistencia fachada = mock(FachadaPersistencia.class);

    /**
     * Cuota pendiente de $ 50.000 con $ 10.000 de crédito aplicado.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return cuota
     */
    private Cuota cuota() {
        Cuota c = new Cuota("2026-11", Dinero.de("50000"), FechasCuota.delPeriodo(YearMonth.of(2026, 11)));
        c.aplicarCredito(Dinero.de("10000"));
        return c;
    }

    /**
     * El creador online crea un {@code PagoOnline} PENDIENTE por el importe a pagar, con referencia, y lo guarda.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void creadorOnline() {
        Pago p = new ProcesadorPagoOnline(fachada).registrar(cuota(), new DatosPago(null, AHORA, null, null, null));
        PagoOnline online = assertInstanceOf(PagoOnline.class, p);
        assertEquals("PENDIENTE", online.getEstado().codigo());
        assertEquals(Dinero.de("40000"), online.getImporte());
        assertNotNull(online.getReferenciaExterna());
        verify(fachada).put(p);
    }

    /**
     * El creador manual crea un {@code PagoManual} APROBADO con el medio.
     *
     * <p>Casos de uso: CU-36
     */
    @Test
    void creadorManual() {
        MedioPago medio = MedioPago.reconstituir(2L, "Transferencia bancaria", true);
        Pago p = new ProcesadorPagoManual(fachada).registrar(cuota(), new DatosPago(Dinero.de("40000"), AHORA, medio, null, null));
        assertInstanceOf(PagoManual.class, p);
        assertEquals("APROBADO", p.getEstado().codigo());
        assertEquals("Transferencia bancaria", p.descripcionMedio());
        verify(fachada).put(p);
    }

    /**
     * El creador con saldo crea un {@code PagoConSaldo} APROBADO.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     */
    @Test
    void creadorConSaldo() {
        Pago p = new ProcesadorPagoConSaldo(fachada).registrar(cuota(), new DatosPago(Dinero.de("40000"), AHORA, null, null, null));
        assertInstanceOf(PagoConSaldo.class, p);
        assertEquals("APROBADO", p.getEstado().codigo());
    }

    /**
     * El método plantilla rechaza una cuota que ya no admite pago y no guarda nada (RN-18).
     *
     * <p>Casos de uso: CU-35, CU-36
     */
    @Test
    void cuotaPagadaNoAdmitePago() {
        Cuota pagada = cuota();
        pagada.registrarPagoAprobado(null);
        assertThrows(ErrorNegocio.class, () -> new ProcesadorPagoManual(fachada)
                .registrar(pagada, new DatosPago(Dinero.de("40000"), AHORA, null, null, null)));
        verify(fachada, never()).put(any());
    }
}

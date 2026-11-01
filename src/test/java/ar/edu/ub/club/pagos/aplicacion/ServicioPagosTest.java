package ar.edu.ub.club.pagos.aplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.EstadoCuota;
import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import ar.edu.ub.club.pagos.dominio.GeneradorComprobante;
import ar.edu.ub.club.pagos.dominio.ModoPasarela;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import ar.edu.ub.club.pagos.dominio.ProcesadorPagoManual;
import ar.edu.ub.club.pagos.dominio.ProcesadorPagoOnline;
import ar.edu.ub.club.pagos.integracion.MercadoPagoAdapter;
import ar.edu.ub.club.pagos.integracion.PasarelaPago;
import ar.edu.ub.club.pagos.integracion.ResultadoPasarela;
import ar.edu.ub.club.pagos.persistencia.PagoPorReferenciaExterna;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de {@code ServicioPagos.confirmarResultadoPasarela} sin base de datos: un pago aprobado sobre una cuota
 * ya pagada queda ACREDITADO y genera crédito (RN-21); sobre una cuota impaga queda APROBADO y la cuota PAGADA;
 * una notificación repetida no cambia nada (idempotencia); y el vencimiento «Abandonado» (RN-22).
 *
 * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
 */
class ServicioPagosTest {

    /** Momento de prueba. */
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 5, 10, 0);

    /** Fachada simulada. */
    private FachadaPersistencia fachada;

    /** Pasarela simulada. */
    private PasarelaPago pasarela;

    /** Generador de comprobantes simulado. */
    private GeneradorComprobante generador;

    /** Fachada de cuotas simulada. */
    private ServicioCuotas servicioCuotas;

    /** Auditoría simulada. */
    private ServicioAuditoria auditoria;

    /** Fachada bajo prueba. */
    private ServicioPagos servicio;

    /**
     * Arma la fachada con colaboradores simulados y la pasarela habilitada en modo SIMULADA.
     *
     * <p>Casos de uso: CU-35
     */
    @BeforeEach
    void preparar() {
        fachada = mock(FachadaPersistencia.class);
        pasarela = mock(PasarelaPago.class);
        generador = mock(GeneradorComprobante.class);
        servicioCuotas = mock(ServicioCuotas.class);
        auditoria = mock(ServicioAuditoria.class);
        Reloj reloj = new Reloj() {
            /**
             * Fecha fija.
             *
             * <p>Casos de uso: CU-35
             * @return fecha
             */
            @Override
            public LocalDate hoy() {
                return AHORA.toLocalDate();
            }

            /**
             * Momento fijo.
             *
             * <p>Casos de uso: CU-35
             * @return momento
             */
            @Override
            public LocalDateTime ahora() {
                return AHORA;
            }
        };
        when(fachada.get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela.class))
                .thenReturn(ConfiguracionPasarela.reconstituir(1L, "Mercado Pago", ModoPasarela.SIMULADA, true, false));
        servicio = new ServicioPagos(fachada, pasarela, mock(MercadoPagoAdapter.class), mock(ProcesadorPagoOnline.class),
                mock(ProcesadorPagoManual.class), generador, servicioCuotas, mock(ControlAcceso.class), auditoria, reloj,
                mock(UsuarioActual.class), "http://localhost:8080");
    }

    /**
     * Pago online PENDIENTE (id 7) sobre una cuota del grupo 3 en el estado indicado.
     *
     * <p>Casos de uso: CU-35
     * @param estadoCuota código del estado de la cuota
     * @return el pago
     */
    private PagoOnline pagoSobreCuota(String estadoCuota) {
        Cuota cuota = Cuota.reconstituir(20L, 3L, "2026-11", Dinero.de("35000"), Dinero.cero(),
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 10), EstadoCuota.desdeCodigo(estadoCuota), List.of());
        PagoOnline pago = new PagoOnline(cuota, Dinero.de("35000"), UUID.randomUUID(), AHORA.minusMinutes(5));
        pago.asignarId(7L);
        return pago;
    }

    /**
     * Configura la pasarela y la consulta por referencia para devolver el resultado y el pago.
     *
     * <p>Casos de uso: CU-35
     * @param pago pago guardado
     * @param estado estado que informa la pasarela
     */
    private void pasarelaInforma(PagoOnline pago, String estado) {
        when(pasarela.obtenerResultado("SIM-1"))
                .thenReturn(new ResultadoPasarela("SIM-1", pago.getReferenciaExterna(), estado, Dinero.de("35000"), null));
        doReturn(Optional.of(pago)).when(fachada).buscarUno(any(PagoPorReferenciaExterna.class));
    }

    /**
     * Pago aprobado sobre una cuota ya pagada: ACREDITADO, constancia y crédito a favor del grupo (RN-21).
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void aprobadoSobreCuotaPagadaQuedaAcreditado() {
        PagoOnline pago = pagoSobreCuota("PAGADA");
        pasarelaInforma(pago, ResultadoPasarela.APROBADO);
        servicio.confirmarResultadoPasarela("SIM-1");
        assertEquals("ACREDITADO", pago.getEstado().codigo());
        assertEquals("SIM-1", pago.getIdPagoPasarela());
        verify(generador).generar(pago);
        verify(servicioCuotas).registrarCredito(3L, Dinero.de("35000"), 7L);
        verify(auditoria).registrar(eq(null), eq("PAGO_ACREDITADO"), eq("Pago"), eq(7L), anyString());
    }

    /**
     * Pago aprobado sobre una cuota impaga: APROBADO, cuota PAGADA, comprobante y sin crédito.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void aprobadoSobreCuotaImpagaQuedaAprobado() {
        PagoOnline pago = pagoSobreCuota("VENCIDA");
        pasarelaInforma(pago, ResultadoPasarela.APROBADO);
        servicio.confirmarResultadoPasarela("SIM-1");
        assertEquals("APROBADO", pago.getEstado().codigo());
        assertEquals("PAGADA", pago.getCuota().getEstado().codigo());
        verify(generador).generar(pago);
        verify(servicioCuotas, never()).registrarCredito(anyLong(), any(), anyLong());
    }

    /**
     * Una notificación repetida sobre un pago ya aprobado no cambia nada (idempotencia).
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void notificacionRepetidaNoCambiaNada() {
        PagoOnline pago = pagoSobreCuota("PENDIENTE");
        pago.aprobar(AHORA);
        pasarelaInforma(pago, ResultadoPasarela.APROBADO);
        servicio.confirmarResultadoPasarela("SIM-1");
        verify(generador, never()).generar(any());
        verify(fachada, never()).put(any());
    }

    /**
     * Rechazo de la pasarela: el pago queda RECHAZADO y la cuota sigue impaga.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void rechazado() {
        PagoOnline pago = pagoSobreCuota("PENDIENTE");
        pasarelaInforma(pago, ResultadoPasarela.RECHAZADO);
        servicio.confirmarResultadoPasarela("SIM-1");
        assertEquals("RECHAZADO", pago.getEstado().codigo());
        assertEquals("PENDIENTE", pago.getCuota().getEstado().codigo());
    }

    /**
     * Un pago pendiente de más de 24 horas sin resultado en la pasarela vence con motivo «Abandonado» (RN-22).
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     */
    @Test
    void vencePendienteAbandonado() {
        PagoOnline pago = pagoSobreCuota("PENDIENTE");
        doReturn(List.of(pago)).when(fachada).buscar(any());
        assertEquals(1, servicio.vencerPagosPendientes(AHORA));
        assertEquals("VENCIDO", pago.getEstado().codigo());
        assertEquals("Abandonado", pago.getMotivoRechazo());
    }
}

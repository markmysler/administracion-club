package ar.edu.ub.club.cuotas.aplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.CuotaCubiertaConSaldoEvento;
import ar.edu.ub.club.cuotas.dominio.FechasCuota;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import ar.edu.ub.club.cuotas.dominio.TipoMovimientoSaldo;
import ar.edu.ub.club.cuotas.persistencia.CuotasImpagasDeGrupo;
import ar.edu.ub.club.cuotas.persistencia.SaldoDeGrupo;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.grupos.persistencia.GruposVigentesSinCuota;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

/**
 * Pruebas de la aplicación del saldo a favor (RN-21) sin base de datos: en la emisión mensual (crédito parcial y
 * cuota cubierta por completo) y al registrar un crédito por un pago acreditado.
 *
 * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
class ServicioCuotasTest {

    /** Período de prueba. */
    private static final YearMonth PERIODO = YearMonth.of(2026, 12);

    /** Fachada simulada. */
    private FachadaPersistencia fachada;

    /** Publicador de eventos simulado. */
    private ApplicationEventPublisher publicador;

    /** Fachada bajo prueba. */
    private ServicioCuotas servicio;

    /** Objetos guardados con {@code put}. */
    private final List<Object> guardados = new ArrayList<>();

    /**
     * Arma la fachada con colaboradores simulados (el gestor de transacciones no hace nada).
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     */
    @BeforeEach
    void preparar() {
        fachada = mock(FachadaPersistencia.class);
        publicador = mock(ApplicationEventPublisher.class);
        PlatformTransactionManager gestor = mock(PlatformTransactionManager.class);
        when(gestor.getTransaction(any())).thenReturn(mock(TransactionStatus.class));
        org.mockito.Mockito.doAnswer(inv -> {
            Object o = inv.getArgument(0);
            if (o instanceof Cuota c && c.getId() == null) {
                c.asignarId(50L);
            }
            guardados.add(o);
            return null;
        }).when(fachada).put(any());
        servicio = new ServicioCuotas(fachada, publicador, mock(ControlAcceso.class), mock(UsuarioActual.class), gestor);
    }

    /**
     * Prepara un grupo vigente (id 2) cuya {@code emitirCuota} devuelve una cuota de $ 50.000 con el saldo
     * indicado aplicado, y el saldo del grupo.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param saldo saldo a favor del grupo
     */
    private void grupoConSaldo(Dinero saldo) {
        GrupoFamiliar grupo = mock(GrupoFamiliar.class);
        when(grupo.getId()).thenReturn(2L);
        when(grupo.emitirCuota(anyString(), any(FechasCuota.class), any(Dinero.class))).thenAnswer(inv -> {
            Cuota c = new Cuota(inv.getArgument(0), Dinero.de("50000"), inv.getArgument(1));
            c.asignarGrupo(2L);
            c.aplicarCredito(inv.getArgument(2));
            return c;
        });
        doReturn(List.of(grupo)).when(fachada).buscar(any(GruposVigentesSinCuota.class));
        when(fachada.get(2L, GrupoFamiliar.class)).thenReturn(grupo);
        doReturn(Optional.of(saldo)).when(fachada).buscarUno(any(SaldoDeGrupo.class));
    }

    /**
     * Crédito parcial: la cuota se emite con $ 20.000 aplicados, se registra el DEBITO y no se publica el evento.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     */
    @Test
    void emisionConCreditoParcial() {
        grupoConSaldo(Dinero.de("20000"));
        assertEquals(1, servicio.emitirCuotasDelMes(PERIODO));
        Cuota cuota = (Cuota) guardados.get(0);
        assertEquals(Dinero.de("30000"), cuota.importeAPagar());
        MovimientoSaldo debito = (MovimientoSaldo) guardados.get(1);
        assertEquals(TipoMovimientoSaldo.DEBITO, debito.getTipo());
        assertEquals(Dinero.de("20000"), debito.getImporte());
        assertEquals(2L, debito.getIdGrupo());
        verify(publicador, never()).publishEvent(any(Object.class));
    }

    /**
     * Saldo que cubre toda la cuota: DEBITO por el importe y evento {@code CuotaCubiertaConSaldoEvento} para que
     * pagos cree el {@code PagoConSaldo}.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     */
    @Test
    void emisionCubiertaConSaldo() {
        grupoConSaldo(Dinero.de("80000"));
        servicio.emitirCuotasDelMes(PERIODO);
        Cuota cuota = (Cuota) guardados.get(0);
        assertEquals(Dinero.de("50000"), cuota.getCreditoAplicado());
        ArgumentCaptor<Object> evento = ArgumentCaptor.forClass(Object.class);
        verify(publicador).publishEvent(evento.capture());
        CuotaCubiertaConSaldoEvento e = (CuotaCubiertaConSaldoEvento) evento.getValue();
        assertEquals(50L, e.getIdCuota());
        assertEquals(Dinero.de("50000"), e.getImporte());
    }

    /**
     * Un crédito por pago acreditado cubre la cuota impaga más antigua si alcanza; el resto queda como saldo.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void creditoCubreCuotaImpaga() {
        GrupoFamiliar grupo = mock(GrupoFamiliar.class);
        when(grupo.getId()).thenReturn(2L);
        when(fachada.get(2L, GrupoFamiliar.class)).thenReturn(grupo);
        Cuota vencida = new Cuota("2026-10", Dinero.de("50000"), FechasCuota.delPeriodo(YearMonth.of(2026, 10)));
        vencida.asignarId(2L);
        vencida.asignarGrupo(2L);
        Cuota actual = new Cuota("2026-11", Dinero.de("50000"), FechasCuota.delPeriodo(YearMonth.of(2026, 11)));
        actual.asignarId(5L);
        actual.asignarGrupo(2L);
        doReturn(Optional.of(Dinero.de("65000"))).when(fachada).buscarUno(any(SaldoDeGrupo.class));
        doReturn(List.of(vencida, actual)).when(fachada).buscar(any(CuotasImpagasDeGrupo.class));
        servicio.registrarCredito(2L, Dinero.de("35000"), 9L);
        MovimientoSaldo credito = (MovimientoSaldo) guardados.get(0);
        assertEquals(TipoMovimientoSaldo.CREDITO, credito.getTipo());
        assertEquals(9L, credito.getIdPago());
        MovimientoSaldo debito = (MovimientoSaldo) guardados.get(1);
        assertEquals(TipoMovimientoSaldo.DEBITO, debito.getTipo());
        assertEquals(vencida, debito.getCuota());
        assertEquals(2, guardados.size());
        verify(publicador).publishEvent(any(CuotaCubiertaConSaldoEvento.class));
    }
}

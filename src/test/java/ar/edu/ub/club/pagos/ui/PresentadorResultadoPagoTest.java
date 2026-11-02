package ar.edu.ub.club.pagos.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import ar.edu.ub.club.pagos.dominio.EstadoPago;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.TaskScheduler;

/**
 * Pruebas del diagrama de estados de {@code PresentadorResultadoPago} (P-26) con una {@code PantallaResultadoPago}
 * falsa y un planificador falso que guarda la tarea en lugar de esperar 3 segundos: Consultando → Esperando mientras
 * siga pendiente, estados finales sin reprogramar, Sin confirmar en la décima consulta y cancelación al salir.
 *
 * <p>Casos de uso: CU-35
 */
class PresentadorResultadoPagoTest {

    /** Pantalla falsa que registra lo que se le muestra. */
    private PantallaFalsa pantalla;

    /** Fachada simulada. */
    private ServicioPagos servicioPagos;

    /** Planificador falso. */
    private PlanificadorFalso planificador;

    /** Presentador bajo prueba. */
    private PresentadorResultadoPago presentador;

    /** Referencia del pago. */
    private final UUID referencia = UUID.randomUUID();

    /**
     * Pantalla falsa de P-26.
     *
     * <p>Casos de uso: CU-35
     */
    private static final class PantallaFalsa implements PantallaResultadoPago {

        /** Estados mostrados, en orden. */
        private final List<String> estados = new ArrayList<>();

        /** Si se mostró la espera de confirmación. */
        private boolean espera;

        /** Errores mostrados. */
        private final List<String> errores = new ArrayList<>();

        /**
         * Registra el estado.
         *
         * <p>Casos de uso: CU-35
         * @param resumen estado del pago
         */
        @Override
        public void mostrarEstado(ResumenPagoOnline resumen) {
            estados.add(resumen.estado());
        }

        /**
         * Registra la espera.
         *
         * <p>Casos de uso: CU-35
         */
        @Override
        public void mostrarEsperaConfirmacion() {
            espera = true;
        }

        /**
         * Registra el error.
         *
         * <p>Casos de uso: CU-35
         * @param mensaje mensaje
         */
        @Override
        public void mostrarError(String mensaje) {
            errores.add(mensaje);
        }

        /**
         * Sin uso.
         *
         * <p>Casos de uso: CU-35
         * @param mensaje mensaje
         */
        @Override
        public void mostrarMensaje(String mensaje) {
        }

        /**
         * Sin uso.
         *
         * <p>Casos de uso: CU-35
         * @param campo campo
         * @param mensaje mensaje
         */
        @Override
        public void marcarCampo(String campo, String mensaje) {
        }

        /**
         * Sin uso.
         *
         * <p>Casos de uso: CU-35
         * @param ruta ruta
         */
        @Override
        public void navegarA(String ruta) {
        }
    }

    /**
     * Planificador falso: guarda la última tarea y su futuro simulado.
     *
     * <p>Casos de uso: CU-35
     */
    private static final class PlanificadorFalso {

        /** Planificador simulado con Mockito. */
        private final TaskScheduler planificador = mock(TaskScheduler.class);

        /** Última tarea programada. */
        private Runnable tarea;

        /** Futuro de la última tarea. */
        private ScheduledFuture<?> futuro;

        /** Cantidad de tareas programadas. */
        private int programadas;

        /**
         * Configura el simulador para capturar las tareas.
         *
         * <p>Casos de uso: CU-35
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private PlanificadorFalso() {
            when(planificador.schedule(any(Runnable.class), any(Instant.class))).thenAnswer(inv -> {
                tarea = inv.getArgument(0);
                futuro = mock(ScheduledFuture.class);
                programadas++;
                return (ScheduledFuture) futuro;
            });
        }

        /**
         * Ejecuta la tarea pendiente (pasaron los 3 s).
         *
         * <p>Casos de uso: CU-35
         */
        private void pasanTresSegundos() {
            Runnable t = tarea;
            tarea = null;
            t.run();
        }
    }

    /**
     * Prepara el presentador con sus dependencias falsas.
     *
     * <p>Casos de uso: CU-35
     */
    @BeforeEach
    void preparar() {
        pantalla = new PantallaFalsa();
        servicioPagos = mock(ServicioPagos.class);
        planificador = new PlanificadorFalso();
        UsuarioActual usuarioActual = mock(UsuarioActual.class);
        when(usuarioActual.obtener()).thenReturn(mock(Usuario.class));
        presentador = new PresentadorResultadoPago(servicioPagos, usuarioActual, planificador.planificador);
        presentador.vincular(pantalla);
    }

    /**
     * Configura la fachada para devolver, en orden, pagos con los estados indicados.
     *
     * <p>Casos de uso: CU-35
     * @param codigos códigos de estado sucesivos
     */
    private void estados(String... codigos) {
        PagoOnline[] pagos = new PagoOnline[codigos.length];
        for (int i = 0; i < codigos.length; i++) {
            EstadoPago estado = mock(EstadoPago.class);
            when(estado.codigo()).thenReturn(codigos[i]);
            pagos[i] = mock(PagoOnline.class);
            when(pagos[i].getEstado()).thenReturn(estado);
            when(pagos[i].getReferenciaExterna()).thenReturn(referencia);
        }
        PagoOnline[] resto = java.util.Arrays.copyOfRange(pagos, 1, pagos.length);
        when(servicioPagos.consultarPagoOnline(any(), any())).thenReturn(pagos[0], resto);
    }

    /**
     * Consultando → Esperando → Consultando → Aprobado: se reprograma mientras sigue pendiente y no después.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void pendienteLuegoAprobado() {
        estados(PresentadorResultadoPago.ESTADO_PENDIENTE, "APROBADO");
        presentador.alEntrar(referencia);
        assertEquals(1, planificador.programadas);
        planificador.pasanTresSegundos();
        assertEquals(List.of(PresentadorResultadoPago.ESTADO_PENDIENTE, "APROBADO"), pantalla.estados);
        assertEquals(1, planificador.programadas);
        assertEquals("APROBADO", presentador.getUltimoEstado());
        assertEquals(2, presentador.getIntentos());
        assertFalse(pantalla.espera);
    }

    /**
     * Estados finales en la primera consulta (Rechazado, Acreditado, Vencido): no se programa nada.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void estadosFinalesNoReprograman() {
        for (String codigo : List.of("RECHAZADO", "ACREDITADO", "VENCIDO")) {
            preparar();
            estados(codigo);
            presentador.alEntrar(referencia);
            assertEquals(List.of(codigo), pantalla.estados);
            assertEquals(0, planificador.programadas);
        }
    }

    /**
     * Pendiente en las diez consultas: nueve reprogramaciones y Sin confirmar en la décima (FA13).
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void sinConfirmarTrasDiezConsultas() {
        estados(PresentadorResultadoPago.ESTADO_PENDIENTE);
        presentador.alEntrar(referencia);
        for (int i = 1; i < PresentadorResultadoPago.MAXIMO_INTENTOS; i++) {
            planificador.pasanTresSegundos();
        }
        assertEquals(10, pantalla.estados.size());
        assertEquals(9, planificador.programadas);
        assertEquals(10, presentador.getIntentos());
        assertTrue(pantalla.espera);
        assertNull(planificador.tarea);
    }

    /**
     * Esperando → fin con {@code alSalir()}: cancela la consulta programada y una ejecución tardía no consulta.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void alSalirCancelaLaConsulta() {
        estados(PresentadorResultadoPago.ESTADO_PENDIENTE);
        presentador.alEntrar(referencia);
        ScheduledFuture<?> futuro = planificador.futuro;
        presentador.alSalir();
        org.mockito.Mockito.verify(futuro).cancel(false);
        planificador.pasanTresSegundos();
        assertEquals(1, pantalla.estados.size());
    }

    /**
     * Si la fachada rechaza la consulta (pago de otro grupo, RN-20), se muestra el error y no se reintenta.
     *
     * <p>Casos de uso: CU-35
     */
    @Test
    void errorDeLaFachadaNoReprograma() {
        when(servicioPagos.consultarPagoOnline(any(), any())).thenThrow(new AccesoDenegadoException("ajeno"));
        presentador.alEntrar(referencia);
        assertEquals(List.of("No tiene permiso para realizar esta operación."), pantalla.errores);
        assertEquals(0, planificador.programadas);
    }
}

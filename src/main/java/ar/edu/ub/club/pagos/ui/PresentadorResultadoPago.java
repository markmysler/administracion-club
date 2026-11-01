package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.TaskScheduler;

/**
 * Presentador de la pantalla P-26 Resultado del pago (MVP Vista Pasiva; no usa tipos de Vaadin), la única pantalla
 * con comportamiento dependiente del estado: implementa el diagrama de estados de la clase (est_resultado_pago, 07B):
 *
 * <ul>
 *   <li><b>Consultando</b>: {@code consultar()} llama a {@code ServicioPagos.consultarPagoOnline(usuario,
 *       referencia)} y entrega el resultado con {@code pantalla.mostrarEstado(resumen)};</li>
 *   <li><b>Esperando</b>: si sigue pendiente y {@code intentos < 10}, programa otra consulta a los 3 s con {@code
 *       planificador.schedule(this::consultar, ...)} ({@code consultaProgramada});</li>
 *   <li><b>Aprobado, Rechazado, Acreditado, Vencido</b>: estados finales, no se programa nada más;</li>
 *   <li><b>Sin confirmar</b>: pendiente en la décima consulta → {@code pantalla.mostrarEsperaConfirmacion()};</li>
 *   <li>{@code alSalir()} cancela la consulta programada (salida desde Esperando).</li>
 * </ul>
 *
 * <p>Las consultas programadas corren en un hilo del {@code TaskScheduler}, fuera de la sesión de Vaadin y del
 * contexto de seguridad: por eso se guarda la cuenta en {@code usuario} al entrar (RN-20) y la vista ejecuta cada
 * operación de la pantalla dentro de {@code UI.access(...)} (server push). Los métodos están sincronizados porque los
 * llaman el hilo de la interfaz y el del planificador. El resultado nunca se toma de la URL (RN-17). Diagrama
 * sec_cu35_p9.
 *
 * <p>Casos de uso: CU-35
 */
@SpringComponent
@UIScope
public class PresentadorResultadoPago extends PresentadorBase<PantallaResultadoPago> {

    /** Código del estado pendiente del pago (el único que sigue consultando). */
    static final String ESTADO_PENDIENTE = "PENDIENTE";

    /** Cantidad máxima de consultas. */
    static final int MAXIMO_INTENTOS = 10;

    /** Espera entre consultas. */
    static final Duration ESPERA = Duration.ofSeconds(3);

    /** Fachada del módulo pagos. */
    private final ServicioPagos servicioPagos;

    /** Cuenta de la sesión. */
    private final UsuarioActual usuarioActual;

    /** Programa la siguiente consulta. */
    private final TaskScheduler planificador;

    /** Referencia externa del pago (parámetro de la ruta). */
    private UUID referencia;

    /** Cuenta que consulta (RN-20), tomada de {@code usuarioActual.obtener()} al entrar. */
    private Usuario usuario;

    /** Último estado informado ({@code null} antes de la primera consulta). */
    private String ultimoEstado;

    /** Consultas realizadas (0 a 10). */
    private Integer intentos = 0;

    /** Próxima consulta programada ({@code null} si no hay ninguna). */
    private ScheduledFuture<?> consultaProgramada;

    /**
     * Crea el presentador con sus dependencias.
     *
     * <p>Casos de uso: CU-35
     * @param servicioPagos fachada de pagos
     * @param usuarioActual cuenta de la sesión
     * @param planificador planificador de tareas de Spring
     */
    public PresentadorResultadoPago(ServicioPagos servicioPagos, UsuarioActual usuarioActual,
                                    TaskScheduler planificador) {
        this.servicioPagos = servicioPagos;
        this.usuarioActual = usuarioActual;
        this.planificador = planificador;
    }

    /**
     * Transición inicial → Consultando: cancela una consulta anterior, guarda la referencia y la cuenta de la
     * sesión, pone {@code intentos = 0} y llama a {@code consultar()}.
     *
     * <p>Casos de uso: CU-35
     * @param referencia referencia externa del pago
     */
    public synchronized void alEntrar(UUID referencia) {
        cancelarConsultaProgramada();
        this.referencia = referencia;
        this.usuario = usuarioActual.obtener();
        this.intentos = 0;
        this.ultimoEstado = null;
        consultar();
    }

    /**
     * Estado Consultando. Método público sin parámetros, distinto del método plantilla protegido {@code
     * consultar(Supplier)} heredado de {@code PresentadorBase} (sobrecarga): cuenta la consulta, llama a {@code
     * ServicioPagos.consultarPagoOnline(usuario, referencia)} dentro de ese método plantilla → {@code
     * ResumenPagoOnline.desde(pago)} → {@code pantalla.mostrarEstado(resumen)}. Si sigue pendiente y {@code intentos
     * < 10}, pasa a Esperando programando otra consulta a los 3 s; en la décima consulta pasa a Sin confirmar con
     * {@code pantalla.mostrarEsperaConfirmacion()}. Si la fachada falla, el método plantilla ya mostró el error y no
     * se reintenta.
     *
     * <p>Casos de uso: CU-35
     */
    public synchronized void consultar() {
        consultaProgramada = null;
        if (referencia == null) {
            return;
        }
        intentos++;
        Optional<ResumenPagoOnline> resumen = consultar(() -> servicioPagos.consultarPagoOnline(usuario, referencia))
                .map(ResumenPagoOnline::desde);
        if (resumen.isEmpty()) {
            return;
        }
        ultimoEstado = resumen.get().estado();
        pantalla.mostrarEstado(resumen.get());
        if (ESTADO_PENDIENTE.equals(ultimoEstado)) {
            if (intentos < MAXIMO_INTENTOS) {
                consultaProgramada = planificador.schedule(this::consultar, Instant.now().plus(ESPERA));
            } else {
                pantalla.mostrarEsperaConfirmacion();
            }
        }
    }

    /**
     * Salida desde Esperando: cancela {@code consultaProgramada} cuando la vista se desprende de la interfaz.
     *
     * <p>Casos de uso: CU-35
     */
    public synchronized void alSalir() {
        cancelarConsultaProgramada();
        referencia = null;
    }

    /**
     * Cancela la consulta programada, si hay una.
     *
     * <p>Casos de uso: CU-35
     */
    private void cancelarConsultaProgramada() {
        if (consultaProgramada != null) {
            consultaProgramada.cancel(false);
            consultaProgramada = null;
        }
    }

    /**
     * Devuelve el último estado informado (para las pruebas del diagrama de estados).
     *
     * <p>Agregado en la implementación: consulta de solo lectura del estado del presentador.
     *
     * <p>Casos de uso: CU-35
     * @return código del último estado o {@code null}
     */
    public synchronized String getUltimoEstado() {
        return ultimoEstado;
    }

    /**
     * Devuelve la cantidad de consultas realizadas (para las pruebas del diagrama de estados).
     *
     * <p>Agregado en la implementación: consulta de solo lectura del estado del presentador.
     *
     * <p>Casos de uso: CU-35
     * @return intentos
     */
    public synchronized Integer getIntentos() {
        return intentos;
    }
}

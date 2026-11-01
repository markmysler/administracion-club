package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.TaskScheduler;

/**
 * Presentador de la pantalla P-26: Página de retorno desde la pasarela.
 *
 * <p>Casos de uso: CU-35
 */
public class PresentadorResultadoPago extends PresentadorBase<PantallaResultadoPago> {

    /** Fachada del módulo pagos. */
    private ServicioPagos servicioPagos;

    /** Cuenta de la sesión. */
    private UsuarioActual usuarioActual;

    /** Programa la siguiente consulta. */
    private TaskScheduler planificador;

    /** Referencia externa del pago. */
    private UUID referencia;

    /** Cuenta que consulta (RN-20). */
    private Usuario usuario;

    /** Último estado informado. */
    private String ultimoEstado;

    /** Consultas realizadas. */
    private Integer intentos;

    /** Próxima consulta programada. */
    private ScheduledFuture<?> consultaProgramada;

    /**
     * Guarda la referencia y la cuenta de la sesión y llama a {@code consultar()}.
     *
     * <p>Casos de uso: CU-35
     * @param referencia Referencia externa del pago.
     */
    public void alEntrar(UUID referencia) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Método público sin parámetros, distinto del método plantilla protegido {@code consultar&lt;R&gt;(consulta:
     * Supplier&lt;R&gt;)} heredado de {@code PresentadorBase} (sobrecarga): llama a {@code
     * ServicioPagos.consultarPagoOnline(usuario, referencia)} dentro de ese método plantilla → {@code
     * ResumenPagoOnline.desde(pago)} → {@code pantalla.mostrarEstado(resumen)}. Si sigue PENDIENTE y {@code intentos
     * &lt; 10}, programa otra consulta a los 3 segundos con {@code planificador.schedule(this::consultar, 3 s)}; en
     * el décimo intento llama a {@code pantalla.mostrarEsperaConfirmacion()}.
     *
     * <p>Casos de uso: CU-35
     */
    public void consultar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Cancela {@code consultaProgramada} cuando la vista se desprende de la interfaz.
     *
     * <p>Casos de uso: CU-35
     */
    public void alSalir() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

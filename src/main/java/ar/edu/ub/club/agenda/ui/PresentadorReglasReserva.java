package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosReglaReserva;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-18: Formulario de las seis reglas de reserva. Patrón MVP (Vista Pasiva): llama a
 * {@code ServicioAgenda} dentro de {@code ejecutar}/{@code consultar}; un {@code ErrorNegocio} con campo lo marca
 * en el formulario (CU-25 FA2, FA3).
 *
 * <p>Casos de uso: CU-25
 */
@SpringComponent
@UIScope
public class PresentadorReglasReserva extends PresentadorBase<PantallaReglasReserva> {

    /** Fachada del módulo agenda. */
    private final ServicioAgenda servicioAgenda;

    /**
     * Crea el presentador con la fachada (inyección por constructor).
     *
     * <p>Casos de uso: CU-25
     * @param servicioAgenda fachada del módulo agenda
     */
    public PresentadorReglasReserva(ServicioAgenda servicioAgenda) {
        this.servicioAgenda = servicioAgenda;
    }

    /**
     * {@code ServicioAgenda.consultarReglas(): ReglaReserva} → {@code DatosReglaReserva.desde(regla)}
     *
     * <p>Casos de uso: CU-25
     */
    public void cargar() {
        consultar(servicioAgenda::consultarReglas).ifPresent(regla -> pantalla.mostrarReglas(DatosReglaReserva.desde(regla)));
    }

    /**
     * {@code ServicioAgenda.configurarReglas(datos)}; si termina bien muestra las reglas guardadas e informa que se
     * aplican a las nuevas reservas, modificaciones y cancelaciones (paso 8).
     *
     * <p>Casos de uso: CU-25
     * @param datos seis parámetros
     */
    public void alGuardar(DatosReglaReserva datos) {
        consultar(() -> servicioAgenda.configurarReglas(datos)).ifPresent(regla -> {
            pantalla.mostrarReglas(DatosReglaReserva.desde(regla));
            pantalla.mostrarMensaje("Las reglas se aplican a las nuevas reservas, modificaciones y cancelaciones.");
        });
    }
}

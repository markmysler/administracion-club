package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosReglaReserva;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.ui.PresentadorBase;

/**
 * Presentador de la pantalla P-18: Formulario de las seis reglas de reserva.
 *
 * <p>Casos de uso: CU-25
 */
public class PresentadorReglasReserva extends PresentadorBase<PantallaReglasReserva> {

    /** Atributo servicioAgenda. */
    private ServicioAgenda servicioAgenda;

    /**
     * {@code ServicioAgenda.consultarReglas(): ReglaReserva} → {@code DatosReglaReserva.desde(regla)}
     *
     * <p>Casos de uso: CU-25
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAgenda.configurarReglas(datos)}
     *
     * <p>Casos de uso: CU-25
     * @param datos valor de {@code datos}
     */
    public void alGuardar(DatosReglaReserva datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

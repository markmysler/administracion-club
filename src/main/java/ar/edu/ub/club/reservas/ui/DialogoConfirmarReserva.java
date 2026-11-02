package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.DialogoBase;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Vista de la pantalla P-20 (MVP, Vista Pasiva): diálogo de P-19 que muestra cancha, fecha y horario elegidos y
 * confirma la reserva. No tiene lógica: «Confirmar» llama a {@code presentador.alConfirmar()}; al abrir llama a
 * {@code presentador.alAbrir(turno, idSocio)}. No verifica permiso propio: lo abre la vista protegida P-19 y la
 * fachada verifica la operación.
 *
 * <p>Casos de uso: CU-27, CU-28
 */
public class DialogoConfirmarReserva extends DialogoBase implements PantallaConfirmarReserva {

    /** Presentador del diálogo. */
    private final PresentadorConfirmarReserva presentador;

    /** Turno elegido en la grilla. */
    private final FilaTurno turno;

    /** Socio elegido por el administrador ({@code null}: reserva online). */
    private final Long idSocio;

    /** Texto con cancha, fecha y horario. */
    private final Paragraph datosTurno = new Paragraph();

    /** Texto con el socio de la reserva manual. */
    private final Paragraph datosSocio = new Paragraph();

    /**
     * Arma el diálogo y vincula el presentador.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param presentador presentador de P-20
     * @param turno turno elegido
     * @param idSocio socio elegido o {@code null}
     */
    public DialogoConfirmarReserva(PresentadorConfirmarReserva presentador, FilaTurno turno, Long idSocio) {
        this.presentador = presentador;
        this.turno = turno;
        this.idSocio = idSocio;
        presentador.vincular(this);
        getContent().setHeaderTitle("Confirmar reserva");
        datosSocio.setVisible(false);
        getContent().add(new VerticalLayout(datosTurno, datosSocio));
        Button confirmar = new Button("Confirmar", e -> presentador.alConfirmar());
        confirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cerrar = new Button("Cerrar", e -> cerrar());
        getContent().getFooter().add(cerrar, confirmar);
    }

    /**
     * Abre el diálogo y pide al presentador que muestre el turno (sec_cu27_p1: «create»; abrir(); alAbrir).
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    @Override
    public void abrir() {
        presentador.alAbrir(turno, idSocio);
        super.abrir();
    }

    /**
     * Cierra el diálogo sin registrar la reserva (CU-27 FA1, CU-28 FA2).
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    @Override
    public void cerrar() {
        super.cerrar();
    }

    /**
     * Agregado en la implementación: registra un oyente que se ejecuta cada vez que el diálogo se cierra (con o sin
     * éxito), para que la vista anfitriona recargue la grilla (CU-27 FA9).
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param oyente acción de la vista anfitriona
     */
    public void alCerrar(Runnable oyente) {
        getContent().addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                oyente.run();
            }
        });
    }

    /**
     * Muestra cancha, fecha y horario y, si corresponde, el socio.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param turno turno elegido
     * @param socio descripción del socio o {@code null}
     */
    @Override
    public void mostrarTurno(FilaTurno turno, String socio) {
        datosTurno.setText(turno.cancha() + " (" + turno.deporte() + ") · " + FormatoUI.fecha(turno.fecha()) + " · "
                + FormatoUI.hora(turno.horaInicio()) + " a " + FormatoUI.hora(turno.horaFin()));
        datosSocio.setVisible(socio != null);
        datosSocio.setText(socio == null ? "" : "Socio: " + socio);
    }

    /**
     * Cierra el diálogo y dispara {@code DialogoResueltoEvento} para la vista anfitriona.
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    @Override
    public void cerrarConExito() {
        notificarResuelto();
    }
}

package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.DialogoBase;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.List;

/**
 * Diálogo de la pantalla P-09: baja de socio en dos pasos. Muestra las reservas futuras que se cancelarán y, si el
 * socio es titular de un grupo con otros integrantes activos, pide elegir el nuevo titular. Al terminar con éxito
 * dispara {@code DialogoResueltoEvento} para que {@code VistaFichaSocio} recargue la ficha.
 *
 * <p>Diálogo pasivo del patrón MVP (estados de {@code est_dialogo_baja}): sus oyentes solo llaman a
 * {@code presentador.al…(...)}.
 *
 * <p>Casos de uso: CU-03
 */
public class DialogoDesactivarSocio extends DialogoBase implements PantallaDesactivarSocio {

    /** Presentador del diálogo. */
    private final PresentadorDesactivarSocio presentador;

    /** Socio que se da de baja. */
    private final Paragraph socio = new Paragraph();

    /** Aviso sobre las reservas. */
    private final Paragraph aviso = new Paragraph();

    /** Reservas futuras que se cancelarán. */
    private final TablaDatos<FilaResumenReserva> reservas = new TablaDatos<>(FilaResumenReserva.class);

    /** Selector del nuevo titular (visible solo si hay candidatos). */
    private final ComboBox<OpcionSeleccion> nuevoTitular = new ComboBox<>("Nuevo titular del grupo");

    /** Botón de confirmación (habilitado al recibir la advertencia). */
    private final Button confirmar;

    /**
     * Arma el diálogo y se vincula con el presentador.
     *
     * <p>Casos de uso: CU-03
     * @param presentador presentador de P-09
     */
    public DialogoDesactivarSocio(PresentadorDesactivarSocio presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        Dialog dialogo = getContent();
        dialogo.setHeaderTitle("Desactivar socio");
        reservas.agregarColumna("Fecha", f -> FormatoUI.fecha(f.fecha()));
        reservas.agregarColumna("Horario", f -> FormatoUI.hora(f.horaInicio()) + " a " + FormatoUI.hora(f.horaFin()));
        reservas.agregarColumna("Cancha", FilaResumenReserva::cancha);
        reservas.setVisible(false);
        nuevoTitular.setItemLabelGenerator(OpcionSeleccion::texto);
        nuevoTitular.setVisible(false);
        nuevoTitular.setWidthFull();
        VerticalLayout cuerpo = new VerticalLayout(socio, aviso, reservas, nuevoTitular);
        cuerpo.setPadding(false);
        dialogo.add(cuerpo);
        Button cancelar = new Button("Cancelar", e -> cerrar());
        confirmar = new Button("Confirmar baja", e -> presentador.alConfirmar(
                nuevoTitular.getValue() == null ? null : nuevoTitular.getValue().id()));
        confirmar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        confirmar.setEnabled(false);
        dialogo.getFooter().add(cancelar, confirmar);
        dialogo.setWidth("min(640px, 95vw)");
    }

    /**
     * Abre el diálogo y le pide al presentador el primer paso ({@code alAbrir}).
     *
     * <p>Agregado en la implementación: sobrecarga de {@code DialogoBase.abrir()} con los datos que la vista
     * anfitriona recibe en {@code abrirDesactivacion} ({@code D -> P: alAbrir(...)} en {@code sec_cu03_p1}).
     *
     * <p>Casos de uso: CU-03
     * @param idSocio socio
     * @param nombre apellido y nombre
     * @param dni documento
     * @param candidatosTitular candidatos a nuevo titular
     */
    public void abrir(Long idSocio, String nombre, String dni, List<OpcionSeleccion> candidatosTitular) {
        abrir();
        presentador.alAbrir(idSocio, nombre, dni, candidatosTitular);
    }

    /**
     * Muestra el socio que se dará de baja.
     *
     * <p>Casos de uso: CU-03
     * @param nombre apellido y nombre
     * @param dni documento
     */
    @Override
    public void mostrarSocio(String nombre, String dni) {
        socio.setText("Socio: " + nombre + " (DNI " + dni + "). La cuenta pasará a inactiva y se conservará su historial.");
    }

    /**
     * Muestra las reservas futuras que se cancelarán (o que no hay) y, si hay candidatos, el selector de nuevo
     * titular; habilita la confirmación (estado «Advertencia»).
     *
     * <p>Casos de uso: CU-03
     * @param reservas reservas afectadas
     * @param candidatosTitular integrantes que pueden ser titulares (vacía si no corresponde)
     */
    @Override
    public void mostrarAfectadas(List<FilaResumenReserva> reservas, List<OpcionSeleccion> candidatosTitular) {
        aviso.setText(reservas.isEmpty() ? "El socio no tiene reservas futuras."
                : "Se cancelarán " + reservas.size() + " reserva(s) futura(s) con el motivo «Baja de socio»:");
        this.reservas.mostrar(reservas);
        this.reservas.setVisible(!reservas.isEmpty());
        nuevoTitular.setItems(candidatosTitular);
        nuevoTitular.setVisible(!candidatosTitular.isEmpty());
        confirmar.setEnabled(true);
    }

    /**
     * Cierra el diálogo y dispara {@code DialogoResueltoEvento} (estado «Realizada»).
     *
     * <p>Casos de uso: CU-03
     */
    @Override
    public void cerrarConExito() {
        notificarResuelto();
    }
}

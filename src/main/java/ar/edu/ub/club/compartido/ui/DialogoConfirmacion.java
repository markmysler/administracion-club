package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import java.util.List;
import java.util.function.Consumer;

/**
 * Componente común de confirmación: toda acción destructiva o en dos pasos (bajas con reservas afectadas, cambio
 * de deporte de una cancha, cambio de disponibilidad, feriado con reservas, cancelaciones, quitar integrante,
 * cambiar titular, desactivar cuenta) pasa antes por este diálogo. Puede mostrar advertencias y pedir un motivo.
 *
 * <p>Casos de uso: CU-03, CU-08, CU-09, CU-17, CU-20, CU-22, CU-23, CU-24, CU-30, CU-33
 */
public class DialogoConfirmacion extends Composite<Dialog> {

    /** Cuerpo del diálogo. */
    private final VerticalLayout cuerpo = new VerticalLayout();

    /** Campo de motivo (si se pide). */
    private TextArea motivo;

    /** Indica si el motivo es obligatorio. */
    private boolean motivoObligatorio;

    /** Oyente de la confirmación. */
    private Consumer<String> alConfirmar = m -> { };

    /** Oyente de la cancelación. */
    private Runnable alCancelar = () -> { };

    /**
     * Crea el diálogo con título y mensaje.
     *
     * <p>Casos de uso: CU-03, CU-08, CU-09, CU-17, CU-20, CU-22, CU-23, CU-24, CU-30, CU-33
     * @param titulo título
     * @param mensaje pregunta de confirmación
     */
    public DialogoConfirmacion(String titulo, String mensaje) {
        Dialog dialogo = getContent();
        dialogo.setHeaderTitle(titulo);
        cuerpo.setPadding(false);
        cuerpo.add(new Paragraph(mensaje));
        dialogo.add(cuerpo);
        Button confirmar = new Button("Confirmar", e -> confirmar());
        confirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelar = new Button("Cancelar", e -> {
            cerrar();
            alCancelar.run();
        });
        dialogo.getFooter().add(cancelar, confirmar);
    }

    /**
     * Muestra advertencias (por ejemplo, las reservas que se cancelarán).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-22, CU-23, CU-24
     * @param lineas textos de las advertencias
     */
    public void mostrarAdvertencias(List<String> lineas) {
        if (lineas.isEmpty()) {
            return;
        }
        UnorderedList lista = new UnorderedList();
        lineas.forEach(l -> lista.add(new ListItem(l)));
        cuerpo.add(lista);
    }

    /**
     * Pide el motivo de la acción.
     *
     * <p>Casos de uso: CU-33
     * @param obligatorio true si no se puede confirmar sin motivo
     */
    public void pedirMotivo(boolean obligatorio) {
        this.motivoObligatorio = obligatorio;
        this.motivo = new TextArea("Motivo");
        motivo.setWidthFull();
        motivo.setMaxLength(200);
        cuerpo.add(motivo);
    }

    /**
     * Registra el oyente de la confirmación (recibe el motivo o {@code null}).
     *
     * <p>Casos de uso: CU-03, CU-08, CU-09, CU-17, CU-20, CU-22, CU-23, CU-24, CU-30, CU-33
     * @param oyente oyente
     */
    public void alConfirmar(Consumer<String> oyente) {
        this.alConfirmar = oyente;
    }

    /**
     * Registra el oyente de la cancelación.
     *
     * <p>Casos de uso: CU-03, CU-08, CU-09, CU-17, CU-20, CU-22, CU-23, CU-24, CU-30, CU-33
     * @param oyente oyente
     */
    public void alCancelar(Runnable oyente) {
        this.alCancelar = oyente;
    }

    /**
     * Abre el diálogo.
     *
     * <p>Casos de uso: CU-03, CU-08, CU-09, CU-17, CU-20, CU-22, CU-23, CU-24, CU-30, CU-33
     */
    public void abrir() {
        getContent().open();
    }

    /**
     * Cierra el diálogo.
     *
     * <p>Casos de uso: CU-03, CU-08, CU-09, CU-17, CU-20, CU-22, CU-23, CU-24, CU-30, CU-33
     */
    public void cerrar() {
        getContent().close();
    }

    /**
     * Valida el motivo obligatorio, cierra y avisa al oyente.
     *
     * <p>Casos de uso: CU-03, CU-08, CU-09, CU-17, CU-20, CU-22, CU-23, CU-24, CU-30, CU-33
     */
    private void confirmar() {
        String texto = motivo == null ? null : motivo.getValue();
        if (motivoObligatorio && (texto == null || texto.isBlank())) {
            motivo.setErrorMessage("Ingrese el motivo");
            motivo.setInvalid(true);
            return;
        }
        cerrar();
        alConfirmar.accept(texto == null || texto.isBlank() ? null : texto.trim());
    }
}

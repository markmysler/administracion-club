package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.ComponentEvent;

/**
 * Evento de componente de Vaadin que significa «el diálogo terminó con éxito»: lo dispara un {@code DialogoBase}
 * y la vista anfitriona, al recibirlo, vuelve a cargar con su presentador.
 *
 * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
 */
public class DialogoResueltoEvento extends ComponentEvent<DialogoBase> {

    /**
     * Crea el evento.
     *
     * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
     * @param fuente diálogo que terminó
     * @param desdeCliente true si lo originó el navegador
     */
    public DialogoResueltoEvento(DialogoBase fuente, boolean desdeCliente) {
        super(fuente, desdeCliente);
    }
}

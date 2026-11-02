package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.shared.Registration;

/**
 * Base abstracta de los diálogos con presentador propio (MVP): {@code DialogoDesactivarSocio} (P-09),
 * {@code DialogoAsociarSocio} (P-12) y {@code DialogoConfirmarReserva} (P-20). No verifica permiso propio: lo abre
 * una vista protegida y la fachada verifica la operación. Al terminar con éxito dispara
 * {@code DialogoResueltoEvento} para que la vista anfitriona recargue.
 *
 * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
 */
public abstract class DialogoBase extends Composite<Dialog> implements PantallaBase {

    /** Zona de mensajes del diálogo. */
    private final MensajeEstado mensajes = new MensajeEstado();

    /**
     * Abre el diálogo.
     *
     * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
     */
    public void abrir() {
        getContent().open();
    }

    /**
     * Cierra el diálogo.
     *
     * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
     */
    public void cerrar() {
        getContent().close();
    }

    /**
     * Registra un oyente de los eventos del diálogo (público para que la vista anfitriona escuche
     * {@code DialogoResueltoEvento}).
     *
     * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
     * @param tipo clase del evento
     * @param oyente oyente
     * @param <T> tipo de evento
     * @return registro para quitar el oyente
     */
    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> tipo, ComponentEventListener<T> oyente) {
        return super.addListener(tipo, oyente);
    }

    /**
     * Cierra el diálogo y avisa a la vista anfitriona que terminó con éxito.
     *
     * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
     */
    protected void notificarResuelto() {
        cerrar();
        fireEvent(new DialogoResueltoEvento(this, false));
    }

    /**
     * Muestra un error dentro del diálogo.
     *
     * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
     * @param mensaje texto del error
     */
    @Override
    public void mostrarError(String mensaje) {
        mensajes.anunciarError(mensaje);
    }

    /**
     * Muestra un mensaje.
     *
     * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
     * @param mensaje texto
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        mensajes.anunciar(mensaje);
    }

    /**
     * Los diálogos no tienen formulario vinculado: muestra el mensaje como error.
     *
     * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
     * @param campo campo (no se usa)
     * @param mensaje texto del error
     */
    @Override
    public void marcarCampo(String campo, String mensaje) {
        mostrarError(mensaje);
    }

    /**
     * Cierra el diálogo y navega.
     *
     * <p>Casos de uso: CU-03, CU-07, CU-27, CU-28
     * @param ruta ruta destino
     */
    @Override
    public void navegarA(String ruta) {
        cerrar();
        UI.getCurrent().navigate(ruta);
    }
}

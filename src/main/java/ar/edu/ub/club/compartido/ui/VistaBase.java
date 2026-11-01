package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.Optional;

/**
 * Base abstracta de las vistas Vaadin (MVP, Vista Pasiva): implementa las operaciones comunes de
 * {@code PantallaBase} sobre los componentes. Es la base directa de las vistas públicas ({@code @AnonymousAllowed}):
 * {@code VistaInicioSesion}, {@code VistaRecuperarClave}, {@code VistaRestablecerClave},
 * {@code VistaSimuladorPasarela} y {@code VistaAccesoDenegado}; las demás extienden {@code VistaProtegida}.
 *
 * <p>Casos de uso: todos
 */
public abstract class VistaBase extends Composite<VerticalLayout> implements PantallaBase {

    /** Zona de mensajes propia, para las vistas que no están dentro de {@code DisposicionPrincipal}. */
    private final MensajeEstado mensajesPropios = new MensajeEstado();

    /**
     * Zona de mensajes de {@code DisposicionPrincipal} si la vista está dentro de ella; si no, la propia.
     *
     * <p>Casos de uso: todos
     * @return zona de mensajes
     */
    protected MensajeEstado mensajes() {
        Optional<Component> ancestro = getParent();
        while (ancestro.isPresent()) {
            if (ancestro.get() instanceof DisposicionPrincipal disposicion) {
                return disposicion.mensajes();
            }
            ancestro = ancestro.get().getParent();
        }
        return mensajesPropios;
    }

    /**
     * Muestra un error con {@code MensajeEstado.anunciarError}.
     *
     * <p>Casos de uso: todos
     * @param mensaje texto del error
     */
    @Override
    public void mostrarError(String mensaje) {
        mensajes().anunciarError(mensaje);
    }

    /**
     * Muestra un mensaje con {@code MensajeEstado.anunciar}.
     *
     * <p>Casos de uso: todos
     * @param mensaje texto
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        mensajes().anunciar(mensaje);
    }

    /**
     * Marca el campo en el formulario de la vista ({@code formulario().marcarError}); si la vista no tiene
     * formulario, muestra el mensaje como error general.
     *
     * <p>Casos de uso: todos
     * @param campo propiedad del objeto de comando
     * @param mensaje texto del error
     */
    @Override
    public void marcarCampo(String campo, String mensaje) {
        FormularioEntidad<?> formulario = formulario();
        if (formulario != null && formulario.tieneCampo(campo)) {
            formulario.marcarError(campo, mensaje);
        } else {
            mostrarError(mensaje);
        }
    }

    /**
     * Navega con {@code UI.getCurrent().navigate(ruta)}.
     *
     * <p>Casos de uso: todos
     * @param ruta ruta destino
     */
    @Override
    public void navegarA(String ruta) {
        UI.getCurrent().navigate(ruta);
    }

    /**
     * Gancho: formulario principal de la vista, para {@code marcarCampo}. Por omisión ninguno.
     *
     * <p>Casos de uso: todos
     * @return formulario o {@code null}
     */
    protected FormularioEntidad<?> formulario() {
        return null;
    }
}

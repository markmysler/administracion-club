package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Vista pública {@code acceso-denegado}: muestra «No tiene permiso para acceder a esta pantalla» y el enlace al
 * inicio. No tiene presentador. A ella lleva {@code VistaProtegida} cuando falta el permiso de la pantalla.
 *
 * <p>Casos de uso: todos
 */
@Route("acceso-denegado")
@PageTitle("Acceso denegado")
@AnonymousAllowed
public class VistaAccesoDenegado extends VistaBase {

    /**
     * Arma el mensaje y el enlace al inicio.
     *
     * <p>Casos de uso: todos
     */
    public VistaAccesoDenegado() {
        getContent().add(new H2("Acceso denegado"),
                new Paragraph("No tiene permiso para acceder a esta pantalla."),
                new Anchor("", "Volver al inicio"));
    }
}

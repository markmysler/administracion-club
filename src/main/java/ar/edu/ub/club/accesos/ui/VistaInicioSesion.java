package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.ui.VistaBase;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Vista de la pantalla P-01: Formulario de usuario y contraseña, casilla «Mantener la sesión iniciada» y enlace
 * «Olvidé mi contraseña» (punto de extensión hacia CU-13). Usa el {@code LoginForm} de Vaadin; es la vista de
 * ingreso que fija {@code ConfiguracionSeguridad}. Vista pública (MVP, Vista Pasiva): los oyentes solo llaman al
 * presentador.
 *
 * <p>Ruta: {@code inicio-sesion}.
 *
 * <p>Casos de uso: CU-11, CU-13
 */
@Route("inicio-sesion")
@PageTitle("Iniciar sesión")
@AnonymousAllowed
public class VistaInicioSesion extends VistaBase implements PantallaInicioSesion {

    /** Formulario de usuario y contraseña. */
    private final LoginForm formulario = new LoginForm();

    /** Casilla «Mantener la sesión iniciada» (solo tiene efecto en cuentas de socio). */
    private final Checkbox mantenerSesion = new Checkbox("Mantener la sesión iniciada");

    /**
     * Arma P-01 en castellano y conecta los oyentes con el presentador.
     *
     * <p>Casos de uso: CU-11, CU-13
     * @param presentador presentador de P-01
     */
    public VistaInicioSesion(PresentadorInicioSesion presentador) {
        presentador.vincular(this);
        LoginI18n textos = LoginI18n.createDefault();
        LoginI18n.Form form = textos.getForm();
        form.setTitle("Iniciar sesión");
        form.setUsername("Usuario");
        form.setPassword("Contraseña");
        form.setSubmit("Ingresar");
        form.setForgotPassword("Olvidé mi contraseña");
        textos.setErrorMessage(null);
        formulario.setI18n(textos);
        formulario.setForgotPasswordButtonVisible(true);
        formulario.addLoginListener(e -> presentador.alIngresar(e.getUsername(), e.getPassword(), mantenerSesion.getValue()));
        formulario.addForgotPasswordListener(e -> presentador.alOlvidarClave());
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().add(mensajes(), formulario, mantenerSesion);
    }

    /**
     * Borra la contraseña escrita en el {@code LoginForm} (el componente no tiene API de servidor para eso).
     *
     * <p>Casos de uso: CU-11
     */
    @Override
    public void limpiarClave() {
        formulario.getElement().executeJs(
                "const c = this.querySelector('vaadin-password-field') || "
                        + "(this.shadowRoot && this.shadowRoot.querySelector('vaadin-password-field'));"
                        + " if (c) { c.value = ''; }");
    }

    /**
     * Habilita o deshabilita el botón Ingresar.
     *
     * <p>Casos de uso: CU-11
     * @param habilitado true para habilitarlo
     */
    @Override
    public void habilitarEnvio(boolean habilitado) {
        formulario.setEnabled(habilitado);
    }
}

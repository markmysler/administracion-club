package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.DatosAcceso;
import ar.edu.ub.club.accesos.aplicacion.DatosAdministrador;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormularioEntidad;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-14: Formulario de cuenta de acceso: alta de administrador o modificación de datos de
 * acceso de cualquier cuenta. MVP (Vista Pasiva): el {@code FormularioEntidad} valida el formato y entrega los
 * datos al presentador; «Cancelar» vuelve al listado sin llamar al presentador (CU-14 FA1, CU-15 FA3). El tipo de
 * cuenta se muestra solo para lectura (CU-16 FA1).
 *
 * <p>Ruta: {@code cuentas/formulario/:id?}. Permiso: {@code GESTIONAR_CUENTAS}.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-16
 */
@Route(value = "cuentas/formulario/:id?", layout = DisposicionPrincipal.class)
@PageTitle("Cuenta de acceso")
public class VistaFormularioCuenta extends VistaProtegida implements PantallaFormularioCuenta {

    /** Presentador de P-14. */
    private final PresentadorFormularioCuenta presentador;

    /** Formulario que se muestra (alta o modificación). */
    private FormularioEntidad<?> formulario;

    /**
     * Crea la vista y se vincula al presentador; el contenido lo arma {@code mostrarAlta} o
     * {@code mostrarModificacion}.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param presentador presentador de P-14
     */
    public VistaFormularioCuenta(PresentadorFormularioCuenta presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @return {@code GESTIONAR_CUENTAS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_CUENTAS;
    }

    /**
     * Lee el id opcional de la ruta y pide al presentador el formulario de alta o de modificación.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param parametros parámetros de la ruta ({@code id} opcional)
     * @param consulta parámetros de consulta (no usa)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar(parametros.getLong("id").orElse(null));
    }

    /**
     * Muestra el formulario de alta de administrador.
     *
     * <p>Casos de uso: CU-14, CU-16
     * @param datos datos iniciales
     */
    @Override
    public void mostrarAlta(DatosAdministrador datos) {
        FormularioEntidad<DatosAdministrador> alta = new FormularioEntidad<>(DatosAdministrador.class);
        TextField nombre = new TextField("Nombre");
        TextField apellido = new TextField("Apellido");
        TextField usuario = new TextField("Nombre de usuario");
        EmailField email = new EmailField("Correo electrónico");
        PasswordField clave = new PasswordField("Clave inicial");
        clave.getElement().setAttribute("autocomplete", "new-password");
        clave.setHelperText("De 15 a 64 caracteres; puede usar espacios.");
        alta.vincular(nombre, "nombre", List.of(longitud("Ingrese el nombre.", 1, 60)));
        alta.vincular(apellido, "apellido", List.of(longitud("Ingrese el apellido.", 1, 60)));
        alta.vincular(usuario, "username", List.of(longitud("De 4 a 50 caracteres.", 4, 50)));
        alta.vincular(email, "email", List.of(new EmailValidator("Ingrese un correo válido.")));
        alta.vincular(clave, "claveInicial", List.of(longitud("De 15 a 64 caracteres.", 15, 64)));
        alta.cargar(datos);
        alta.alEnviar(presentador::alCrear);
        mostrar(new H2("Nueva cuenta de administrador"),
                new Paragraph("Tipo de cuenta: Administrador (queda fijado al crearla)."), alta);
        this.formulario = alta;
    }

    /**
     * Muestra el formulario de modificación de datos de acceso con el detalle de solo lectura.
     *
     * <p>Casos de uso: CU-15, CU-16
     * @param datos datos de acceso precargados
     * @param cuenta detalle de la cuenta
     */
    @Override
    public void mostrarModificacion(DatosAcceso datos, DetalleCuenta cuenta) {
        FormularioEntidad<DatosAcceso> modificacion = new FormularioEntidad<>(DatosAcceso.class);
        TextField usuario = new TextField("Nombre de usuario");
        EmailField email = new EmailField("Correo electrónico");
        PasswordField clave = new PasswordField("Nueva contraseña (opcional)");
        clave.getElement().setAttribute("autocomplete", "new-password");
        clave.setHelperText("Déjela vacía para no cambiarla. De 15 a 64 caracteres.");
        modificacion.vincular(usuario, "username", List.of(longitud("De 4 a 50 caracteres.", 4, 50)));
        modificacion.vincular(email, "email", List.of(new EmailValidator("Ingrese un correo válido.")));
        modificacion.vincular(clave, "nuevaClave", List.of(longitud("Hasta 64 caracteres.", 0, 64)));
        modificacion.cargar(datos);
        modificacion.alEnviar(presentador::alModificar);
        mostrar(new H2("Datos de acceso de " + cuenta.nombreCompleto()),
                new Paragraph("Tipo de cuenta: " + cuenta.tipoCuenta() + " · Estado: " + cuenta.estado()),
                modificacion);
        this.formulario = modificacion;
    }

    /**
     * Formulario actual, para que {@code marcarCampo} marque el campo rechazado por la fachada.
     *
     * <p>Casos de uso: CU-14, CU-15
     * @return formulario o {@code null}
     */
    @Override
    protected FormularioEntidad<?> formulario() {
        return formulario;
    }

    /**
     * Reemplaza el contenido de la vista y agrega el botón Cancelar (vuelve al listado sin llamar al presentador).
     *
     * <p>Casos de uso: CU-14, CU-15
     * @param componentes título, detalle y formulario
     */
    private void mostrar(Component... componentes) {
        getContent().removeAll();
        getContent().add(componentes);
        getContent().add(new Button("Cancelar", e -> navegarA("cuentas")));
    }

    /**
     * Validador de longitud de texto para los campos del formulario.
     *
     * <p>Casos de uso: CU-14, CU-15
     * @param mensaje mensaje de error
     * @param minimo mínimo de caracteres
     * @param maximo máximo de caracteres
     * @return validador
     */
    private static Validator<?> longitud(String mensaje, int minimo, int maximo) {
        return new StringLengthValidator(mensaje, minimo, maximo);
    }
}

package ar.edu.ub.club.configuracion.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormularioEntidad;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.configuracion.aplicacion.DatosConfiguracionClub;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-34: Formulario de datos institucionales del club (MVP, Vista Pasiva: no tiene lógica, los
 * oyentes solo llaman al presentador). La validación de formato la hace el {@code FormularioEntidad}.
 *
 * <p>Ruta: {@code configuracion-club}. Permiso: {@code CONFIGURAR_CLUB}.
 *
 * <p>Casos de uso: CU-41
 */
@Route(value = "configuracion-club", layout = DisposicionPrincipal.class)
@PageTitle("Configuración del club")
public class VistaConfiguracionClub extends VistaProtegida implements PantallaConfiguracionClub {

    /** Presentador de la pantalla. */
    private final PresentadorConfiguracionClub presentador;

    /** Formulario vinculado a {@code DatosConfiguracionClub}. */
    private final FormularioEntidad<DatosConfiguracionClub> formulario =
            new FormularioEntidad<>(DatosConfiguracionClub.class);

    /** Últimos datos mostrados (para «Cancelar»). */
    private DatosConfiguracionClub datosMostrados;

    /**
     * Arma el formulario (nombre, dirección, teléfono y correo) y se vincula al presentador.
     *
     * <p>Casos de uso: CU-41
     * @param presentador presentador de P-34
     */
    public VistaConfiguracionClub(PresentadorConfiguracionClub presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        TextField nombre = new TextField("Nombre del club");
        nombre.setRequiredIndicatorVisible(true);
        TextField direccion = new TextField("Dirección");
        TextField telefono = new TextField("Teléfono");
        EmailField email = new EmailField("Correo de contacto");
        formulario.vincular(nombre, "nombre", List.of(new StringLengthValidator("Ingrese el nombre (hasta 100 caracteres).", 1, 100)));
        formulario.vincular(direccion, "direccion", List.of(new StringLengthValidator("Hasta 150 caracteres.", 0, 150)));
        formulario.vincular(telefono, "telefono", List.of(new StringLengthValidator("Hasta 30 caracteres.", 0, 30)));
        formulario.vincular(email, "email", List.of(new EmailValidator("Ingrese un correo válido.", true)));
        formulario.alEnviar(presentador::alGuardar);
        Button cancelar = new Button("Cancelar", e -> {
            if (datosMostrados != null) {
                formulario.cargar(datosMostrados);
            }
        });
        getContent().add(new H2("Datos del club"), formulario, cancelar);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-41
     * @return {@code CONFIGURAR_CLUB}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONFIGURAR_CLUB;
    }

    /**
     * Al entrar pide los datos al presentador (CU-41 paso 1).
     *
     * <p>Casos de uso: CU-41
     * @param parametros parámetros de la ruta (no usa)
     * @param consulta parámetros de consulta (no usa)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Carga los datos en el formulario.
     *
     * <p>Casos de uso: CU-41
     * @param datos datos a mostrar
     */
    @Override
    public void mostrarDatos(DatosConfiguracionClub datos) {
        this.datosMostrados = datos;
        formulario.cargar(datos);
    }

    /**
     * Formulario de la pantalla, para que {@code marcarCampo} marque el campo que rechazó la fachada.
     *
     * <p>Casos de uso: CU-41
     * @return formulario
     */
    @Override
    protected FormularioEntidad<?> formulario() {
        return formulario;
    }
}

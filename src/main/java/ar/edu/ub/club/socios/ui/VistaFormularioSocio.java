package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormularioEntidad;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.socios.aplicacion.DatosSocio;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Vista de la pantalla P-07: Formulario de alta y modificación de socio. Vincula un
 * {@code FormularioEntidad<DatosSocio>}; en el alta muestra además usuario y contraseña inicial (CU-16); el tipo
 * de cuenta no es un campo.
 *
 * <p>Ruta: {@code socios/formulario/:id?}. Permiso: {@code GESTIONAR_SOCIOS}.
 *
 * <p>Vista pasiva del patrón MVP: valida el formato con los validadores del {@code Binder} (CU-01 FA3) y sus
 * oyentes solo llaman a {@code presentador.al…(...)}.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-16
 */
@Route(value = "socios/formulario/:id?", layout = DisposicionPrincipal.class)
@PageTitle("Formulario de socio")
public class VistaFormularioSocio extends VistaProtegida implements PantallaFormularioSocio {

    /** Presentador de la pantalla. */
    private final PresentadorFormularioSocio presentador;

    /** Formulario vinculado a {@code DatosSocio}. */
    private final FormularioEntidad<DatosSocio> formulario = new FormularioEntidad<>(DatosSocio.class);

    /** Título (alta o modificación). */
    private final H2 titulo = new H2("Socio");

    /** Selector de categoría. */
    private final CampoOpcion categoria = new CampoOpcion("Categoría");

    /** Nombre de usuario (solo en el alta). */
    private final TextField username = new TextField("Nombre de usuario");

    /** Contraseña inicial que elige el socio (solo en el alta). */
    private final PasswordField claveInicial = new PasswordField("Contraseña inicial (15 a 64 caracteres)");

    /** Correo electrónico. */
    private final EmailField email = new EmailField("Correo electrónico");

    /**
     * Arma el formulario con sus validadores de formato y se vincula con el presentador.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param presentador presentador de P-07
     */
    public VistaFormularioSocio(PresentadorFormularioSocio presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        formulario.vincular(new TextField("DNI"), "dni",
                List.of(new RegexpValidator("El DNI debe tener 7 u 8 dígitos.", "^[0-9]{7,8}$")));
        formulario.vincular(new TextField("Nombre"), "nombre",
                List.of(new StringLengthValidator("El nombre es obligatorio.", 1, 60)));
        formulario.vincular(new TextField("Apellido"), "apellido",
                List.of(new StringLengthValidator("El apellido es obligatorio.", 1, 60)));
        DatePicker nacimiento = new DatePicker("Fecha de nacimiento");
        formulario.vincular(nacimiento, "fechaNacimiento",
                List.of(com.vaadin.flow.data.binder.Validator.from(Objects::nonNull,
                        "La fecha de nacimiento es obligatoria.")));
        formulario.vincular(new TextField("Teléfono"), "telefono", List.of());
        formulario.vincular(new TextField("Dirección"), "direccion", List.of());
        formulario.vincular(email, "email", List.of(new EmailValidator("El correo no tiene un formato válido.")));
        formulario.vincular(categoria, "idCategoria",
                List.of(com.vaadin.flow.data.binder.Validator.from(Objects::nonNull, "Debe elegir una categoría.")));
        formulario.vincular(username, "username", List.of());
        formulario.vincular(claveInicial, "claveInicial", List.of());
        formulario.alEnviar(presentador::alGuardar);
        Button cancelar = new Button("Cancelar", e -> presentador.alCancelar());
        formulario.setVisible(false);
        getContent().add(titulo, formulario, cancelar);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @return {@code GESTIONAR_SOCIOS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_SOCIOS;
    }

    /**
     * Pide al presentador la carga del alta (sin id) o de la modificación (con id).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param parametros parámetros de la ruta ({@code id} opcional)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar(parametros.getLong("id").orElse(null));
    }

    /**
     * Formulario de la vista, para que {@code marcarCampo} marque el campo del {@code ErrorNegocio}.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return el formulario
     */
    @Override
    protected FormularioEntidad<?> formulario() {
        return formulario;
    }

    /**
     * Carga las opciones del selector de categoría.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param opciones categorías asignables
     */
    @Override
    public void mostrarCategorias(List<OpcionSeleccion> opciones) {
        categoria.opciones(opciones);
    }

    /**
     * Carga los datos en el formulario; en la modificación oculta usuario, correo y contraseña (se modifican con
     * CU-15).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @param datos datos a mostrar
     * @param esAlta true en el alta
     */
    @Override
    public void mostrarFormulario(DatosSocio datos, boolean esAlta) {
        titulo.setText(esAlta ? "Registrar socio" : "Modificar socio");
        username.setVisible(esAlta);
        claveInicial.setVisible(esAlta);
        email.setReadOnly(!esAlta);
        formulario.cargar(datos);
        formulario.setVisible(true);
    }

    /**
     * Campo de formulario cuyo valor es el id ({@code Long}) de una {@code OpcionSeleccion} elegida en un
     * {@code ComboBox}: permite vincular {@code DatosSocio.idCategoria} con el {@code Binder}.
     *
     * <p>Casos de uso: CU-01, CU-02
     */
    private static final class CampoOpcion extends CustomField<Long> {

        /** Selector interno. */
        private final ComboBox<OpcionSeleccion> selector = new ComboBox<>();

        /** Opciones disponibles. */
        private List<OpcionSeleccion> disponibles = new ArrayList<>();

        /**
         * Crea el campo con su etiqueta.
         *
         * <p>Casos de uso: CU-01, CU-02
         * @param etiqueta etiqueta visible
         */
        CampoOpcion(String etiqueta) {
            setLabel(etiqueta);
            selector.setItemLabelGenerator(OpcionSeleccion::texto);
            selector.setWidthFull();
            add(selector);
        }

        /**
         * Reemplaza las opciones del selector.
         *
         * <p>Casos de uso: CU-01, CU-02
         * @param opciones opciones
         */
        void opciones(List<OpcionSeleccion> opciones) {
            this.disponibles = new ArrayList<>(opciones);
            selector.setItems(disponibles);
        }

        /**
         * Valor del campo: id de la opción elegida.
         *
         * <p>Casos de uso: CU-01, CU-02
         * @return id o {@code null}
         */
        @Override
        protected Long generateModelValue() {
            OpcionSeleccion elegida = selector.getValue();
            return elegida == null ? null : elegida.id();
        }

        /**
         * Muestra la opción cuyo id es el valor.
         *
         * <p>Casos de uso: CU-02
         * @param valor id
         */
        @Override
        protected void setPresentationValue(Long valor) {
            selector.setValue(disponibles.stream().filter(o -> o.id().equals(valor)).findFirst().orElse(null));
        }
    }
}

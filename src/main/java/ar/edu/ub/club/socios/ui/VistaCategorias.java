package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.FormularioEntidad;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.socios.aplicacion.DatosCategoria;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-10: Listado de categorías con alta y edición (formulario en un diálogo).
 *
 * <p>Ruta: {@code categorias}. Permiso: {@code GESTIONAR_CATEGORIAS}.
 *
 * <p>Vista pasiva del patrón MVP: valida el formato (nombre obligatorio, importe mayor o igual a 0, CU-06 FA2) con
 * el {@code Binder}; sus oyentes solo llaman a {@code presentador.al…(...)}.
 *
 * <p>Casos de uso: CU-06
 */
@Route(value = "categorias", layout = DisposicionPrincipal.class)
@PageTitle("Categorías de socio")
public class VistaCategorias extends VistaProtegida implements PantallaCategorias {

    /** Presentador de la pantalla. */
    private final PresentadorCategorias presentador;

    /** Listado de categorías. */
    private final TablaDatos<FilaCategoria> tabla = new TablaDatos<>(FilaCategoria.class);

    /** Diálogo del formulario abierto ({@code null} si no hay). */
    private Dialog dialogo;

    /** Formulario abierto ({@code null} si no hay). */
    private FormularioEntidad<DatosCategoria> formulario;

    /**
     * Arma la tabla y el botón de alta y se vincula con el presentador.
     *
     * <p>Casos de uso: CU-06
     * @param presentador presentador de P-10
     */
    public VistaCategorias(PresentadorCategorias presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        tabla.agregarColumna("Nombre", FilaCategoria::nombre);
        tabla.agregarColumna("Importe", f -> FormatoUI.dinero(f.importe()));
        tabla.agregarColumna("Estado", f -> f.activa() ? "Activa" : "Inactiva");
        tabla.agregarColumnaAccion("Editar", presentador::alEditar);
        Button nueva = new Button("Nueva categoría", e -> presentador.alEditar(null));
        getContent().add(new H2("Categorías de socio"), nueva, tabla);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-06
     * @return {@code GESTIONAR_CATEGORIAS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_CATEGORIAS;
    }

    /**
     * Pide la carga del listado.
     *
     * <p>Casos de uso: CU-06
     * @param parametros parámetros de la ruta (sin uso)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Formulario abierto, para que {@code marcarCampo} marque el campo del {@code ErrorNegocio}.
     *
     * <p>Casos de uso: CU-06
     * @return el formulario o {@code null}
     */
    @Override
    protected FormularioEntidad<?> formulario() {
        return formulario;
    }

    /**
     * Muestra el listado y cierra el formulario si estaba abierto (se llama tras guardar con éxito).
     *
     * <p>Casos de uso: CU-06
     * @param filas categorías
     */
    @Override
    public void mostrarCategorias(List<FilaCategoria> filas) {
        tabla.mostrar(filas);
        if (dialogo != null) {
            dialogo.close();
            dialogo = null;
            formulario = null;
        }
    }

    /**
     * Abre el formulario de la categoría (vacío o con los datos actuales). Cancelar lo cierra sin llamar al
     * presentador (CU-06 FA1).
     *
     * <p>Casos de uso: CU-06
     * @param datos datos a editar
     * @param idCategoria categoría editada o {@code null} para el alta
     */
    @Override
    public void editar(DatosCategoria datos, Long idCategoria) {
        formulario = new FormularioEntidad<>(DatosCategoria.class);
        formulario.vincular(new TextField("Nombre"), "nombre",
                List.of(new StringLengthValidator("El nombre es obligatorio.", 1, 40)));
        formulario.vincular(new CampoDinero("Importe mensual"), "importe",
                List.of(Validator.<Dinero>from(d -> d != null && !d.esNegativo(),
                        "El importe debe ser un monto mayor o igual a cero.")));
        formulario.vincular(new Checkbox("Activa"), "activa", List.of());
        formulario.alEnviar(d -> presentador.alGuardar(idCategoria, d));
        formulario.cargar(datos);
        dialogo = new Dialog();
        dialogo.setHeaderTitle(idCategoria == null ? "Nueva categoría" : "Modificar categoría");
        dialogo.add(formulario);
        Dialog propio = dialogo;
        dialogo.getFooter().add(new Button("Cancelar", e -> propio.close()));
        dialogo.open();
    }

    /**
     * Campo de formulario de un importe {@code Dinero} sobre un {@code BigDecimalField}, para vincular
     * {@code DatosCategoria.importe} con el {@code Binder}.
     *
     * <p>Casos de uso: CU-06
     */
    private static final class CampoDinero extends CustomField<Dinero> {

        /** Campo numérico interno. */
        private final BigDecimalField monto = new BigDecimalField();

        /**
         * Crea el campo con su etiqueta.
         *
         * <p>Casos de uso: CU-06
         * @param etiqueta etiqueta visible
         */
        CampoDinero(String etiqueta) {
            setLabel(etiqueta);
            monto.setPrefixComponent(new com.vaadin.flow.component.html.Span("$"));
            add(monto);
        }

        /**
         * Valor del campo.
         *
         * <p>Casos de uso: CU-06
         * @return importe o {@code null}
         */
        @Override
        protected Dinero generateModelValue() {
            return monto.getValue() == null ? null : Dinero.de(monto.getValue());
        }

        /**
         * Muestra el importe.
         *
         * <p>Casos de uso: CU-06
         * @param valor importe
         */
        @Override
        protected void setPresentationValue(Dinero valor) {
            monto.setValue(valor == null ? null : valor.getMonto());
        }
    }
}

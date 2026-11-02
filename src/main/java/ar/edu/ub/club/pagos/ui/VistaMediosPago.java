package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormularioEntidad;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.pagos.aplicacion.DatosConfiguracionPagos;
import ar.edu.ub.club.pagos.aplicacion.DatosMedioPago;
import ar.edu.ub.club.pagos.dominio.ModoPasarela;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista de la pantalla P-31 Medios de pago y pasarela (MVP Vista Pasiva, sin lógica): grilla de medios manuales con
 * su casilla «Habilitado», línea de alta de un medio, y el grupo de la pasarela (proveedor, modo, «Aceptar pagos
 * online» y si las credenciales están configuradas; nunca las muestra, RNF-05). El modo y la habilitación se vinculan
 * con {@code FormularioEntidad} sobre {@code DatosConfiguracionPagos}; Guardar llama a {@code
 * presentador.alGuardar(datos)}. Ruta {@code pagos/configuracion}, permiso CONFIGURAR_PAGOS.
 *
 * <p>Casos de uso: CU-34
 */
@Route(value = "pagos/configuracion", layout = DisposicionPrincipal.class)
@PageTitle("Medios de pago y pasarela")
public class VistaMediosPago extends VistaProtegida implements PantallaMediosPago {

    /** Presentador de la pantalla. */
    private final PresentadorMediosPago presentador;

    /** Formulario de la pasarela (modo y habilitación). */
    private final FormularioEntidad<DatosConfiguracionPagos> formulario =
            new FormularioEntidad<>(DatosConfiguracionPagos.class);

    /** Grilla editable de medios. */
    private final Grid<DatosMedioPago> medios = new Grid<>(DatosMedioPago.class, false);

    /** Nombre del medio nuevo. */
    private final TextField nuevoMedio = new TextField("Nuevo medio");

    /** Proveedor de la pasarela (solo lectura). */
    private final Span proveedor = new Span();

    /** Estado de las credenciales (solo lectura). */
    private final Span credenciales = new Span();

    /** Medios en edición (incluye los nuevos sin id). */
    private List<DatosMedioPago> filas = new ArrayList<>();

    /** Datos cargados por última vez (para Cancelar). */
    private DatosConfiguracionPagos datos;

    /**
     * Arma los componentes y vincula el presentador.
     *
     * <p>Casos de uso: CU-34
     * @param presentador presentador de la pantalla
     */
    public VistaMediosPago(PresentadorMediosPago presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        medios.addColumn(DatosMedioPago::getNombre).setHeader("Medio").setAutoWidth(true);
        medios.addComponentColumn(m -> {
            Checkbox habilitado = new Checkbox(m.getHabilitado());
            habilitado.addValueChangeListener(e -> m.setHabilitado(e.getValue()));
            return habilitado;
        }).setHeader("Habilitado");
        medios.setAllRowsVisible(true);
        nuevoMedio.setMaxLength(40);
        Button agregar = new Button("Agregar medio", e -> alAgregarMedio());
        HorizontalLayout alta = new HorizontalLayout(nuevoMedio, agregar);
        alta.setAlignItems(HorizontalLayout.Alignment.BASELINE);
        ComboBox<ModoPasarela> modo = new ComboBox<>("Modo", ModoPasarela.values());
        modo.setItemLabelGenerator(m -> m == ModoPasarela.SIMULADA ? "Simulada (entorno académico)" : "Mercado Pago");
        modo.setRequiredIndicatorVisible(true);
        Checkbox habilitada = new Checkbox("Aceptar pagos online");
        formulario.vincular(modo, "modo", List.of((v, c) -> v == null
                ? com.vaadin.flow.data.binder.ValidationResult.error("Elija el modo")
                : com.vaadin.flow.data.binder.ValidationResult.ok()));
        formulario.vincular(habilitada, "habilitada", List.of());
        formulario.alEnviar(d -> {
            d.setMedios(new ArrayList<>(filas));
            presentador.alGuardar(d);
        });
        Button cancelar = new Button("Cancelar", e -> presentador.cargar());
        getContent().add(new H4("Medios de pago manuales"), medios, alta, new H4("Pasarela de pago online"),
                proveedor, credenciales, formulario, cancelar);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-34
     * @return CONFIGURAR_PAGOS
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONFIGURAR_PAGOS;
    }

    /**
     * Al entrar llama a {@code presentador.cargar()} (pasos 1 y 2).
     *
     * <p>Casos de uso: CU-34
     * @param parametros parámetros de ruta (sin uso)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Carga la grilla de medios, el formulario de la pasarela y los datos de solo lectura.
     *
     * <p>Casos de uso: CU-34
     * @param datos datos editables
     * @param pasarela estado de la pasarela
     */
    @Override
    public void mostrarConfiguracion(DatosConfiguracionPagos datos, ResumenPasarela pasarela) {
        this.datos = datos;
        this.filas = datos.getMedios() == null ? new ArrayList<>() : new ArrayList<>(datos.getMedios());
        medios.setItems(filas);
        formulario.cargar(datos);
        proveedor.setText("Proveedor: " + (pasarela.proveedor() == null ? "" : pasarela.proveedor())
                + (pasarela.habilitada() ? " · habilitada" : " · deshabilitada"));
        credenciales.setText("Credenciales: " + (pasarela.credencialesConfiguradas()
                ? "Configuradas en el servidor" : "No configuradas"));
    }

    /**
     * Marca un campo con error; el nombre del medio nuevo se marca en su caja de texto (FA1).
     *
     * <p>Casos de uso: CU-34
     * @param campo nombre del campo
     * @param mensaje mensaje
     */
    @Override
    public void marcarCampo(String campo, String mensaje) {
        if ("nombre".equals(campo) || "medios".equals(campo)) {
            nuevoMedio.setErrorMessage(mensaje);
            nuevoMedio.setInvalid(true);
        }
        super.marcarCampo(campo, mensaje);
    }

    /**
     * Formulario de la vista, para que {@code marcarCampo} marque sus campos.
     *
     * <p>Casos de uso: CU-34
     * @return el formulario
     */
    @Override
    protected FormularioEntidad<?> formulario() {
        return formulario;
    }

    /**
     * Oyente de Agregar medio: valida el formato del nombre (1 a 40 caracteres) y agrega la fila sin id, habilitada y
     * pendiente de guardar; la unicidad la verifica la fachada al guardar.
     *
     * <p>Casos de uso: CU-34
     */
    private void alAgregarMedio() {
        String nombre = nuevoMedio.getValue() == null ? "" : nuevoMedio.getValue().trim();
        if (new StringLengthValidator("", 1, 40).apply(nombre, null).isError()) {
            nuevoMedio.setErrorMessage("Ingrese un nombre de 1 a 40 caracteres");
            nuevoMedio.setInvalid(true);
            return;
        }
        nuevoMedio.setInvalid(false);
        DatosMedioPago medio = new DatosMedioPago();
        medio.setNombre(nombre);
        medio.setHabilitado(true);
        filas.add(medio);
        medios.setItems(filas);
        nuevoMedio.clear();
    }
}

package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoConfirmacion;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.FormularioEntidad;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.recursos.aplicacion.DatosDeporte;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-15: Listado de deportes con alta, edición y desactivación con advertencia.
 * Patrón MVP (Vista Pasiva): arma los componentes y reenvía cada acción del usuario a {@code PresentadorDeportes};
 * no tiene lógica de negocio. La validación de formato (nombre obligatorio de hasta 40 caracteres, descripción de
 * hasta 200) la hace el {@code Binder} de {@code FormularioEntidad} (CU-18 FA2, CU-19 FA3).
 *
 * <p>Ruta: {@code deportes}. Permiso: {@code GESTIONAR_RECURSOS}.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20
 */
@Route(value = "deportes", layout = DisposicionPrincipal.class)
@PageTitle("Deportes")
public class VistaDeportes extends VistaProtegida implements PantallaDeportes {

    /** Presentador de la pantalla. */
    private final PresentadorDeportes presentador;

    /** Grilla de deportes. */
    private final TablaDatos<FilaDeporte> tabla = new TablaDatos<>(FilaDeporte.class);

    /** Formulario de alta y edición. */
    private final FormularioEntidad<DatosDeporte> formulario = new FormularioEntidad<>(DatosDeporte.class);

    /** Panel que contiene el formulario (oculto mientras no se edita). */
    private final Div panelEdicion = new Div();

    /** Título del panel («Nuevo deporte» o «Modificar deporte»). */
    private final H3 tituloPanel = new H3();

    /** Casilla para reactivar un deporte inactivo (CU-19 FA1); solo visible en ese caso. */
    private final Checkbox activo = new Checkbox("Reactivar el deporte");

    /** Deporte que se está editando (null en el alta). */
    private Long idEnEdicion;

    /**
     * Crea la vista, se vincula con el presentador y arma los componentes.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param presentador presentador de P-15 (inyectado por Spring)
     */
    public VistaDeportes(PresentadorDeportes presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        construir();
    }

    /**
     * Arma la grilla con sus acciones por fila, el botón «Nuevo deporte» y el panel de edición.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     */
    private void construir() {
        Button nuevo = new Button("Nuevo deporte", e -> presentador.alEditar(null));
        nuevo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        tabla.agregarColumna("Nombre", FilaDeporte::nombre);
        tabla.agregarColumna("Descripción", f -> f.descripcion() == null ? "" : f.descripcion());
        tabla.agregarColumna("Canchas", FilaDeporte::cantidadCanchas);
        tabla.agregarColumna("Estado", f -> f.activo() ? "Activo" : "Inactivo");
        tabla.agregarColumnaAccion("Modificar", presentador::alEditar);
        tabla.agregarColumnaAccion("Desactivar", f -> presentador.alDesactivar(f.id()));

        TextField nombre = new TextField("Nombre");
        nombre.setMaxLength(40);
        nombre.setRequiredIndicatorVisible(true);
        TextArea descripcion = new TextArea("Descripción");
        descripcion.setMaxLength(200);
        formulario.vincular(nombre, "nombre",
                List.of(new StringLengthValidator("Ingrese un nombre de hasta 40 caracteres.", 1, 40)));
        formulario.vincular(descripcion, "descripcion",
                List.of(new StringLengthValidator("La descripción admite hasta 200 caracteres.", 0, 200)));
        formulario.vincular(activo, "activo", List.of());
        formulario.alEnviar(datos -> presentador.alGuardar(idEnEdicion, datos));
        Button cancelar = new Button("Cancelar", e -> cerrarEdicion());

        panelEdicion.add(tituloPanel, formulario, cancelar);
        panelEdicion.setVisible(false);
        getContent().add(new H2("Deportes"), nuevo, panelEdicion, tabla);
    }

    /**
     * Cierra el panel de edición sin guardar (CU-18 FA1, CU-19 FA2).
     *
     * <p>Casos de uso: CU-18, CU-19
     */
    private void cerrarEdicion() {
        panelEdicion.setVisible(false);
        idEnEdicion = null;
    }

    /**
     * Permiso que exige la ruta.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @return {@code GESTIONAR_RECURSOS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_RECURSOS;
    }

    /**
     * Al entrar a la ruta pide al presentador cargar la grilla (CU-18 paso 1).
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param parametros parámetros de ruta (no usa)
     * @param consulta parámetros de consulta (no usa)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Formulario de la vista, para que {@code marcarCampo} marque el campo del {@code ErrorNegocio}.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @return formulario de deporte
     */
    @Override
    protected FormularioEntidad<?> formulario() {
        return formulario;
    }

    /**
     * Muestra la grilla y cierra el panel de edición.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param filas filas de la grilla
     */
    @Override
    public void mostrarDeportes(List<FilaDeporte> filas) {
        tabla.mostrar(filas);
        cerrarEdicion();
    }

    /**
     * Abre el panel con los datos precargados; la casilla de reactivación solo aparece para un deporte inactivo.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param datos datos del formulario
     * @param idDeporte deporte que se modifica (null en el alta)
     */
    @Override
    public void editar(DatosDeporte datos, Long idDeporte) {
        this.idEnEdicion = idDeporte;
        tituloPanel.setText(idDeporte == null ? "Nuevo deporte" : "Modificar deporte");
        formulario.cargar(datos);
        activo.setVisible(idDeporte != null && !datos.getActivo());
        panelEdicion.setVisible(true);
    }

    /**
     * Abre el {@code DialogoConfirmacion} con las reservas que se cancelarán (sin lista si no hay, FA2); al
     * confirmar llama a {@code alConfirmarDesactivacion}; al cancelar no hace nada (FA3).
     *
     * <p>Casos de uso: CU-20
     * @param idDeporte deporte a desactivar
     * @param afectadas reservas que se cancelarán
     */
    @Override
    public void pedirConfirmacionBaja(Long idDeporte, List<FilaResumenReserva> afectadas) {
        String mensaje = afectadas.isEmpty()
                ? "No hay reservas futuras en canchas de este deporte. ¿Confirma la desactivación?"
                : "Se cancelarán " + afectadas.size() + " reservas futuras con el motivo «Baja de deporte» y se avisará a cada socio:";
        DialogoConfirmacion dialogo = new DialogoConfirmacion("Desactivar deporte", mensaje);
        dialogo.mostrarAdvertencias(afectadas.stream().map(FilaResumenReserva::comoTexto).toList());
        dialogo.alConfirmar(motivo -> presentador.alConfirmarDesactivacion(idDeporte));
        dialogo.abrir();
    }
}

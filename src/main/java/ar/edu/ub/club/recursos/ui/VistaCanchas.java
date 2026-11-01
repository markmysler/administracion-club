package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoConfirmacion;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.FormularioEntidad;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.recursos.aplicacion.DatosCancha;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Vista de la pantalla P-16: Listado de canchas por deporte con alta, edición y desactivación con advertencia.
 * Patrón MVP (Vista Pasiva): arma los componentes y reenvía cada acción a {@code PresentadorCanchas}; no tiene
 * lógica de negocio. Los combos de deporte son {@code ComboBox<Long>} (el valor es el id del deporte y la etiqueta
 * su nombre), así el {@code Binder} vincula directamente {@code DatosCancha.idDeporte}.
 *
 * <p>Ruta: {@code canchas}. Permiso: {@code GESTIONAR_RECURSOS}.
 *
 * <p>Casos de uso: CU-21, CU-22, CU-23
 */
@Route(value = "canchas", layout = DisposicionPrincipal.class)
@PageTitle("Canchas")
public class VistaCanchas extends VistaProtegida implements PantallaCanchas {

    /** Presentador de la pantalla. */
    private final PresentadorCanchas presentador;

    /** Grilla de canchas. */
    private final TablaDatos<FilaCancha> tabla = new TablaDatos<>(FilaCancha.class);

    /** Formulario de alta y edición. */
    private final FormularioEntidad<DatosCancha> formulario = new FormularioEntidad<>(DatosCancha.class);

    /** Filtro de la grilla por deporte (vacío = todos). */
    private final ComboBox<Long> filtroDeporte = new ComboBox<>("Deporte");

    /** Combo de deporte del formulario (solo deportes activos). */
    private final ComboBox<Long> deporte = new ComboBox<>("Deporte");

    /** Casilla para reactivar una cancha inactiva (CU-22 FA1); solo visible en ese caso. */
    private final Checkbox activa = new Checkbox("Reactivar la cancha");

    /** Panel que contiene el formulario (oculto mientras no se edita). */
    private final Div panelEdicion = new Div();

    /** Título del panel. */
    private final H3 tituloPanel = new H3();

    /** Nombres de los deportes mostrados, por id (etiquetas de los combos). */
    private final Map<Long, String> nombresDeportes = new LinkedHashMap<>();

    /** Cancha que se está editando (null en el alta). */
    private Long idEnEdicion;

    /** Datos enviados por última vez, para reenviarlos con {@code confirmar = true} (CU-22 FA6). */
    private DatosCancha datosEnviados;

    /** true mientras la vista actualiza los combos (evita que el filtro dispare una recarga). */
    private boolean actualizandoCombos;

    /**
     * Crea la vista, se vincula con el presentador y arma los componentes.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param presentador presentador de P-16 (inyectado por Spring)
     */
    public VistaCanchas(PresentadorCanchas presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        construir();
    }

    /**
     * Arma el filtro, la grilla con sus acciones por fila, el botón «Nueva cancha» y el panel de edición.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     */
    private void construir() {
        filtroDeporte.setClearButtonVisible(true);
        filtroDeporte.setPlaceholder("Todos");
        filtroDeporte.setItemLabelGenerator(this::etiquetaDeporte);
        filtroDeporte.addValueChangeListener(e -> {
            if (!actualizandoCombos) {
                presentador.cargar(e.getValue());
            }
        });
        Button nueva = new Button("Nueva cancha", e -> presentador.alEditar(null));
        nueva.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        tabla.agregarColumna("Nombre", FilaCancha::nombre);
        tabla.agregarColumna("Deporte", FilaCancha::deporte);
        tabla.agregarColumna("Ubicación", f -> f.ubicacion() == null ? "" : f.ubicacion());
        tabla.agregarColumna("Características", f -> f.caracteristicas() == null ? "" : f.caracteristicas());
        tabla.agregarColumna("Estado", f -> f.activa() ? "Activa" : "Inactiva");
        tabla.agregarColumnaAccion("Modificar", presentador::alEditar);
        tabla.agregarColumnaAccion("Desactivar", f -> presentador.alDesactivar(f.id()));

        TextField nombre = new TextField("Nombre");
        nombre.setMaxLength(40);
        nombre.setRequiredIndicatorVisible(true);
        deporte.setItemLabelGenerator(this::etiquetaDeporte);
        deporte.setRequiredIndicatorVisible(true);
        TextField ubicacion = new TextField("Ubicación");
        ubicacion.setMaxLength(100);
        TextField caracteristicas = new TextField("Características");
        caracteristicas.setMaxLength(200);
        formulario.vincular(nombre, "nombre",
                List.of(new StringLengthValidator("Ingrese un nombre de hasta 40 caracteres.", 1, 40)));
        formulario.vincular(deporte, "idDeporte",
                List.of(Validator.<Long>from(v -> v != null, "Elija el deporte de la cancha.")));
        formulario.vincular(ubicacion, "ubicacion",
                List.of(new StringLengthValidator("Admite hasta 100 caracteres.", 0, 100)));
        formulario.vincular(caracteristicas, "caracteristicas",
                List.of(new StringLengthValidator("Admite hasta 200 caracteres.", 0, 200)));
        formulario.vincular(activa, "activa", List.of());
        formulario.alEnviar(this::enviar);
        Button cancelar = new Button("Cancelar", e -> cerrarEdicion());

        panelEdicion.add(tituloPanel, formulario, cancelar);
        panelEdicion.setVisible(false);
        getContent().add(new H2("Canchas"), new HorizontalLayout(filtroDeporte, nueva), panelEdicion, tabla);
    }

    /**
     * Envía el formulario válido al presentador con {@code confirmar = false} (primer paso del protocolo de
     * confirmación) y recuerda los datos por si hay que reenviarlos.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param datos datos leídos del formulario
     */
    private void enviar(DatosCancha datos) {
        datos.setConfirmar(false);
        datosEnviados = datos;
        presentador.alGuardar(idEnEdicion, datos);
    }

    /**
     * Etiqueta de un deporte en los combos (su nombre).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param id identificador del deporte
     * @return nombre, o un texto genérico si el deporte no está entre los activos
     */
    private String etiquetaDeporte(Long id) {
        return nombresDeportes.getOrDefault(id, "Deporte inactivo");
    }

    /**
     * Cierra el panel de edición sin guardar (CU-21 FA2, CU-22 FA2).
     *
     * <p>Casos de uso: CU-21, CU-22
     */
    private void cerrarEdicion() {
        panelEdicion.setVisible(false);
        idEnEdicion = null;
        datosEnviados = null;
    }

    /**
     * Permiso que exige la ruta.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @return {@code GESTIONAR_RECURSOS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_RECURSOS;
    }

    /**
     * Al entrar a la ruta pide al presentador cargar todas las canchas y los deportes activos.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param parametros parámetros de ruta (no usa)
     * @param consulta parámetros de consulta (no usa)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar(null);
    }

    /**
     * Formulario de la vista, para que {@code marcarCampo} marque el campo del {@code ErrorNegocio}.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return formulario de cancha
     */
    @Override
    protected FormularioEntidad<?> formulario() {
        return formulario;
    }

    /**
     * Carga los deportes activos en ambos combos conservando el valor elegido.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param opciones deportes activos
     */
    @Override
    public void mostrarDeportes(List<OpcionSeleccion> opciones) {
        actualizandoCombos = true;
        try {
            Long filtro = filtroDeporte.getValue();
            Long elegido = deporte.getValue();
            nombresDeportes.clear();
            opciones.forEach(o -> nombresDeportes.put(o.id(), o.texto()));
            List<Long> ids = List.copyOf(nombresDeportes.keySet());
            filtroDeporte.setItems(ids);
            deporte.setItems(ids);
            filtroDeporte.setValue(filtro != null && ids.contains(filtro) ? filtro : null);
            deporte.setValue(elegido != null && ids.contains(elegido) ? elegido : null);
        } finally {
            actualizandoCombos = false;
        }
    }

    /**
     * Muestra la grilla y cierra el panel de edición.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param filas filas de la grilla
     */
    @Override
    public void mostrarCanchas(List<FilaCancha> filas) {
        tabla.mostrar(filas);
        cerrarEdicion();
    }

    /**
     * Abre el panel con los datos precargados; la casilla de reactivación solo aparece para una cancha inactiva.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param datos datos del formulario
     * @param idCancha cancha que se modifica (null en el alta)
     */
    @Override
    public void editar(DatosCancha datos, Long idCancha) {
        this.idEnEdicion = idCancha;
        tituloPanel.setText(idCancha == null ? "Nueva cancha" : "Modificar cancha");
        formulario.cargar(datos);
        activa.setVisible(idCancha != null && !datos.getActiva());
        panelEdicion.setVisible(true);
    }

    /**
     * Abre el {@code DialogoConfirmacion} con las reservas que se cancelarán (sin lista si no hay, FA2); al
     * confirmar llama a {@code alConfirmarDesactivacion}; al cancelar no hace nada (FA3).
     *
     * <p>Casos de uso: CU-23
     * @param idCancha cancha a desactivar
     * @param afectadas reservas que se cancelarán
     */
    @Override
    public void pedirConfirmacionBaja(Long idCancha, List<FilaResumenReserva> afectadas) {
        String mensaje = afectadas.isEmpty()
                ? "La cancha no tiene reservas futuras. ¿Confirma la desactivación?"
                : "Se cancelarán " + afectadas.size() + " reservas futuras con el motivo «Baja de cancha» y se avisará a cada socio:";
        DialogoConfirmacion dialogo = new DialogoConfirmacion("Desactivar cancha", mensaje);
        dialogo.mostrarAdvertencias(afectadas.stream().map(FilaResumenReserva::comoTexto).toList());
        dialogo.alConfirmar(motivo -> presentador.alConfirmarDesactivacion(idCancha));
        dialogo.abrir();
    }

    /**
     * Advierte las reservas futuras de la cancha, que se conservan (CU-22 FA6, 7.1); al confirmar reenvía el
     * guardado con {@code datos.confirmar = true} (7.2).
     *
     * <p>Casos de uso: CU-22
     * @param afectadas reservas futuras de la cancha
     */
    @Override
    public void pedirConfirmacionCambio(List<FilaResumenReserva> afectadas) {
        DialogoConfirmacion dialogo = new DialogoConfirmacion("Cambiar el deporte de la cancha",
                "La cancha tiene " + afectadas.size() + " reservas futuras, que se conservan. ¿Confirma el cambio de deporte?");
        dialogo.mostrarAdvertencias(afectadas.stream().map(FilaResumenReserva::comoTexto).toList());
        Long id = idEnEdicion;
        DatosCancha datos = datosEnviados;
        dialogo.alConfirmar(motivo -> {
            if (datos != null) {
                datos.setConfirmar(true);
                presentador.alGuardar(id, datos);
            }
        });
        dialogo.abrir();
    }
}

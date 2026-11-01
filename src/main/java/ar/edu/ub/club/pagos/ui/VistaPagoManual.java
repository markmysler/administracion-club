package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoConfirmacion;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Vista de la pantalla P-30 Pago manual (MVP Vista Pasiva, sin lógica): búsqueda del grupo, grilla de grupos, grilla
 * de cuotas impagas, combo de medios, importe a pagar (no editable) y confirmación con {@code DialogoConfirmacion}. Sus
 * oyentes solo llaman a métodos {@code al…} de {@code PresentadorPagoManual}. Ruta {@code pagos/manual}, permiso
 * REGISTRAR_PAGO_MANUAL.
 *
 * <p>Casos de uso: CU-36
 */
@Route(value = "pagos/manual", layout = DisposicionPrincipal.class)
@PageTitle("Pago manual")
public class VistaPagoManual extends VistaProtegida implements PantallaPagoManual {

    /** Presentador de la pantalla. */
    private final PresentadorPagoManual presentador;

    /** Texto de búsqueda del grupo (DNI o nombre). */
    private final TextField buscar = new TextField("Buscar grupo");

    /** Grilla de grupos encontrados. */
    private final TablaDatos<FilaGrupo> grupos = new TablaDatos<>(FilaGrupo.class);

    /** Grilla de cuotas impagas. */
    private final TablaDatos<FilaCuota> cuotas = new TablaDatos<>(FilaCuota.class);

    /** Combo de medios habilitados. */
    private final ComboBox<OpcionSeleccion> medio = new ComboBox<>("Medio de pago");

    /** Importe a pagar de la cuota elegida (no editable). */
    private final Span importe = new Span();

    /** Botón Registrar pago (habilitado al elegir una cuota). */
    private final Button registrar = new Button("Registrar pago");

    /** Enlace a P-31 cuando no hay medios habilitados (FA3). */
    private final Anchor configurarMedios = new Anchor("pagos/configuracion", "Configurar medios de pago");

    /** Grupo elegido. */
    private FilaGrupo grupoElegido;

    /** Cuota elegida. */
    private FilaCuota cuotaElegida;

    /**
     * Arma los componentes y vincula el presentador.
     *
     * <p>Casos de uso: CU-36
     * @param presentador presentador de la pantalla
     */
    public VistaPagoManual(PresentadorPagoManual presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        buscar.setMinLength(2);
        buscar.setMaxLength(60);
        Button botonBuscar = new Button("Buscar", e -> presentador.alBuscarGrupo(buscar.getValue()));
        grupos.agregarColumna("Grupo", FilaGrupo::idGrupo);
        grupos.agregarColumna("Titular", FilaGrupo::titular);
        grupos.agregarColumna("DNI del titular", FilaGrupo::dniTitular);
        grupos.agregarColumna("Integrantes", FilaGrupo::cantidadIntegrantes);
        grupos.alSeleccionar(fila -> {
            grupoElegido = fila;
            presentador.alElegirGrupo(fila.idGrupo());
        });
        cuotas.agregarColumna("Período", FilaCuota::periodo);
        cuotas.agregarColumna("Importe", c -> FormatoUI.dinero(c.importe()));
        cuotas.agregarColumna("Crédito aplicado", c -> FormatoUI.dinero(c.creditoAplicado()));
        cuotas.agregarColumna("Importe a pagar", c -> FormatoUI.dinero(c.importeAPagar()));
        cuotas.agregarColumna("Vencimiento", c -> FormatoUI.fecha(c.fechaVencimiento()));
        cuotas.agregarColumna("Estado", FilaCuota::estado);
        cuotas.alSeleccionar(fila -> {
            cuotaElegida = fila;
            importe.setText("Importe a pagar: " + FormatoUI.dinero(fila.importeAPagar()));
            registrar.setEnabled(true);
        });
        registrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registrar.setEnabled(false);
        registrar.addClickListener(e -> confirmar());
        Button cancelar = new Button("Cancelar", e -> limpiar());
        configurarMedios.setVisible(false);
        HorizontalLayout busqueda = new HorizontalLayout(buscar, botonBuscar);
        busqueda.setAlignItems(HorizontalLayout.Alignment.BASELINE);
        HorizontalLayout pago = new HorizontalLayout(medio, importe, registrar, cancelar);
        pago.setAlignItems(HorizontalLayout.Alignment.BASELINE);
        getContent().add(busqueda, grupos, new H4("Cuotas impagas del grupo"), cuotas, pago, configurarMedios);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-36
     * @return REGISTRAR_PAGO_MANUAL
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.REGISTRAR_PAGO_MANUAL;
    }

    /**
     * Al entrar llama a {@code presentador.cargar()} (medios habilitados).
     *
     * <p>Casos de uso: CU-36
     * @param parametros parámetros de ruta (sin uso)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Carga el combo de medios; sin medios deshabilita el registro y muestra el enlace a P-31 (FA3).
     *
     * <p>Casos de uso: CU-36
     * @param opciones medios habilitados
     */
    @Override
    public void mostrarMedios(List<OpcionSeleccion> opciones) {
        medio.setItems(opciones);
        medio.setEnabled(!opciones.isEmpty());
        configurarMedios.setVisible(opciones.isEmpty());
    }

    /**
     * Muestra los grupos encontrados.
     *
     * <p>Casos de uso: CU-36
     * @param filas grupos
     */
    @Override
    public void mostrarGrupos(List<FilaGrupo> filas) {
        grupos.mostrar(filas);
    }

    /**
     * Muestra las cuotas impagas y limpia la cuota elegida.
     *
     * <p>Casos de uso: CU-36
     * @param filas cuotas
     */
    @Override
    public void mostrarCuotas(List<FilaCuota> filas) {
        cuotas.mostrar(filas);
        cuotaElegida = null;
        importe.setText("");
        registrar.setEnabled(false);
    }

    /**
     * Oyente de Registrar pago: abre el {@code DialogoConfirmacion} con grupo, cuota, medio, importe y fecha y, al
     * confirmar, llama a {@code presentador.alRegistrar(idCuota, idMedio, importeAPagar)}.
     *
     * <p>Casos de uso: CU-36
     */
    private void confirmar() {
        if (cuotaElegida == null) {
            return;
        }
        if (medio.getValue() == null) {
            medio.setErrorMessage("Elija el medio de pago");
            medio.setInvalid(true);
            return;
        }
        medio.setInvalid(false);
        FilaCuota cuota = cuotaElegida;
        OpcionSeleccion elegido = medio.getValue();
        DialogoConfirmacion dialogo = new DialogoConfirmacion("Registrar pago manual",
                "¿Confirma el registro del pago?");
        dialogo.mostrarAdvertencias(List.of(
                "Grupo: " + (grupoElegido == null ? "" : grupoElegido.idGrupo() + " · " + grupoElegido.titular()),
                "Cuota: " + cuota.periodo(),
                "Medio: " + elegido.texto(),
                "Importe: " + FormatoUI.dinero(cuota.importeAPagar()),
                "Fecha: " + FormatoUI.fechaHora(LocalDateTime.now())));
        dialogo.alConfirmar(m -> presentador.alRegistrar(cuota.id(), elegido.id(), cuota.importeAPagar()));
        dialogo.abrir();
    }

    /**
     * Oyente de Cancelar: limpia la selección sin registrar (FA4).
     *
     * <p>Casos de uso: CU-36
     */
    private void limpiar() {
        cuotaElegida = null;
        importe.setText("");
        registrar.setEnabled(false);
        medio.clear();
    }
}

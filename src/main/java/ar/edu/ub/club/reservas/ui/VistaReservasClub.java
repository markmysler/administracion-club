package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoConfirmacion;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FiltroFechas;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.reservas.aplicacion.FiltroReservas;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista de la pantalla P-22 (MVP, Vista Pasiva): reservas del club con filtros (fechas, deporte, socio y estado),
 * detalle con notificaciones, acceso a modificar (P-23) y cancelación administrativa con motivo obligatorio
 * ({@code DialogoConfirmacion.pedirMotivo(true)}). No tiene lógica: cada oyente llama a un método del presentador.
 *
 * <p>Ruta: {@code reservas-club}. Permiso: {@code GESTIONAR_RESERVAS_CLUB}.
 *
 * <p>Casos de uso: CU-31, CU-32, CU-33
 */
@Route(value = "reservas-club", layout = DisposicionPrincipal.class)
@PageTitle("Reservas del club")
public class VistaReservasClub extends VistaProtegida implements PantallaReservasClub {

    /** Presentador de P-22. */
    private final PresentadorReservasClub presentador;

    /** Rango de fechas. */
    private final FiltroFechas fechas = new FiltroFechas();

    /** Filtro de deporte. */
    private final ComboBox<OpcionSeleccion> deporte = new ComboBox<>("Deporte");

    /** Filtro de socio (DNI o apellido). */
    private final TextField socio = new TextField("Socio");

    /** Filtro de estado. */
    private final ComboBox<String> estado = new ComboBox<>("Estado");

    /** Reservas del club. */
    private final TablaDatos<FilaReserva> reservas = new TablaDatos<>(FilaReserva.class);

    /** Panel del detalle de la reserva elegida. */
    private final VerticalLayout detalle = new VerticalLayout();

    /**
     * Arma la vista y vincula el presentador.
     *
     * <p>Casos de uso: CU-31, CU-32, CU-33
     * @param presentador presentador de P-22
     */
    public VistaReservasClub(PresentadorReservasClub presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        deporte.setItemLabelGenerator(OpcionSeleccion::texto);
        deporte.setClearButtonVisible(true);
        estado.setItems("CONFIRMADA", "CANCELADA", "FINALIZADA");
        estado.setClearButtonVisible(true);
        Button buscar = new Button("Buscar", e -> buscar());
        buscar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout filtros = new HorizontalLayout(fechas, deporte, socio, estado, buscar);
        filtros.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        filtros.setWrap(true);
        reservas.agregarColumna("Socio", FilaReserva::socio);
        reservas.agregarColumna("DNI", FilaReserva::dniSocio);
        reservas.agregarColumna("Cancha", FilaReserva::cancha);
        reservas.agregarColumna("Fecha", f -> FormatoUI.fecha(f.fecha()));
        reservas.agregarColumna("Horario", f -> FormatoUI.hora(f.horaInicio()) + " a " + FormatoUI.hora(f.horaFin()));
        reservas.agregarColumna("Estado", FilaReserva::estado);
        reservas.agregarColumnaAccion("Detalle", f -> presentador.alSeleccionar(f.id()));
        reservas.agregarColumnaAccion("Modificar", f -> presentador.alModificar(f.id()));
        reservas.agregarColumnaAccion("Cancelar", f -> pedirMotivoCancelacion(f.id()));
        detalle.setPadding(false);
        getContent().add(new H2("Reservas del club"), filtros, reservas, detalle);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-31
     * @return {@code GESTIONAR_RESERVAS_CLUB}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_RESERVAS_CLUB;
    }

    /**
     * Gancho de {@code VistaProtegida}: carga las reservas del día (CU-31 paso 2).
     *
     * <p>Casos de uso: CU-31
     * @param parametros parámetros de la ruta (no se usan)
     * @param consulta parámetros de consulta (no se usan)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        LocalDate hoy = LocalDate.now();
        fechas.fijar(hoy, hoy);
        FiltroReservas delDia = new FiltroReservas();
        delDia.setDesde(hoy);
        delDia.setHasta(hoy);
        presentador.cargar(delDia);
    }

    /**
     * Muestra el filtro de deporte.
     *
     * <p>Casos de uso: CU-31
     * @param deportes deportes
     */
    @Override
    public void mostrarFiltros(List<OpcionSeleccion> deportes) {
        deporte.setItems(deportes);
    }

    /**
     * Muestra las reservas con carga perezosa por páginas ({@code TablaDatos.mostrarPaginado}): la grilla pide a
     * Vaadin solo las filas visibles (con el orden de la columna elegida) y limpia el detalle.
     *
     * <p>Casos de uso: CU-31, CU-33
     * @param filas reservas
     */
    @Override
    public void mostrarReservas(List<FilaReserva> filas) {
        reservas.mostrarPaginado(new CallbackDataProvider<FilaReserva, Void>(
                consulta -> {
                    java.util.stream.Stream<FilaReserva> datos = filas.stream();
                    if (consulta.getSortingComparator().isPresent()) {
                        datos = datos.sorted(consulta.getSortingComparator().get());
                    }
                    return datos.skip(consulta.getOffset()).limit(consulta.getLimit());
                },
                consulta -> filas.size()));
        detalle.removeAll();
    }

    /**
     * Muestra quién registró o canceló, el motivo y el estado de las notificaciones; si está confirmada, ofrece
     * Modificar (CU-32) y Cancelar (CU-33).
     *
     * <p>Casos de uso: CU-31
     * @param d detalle de la reserva
     */
    @Override
    public void mostrarDetalle(DetalleReserva d) {
        detalle.removeAll();
        detalle.add(new H3("Reserva n.º " + d.id() + " · " + d.socio() + " (DNI " + d.dniSocio() + ")"));
        detalle.add(new Paragraph(d.cancha() + " (" + d.deporte() + ") · " + FormatoUI.fecha(d.fecha()) + " "
                + FormatoUI.hora(d.horaInicio()) + " a " + FormatoUI.hora(d.horaFin()) + " · Estado: " + d.estado()));
        detalle.add(new Paragraph("Registrada por " + d.registradaPor() + " el " + FormatoUI.fechaHora(d.fechaCreacion())));
        if (d.canceladaPor() != null) {
            detalle.add(new Paragraph("Cancelada por " + d.canceladaPor() + ". Motivo: " + d.motivoCancelacion()));
        }
        for (FilaNotificacion n : d.notificaciones()) {
            detalle.add(new Paragraph("Aviso " + n.tipo() + " del " + FormatoUI.fechaHora(n.fecha()) + ": "
                    + n.estadoEnvio() + (n.detalle() == null ? "" : " (" + n.detalle() + ")")));
        }
        if ("CONFIRMADA".equals(d.estado())) {
            Button modificar = new Button("Modificar", e -> presentador.alModificar(d.id()));
            Button cancelar = new Button("Cancelar", e -> pedirMotivoCancelacion(d.id()));
            cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
            detalle.add(new HorizontalLayout(modificar, cancelar));
        }
    }

    /**
     * Abre {@code DialogoConfirmacion} con motivo obligatorio; al confirmar llama a {@code
     * presentador.alCancelar(idReserva, motivo)} (sec_cu33_p1).
     *
     * <p>Casos de uso: CU-33
     * @param idReserva reserva a cancelar
     */
    @Override
    public void pedirMotivoCancelacion(Long idReserva) {
        DialogoConfirmacion dialogo = new DialogoConfirmacion("Cancelar reserva",
                "Indique el motivo de la cancelación; se notificará al socio.");
        dialogo.pedirMotivo(true);
        dialogo.alConfirmar(motivo -> presentador.alCancelar(idReserva, motivo));
        dialogo.abrir();
    }

    /**
     * Si el rango de fechas es válido arma el filtro y llama a {@code presentador.cargar}.
     *
     * <p>Casos de uso: CU-31
     */
    private void buscar() {
        fechas.valor().ifPresent(rango -> {
            FiltroReservas filtro = new FiltroReservas();
            filtro.setDesde(rango.desde());
            filtro.setHasta(rango.hasta());
            filtro.setIdDeporte(deporte.getValue() == null ? null : deporte.getValue().id());
            filtro.setTextoSocio(socio.getValue());
            filtro.setEstado(estado.getValue());
            presentador.cargar(filtro);
        });
    }
}

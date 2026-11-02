package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoConfirmacion;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FiltroFechas;
import ar.edu.ub.club.compartido.ui.FormatoUI;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-21 (MVP, Vista Pasiva): reservas propias vigentes e históricas, detalle con sus
 * notificaciones y acción de cancelar con confirmación ({@code DialogoConfirmacion}, RN-10). No tiene lógica: cada
 * oyente llama a un método del presentador; el rango de fechas lo valida {@code FiltroFechas} (CU-29 FA2).
 *
 * <p>Ruta: {@code mis-reservas}. Permiso: {@code GESTIONAR_RESERVAS_PROPIAS}.
 *
 * <p>Casos de uso: CU-29, CU-30
 */
@Route(value = "mis-reservas", layout = DisposicionPrincipal.class)
@PageTitle("Mis reservas")
public class VistaMisReservas extends VistaProtegida implements PantallaMisReservas {

    /** Presentador de P-21. */
    private final PresentadorMisReservas presentador;

    /** Rango de fechas. */
    private final FiltroFechas fechas = new FiltroFechas();

    /** Filtro de estado. */
    private final ComboBox<String> estado = new ComboBox<>("Estado");

    /** Reservas del socio. */
    private final TablaDatos<FilaReserva> reservas = new TablaDatos<>(FilaReserva.class);

    /** Panel del detalle de la reserva elegida. */
    private final VerticalLayout detalle = new VerticalLayout();

    /**
     * Arma la vista y vincula el presentador.
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param presentador presentador de P-21
     */
    public VistaMisReservas(PresentadorMisReservas presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        estado.setItems("CONFIRMADA", "CANCELADA", "FINALIZADA");
        estado.setClearButtonVisible(true);
        Button buscar = new Button("Buscar", e -> buscar());
        buscar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout filtros = new HorizontalLayout(fechas, estado, buscar);
        filtros.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        filtros.setWrap(true);
        reservas.agregarColumna("Fecha", f -> FormatoUI.fecha(f.fecha()));
        reservas.agregarColumna("Horario", f -> FormatoUI.hora(f.horaInicio()) + " a " + FormatoUI.hora(f.horaFin()));
        reservas.agregarColumna("Cancha", FilaReserva::cancha);
        reservas.agregarColumna("Deporte", FilaReserva::deporte);
        reservas.agregarColumna("Estado", FilaReserva::estado);
        reservas.agregarColumnaAccion("Ver detalle", f -> presentador.alVerDetalle(f.id()));
        detalle.setPadding(false);
        getContent().add(new H2("Mis reservas"), filtros, reservas, detalle);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-29
     * @return {@code GESTIONAR_RESERVAS_PROPIAS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_RESERVAS_PROPIAS;
    }

    /**
     * Gancho de {@code VistaProtegida}: carga todas las reservas del socio (paso 1).
     *
     * <p>Casos de uso: CU-29
     * @param parametros parámetros de la ruta (no se usan)
     * @param consulta parámetros de consulta (no se usan)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar(new FiltroReservas());
    }

    /**
     * Muestra las reservas y limpia el detalle.
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param filas reservas
     */
    @Override
    public void mostrarReservas(List<FilaReserva> filas) {
        reservas.mostrar(filas);
        detalle.removeAll();
    }

    /**
     * Muestra el detalle con sus notificaciones y, si está confirmada, la opción Cancelar (CU-30).
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param d detalle de la reserva
     */
    @Override
    public void mostrarDetalle(DetalleReserva d) {
        detalle.removeAll();
        detalle.add(new H3("Reserva del " + FormatoUI.fecha(d.fecha()) + " " + FormatoUI.hora(d.horaInicio())
                + " a " + FormatoUI.hora(d.horaFin())));
        detalle.add(new Paragraph(d.cancha() + " (" + d.deporte() + ") · Estado: " + d.estado()));
        if (d.motivoCancelacion() != null) {
            detalle.add(new Paragraph("Cancelada por " + d.canceladaPor() + ": " + d.motivoCancelacion()));
        }
        for (FilaNotificacion n : d.notificaciones()) {
            detalle.add(new Paragraph("Aviso " + n.tipo() + " del " + FormatoUI.fechaHora(n.fecha()) + ": "
                    + n.estadoEnvio() + (n.detalle() == null ? "" : " (" + n.detalle() + ")")));
        }
        if ("CONFIRMADA".equals(d.estado())) {
            String plazo = d.plazoCancelacionHoras() == null ? ""
                    : " Puede cancelar hasta " + d.plazoCancelacionHoras() + " horas antes del turno.";
            Button cancelar = new Button("Cancelar reserva", e -> confirmarCancelacion(d));
            cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
            detalle.add(new Paragraph(plazo.trim()), cancelar);
        }
    }

    /**
     * Pide la confirmación de la cancelación (RN-10) y, si se confirma, llama a {@code presentador.alCancelar}.
     *
     * <p>Casos de uso: CU-30
     * @param d reserva a cancelar
     */
    private void confirmarCancelacion(DetalleReserva d) {
        DialogoConfirmacion dialogo = new DialogoConfirmacion("Cancelar reserva",
                "¿Confirma la cancelación de la reserva del " + FormatoUI.fecha(d.fecha()) + " a las "
                        + FormatoUI.hora(d.horaInicio()) + " en " + d.cancha() + "?");
        dialogo.alConfirmar(motivo -> presentador.alCancelar(d.id()));
        dialogo.abrir();
    }

    /**
     * Si el rango de fechas es válido arma el filtro y llama a {@code presentador.cargar}.
     *
     * <p>Casos de uso: CU-29
     */
    private void buscar() {
        fechas.valor().ifPresent(rango -> {
            FiltroReservas filtro = new FiltroReservas();
            filtro.setDesde(rango.desde());
            filtro.setHasta(rango.hasta());
            filtro.setEstado(estado.getValue());
            presentador.cargar(filtro);
        });
    }
}

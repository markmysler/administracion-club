package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.agenda.aplicacion.FiltroDisponibilidad;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-23 (MVP, Vista Pasiva): datos actuales de la reserva, consulta de turnos por fecha y
 * cancha, elección del nuevo turno y guardado. No tiene lógica: los oyentes llaman a {@code cargar}, {@code
 * alConsultarTurnos} y {@code alGuardar} del presentador; el turno elegido es solo estado de la vista.
 *
 * <p>Ruta: {@code reservas-club/modificar/:id}. Permiso: {@code GESTIONAR_RESERVAS_CLUB}.
 *
 * <p>Casos de uso: CU-32
 */
@Route(value = "reservas-club/modificar/:id", layout = DisposicionPrincipal.class)
@PageTitle("Modificar reserva")
public class VistaModificarReserva extends VistaProtegida implements PantallaModificarReserva {

    /** Presentador de P-23. */
    private final PresentadorModificarReserva presentador;

    /** Datos actuales de la reserva. */
    private final Paragraph actual = new Paragraph();

    /** Cancha nueva. */
    private final ComboBox<OpcionSeleccion> cancha = new ComboBox<>("Cancha");

    /** Fecha nueva. */
    private final DatePicker fecha = new DatePicker("Fecha");

    /** Turnos de la fecha y cancha consultadas. */
    private final TablaDatos<FilaTurno> turnos = new TablaDatos<>(FilaTurno.class);

    /** Turno elegido. */
    private final Paragraph elegido = new Paragraph("Nuevo turno: (sin elegir)");

    /** Deporte de la reserva (los turnos se consultan para ese deporte). */
    private Long idDeporte;

    /** Turno elegido en la grilla (estado de la vista hasta guardar). */
    private FilaTurno turnoElegido;

    /**
     * Arma la vista y vincula el presentador.
     *
     * <p>Casos de uso: CU-32
     * @param presentador presentador de P-23
     */
    public VistaModificarReserva(PresentadorModificarReserva presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        cancha.setItemLabelGenerator(OpcionSeleccion::texto);
        fecha.setLocale(FormatoUI.ES_AR);
        Button consultar = new Button("Consultar turnos", e -> presentador.alConsultarTurnos(filtro()));
        HorizontalLayout filtros = new HorizontalLayout(cancha, fecha, consultar);
        filtros.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        filtros.setWrap(true);
        turnos.agregarColumna("Cancha", FilaTurno::cancha);
        turnos.agregarColumna("Inicio", f -> FormatoUI.hora(f.horaInicio()));
        turnos.agregarColumna("Fin", f -> FormatoUI.hora(f.horaFin()));
        turnos.agregarColumna("Estado", FilaTurno::estado);
        turnos.agregarColumna("Ocupado por", f -> f.ocupadoPor() == null ? "" : f.ocupadoPor());
        turnos.agregarColumnaAccion("Elegir", this::elegir);
        Button guardar = new Button("Guardar", e -> presentador.alGuardar(turnoElegido));
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelar = new Button("Cancelar", e -> navegarA("reservas-club"));
        getContent().add(new H2("Modificar reserva"), actual, filtros, turnos, elegido,
                new HorizontalLayout(cancelar, guardar));
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-32
     * @return {@code GESTIONAR_RESERVAS_CLUB}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_RESERVAS_CLUB;
    }

    /**
     * Gancho de {@code VistaProtegida}: lee {@code :id} y llama a {@code presentador.cargar(id)}.
     *
     * <p>Casos de uso: CU-32
     * @param parametros parámetros de la ruta ({@code :id})
     * @param consulta parámetros de consulta (no se usan)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        parametros.getLong("id").ifPresentOrElse(presentador::cargar,
                () -> mostrarError("El elemento ya no existe."));
    }

    /**
     * Muestra los datos actuales y propone la misma cancha y fecha.
     *
     * <p>Casos de uso: CU-32
     * @param detalle detalle de la reserva
     * @param canchas canchas del deporte
     */
    @Override
    public void mostrarReserva(DetalleReserva detalle, List<OpcionSeleccion> canchas) {
        idDeporte = detalle.idDeporte();
        turnoElegido = null;
        elegido.setText("Nuevo turno: (sin elegir)");
        actual.setText(detalle.socio() + " (DNI " + detalle.dniSocio() + ") · " + detalle.cancha() + " · "
                + FormatoUI.fecha(detalle.fecha()) + " " + FormatoUI.hora(detalle.horaInicio()) + " a "
                + FormatoUI.hora(detalle.horaFin()) + " · Estado: " + detalle.estado());
        cancha.setItems(canchas);
        canchas.stream().filter(o -> o.id().equals(detalle.idCancha())).findFirst().ifPresent(cancha::setValue);
        fecha.setValue(detalle.fecha());
        turnos.mostrar(List.of());
    }

    /**
     * Muestra los turnos consultados.
     *
     * <p>Casos de uso: CU-32
     * @param filas turnos
     */
    @Override
    public void mostrarTurnos(List<FilaTurno> filas) {
        turnos.mostrar(filas);
    }

    /**
     * Guarda el turno elegido en la vista y lo muestra.
     *
     * <p>Casos de uso: CU-32
     * @param fila turno elegido
     */
    private void elegir(FilaTurno fila) {
        turnoElegido = fila;
        elegido.setText("Nuevo turno: " + fila.cancha() + " · " + FormatoUI.fecha(fila.fecha()) + " "
                + FormatoUI.hora(fila.horaInicio()) + " a " + FormatoUI.hora(fila.horaFin()));
    }

    /**
     * Arma el filtro de disponibilidad con la fecha, el deporte de la reserva y la cancha elegida.
     *
     * <p>Casos de uso: CU-32
     * @return filtro
     */
    private FiltroDisponibilidad filtro() {
        FiltroDisponibilidad f = new FiltroDisponibilidad();
        f.setFecha(fecha.getValue());
        f.setIdDeporte(idDeporte);
        f.setIdCancha(cancha.getValue() == null ? null : cancha.getValue().id());
        return f;
    }
}

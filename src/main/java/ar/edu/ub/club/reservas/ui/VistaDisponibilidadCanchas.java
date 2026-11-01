package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.agenda.aplicacion.FiltroDisponibilidad;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoResueltoEvento;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.socios.aplicacion.CriterioBusquedaSocio;
import ar.edu.ub.club.socios.ui.FilaSocio;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista de la pantalla P-19 (MVP, Vista Pasiva): grilla de turnos por cancha y horario para una fecha, con filtros
 * por deporte y cancha; para el administrador, la búsqueda y elección del socio de una reserva manual. No tiene
 * lógica: cada oyente llama a un método {@code al...} del presentador; abre el diálogo P-20 y recarga la grilla al
 * cerrarlo.
 *
 * <p>Ruta: {@code turnos}. Permiso: {@code CONSULTAR_DISPONIBILIDAD}.
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28
 */
@Route(value = "turnos", layout = DisposicionPrincipal.class)
@PageTitle("Turnos disponibles")
public class VistaDisponibilidadCanchas extends VistaProtegida implements PantallaDisponibilidadCanchas {

    /** Presentador de P-19. */
    private final PresentadorDisponibilidadCanchas presentador;

    /** Presentador del diálogo P-20 (bean de alcance de UI que se vincula a cada diálogo). */
    private final PresentadorConfirmarReserva presentadorConfirmar;

    /** Filtro de deporte. */
    private final ComboBox<OpcionSeleccion> deporte = new ComboBox<>("Deporte");

    /** Filtro de cancha. */
    private final ComboBox<OpcionSeleccion> cancha = new ComboBox<>("Cancha");

    /** Fecha a consultar. */
    private final DatePicker fecha = new DatePicker("Fecha");

    /** Sección de elección del socio (solo administrador). */
    private final VerticalLayout seccionSocio = new VerticalLayout();

    /** Texto de búsqueda del socio. */
    private final TextField textoSocio = new TextField("Socio (DNI o apellido)");

    /** Socios encontrados. */
    private final TablaDatos<FilaSocio> socios = new TablaDatos<>(FilaSocio.class);

    /** Resumen del socio elegido y de la cuota de su grupo. */
    private final Paragraph socioElegido = new Paragraph();

    /** Grilla de turnos. */
    private final TablaDatos<FilaTurno> turnos = new TablaDatos<>(FilaTurno.class);

    /**
     * Arma la vista y vincula el presentador.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param presentador presentador de P-19
     * @param presentadorConfirmar presentador de P-20
     */
    public VistaDisponibilidadCanchas(PresentadorDisponibilidadCanchas presentador,
                                      PresentadorConfirmarReserva presentadorConfirmar) {
        this.presentador = presentador;
        this.presentadorConfirmar = presentadorConfirmar;
        presentador.vincular(this);

        deporte.setItemLabelGenerator(OpcionSeleccion::texto);
        deporte.setClearButtonVisible(true);
        deporte.addValueChangeListener(e -> presentador.alCambiarDeporte(e.getValue() == null ? null : e.getValue().id()));
        cancha.setItemLabelGenerator(OpcionSeleccion::texto);
        cancha.setClearButtonVisible(true);
        fecha.setLocale(FormatoUI.ES_AR);
        Button consultar = new Button("Consultar", e -> presentador.alConsultar(filtro()));
        consultar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout filtros = new HorizontalLayout(deporte, cancha, fecha, consultar);
        filtros.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        filtros.setWrap(true);

        Button buscar = new Button("Buscar socio", e -> {
            CriterioBusquedaSocio criterio = new CriterioBusquedaSocio();
            criterio.setTexto(textoSocio.getValue());
            presentador.alBuscarSocio(criterio);
        });
        HorizontalLayout busqueda = new HorizontalLayout(textoSocio, buscar);
        busqueda.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        socios.agregarColumna("DNI", FilaSocio::dni);
        socios.agregarColumna("Apellido", FilaSocio::apellido);
        socios.agregarColumna("Nombre", FilaSocio::nombre);
        socios.agregarColumna("Categoría", FilaSocio::categoria);
        socios.agregarColumna("Estado", FilaSocio::estado);
        socios.agregarColumnaAccion("Elegir", f -> presentador.alElegirSocio(f.id()));
        seccionSocio.add(new H3("Reserva manual: socio"), busqueda, socios, socioElegido);
        seccionSocio.setPadding(false);
        seccionSocio.setVisible(false);

        turnos.agregarColumna("Cancha", FilaTurno::cancha);
        turnos.agregarColumna("Deporte", FilaTurno::deporte);
        turnos.agregarColumna("Fecha", f -> FormatoUI.fecha(f.fecha()));
        turnos.agregarColumna("Inicio", f -> FormatoUI.hora(f.horaInicio()));
        turnos.agregarColumna("Fin", f -> FormatoUI.hora(f.horaFin()));
        turnos.agregarColumna("Estado", FilaTurno::estado);
        turnos.agregarColumna("Ocupado por", f -> f.ocupadoPor() == null ? "" : f.ocupadoPor());
        turnos.agregarColumnaAccion("Reservar", presentador::alElegirTurno);

        getContent().add(new H2("Turnos disponibles"), seccionSocio, filtros, turnos);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-26
     * @return {@code CONSULTAR_DISPONIBILIDAD}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONSULTAR_DISPONIBILIDAD;
    }

    /**
     * Gancho de {@code VistaProtegida}: llama a {@code presentador.cargar()}.
     *
     * <p>Casos de uso: CU-26
     * @param parametros parámetros de la ruta (no se usan)
     * @param consulta parámetros de consulta (no se usan)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Muestra los filtros de deporte y cancha.
     *
     * <p>Casos de uso: CU-26
     * @param deportes deportes activos
     * @param canchas canchas
     */
    @Override
    public void mostrarFiltros(List<OpcionSeleccion> deportes, List<OpcionSeleccion> canchas) {
        deporte.setItems(deportes);
        cancha.setItems(canchas);
    }

    /**
     * Reemplaza las opciones de cancha.
     *
     * <p>Casos de uso: CU-26
     * @param opciones canchas del deporte
     */
    @Override
    public void mostrarCanchas(List<OpcionSeleccion> opciones) {
        cancha.clear();
        cancha.setItems(opciones);
    }

    /**
     * Acota el calendario y propone la primera fecha consultable.
     *
     * <p>Casos de uso: CU-26
     * @param desde primera fecha
     * @param hasta última fecha
     */
    @Override
    public void mostrarRangoCalendario(LocalDate desde, LocalDate hasta) {
        fecha.setMin(desde);
        fecha.setMax(hasta);
        if (fecha.getValue() == null) {
            fecha.setValue(desde);
        }
    }

    /**
     * Muestra u oculta la sección del socio.
     *
     * <p>Casos de uso: CU-26, CU-28
     * @param visible true para el administrador
     */
    @Override
    public void mostrarEleccionSocio(boolean visible) {
        seccionSocio.setVisible(visible);
    }

    /**
     * Muestra los socios encontrados.
     *
     * <p>Casos de uso: CU-28
     * @param filas socios
     */
    @Override
    public void mostrarSocios(List<FilaSocio> filas) {
        socios.mostrar(filas);
    }

    /**
     * Muestra el socio elegido y la cuota de su grupo.
     *
     * <p>Casos de uso: CU-28
     * @param fila socio elegido
     * @param cuotaGrupo cuota más reciente del grupo o {@code null}
     */
    @Override
    public void mostrarSocioElegido(FilaSocio fila, FilaCuota cuotaGrupo) {
        String cuota = cuotaGrupo == null ? "sin cuotas emitidas"
                : cuotaGrupo.periodo() + " " + cuotaGrupo.estado() + " (vence " + FormatoUI.fecha(cuotaGrupo.fechaVencimiento()) + ")";
        socioElegido.setText("Socio elegido: " + fila.apellido() + ", " + fila.nombre() + " (DNI " + fila.dni() + ", "
                + fila.estado() + "). Cuota del grupo: " + cuota + ".");
    }

    /**
     * Muestra la grilla de turnos.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param filas turnos
     */
    @Override
    public void mostrarTurnos(List<FilaTurno> filas) {
        turnos.mostrar(filas);
    }

    /**
     * Crea y abre el diálogo P-20; al resolverse muestra la confirmación y al cerrarse recarga la grilla.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param turno turno elegido
     * @param idSocio socio elegido o {@code null}
     */
    @Override
    public void abrirConfirmacion(FilaTurno turno, Long idSocio) {
        DialogoConfirmarReserva dialogo = new DialogoConfirmarReserva(presentadorConfirmar, turno, idSocio);
        dialogo.addListener(DialogoResueltoEvento.class, e -> mostrarMensaje("La reserva quedó confirmada."));
        dialogo.alCerrar(() -> presentador.alConsultar(filtro()));
        dialogo.abrir();
    }

    /**
     * Arma el filtro con los valores elegidos (sin lógica: solo copia los campos).
     *
     * <p>Casos de uso: CU-26
     * @return filtro de disponibilidad
     */
    private FiltroDisponibilidad filtro() {
        FiltroDisponibilidad f = new FiltroDisponibilidad();
        f.setFecha(fecha.getValue());
        f.setIdDeporte(deporte.getValue() == null ? null : deporte.getValue().id());
        f.setIdCancha(cancha.getValue() == null ? null : cancha.getValue().id());
        return f;
    }
}

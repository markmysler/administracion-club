package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoResueltoEvento;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.ui.DialogoAsociarSocio;
import ar.edu.ub.club.grupos.ui.FilaIntegrante;
import ar.edu.ub.club.grupos.ui.PresentadorAsociarSocio;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-08: Ficha del socio con su grupo, cuotas y reservas futuras; acciones modificar,
 * modificar acceso, ver grupo, asociar a otro grupo y desactivar. Es la vista anfitriona de los diálogos P-09
 * ({@code DialogoDesactivarSocio}) y P-12 ({@code DialogoAsociarSocio}): al resolverse, recarga la ficha.
 *
 * <p>Ruta: {@code socios/ficha/:id}. Permiso: {@code GESTIONAR_SOCIOS}.
 *
 * <p>Vista pasiva del patrón MVP: sus oyentes solo llaman a {@code presentador.al…(...)}; las secciones vacías
 * muestran «Sin registros» (CU-04 FA3).
 *
 * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
 */
@Route(value = "socios/ficha/:id", layout = DisposicionPrincipal.class)
@PageTitle("Ficha del socio")
public class VistaFichaSocio extends VistaProtegida implements PantallaFichaSocio {

    /** Presentador de la pantalla. */
    private final PresentadorFichaSocio presentador;

    /** Presentador del diálogo de baja (P-09). */
    private final PresentadorDesactivarSocio presentadorDesactivar;

    /** Presentador del diálogo de asociación (P-12). */
    private final PresentadorAsociarSocio presentadorAsociar;

    /** Título con el nombre del socio. */
    private final H2 titulo = new H2("Ficha del socio");

    /** Datos personales y de acceso. */
    private final Paragraph datos = new Paragraph();

    /** Integrantes del grupo familiar. */
    private final TablaDatos<FilaIntegrante> integrantes = new TablaDatos<>(FilaIntegrante.class);

    /** Cuotas del grupo. */
    private final TablaDatos<FilaCuota> cuotas = new TablaDatos<>(FilaCuota.class);

    /** Reservas futuras. */
    private final TablaDatos<FilaResumenReserva> reservas = new TablaDatos<>(FilaResumenReserva.class);

    /** Leyenda de cuotas vacías. */
    private final Paragraph sinCuotas = new Paragraph("Sin registros");

    /** Leyenda de reservas vacías. */
    private final Paragraph sinReservas = new Paragraph("Sin registros");

    /** Botón de baja (oculto si el socio ya está inactivo). */
    private final Button desactivar;

    /** Socio mostrado (para recargar al resolverse un diálogo). */
    private Long idSocio;

    /**
     * Arma la ficha y las acciones y se vincula con el presentador.
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
     * @param presentador presentador de P-08
     * @param presentadorDesactivar presentador de P-09
     * @param presentadorAsociar presentador de P-12
     */
    public VistaFichaSocio(PresentadorFichaSocio presentador, PresentadorDesactivarSocio presentadorDesactivar,
            PresentadorAsociarSocio presentadorAsociar) {
        this.presentador = presentador;
        this.presentadorDesactivar = presentadorDesactivar;
        this.presentadorAsociar = presentadorAsociar;
        presentador.vincular(this);
        integrantes.agregarColumna("DNI", FilaIntegrante::dni);
        integrantes.agregarColumna("Apellido", FilaIntegrante::apellido);
        integrantes.agregarColumna("Nombre", FilaIntegrante::nombre);
        integrantes.agregarColumna("Categoría", FilaIntegrante::categoria);
        integrantes.agregarColumna("Estado", FilaIntegrante::estado);
        integrantes.agregarColumna("Titular", f -> f.esTitular() ? "Sí" : "");
        cuotas.agregarColumna("Período", FilaCuota::periodo);
        cuotas.agregarColumna("Importe", f -> FormatoUI.dinero(f.importe()));
        cuotas.agregarColumna("A pagar", f -> FormatoUI.dinero(f.importeAPagar()));
        cuotas.agregarColumna("Vencimiento", f -> FormatoUI.fecha(f.fechaVencimiento()));
        cuotas.agregarColumna("Estado", FilaCuota::estado);
        reservas.agregarColumna("Fecha", f -> FormatoUI.fecha(f.fecha()));
        reservas.agregarColumna("Horario", f -> FormatoUI.hora(f.horaInicio()) + " a " + FormatoUI.hora(f.horaFin()));
        reservas.agregarColumna("Cancha", FilaResumenReserva::cancha);
        Button modificar = new Button("Modificar", e -> presentador.alModificar());
        Button acceso = new Button("Modificar acceso", e -> presentador.alModificarAcceso());
        Button grupo = new Button("Ver grupo", e -> presentador.alVerGrupo());
        Button asociar = new Button("Asociar a grupo familiar", e -> presentador.alAsociarAGrupo());
        desactivar = new Button("Desactivar socio", e -> presentador.alDesactivar());
        desactivar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout acciones = new HorizontalLayout(modificar, acceso, grupo, asociar, desactivar);
        acciones.setWrap(true);
        getContent().add(titulo, datos, acciones, new H3("Grupo familiar"), integrantes, new H3("Cuotas del grupo"),
                sinCuotas, cuotas, new H3("Reservas futuras"), sinReservas, reservas);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
     * @return {@code GESTIONAR_SOCIOS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_SOCIOS;
    }

    /**
     * Pide la carga de la ficha del socio de la ruta.
     *
     * <p>Casos de uso: CU-04
     * @param parametros parámetros de la ruta ({@code id})
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        idSocio = parametros.getLong("id").orElse(null);
        presentador.cargar(idSocio);
    }

    /**
     * Muestra la ficha.
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
     * @param detalle modelo de vista de la ficha
     */
    @Override
    public void mostrarFicha(DetalleSocio detalle) {
        titulo.setText(detalle.apellido() + ", " + detalle.nombre());
        datos.setText("DNI " + detalle.dni() + " · Nacimiento " + FormatoUI.fecha(detalle.fechaNacimiento())
                + " · Teléfono " + texto(detalle.telefono()) + " · Dirección " + texto(detalle.direccion())
                + " · Correo " + detalle.email() + " · Usuario " + detalle.username() + " · Categoría "
                + detalle.categoria() + " · Estado " + detalle.estado() + " · Alta " + FormatoUI.fecha(detalle.fechaAlta())
                + " · Grupo " + detalle.idGrupo() + " · Saldo a favor del grupo " + FormatoUI.dinero(detalle.saldo()));
        integrantes.mostrar(detalle.integrantes());
        cuotas.mostrar(detalle.cuotas());
        reservas.mostrar(detalle.reservasFuturas());
        sinCuotas.setVisible(detalle.cuotas().isEmpty());
        sinReservas.setVisible(detalle.reservasFuturas().isEmpty());
        desactivar.setVisible("ACTIVA".equals(detalle.estado()));
    }

    /**
     * Crea y abre el diálogo de baja P-09; al resolverse recarga la ficha.
     *
     * <p>Casos de uso: CU-03
     * @param idSocio socio
     * @param nombre apellido y nombre
     * @param dni documento
     * @param candidatosTitular integrantes que pueden reemplazarlo como titular
     */
    @Override
    public void abrirDesactivacion(Long idSocio, String nombre, String dni, List<OpcionSeleccion> candidatosTitular) {
        DialogoDesactivarSocio dialogo = new DialogoDesactivarSocio(presentadorDesactivar);
        dialogo.addListener(DialogoResueltoEvento.class, e -> presentador.cargar(this.idSocio));
        dialogo.abrir(idSocio, nombre, dni, candidatosTitular);
    }

    /**
     * Crea y abre el diálogo de asociación P-12; al resolverse recarga la ficha.
     *
     * <p>Casos de uso: CU-07
     * @param idSocio socio
     * @param nombre apellido y nombre
     * @param dni documento
     * @param idGrupoActual grupo vigente
     * @param grupoActual texto del grupo vigente
     * @param idGrupoDestino destino preseleccionado o {@code null}
     */
    @Override
    public void abrirAsociacion(Long idSocio, String nombre, String dni, Long idGrupoActual, String grupoActual, Long idGrupoDestino) {
        DialogoAsociarSocio dialogo = new DialogoAsociarSocio(presentadorAsociar);
        dialogo.addListener(DialogoResueltoEvento.class, e -> presentador.cargar(this.idSocio));
        dialogo.abrir(idSocio, nombre, dni, idGrupoActual, grupoActual, idGrupoDestino);
    }

    /**
     * Muestra un texto opcional o un guion.
     *
     * <p>Casos de uso: CU-04
     * @param valor texto
     * @return texto o «-»
     */
    private static String texto(String valor) {
        return valor == null ? "-" : valor;
    }
}

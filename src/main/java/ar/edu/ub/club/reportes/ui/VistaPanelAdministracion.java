package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.reportes.aplicacion.Indicadores;
import ar.edu.ub.club.reportes.aplicacion.TipoReporte;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-04 Panel de administración (MVP Vista Pasiva, sin lógica): cuatro tarjetas de indicadores
 * (reservas de hoy y de la semana, cobrado en el mes, socios activos, cuotas impagas), el grupo «Canchas más
 * utilizadas» y los accesos a las funciones relacionadas. Ruta {@code panel}, permiso CONSULTAR_DASHBOARD.
 *
 * <p>Casos de uso: CU-40
 */
@Route(value = "panel", layout = DisposicionPrincipal.class)
@PageTitle("Panel de administración")
public class VistaPanelAdministracion extends VistaProtegida implements PantallaPanelAdministracion {

    /** Texto de un indicador cuya consulta falló (FA1). */
    private static final String NO_DISPONIBLE = "No disponible";

    /** Presentador de la pantalla. */
    private final PresentadorPanelAdministracion presentador;

    /** Contenedor de las tarjetas. */
    private final FlexLayout tarjetas = new FlexLayout();

    /** Lista de canchas más utilizadas. */
    private final Div canchas = new Div();

    /**
     * Arma los componentes, los accesos directos y vincula el presentador.
     *
     * <p>Casos de uso: CU-40
     * @param presentador presentador de la pantalla
     */
    public VistaPanelAdministracion(PresentadorPanelAdministracion presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        tarjetas.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        tarjetas.getStyle().set("gap", "var(--lumo-space-m)");
        Button usoCanchas = new Button("Ver reporte de uso de canchas",
                e -> presentador.alIrAReporte(TipoReporte.USO_CANCHAS));
        Button cuotas = new Button("Ver reporte de cuotas y deudores",
                e -> presentador.alIrAReporte(TipoReporte.CUOTAS_DEUDORES));
        usoCanchas.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cuotas.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Div accesos = new Div(enlace("Ver reservas del club", "reservas-club"), enlace("Ver socios", "socios"),
                usoCanchas, cuotas);
        getContent().add(tarjetas, new H4("Canchas más utilizadas del mes"), canchas, accesos);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-40
     * @return CONSULTAR_DASHBOARD
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONSULTAR_DASHBOARD;
    }

    /**
     * Al entrar llama a {@code presentador.cargar()} (paso 1).
     *
     * <p>Casos de uso: CU-40
     * @param parametros parámetros de ruta (sin uso)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Muestra las tarjetas y la lista de canchas; un indicador no disponible muestra «No disponible» (FA1) y la lista
     * vacía «Sin datos» (FA2).
     *
     * <p>Casos de uso: CU-40
     * @param r modelo de vista de los indicadores
     */
    @Override
    public void mostrarIndicadores(ResumenIndicadores r) {
        tarjetas.removeAll();
        tarjetas.add(
                tarjeta("Reservas de hoy",
                        valor(r, Indicadores.RESERVAS_HOY, r.reservasHoy()),
                        "Semana: " + valor(r, Indicadores.RESERVAS_SEMANA, r.reservasSemana())),
                tarjeta("Cobrado en el mes",
                        r.noDisponible(Indicadores.RECAUDACION_MES) ? NO_DISPONIBLE
                                : FormatoUI.dinero(r.recaudacionMes()),
                        "Cuotas pagadas: " + valor(r, Indicadores.CUOTAS_PAGADAS_MES, r.cuotasPagadasMes())),
                tarjeta("Socios activos", valor(r, Indicadores.SOCIOS_ACTIVOS, r.sociosActivos()), ""),
                tarjeta("Cuotas impagas",
                        "Pendientes: " + valor(r, Indicadores.CUOTAS_PENDIENTES, r.cuotasPendientes()),
                        "Vencidas: " + valor(r, Indicadores.CUOTAS_VENCIDAS, r.cuotasVencidas())));
        canchas.removeAll();
        if (r.noDisponible(Indicadores.CANCHAS_MAS_USADAS)) {
            canchas.add(new Span(NO_DISPONIBLE));
        } else if (r.canchasMasUsadas().isEmpty()) {
            canchas.add(new Span("Sin datos"));
        } else {
            UnorderedList lista = new UnorderedList();
            r.canchasMasUsadas().forEach(c -> lista.add(new ListItem(c)));
            canchas.add(lista);
        }
    }

    /**
     * Texto de un indicador numérico: su valor o «No disponible».
     *
     * <p>Casos de uso: CU-40
     * @param r modelo de vista
     * @param nombre nombre del indicador
     * @param valor valor del indicador
     * @return texto a mostrar
     */
    private String valor(ResumenIndicadores r, String nombre, Long valor) {
        return r.noDisponible(nombre) || valor == null ? NO_DISPONIBLE : String.valueOf(valor);
    }

    /**
     * Arma una tarjeta con título, valor principal y detalle.
     *
     * <p>Casos de uso: CU-40
     * @param titulo título de la tarjeta
     * @param principal valor principal
     * @param detalle texto secundario
     * @return la tarjeta
     */
    private Component tarjeta(String titulo, String principal, String detalle) {
        VerticalLayout tarjeta = new VerticalLayout(new Span(titulo), new H3(principal), new Span(detalle));
        tarjeta.setWidth("14rem");
        tarjeta.setSpacing(false);
        tarjeta.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-border-radius-l)");
        return tarjeta;
    }

    /**
     * Enlace sin lógica a otra pantalla.
     *
     * <p>Casos de uso: CU-40
     * @param texto texto del enlace
     * @param ruta ruta destino
     * @return el enlace
     */
    private Component enlace(String texto, String ruta) {
        Span s = new Span(new com.vaadin.flow.component.html.Anchor(ruta, texto));
        s.getStyle().set("margin-right", "var(--lumo-space-m)");
        return s;
    }
}

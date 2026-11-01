package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FiltroFechas;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import ar.edu.ub.club.reportes.aplicacion.FormatoExportacion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista de la pantalla P-32 Reporte de uso de canchas (MVP Vista Pasiva, sin lógica): filtros de período ({@code
 * FiltroFechas}), deporte y cancha, botones Generar y Exportar (Excel y PDF), grilla del reporte con su fila de totales
 * y la descarga con {@code StreamResource} y {@code Anchor} (MODELO 11.5.4). Sus oyentes solo llaman a métodos {@code
 * al…} de {@code PresentadorReporteUsoCanchas}. Ruta {@code reportes/uso-canchas}, permiso GENERAR_REPORTES.
 *
 * <p>Casos de uso: CU-38
 */
@Route(value = "reportes/uso-canchas", layout = DisposicionPrincipal.class)
@PageTitle("Reporte de uso de canchas")
public class VistaReporteUsoCanchas extends VistaProtegida implements PantallaReporteUsoCanchas {

    /** Presentador de la pantalla. */
    private final PresentadorReporteUsoCanchas presentador;

    /** Período del reporte. */
    private final FiltroFechas periodo = new FiltroFechas();

    /** Combo de deporte (opcional). */
    private final ComboBox<OpcionSeleccion> deporte = new ComboBox<>("Deporte");

    /** Combo de cancha (opcional). */
    private final ComboBox<OpcionSeleccion> cancha = new ComboBox<>("Cancha");

    /** Exportar a Excel (habilitado después de generar). */
    private final Button exportarExcel = new Button("Exportar Excel");

    /** Exportar a PDF (habilitado después de generar). */
    private final Button exportarPdf = new Button("Exportar PDF");

    /** Zona donde se muestra el reporte generado. */
    private final Div zonaReporte = new Div();

    /** Zona del enlace de descarga. */
    private final Div zonaDescarga = new Div();

    /**
     * Arma los componentes y vincula el presentador.
     *
     * <p>Casos de uso: CU-38
     * @param presentador presentador de la pantalla
     */
    public VistaReporteUsoCanchas(PresentadorReporteUsoCanchas presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        LocalDate hoy = LocalDate.now(RelojSistema.ZONA_CLUB);
        periodo.fijar(hoy.withDayOfMonth(1), hoy.withDayOfMonth(hoy.lengthOfMonth()));
        deporte.setClearButtonVisible(true);
        cancha.setClearButtonVisible(true);
        Button generar = new Button("Generar", e -> alGenerar());
        generar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportarExcel.addClickListener(e -> presentador.alExportar(FormatoExportacion.EXCEL));
        exportarPdf.addClickListener(e -> presentador.alExportar(FormatoExportacion.PDF));
        exportarExcel.setEnabled(false);
        exportarPdf.setEnabled(false);
        HorizontalLayout filtros = new HorizontalLayout(periodo, deporte, cancha, generar, exportarExcel, exportarPdf);
        filtros.setAlignItems(HorizontalLayout.Alignment.BASELINE);
        filtros.setWrap(true);
        getContent().add(filtros, zonaReporte, zonaDescarga,
                new Span("Ocupación = horas reservadas / horas de atención del club en el período"));
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-38
     * @return GENERAR_REPORTES
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GENERAR_REPORTES;
    }

    /**
     * Al entrar carga los filtros con {@code presentador.cargar()} (pasos 1 y 2).
     *
     * <p>Casos de uso: CU-38
     * @param parametros parámetros de ruta (sin uso)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Oyente del botón Generar: lee el período ({@code FiltroFechas.valor()} marca el error si desde &gt; hasta,
     * FA1), arma el {@code FiltroReporte} con los combos y llama a {@code presentador.alGenerar(filtro)}.
     *
     * <p>Casos de uso: CU-38
     */
    private void alGenerar() {
        periodo.valor().ifPresent(rango -> {
            FiltroReporte filtro = new FiltroReporte(rango.desde(), rango.hasta());
            filtro.setIdDeporte(deporte.getValue() == null ? null : deporte.getValue().id());
            filtro.setIdCancha(cancha.getValue() == null ? null : cancha.getValue().id());
            presentador.alGenerar(filtro);
        });
    }

    /**
     * Carga las opciones de los combos.
     *
     * <p>Casos de uso: CU-38
     * @param deportes opciones de deporte
     * @param canchas opciones de cancha
     */
    @Override
    public void mostrarFiltros(List<OpcionSeleccion> deportes, List<OpcionSeleccion> canchas) {
        deporte.setItems(deportes);
        cancha.setItems(canchas);
    }

    /**
     * Muestra el título, la grilla (una columna por encabezado de la tabla) y los totales en negrita, y habilita la
     * exportación.
     *
     * <p>Casos de uso: CU-38
     * @param tabla modelo de vista del reporte
     */
    @Override
    public void mostrarResultado(TablaReporte tabla) {
        zonaReporte.removeAll();
        zonaReporte.add(new H3(tabla.titulo()), grilla(tabla));
        for (String total : tabla.totales()) {
            Paragraph p = new Paragraph(total);
            p.getStyle().set("font-weight", "bold");
            zonaReporte.add(p);
        }
        exportarExcel.setEnabled(true);
        exportarPdf.setEnabled(true);
    }

    /**
     * Ofrece el archivo: crea {@code new StreamResource(nombre, () -> new ByteArrayInputStream(contenido))} con el
     * tipo MIME, lo asocia a un {@code Anchor} de descarga y lo activa (paso 10).
     *
     * <p>Casos de uso: CU-38
     * @param nombre nombre del archivo
     * @param tipoContenido tipo MIME
     * @param contenido bytes del archivo
     */
    @Override
    @SuppressWarnings("deprecation")
    public void descargar(String nombre, String tipoContenido, byte[] contenido) {
        StreamResource recurso = new StreamResource(nombre, () -> new ByteArrayInputStream(contenido));
        recurso.setContentType(tipoContenido);
        Anchor enlace = new Anchor(recurso, "Descargar " + nombre);
        enlace.getElement().setAttribute("download", true);
        zonaDescarga.removeAll();
        zonaDescarga.add(enlace);
        enlace.getElement().executeJs("this.click()");
    }

    /**
     * Arma la grilla del reporte con {@code TablaDatos}: una columna por encabezado, que muestra la celda de igual
     * posición de cada fila.
     *
     * <p>Casos de uso: CU-38
     * @param tabla modelo de vista del reporte
     * @return grilla cargada
     */
    @SuppressWarnings("unchecked")
    private TablaDatos<List<String>> grilla(TablaReporte tabla) {
        TablaDatos<List<String>> grilla = new TablaDatos<>((Class<List<String>>) (Class<?>) List.class);
        for (int i = 0; i < tabla.columnas().size(); i++) {
            int indice = i;
            grilla.agregarColumna(tabla.columnas().get(i), fila -> fila.get(indice));
        }
        grilla.mostrar(tabla.filas());
        return grilla;
    }
}

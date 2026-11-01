package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FiltroFechas;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import ar.edu.ub.club.reportes.aplicacion.FormatoExportacion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
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
 * Vista de la pantalla P-33 Reporte de cuotas y deudores (MVP Vista Pasiva, sin lógica): filtro de período de emisión
 * ({@code FiltroFechas}), Generar y Exportar (Excel y PDF), resumen por estado (totales), grilla de grupos deudores y
 * descarga con {@code StreamResource} y {@code Anchor}. Sus oyentes solo llaman a métodos {@code al…} de {@code
 * PresentadorReporteCuotas}. Ruta {@code reportes/cuotas}, permiso GENERAR_REPORTES.
 *
 * <p>Casos de uso: CU-39
 */
@Route(value = "reportes/cuotas", layout = DisposicionPrincipal.class)
@PageTitle("Reporte de cuotas y deudores")
public class VistaReporteCuotas extends VistaProtegida implements PantallaReporteCuotas {

    /** Presentador de la pantalla. */
    private final PresentadorReporteCuotas presentador;

    /** Período de emisión. */
    private final FiltroFechas periodo = new FiltroFechas();

    /** Exportar a Excel (habilitado después de generar). */
    private final Button exportarExcel = new Button("Exportar Excel");

    /** Exportar a PDF (habilitado después de generar). */
    private final Button exportarPdf = new Button("Exportar PDF");

    /** Zona donde se muestra el reporte generado. */
    private final Div zonaReporte = new Div();

    /** Zona del enlace de descarga. */
    private final Div zonaDescarga = new Div();

    /**
     * Arma los componentes y vincula el presentador; el período inicial es el mes en curso.
     *
     * <p>Casos de uso: CU-39
     * @param presentador presentador de la pantalla
     */
    public VistaReporteCuotas(PresentadorReporteCuotas presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        LocalDate hoy = LocalDate.now(RelojSistema.ZONA_CLUB);
        periodo.fijar(hoy.withDayOfMonth(1), hoy.withDayOfMonth(hoy.lengthOfMonth()));
        Button generar = new Button("Generar", e -> alGenerar());
        generar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportarExcel.addClickListener(e -> presentador.alExportar(FormatoExportacion.EXCEL));
        exportarPdf.addClickListener(e -> presentador.alExportar(FormatoExportacion.PDF));
        exportarExcel.setEnabled(false);
        exportarPdf.setEnabled(false);
        HorizontalLayout filtros = new HorizontalLayout(periodo, generar, exportarExcel, exportarPdf);
        filtros.setAlignItems(HorizontalLayout.Alignment.BASELINE);
        filtros.setWrap(true);
        getContent().add(filtros, zonaReporte, zonaDescarga);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-39
     * @return GENERAR_REPORTES
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GENERAR_REPORTES;
    }

    /**
     * Al entrar no carga nada: muestra el filtro de período (paso 2).
     *
     * <p>Casos de uso: CU-39
     * @param parametros parámetros de ruta (sin uso)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        zonaDescarga.removeAll();
    }

    /**
     * Oyente del botón Generar: lee el período ({@code FiltroFechas} marca el error si desde &gt; hasta, FA1) y llama
     * a {@code presentador.alGenerar(filtro)}.
     *
     * <p>Casos de uso: CU-39
     */
    private void alGenerar() {
        periodo.valor().ifPresent(rango -> presentador.alGenerar(new FiltroReporte(rango.desde(), rango.hasta())));
    }

    /**
     * Muestra el resumen por estado (totales) y la grilla de grupos deudores, y habilita la exportación.
     *
     * <p>Casos de uso: CU-39
     * @param tabla modelo de vista del reporte
     */
    @Override
    public void mostrarResultado(TablaReporte tabla) {
        zonaReporte.removeAll();
        UnorderedList resumen = new UnorderedList();
        tabla.totales().forEach(t -> resumen.add(new ListItem(t)));
        zonaReporte.add(new H3(tabla.titulo()), new H4("Resumen por estado"), resumen, new H4("Grupos deudores"),
                grilla(tabla));
        exportarExcel.setEnabled(true);
        exportarPdf.setEnabled(true);
    }

    /**
     * Ofrece el archivo con {@code new StreamResource(nombre, () -> new ByteArrayInputStream(contenido))}, un {@code
     * Anchor} de descarga y su activación (paso 11).
     *
     * <p>Casos de uso: CU-39
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
     * Arma la grilla de deudores con {@code TablaDatos}: una columna por encabezado.
     *
     * <p>Casos de uso: CU-39
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

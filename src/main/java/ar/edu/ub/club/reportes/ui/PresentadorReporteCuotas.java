package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import ar.edu.ub.club.reportes.aplicacion.FormatoExportacion;
import ar.edu.ub.club.reportes.aplicacion.ServicioReportes;
import ar.edu.ub.club.reportes.aplicacion.TipoReporte;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-33 Reporte de cuotas y deudores (MVP Vista Pasiva; no usa tipos de Vaadin). Genera el
 * reporte y lo exporta con {@code ServicioReportes}; guarda el último filtro generado. Diagrama sec_cu39_p1.
 *
 * <p>Casos de uso: CU-39
 */
@SpringComponent
@UIScope
public class PresentadorReporteCuotas extends PresentadorBase<PantallaReporteCuotas> {

    /** Fachada de reportes. */
    private final ServicioReportes servicioReportes;

    /** Último filtro con que se generó el reporte en pantalla ({@code null} si todavía no se generó). */
    private FiltroReporte ultimoFiltro;

    /**
     * Crea el presentador con su fachada.
     *
     * <p>Casos de uso: CU-39
     * @param servicioReportes fachada de reportes
     */
    public PresentadorReporteCuotas(ServicioReportes servicioReportes) {
        this.servicioReportes = servicioReportes;
    }

    /**
     * Genera el reporte (pasos 3 a 8): {@code ServicioReportes.generarReporte(CUOTAS_DEUDORES, filtro)} → {@code
     * TablaReporte.desde} → {@code pantalla.mostrarResultado}. Sin grupos deudores avisa «No hay cuotas o deudores en
     * el período» (FA2).
     *
     * <p>Casos de uso: CU-39
     * @param filtro período de emisión
     */
    public void alGenerar(FiltroReporte filtro) {
        consultar(() -> servicioReportes.generarReporte(TipoReporte.CUOTAS_DEUDORES, filtro)).ifPresent(r -> {
            ultimoFiltro = filtro;
            pantalla.mostrarResultado(TablaReporte.desde(r));
            if (r.getFilas().isEmpty()) {
                pantalla.mostrarMensaje("No hay cuotas o deudores en el período");
            }
        });
    }

    /**
     * Exporta el último reporte generado (pasos 9 a 11): {@code ServicioReportes.exportarReporte(CUOTAS_DEUDORES,
     * ultimoFiltro, formato)} → {@code pantalla.descargar(...)}. Si falla, el reporte en pantalla se conserva (FA3).
     *
     * <p>Casos de uso: CU-39
     * @param formato EXCEL o PDF
     */
    public void alExportar(FormatoExportacion formato) {
        if (ultimoFiltro == null) {
            pantalla.mostrarError("Genere el reporte antes de exportarlo");
            return;
        }
        consultar(() -> servicioReportes.exportarReporte(TipoReporte.CUOTAS_DEUDORES, ultimoFiltro, formato))
                .ifPresent(a -> pantalla.descargar(a.getNombre(), a.getTipoContenido(), a.getContenido()));
    }
}

package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import ar.edu.ub.club.reportes.aplicacion.FormatoExportacion;
import ar.edu.ub.club.reportes.aplicacion.ReporteUsoCanchas;
import ar.edu.ub.club.reportes.aplicacion.ServicioReportes;
import ar.edu.ub.club.reportes.aplicacion.TipoReporte;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;

/**
 * Presentador de la pantalla P-32 Reporte de uso de canchas (MVP Vista Pasiva; no usa tipos de Vaadin). Carga los
 * filtros con {@code ServicioRecursos}, genera el reporte y lo exporta con {@code ServicioReportes}; guarda el último
 * filtro generado para exportar exactamente lo que se ve. Diagramas sec_cu38_p1 y sec_cu38_p2.
 *
 * <p>Casos de uso: CU-38
 */
@SpringComponent
@UIScope
public class PresentadorReporteUsoCanchas extends PresentadorBase<PantallaReporteUsoCanchas> {

    /** Fachada de recursos (deportes y canchas para los filtros). */
    private final ServicioRecursos servicioRecursos;

    /** Fachada de reportes. */
    private final ServicioReportes servicioReportes;

    /** Último filtro con que se generó el reporte en pantalla ({@code null} si todavía no se generó). */
    private FiltroReporte ultimoFiltro;

    /**
     * Crea el presentador con sus dependencias.
     *
     * <p>Casos de uso: CU-38
     * @param servicioRecursos fachada de recursos
     * @param servicioReportes fachada de reportes
     */
    public PresentadorReporteUsoCanchas(ServicioRecursos servicioRecursos, ServicioReportes servicioReportes) {
        this.servicioRecursos = servicioRecursos;
        this.servicioReportes = servicioReportes;
    }

    /**
     * Carga los filtros (paso 2): {@code ServicioRecursos.listarDeportes()} y {@code listarCanchas(null)} → {@code
     * OpcionSeleccion} → {@code pantalla.mostrarFiltros}.
     *
     * <p>Casos de uso: CU-38
     */
    public void cargar() {
        ultimoFiltro = null;
        consultar(() -> {
            List<OpcionSeleccion> deportes = servicioRecursos.listarDeportes().stream()
                    .map(d -> new OpcionSeleccion(d.getId(), d.getNombre()))
                    .toList();
            List<OpcionSeleccion> canchas = servicioRecursos.listarCanchas(null).stream()
                    .map(c -> new OpcionSeleccion(c.getId(), c.getNombre()
                            + (c.getDeporte() == null ? "" : " (" + c.getDeporte().getNombre() + ")")))
                    .toList();
            pantalla.mostrarFiltros(deportes, canchas);
            return Boolean.TRUE;
        });
    }

    /**
     * Genera el reporte (pasos 3 a 7): {@code ServicioReportes.generarReporte(USO_CANCHAS, filtro)} → {@code
     * TablaReporte.desde} → {@code pantalla.mostrarResultado}. Sin reservas avisa «No hay reservas en el período»
     * (FA2); si el filtro es inválido muestra el error de la fachada (FA1).
     *
     * <p>Casos de uso: CU-38
     * @param filtro período, deporte y cancha
     */
    public void alGenerar(FiltroReporte filtro) {
        consultar(() -> servicioReportes.generarReporte(TipoReporte.USO_CANCHAS, filtro)).ifPresent(resultado -> {
            ultimoFiltro = filtro;
            pantalla.mostrarResultado(TablaReporte.desde(resultado));
            Object reservas = resultado.getTotales().getValores().get(ReporteUsoCanchas.TOTAL_RESERVAS);
            if (reservas instanceof Number n && n.longValue() == 0) {
                pantalla.mostrarMensaje("No hay reservas en el período");
            }
        });
    }

    /**
     * Exporta el último reporte generado (pasos 8 a 10): {@code ServicioReportes.exportarReporte(USO_CANCHAS,
     * ultimoFiltro, formato)} → {@code pantalla.descargar(nombre, tipoContenido, contenido)}. Si falla, el reporte en
     * pantalla se conserva (FA3).
     *
     * <p>Casos de uso: CU-38
     * @param formato EXCEL o PDF
     */
    public void alExportar(FormatoExportacion formato) {
        if (ultimoFiltro == null) {
            pantalla.mostrarError("Genere el reporte antes de exportarlo");
            return;
        }
        consultar(() -> servicioReportes.exportarReporte(TipoReporte.USO_CANCHAS, ultimoFiltro, formato))
                .ifPresent(a -> pantalla.descargar(a.getNombre(), a.getTipoContenido(), a.getContenido()));
    }
}

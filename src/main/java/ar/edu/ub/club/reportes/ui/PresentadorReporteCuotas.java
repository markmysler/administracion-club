package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import ar.edu.ub.club.reportes.aplicacion.FormatoExportacion;
import ar.edu.ub.club.reportes.aplicacion.ServicioReportes;

/**
 * Presentador de la pantalla P-33: Reporte de cuotas cobradas, pendientes y grupos deudores, con exportación.
 *
 * <p>Casos de uso: CU-39
 */
public class PresentadorReporteCuotas extends PresentadorBase<PantallaReporteCuotas> {

    /** Atributo servicioReportes. */
    private ServicioReportes servicioReportes;

    /** Atributo ultimoFiltro. */
    private FiltroReporte ultimoFiltro;

    /**
     * {@code ServicioReportes.generarReporte(TipoReporte.CUOTAS_DEUDORES, filtro)} → {@code
     * TablaReporte.desde(resultado)}
     *
     * <p>Casos de uso: CU-39
     * @param filtro valor de {@code filtro}
     */
    public void alGenerar(FiltroReporte filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioReportes.exportarReporte(TipoReporte.CUOTAS_DEUDORES, ultimoFiltro, formato)} → {@code
     * pantalla.descargar(...)}
     *
     * <p>Casos de uso: CU-39
     * @param formato valor de {@code formato}
     */
    public void alExportar(FormatoExportacion formato) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

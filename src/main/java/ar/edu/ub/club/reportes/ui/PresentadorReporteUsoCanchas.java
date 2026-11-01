package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import ar.edu.ub.club.reportes.aplicacion.FormatoExportacion;
import ar.edu.ub.club.reportes.aplicacion.ServicioReportes;

/**
 * Presentador de la pantalla P-32: Reporte de uso de canchas por período, deporte y cancha, con exportación.
 *
 * <p>Casos de uso: CU-38
 */
public class PresentadorReporteUsoCanchas extends PresentadorBase<PantallaReporteUsoCanchas> {

    /** Atributo servicioRecursos. */
    private ServicioRecursos servicioRecursos;

    /** Atributo servicioReportes. */
    private ServicioReportes servicioReportes;

    /** Atributo ultimoFiltro. */
    private FiltroReporte ultimoFiltro;

    /**
     * {@code ServicioRecursos.listarDeportes()}, {@code listarCanchas(null)} (filtros)
     *
     * <p>Casos de uso: CU-38
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioReportes.generarReporte(TipoReporte.USO_CANCHAS, filtro): ResultadoReporte} → {@code
     * TablaReporte.desde(resultado)}
     *
     * <p>Casos de uso: CU-38
     * @param filtro valor de {@code filtro}
     */
    public void alGenerar(FiltroReporte filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioReportes.exportarReporte(TipoReporte.USO_CANCHAS, ultimoFiltro, formato): ArchivoExportado} →
     * {@code pantalla.descargar(archivo.nombre, archivo.tipoContenido, archivo.contenido)}
     *
     * <p>Casos de uso: CU-38
     * @param formato valor de {@code formato}
     */
    public void alExportar(FormatoExportacion formato) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

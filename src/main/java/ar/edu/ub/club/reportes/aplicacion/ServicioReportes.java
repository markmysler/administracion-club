package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import java.util.Map;

/**
 * Fachada de reportes. Elige la subclase de {@code Reporte} (Template Method) según el tipo y el {@code
 * ExportadorReporte} (Strategy) según el formato.
 *
 * <p>Casos de uso: CU-38, CU-39
 */
public class ServicioReportes {

    /** Reportes disponibles. */
    private Map<TipoReporte, Reporte> reportes;

    /** Estrategias de exportación. */
    private Map<FormatoExportacion, ExportadorReporte> exportadores;

    /** Verifica permisos. */
    private ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private UsuarioActual usuarioActual;

    /**
     * Invoca {@code reportes.get(tipo).generar(filtro)}: validar filtro, obtener datos, calcular totales y armar
     * resultado.
     *
     * <p>Permiso: {@code GENERAR_REPORTES}.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param tipo Tipo.
     * @param filtro Período y filtros.
     * @return {@code ResultadoReporte}.
     */
    public ResultadoReporte generarReporte(TipoReporte tipo, FiltroReporte filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Genera el reporte y lo pasa a {@code exportadores.get(formato).exportar(resultado)}.
     *
     * <p>Permiso: {@code GENERAR_REPORTES}.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param tipo Tipo.
     * @param filtro Filtro.
     * @param formato EXCEL o PDF.
     * @return {@code ArchivoExportado} (nombre, tipo MIME, bytes).
     */
    public ArchivoExportado exportarReporte(TipoReporte tipo, FiltroReporte filtro, FormatoExportacion formato) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

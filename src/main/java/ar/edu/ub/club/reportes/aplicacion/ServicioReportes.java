package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada de reportes (patrón Facade, GRASP Controlador de los casos de uso de reportes). Elige la subclase de {@code
 * Reporte} (Template Method) según el tipo y el {@code ExportadorReporte} (Strategy) según el formato, sin
 * condicionales sobre el tipo. Cada método público es una transacción de solo lectura y su primera instrucción es
 * {@code controlAcceso.verificar(usuario, GENERAR_REPORTES)} (RNF-04).
 *
 * <p>Casos de uso: CU-38, CU-39
 */
@Service
@Transactional(readOnly = true)
public class ServicioReportes {

    /** Reportes disponibles, uno por tipo. */
    private final Map<TipoReporte, Reporte> reportes;

    /** Estrategias de exportación, una por formato. */
    private final Map<FormatoExportacion, ExportadorReporte> exportadores;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea la fachada y arma los mapas {@code reportes} (uso de canchas y cuotas y deudores) y {@code exportadores}
     * (Excel y PDF) con los beans recibidos.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param reporteUsoCanchas reporte de uso de canchas
     * @param reporteCuotasDeudores reporte de cuotas y deudores
     * @param exportadorExcel estrategia de exportación a Excel
     * @param exportadorPdf estrategia de exportación a PDF
     * @param controlAcceso control de acceso
     * @param usuarioActual cuenta de la sesión
     */
    public ServicioReportes(ReporteUsoCanchas reporteUsoCanchas, ReporteCuotasDeudores reporteCuotasDeudores,
                            ExportadorExcel exportadorExcel, ExportadorPdf exportadorPdf,
                            ControlAcceso controlAcceso, UsuarioActual usuarioActual) {
        this.reportes = new EnumMap<>(TipoReporte.class);
        this.reportes.put(TipoReporte.USO_CANCHAS, reporteUsoCanchas);
        this.reportes.put(TipoReporte.CUOTAS_DEUDORES, reporteCuotasDeudores);
        this.exportadores = new EnumMap<>(FormatoExportacion.class);
        this.exportadores.put(FormatoExportacion.EXCEL, exportadorExcel);
        this.exportadores.put(FormatoExportacion.PDF, exportadorPdf);
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Invoca {@code reportes.get(tipo).generar(filtro)}: validar filtro, obtener datos, calcular totales y armar
     * resultado (sec_cu38_p3, sec_cu39_p2).
     *
     * <p>Permiso: GENERAR_REPORTES · Transacción: solo lectura.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param tipo tipo de reporte
     * @param filtro período y filtros
     * @return el resultado del reporte
     * @throws ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException sin el permiso
     * @throws ar.edu.ub.club.compartido.excepciones.ErrorNegocio si el filtro es inválido (FA1)
     */
    public ResultadoReporte generarReporte(TipoReporte tipo, FiltroReporte filtro) {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.GENERAR_REPORTES);
        Reporte r = reportes.get(tipo);
        return r.generar(filtro);
    }

    /**
     * Genera el reporte y lo pasa a {@code exportadores.get(formato).exportar(resultado)} (sec_cu38_p4).
     *
     * <p>Permiso: GENERAR_REPORTES · Transacción: solo lectura.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param tipo tipo de reporte
     * @param filtro filtro (el último con que se generó en pantalla)
     * @param formato EXCEL o PDF
     * @return archivo con nombre, tipo MIME y bytes
     * @throws ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException sin el permiso
     * @throws ar.edu.ub.club.compartido.excepciones.ErrorNegocio si el filtro es inválido o falla la exportación
     *     (FA3)
     */
    public ArchivoExportado exportarReporte(TipoReporte tipo, FiltroReporte filtro, FormatoExportacion formato) {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.GENERAR_REPORTES);
        ResultadoReporte resultado = reportes.get(tipo).generar(filtro);
        ExportadorReporte e = exportadores.get(formato);
        return e.exportar(resultado);
    }
}

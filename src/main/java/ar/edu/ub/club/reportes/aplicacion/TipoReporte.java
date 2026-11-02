package ar.edu.ub.club.reportes.aplicacion;

/**
 * Tipos de reporte que ofrece el sistema. Es la clave del mapa {@code ServicioReportes.reportes}: cada valor tiene su
 * subclase concreta de {@code Reporte} (patrón Template Method). También lo usa el panel P-04 para navegar al reporte
 * elegido.
 *
 * <p>Casos de uso: CU-38, CU-39, CU-40
 */
public enum TipoReporte {

    /** Reporte de uso de canchas (P-32, CU-38), resuelto por {@code ReporteUsoCanchas}. */
    USO_CANCHAS,
    /** Reporte de cuotas y deudores (P-33, CU-39), resuelto por {@code ReporteCuotasDeudores}. */
    CUOTAS_DEUDORES;
}

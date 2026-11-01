package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.reportes.aplicacion.ServicioDashboard;
import ar.edu.ub.club.reportes.aplicacion.TipoReporte;

/**
 * Presentador de la pantalla P-04: Panel de inicio del administrador con los indicadores del club y accesos
 * directos a reportes.
 *
 * <p>Casos de uso: CU-40
 */
public class PresentadorPanelAdministracion extends PresentadorBase<PantallaPanelAdministracion> {

    /** Atributo servicioDashboard. */
    private ServicioDashboard servicioDashboard;

    /** Atributo reloj. */
    private Reloj reloj;

    /**
     * {@code ServicioDashboard.obtenerIndicadores(reloj.hoy()): Indicadores} → {@code
     * ResumenIndicadores.desde(indicadores)}
     *
     * <p>Casos de uso: CU-40
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("reportes/uso-canchas")} o {@code navegarA("reportes/cuotas")}
     *
     * <p>Casos de uso: CU-40
     * @param tipo valor de {@code tipo}
     */
    public void alIrAReporte(TipoReporte tipo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

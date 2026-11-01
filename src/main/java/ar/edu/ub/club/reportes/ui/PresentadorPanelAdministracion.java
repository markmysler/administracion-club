package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.reportes.aplicacion.ServicioDashboard;
import ar.edu.ub.club.reportes.aplicacion.TipoReporte;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-04 Panel de administración (MVP Vista Pasiva; no usa tipos de Vaadin). Obtiene la
 * fecha del {@code Reloj}, pide los indicadores a {@code ServicioDashboard} dentro de {@code consultar(...)} y los
 * entrega como {@code ResumenIndicadores}; atiende los accesos directos a los reportes. Diagrama sec_cu40.
 *
 * <p>Casos de uso: CU-40
 */
@SpringComponent
@UIScope
public class PresentadorPanelAdministracion extends PresentadorBase<PantallaPanelAdministracion> {

    /** Fachada del panel de administración. */
    private final ServicioDashboard servicioDashboard;

    /** Reloj del sistema (fecha de referencia, paso 2). */
    private final Reloj reloj;

    /**
     * Crea el presentador con sus dependencias.
     *
     * <p>Casos de uso: CU-40
     * @param servicioDashboard fachada del panel
     * @param reloj reloj del sistema
     */
    public PresentadorPanelAdministracion(ServicioDashboard servicioDashboard, Reloj reloj) {
        this.servicioDashboard = servicioDashboard;
        this.reloj = reloj;
    }

    /**
     * Carga el panel: {@code ServicioDashboard.obtenerIndicadores(reloj.hoy())} → {@code ResumenIndicadores.desde}
     * → {@code pantalla.mostrarIndicadores} (pasos 2 a 8).
     *
     * <p>Casos de uso: CU-40
     */
    public void cargar() {
        consultar(() -> servicioDashboard.obtenerIndicadores(reloj.hoy()))
                .map(ResumenIndicadores::desde)
                .ifPresent(pantalla::mostrarIndicadores);
    }

    /**
     * Abre el reporte relacionado con el indicador elegido (pasos 9 y 10): {@code navegarA("reportes/uso-canchas")}
     * o {@code navegarA("reportes/cuotas")}. Los reportes se abren con el mes en curso.
     *
     * <p>Casos de uso: CU-40
     * @param tipo reporte a abrir
     */
    public void alIrAReporte(TipoReporte tipo) {
        if (tipo == TipoReporte.USO_CANCHAS) {
            pantalla.navegarA("reportes/uso-canchas");
        } else {
            pantalla.navegarA("reportes/cuotas");
        }
    }
}

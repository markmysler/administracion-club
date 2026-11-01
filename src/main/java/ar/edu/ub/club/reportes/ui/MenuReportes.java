package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Proveedor de las opciones de menú del módulo reportes (MODELO 11.4.1): Panel ({@code panel},
 * CONSULTAR_DASHBOARD), Uso de canchas ({@code reportes/uso-canchas}, GENERAR_REPORTES) y Cuotas y deudores ({@code
 * reportes/cuotas}, GENERAR_REPORTES). {@code MenuNavegacion} muestra solo las que la cuenta tiene permitidas.
 *
 * <p>Casos de uso: CU-38, CU-39, CU-40
 */
@Component
public class MenuReportes implements ProveedorOpcionesMenu {

    /**
     * Crea el proveedor (sin estado).
     *
     * <p>Casos de uso: CU-38, CU-39, CU-40
     */
    public MenuReportes() {
    }

    /**
     * Devuelve las opciones del módulo, en el área «Reportes» (el panel va primero en el menú).
     *
     * <p>Casos de uso: CU-38, CU-39, CU-40
     * @return opciones de menú
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(
                new OpcionMenu("Panel", "panel", Permiso.CONSULTAR_DASHBOARD, "Inicio", 1),
                new OpcionMenu("Uso de canchas", "reportes/uso-canchas", Permiso.GENERAR_REPORTES, "Reportes", 90),
                new OpcionMenu("Cuotas y deudores", "reportes/cuotas", Permiso.GENERAR_REPORTES, "Reportes", 91));
    }
}

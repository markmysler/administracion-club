package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Aporta al menú común la opción «Estado de cuenta» (P-25), visible con CONSULTAR_PERFIL_PROPIO, en el área
 * «Pagos».
 *
 * <p>Proveedor de las opciones de menú del módulo ({@code ProveedorOpcionesMenu}).
 *
 * <p>Casos de uso: CU-05, CU-35
 */
@Component
public class MenuCuotas implements ProveedorOpcionesMenu {

    /**
     * Opciones del módulo cuotas.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @return «Estado de cuenta» → {@code estado-cuenta}
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(new OpcionMenu("Estado de cuenta", "estado-cuenta", Permiso.CONSULTAR_PERFIL_PROPIO, "Pagos", 79));
    }
}

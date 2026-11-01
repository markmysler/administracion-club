package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Opciones de menú del módulo accesos: «Cuentas de acceso» → {@code cuentas} (GESTIONAR_CUENTAS). La opción
 * «Cerrar sesión» (P-35) la agrega {@code MenuNavegacion} y la atiende {@code PresentadorCierreSesion}.
 *
 * <p>Proveedor de las opciones de menú del módulo ({@code ProveedorOpcionesMenu}).
 *
 * <p>Casos de uso: CU-14, CU-15, CU-17
 */
@Component
public class MenuAccesos implements ProveedorOpcionesMenu {

    /**
     * Devuelve la opción del módulo (área «Administración»).
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @return opciones
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(new OpcionMenu("Cuentas de acceso", "cuentas", Permiso.GESTIONAR_CUENTAS,
                "Administración", 90));
    }
}

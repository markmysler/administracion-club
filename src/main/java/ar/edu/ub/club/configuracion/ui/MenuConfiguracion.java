package ar.edu.ub.club.configuracion.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Opciones de menú del módulo configuracion: «Configuración del club» → {@code configuracion-club}
 * (CONFIGURAR_CLUB). {@code MenuNavegacion} muestra solo las opciones cuyo permiso tiene la cuenta.
 *
 * <p>Proveedor de las opciones de menú del módulo ({@code ProveedorOpcionesMenu}).
 *
 * <p>Casos de uso: CU-41
 */
@Component
public class MenuConfiguracion implements ProveedorOpcionesMenu {

    /**
     * Devuelve la opción del módulo (área «Administración»).
     *
     * <p>Casos de uso: CU-41
     * @return opciones
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(new OpcionMenu("Configuración del club", "configuracion-club", Permiso.CONFIGURAR_CLUB,
                "Administración", 92));
    }
}

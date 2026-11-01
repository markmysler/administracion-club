package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Opciones de menú del módulo recursos (MODELO §11): Deportes → {@code deportes} y Canchas → {@code canchas},
 * ambas con {@code GESTIONAR_RECURSOS}. {@code MenuNavegacion} reúne todos los proveedores y muestra solo las
 * opciones que permite la cuenta.
 *
 * <p>Proveedor de las opciones de menú del módulo ({@code ProveedorOpcionesMenu}).
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-23
 */
@Component
public class MenuRecursos implements ProveedorOpcionesMenu {

    /** Área del menú en la que se agrupan las opciones. */
    private static final String AREA = "Instalaciones";

    /**
     * Devuelve las dos opciones del módulo.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-23
     * @return opciones de menú
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(
                new OpcionMenu("Deportes", "deportes", Permiso.GESTIONAR_RECURSOS, AREA, 10),
                new OpcionMenu("Canchas", "canchas", Permiso.GESTIONAR_RECURSOS, AREA, 20));
    }
}

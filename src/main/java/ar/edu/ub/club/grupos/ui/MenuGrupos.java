package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Proveedor de las opciones de menú del módulo grupos ({@code ProveedorOpcionesMenu}): «Grupos familiares» →
 * {@code grupos} con {@code GESTIONAR_GRUPOS} (MODELO §11.4.1). {@code MenuNavegacion} recibe todos los
 * proveedores por Spring, así {@code compartido.ui} no depende del módulo (D-10).
 *
 * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
 */
@Component
public class MenuGrupos implements ProveedorOpcionesMenu {

    /**
     * Opciones del módulo, en el área «Socios» junto a las de {@code MenuSocios}.
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     * @return opciones de menú
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(new OpcionMenu("Grupos familiares", "grupos", Permiso.GESTIONAR_GRUPOS, "Socios", 30));
    }
}

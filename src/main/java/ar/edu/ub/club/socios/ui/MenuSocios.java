package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Proveedor de las opciones de menú del módulo socios ({@code ProveedorOpcionesMenu}, MODELO §11.4.1): Inicio y
 * Mi perfil (socio, {@code CONSULTAR_PERFIL_PROPIO}), Socios ({@code GESTIONAR_SOCIOS}) y Categorías
 * ({@code GESTIONAR_CATEGORIAS}). {@code MenuNavegacion} muestra solo las que la cuenta tiene permitidas.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-03, CU-04, CU-05, CU-06
 */
@Component
public class MenuSocios implements ProveedorOpcionesMenu {

    /**
     * Opciones del módulo (área «Mi club» para el socio y «Socios» para el administrador).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-03, CU-04, CU-05, CU-06
     * @return opciones de menú
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(
                new OpcionMenu("Inicio", "inicio", Permiso.CONSULTAR_PERFIL_PROPIO, "Mi club", 10),
                new OpcionMenu("Mi perfil", "mi-perfil", Permiso.CONSULTAR_PERFIL_PROPIO, "Mi club", 20),
                new OpcionMenu("Socios", "socios", Permiso.GESTIONAR_SOCIOS, "Socios", 10),
                new OpcionMenu("Categorías", "categorias", Permiso.GESTIONAR_CATEGORIAS, "Socios", 20));
    }
}

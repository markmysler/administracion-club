package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Proveedor de las opciones de menú del módulo reservas ({@code ProveedorOpcionesMenu}): Turnos disponibles →
 * {@code turnos} (CONSULTAR_DISPONIBILIDAD); Mis reservas → {@code mis-reservas} (GESTIONAR_RESERVAS_PROPIAS);
 * Reservas del club → {@code reservas-club} (GESTIONAR_RESERVAS_CLUB). {@code MenuNavegacion} muestra solo las que
 * la cuenta tiene permitidas.
 *
 * <p>Casos de uso: CU-26, CU-29, CU-31
 */
@Component
public class MenuReservas implements ProveedorOpcionesMenu {

    /**
     * Crea el proveedor (bean de Spring).
     *
     * <p>Casos de uso: ninguno (configuración)
     */
    public MenuReservas() {
        // sin dependencias
    }

    /**
     * Opciones del módulo, en el área «Reservas».
     *
     * <p>Casos de uso: CU-26, CU-29, CU-31
     * @return opciones con su ruta y su permiso
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(
                new OpcionMenu("Turnos disponibles", "turnos", Permiso.CONSULTAR_DISPONIBILIDAD, "Reservas", 10),
                new OpcionMenu("Mis reservas", "mis-reservas", Permiso.GESTIONAR_RESERVAS_PROPIAS, "Reservas", 20),
                new OpcionMenu("Reservas del club", "reservas-club", Permiso.GESTIONAR_RESERVAS_CLUB, "Reservas", 30));
    }
}

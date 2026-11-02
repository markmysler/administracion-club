package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Opciones de menú del módulo agenda (MODELO §11): Disponibilidad del club → {@code agenda/disponibilidad} y
 * Reglas de reserva → {@code agenda/reglas}, ambas con {@code CONFIGURAR_AGENDA}.
 *
 * <p>Proveedor de las opciones de menú del módulo ({@code ProveedorOpcionesMenu}).
 *
 * <p>Casos de uso: CU-24, CU-25
 */
@Component
public class MenuAgenda implements ProveedorOpcionesMenu {

    /** Área del menú en la que se agrupan las opciones (la misma que recursos). */
    private static final String AREA = "Instalaciones";

    /**
     * Devuelve las dos opciones del módulo.
     *
     * <p>Casos de uso: CU-24, CU-25
     * @return opciones de menú
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(
                new OpcionMenu("Disponibilidad del club", "agenda/disponibilidad", Permiso.CONFIGURAR_AGENDA, AREA, 30),
                new OpcionMenu("Reglas de reserva", "agenda/reglas", Permiso.CONFIGURAR_AGENDA, AREA, 40));
    }
}

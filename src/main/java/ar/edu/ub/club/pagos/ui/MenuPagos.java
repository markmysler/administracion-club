package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionMenu;
import ar.edu.ub.club.compartido.ui.ProveedorOpcionesMenu;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Proveedor de las opciones de menú del módulo pagos (MODELO 11.4.1): Mis pagos y comprobantes ({@code pagos},
 * CONSULTAR_PAGOS_PROPIOS), Pagos y comprobantes ({@code pagos}, CONSULTAR_PAGOS_CLUB), Pago manual ({@code
 * pagos/manual}, REGISTRAR_PAGO_MANUAL) y Medios de pago y pasarela ({@code pagos/configuracion}, CONFIGURAR_PAGOS).
 * Ninguna cuenta tiene a la vez los dos permisos de consulta, así que la ruta {@code pagos} aparece una sola vez.
 *
 * <p>Casos de uso: CU-34, CU-36, CU-37
 */
@Component
public class MenuPagos implements ProveedorOpcionesMenu {

    /**
     * Crea el proveedor (sin estado).
     *
     * <p>Casos de uso: CU-34, CU-36, CU-37
     */
    public MenuPagos() {
    }

    /**
     * Devuelve las opciones del módulo, en el área «Pagos».
     *
     * <p>Casos de uso: CU-34, CU-36, CU-37
     * @return opciones de menú
     */
    @Override
    public List<OpcionMenu> opciones() {
        return List.of(
                new OpcionMenu("Mis pagos y comprobantes", "pagos", Permiso.CONSULTAR_PAGOS_PROPIOS, "Pagos", 80),
                new OpcionMenu("Pagos y comprobantes", "pagos", Permiso.CONSULTAR_PAGOS_CLUB, "Pagos", 80),
                new OpcionMenu("Pago manual", "pagos/manual", Permiso.REGISTRAR_PAGO_MANUAL, "Pagos", 81),
                new OpcionMenu("Medios de pago y pasarela", "pagos/configuracion", Permiso.CONFIGURAR_PAGOS,
                        "Pagos", 82));
    }
}

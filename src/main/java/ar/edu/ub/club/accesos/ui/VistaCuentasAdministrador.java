package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-13: Listado de cuentas de administrador con alta y desactivación, y búsqueda de cuentas
 * de cualquier tipo (columna Tipo de cuenta) para modificar sus datos de acceso (CU-15).
 *
 * <p>Ruta: {@code cuentas}. Permiso: {@code GESTIONAR_CUENTAS}.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-17
 */
public class VistaCuentasAdministrador extends VistaProtegida implements PantallaCuentasAdministrador {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarCuentas(List<FilaCuenta> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

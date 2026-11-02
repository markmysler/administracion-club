package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-13: Listado de cuentas de administrador con alta y desactivación, y búsqueda
 * de cuentas de cualquier tipo (columna Tipo de cuenta) para modificar sus datos de acceso (CU-15). MVP, Vista
 * Pasiva.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-17
 */
public interface PantallaCuentasAdministrador extends PantallaBase {

    /**
     * Muestra las filas en la tabla de cuentas.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param filas filas a mostrar
     */
    void mostrarCuentas(List<FilaCuenta> filas);
}

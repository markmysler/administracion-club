package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.DatosAcceso;
import ar.edu.ub.club.accesos.aplicacion.DatosAdministrador;
import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-14: Formulario de cuenta de acceso: alta de administrador o modificación de
 * datos de acceso de cualquier cuenta (MVP, Vista Pasiva).
 *
 * <p>Casos de uso: CU-14, CU-15, CU-16
 */
public interface PantallaFormularioCuenta extends PantallaBase {

    /**
     * Muestra el formulario de alta de administrador (nombre, apellido, usuario, correo y clave inicial; sin
     * campo de tipo de cuenta, CU-16).
     *
     * <p>Casos de uso: CU-14, CU-16
     * @param datos datos iniciales (vacíos)
     */
    void mostrarAlta(DatosAdministrador datos);

    /**
     * Muestra el formulario de modificación de datos de acceso y, de solo lectura, el tipo y el estado de la
     * cuenta.
     *
     * <p>Casos de uso: CU-15, CU-16
     * @param datos datos de acceso precargados
     * @param cuenta detalle de solo lectura
     */
    void mostrarModificacion(DatosAcceso datos, DetalleCuenta cuenta);
}

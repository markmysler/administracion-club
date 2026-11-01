package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.DatosAcceso;
import ar.edu.ub.club.accesos.aplicacion.DatosAdministrador;
import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-14: Formulario de cuenta de acceso: alta de administrador o modificación de
 * datos de acceso de cualquier cuenta.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-16
 */
public interface PantallaFormularioCuenta extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param datos valor de {@code datos}
     */
    void mostrarAlta(DatosAdministrador datos);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param datos valor de {@code datos}
     * @param cuenta valor de {@code cuenta}
     */
    void mostrarModificacion(DatosAcceso datos, DetalleCuenta cuenta);
}

package ar.edu.ub.club.configuracion.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.configuracion.aplicacion.DatosConfiguracionClub;

/**
 * Interfaz pasiva de la pantalla P-34: Formulario de datos institucionales del club (MVP, Vista Pasiva).
 *
 * <p>Casos de uso: CU-41
 */
public interface PantallaConfiguracionClub extends PantallaBase {

    /**
     * Carga los datos del club en el formulario.
     *
     * <p>Casos de uso: CU-41
     * @param datos datos a mostrar
     */
    void mostrarDatos(DatosConfiguracionClub datos);
}

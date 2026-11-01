package ar.edu.ub.club.configuracion.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.configuracion.aplicacion.DatosConfiguracionClub;

/**
 * Interfaz pasiva de la pantalla P-34: Formulario de datos institucionales del club.
 *
 * <p>Casos de uso: todos
 */
public interface PantallaConfiguracionClub extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-41
     * @param datos valor de {@code datos}
     */
    void mostrarDatos(DatosConfiguracionClub datos);
}

package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosReglaReserva;
import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-18: Formulario de las seis reglas de reserva.
 *
 * <p>Casos de uso: CU-25
 */
public interface PantallaReglasReserva extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-25
     * @param datos valor de {@code datos}
     */
    void mostrarReglas(DatosReglaReserva datos);
}

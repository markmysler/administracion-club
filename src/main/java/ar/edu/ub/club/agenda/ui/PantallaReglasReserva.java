package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosReglaReserva;
import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-18: Formulario de las seis reglas de reserva. Patrón MVP (Vista Pasiva):
 * {@code PresentadorReglasReserva} solo conoce esta interfaz.
 *
 * <p>Casos de uso: CU-25
 */
public interface PantallaReglasReserva extends PantallaBase {

    /**
     * Muestra las reglas vigentes en el formulario (CU-25 paso 2 y después de guardar).
     *
     * <p>Casos de uso: CU-25
     * @param datos reglas vigentes
     */
    void mostrarReglas(DatosReglaReserva datos);
}

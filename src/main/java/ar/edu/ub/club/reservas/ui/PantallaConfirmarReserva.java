package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-20 (MVP, Vista Pasiva): diálogo que muestra cancha, fecha y horario elegidos y
 * confirma la reserva.
 *
 * <p>Casos de uso: CU-27, CU-28
 */
public interface PantallaConfirmarReserva extends PantallaBase {

    /**
     * Muestra el turno a confirmar y, en una reserva manual, el socio.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param turno turno elegido
     * @param socio descripción del socio elegido o {@code null}
     */
    void mostrarTurno(FilaTurno turno, String socio);

    /**
     * Cierra el diálogo y avisa a la vista anfitriona ({@code DialogoResueltoEvento}).
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    void cerrarConExito();
}

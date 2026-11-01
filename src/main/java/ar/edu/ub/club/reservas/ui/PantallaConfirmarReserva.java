package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-20: Diálogo que muestra cancha, fecha y horario elegidos y confirma la
 * reserva.
 *
 * <p>Casos de uso: CU-27, CU-28
 */
public interface PantallaConfirmarReserva extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param turno valor de {@code turno}
     * @param socio valor de {@code socio}
     */
    void mostrarTurno(FilaTurno turno, String socio);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    void cerrarConExito();
}

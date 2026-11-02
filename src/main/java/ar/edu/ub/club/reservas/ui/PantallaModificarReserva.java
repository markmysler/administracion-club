package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-23 (MVP, Vista Pasiva): formulario para cambiar fecha, horario o cancha de una
 * reserva, con consulta de turnos libres.
 *
 * <p>Casos de uso: CU-32
 */
public interface PantallaModificarReserva extends PantallaBase {

    /**
     * Muestra los datos actuales de la reserva y las canchas de su deporte.
     *
     * <p>Casos de uso: CU-32
     * @param detalle detalle de la reserva
     * @param canchas canchas del deporte
     */
    void mostrarReserva(DetalleReserva detalle, List<OpcionSeleccion> canchas);

    /**
     * Muestra los turnos de la fecha y cancha consultadas.
     *
     * <p>Casos de uso: CU-32
     * @param filas turnos
     */
    void mostrarTurnos(List<FilaTurno> filas);
}

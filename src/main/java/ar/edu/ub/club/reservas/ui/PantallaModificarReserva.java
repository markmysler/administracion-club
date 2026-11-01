package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-23: Formulario para cambiar fecha, horario o cancha de una reserva, con
 * consulta de turnos libres.
 *
 * <p>Casos de uso: CU-32
 */
public interface PantallaModificarReserva extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-32
     * @param detalle valor de {@code detalle}
     * @param canchas valor de {@code canchas}
     */
    void mostrarReserva(DetalleReserva detalle, List<OpcionSeleccion> canchas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-32
     * @param filas valor de {@code filas}
     */
    void mostrarTurnos(List<FilaTurno> filas);
}

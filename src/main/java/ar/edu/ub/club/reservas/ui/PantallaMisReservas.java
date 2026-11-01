package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-21 (MVP, Vista Pasiva): reservas propias vigentes e históricas con acción de
 * cancelar dentro del plazo.
 *
 * <p>Casos de uso: CU-29, CU-30
 */
public interface PantallaMisReservas extends PantallaBase {

    /**
     * Muestra las reservas del socio.
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param filas reservas (vigentes primero)
     */
    void mostrarReservas(List<FilaReserva> filas);

    /**
     * Muestra el detalle de una reserva con sus notificaciones.
     *
     * <p>Casos de uso: CU-29
     * @param detalle detalle de la reserva
     */
    void mostrarDetalle(DetalleReserva detalle);
}

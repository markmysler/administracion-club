package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-22 (MVP, Vista Pasiva): reservas del club con filtros, acceso a modificar y
 * cancelación administrativa con motivo.
 *
 * <p>Casos de uso: CU-31, CU-32, CU-33
 */
public interface PantallaReservasClub extends PantallaBase {

    /**
     * Muestra el filtro de deporte.
     *
     * <p>Casos de uso: CU-31
     * @param deportes deportes
     */
    void mostrarFiltros(List<OpcionSeleccion> deportes);

    /**
     * Muestra las reservas que cumplen el filtro.
     *
     * <p>Casos de uso: CU-31, CU-33
     * @param filas reservas
     */
    void mostrarReservas(List<FilaReserva> filas);

    /**
     * Muestra el detalle de una reserva (quién registró o canceló, motivo y notificaciones).
     *
     * <p>Casos de uso: CU-31
     * @param detalle detalle de la reserva
     */
    void mostrarDetalle(DetalleReserva detalle);

    /**
     * Abre el diálogo que pide el motivo obligatorio de la cancelación administrativa.
     *
     * <p>Casos de uso: CU-33
     * @param idReserva reserva a cancelar
     */
    void pedirMotivoCancelacion(Long idReserva);
}

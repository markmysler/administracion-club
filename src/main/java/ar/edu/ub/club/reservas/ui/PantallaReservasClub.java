package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-22: Reservas del club con filtros, acceso a modificar y cancelación
 * administrativa con motivo.
 *
 * <p>Casos de uso: CU-31, CU-32, CU-33
 */
public interface PantallaReservasClub extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-33
     * @param deportes valor de {@code deportes}
     */
    void mostrarFiltros(List<OpcionSeleccion> deportes);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-33
     * @param filas valor de {@code filas}
     */
    void mostrarReservas(List<FilaReserva> filas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-33
     * @param detalle valor de {@code detalle}
     */
    void mostrarDetalle(DetalleReserva detalle);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31, CU-33
     * @param idReserva valor de {@code idReserva}
     */
    void pedirMotivoCancelacion(Long idReserva);
}

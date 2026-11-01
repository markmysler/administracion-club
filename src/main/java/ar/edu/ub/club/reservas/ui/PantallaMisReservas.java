package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-21: Reservas propias vigentes e históricas con acción de cancelar dentro del
 * plazo.
 *
 * <p>Casos de uso: CU-29, CU-30
 */
public interface PantallaMisReservas extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param filas valor de {@code filas}
     */
    void mostrarReservas(List<FilaReserva> filas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-29, CU-30
     * @param detalle valor de {@code detalle}
     */
    void mostrarDetalle(DetalleReserva detalle);
}

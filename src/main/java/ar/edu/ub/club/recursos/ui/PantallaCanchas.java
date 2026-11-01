package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.recursos.aplicacion.DatosCancha;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-16: Listado de canchas por deporte con alta, edición y desactivación con
 * advertencia.
 *
 * <p>Casos de uso: CU-21, CU-22, CU-23
 */
public interface PantallaCanchas extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param opciones valor de {@code opciones}
     */
    void mostrarDeportes(List<OpcionSeleccion> opciones);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param filas valor de {@code filas}
     */
    void mostrarCanchas(List<FilaCancha> filas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param datos valor de {@code datos}
     * @param idCancha valor de {@code idCancha}
     */
    void editar(DatosCancha datos, Long idCancha);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param idCancha valor de {@code idCancha}
     * @param afectadas valor de {@code afectadas}
     */
    void pedirConfirmacionBaja(Long idCancha, List<FilaResumenReserva> afectadas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param afectadas valor de {@code afectadas}
     */
    void pedirConfirmacionCambio(List<FilaResumenReserva> afectadas);
}

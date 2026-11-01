package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.recursos.aplicacion.DatosDeporte;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-15: Listado de deportes con alta, edición y desactivación con advertencia.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20
 */
public interface PantallaDeportes extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param filas valor de {@code filas}
     */
    void mostrarDeportes(List<FilaDeporte> filas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param datos valor de {@code datos}
     * @param idDeporte valor de {@code idDeporte}
     */
    void editar(DatosDeporte datos, Long idDeporte);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param idDeporte valor de {@code idDeporte}
     * @param afectadas valor de {@code afectadas}
     */
    void pedirConfirmacionBaja(Long idDeporte, List<FilaResumenReserva> afectadas);
}

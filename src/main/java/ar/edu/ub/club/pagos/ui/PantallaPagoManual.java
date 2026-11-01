package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-30: Registro de un pago manual: búsqueda del grupo, elección de la cuota
 * impaga, medio habilitado e importe (igual al importe a pagar de la cuota).
 *
 * <p>Casos de uso: CU-36
 */
public interface PantallaPagoManual extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @param opciones valor de {@code opciones}
     */
    void mostrarMedios(List<OpcionSeleccion> opciones);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @param filas valor de {@code filas}
     */
    void mostrarGrupos(List<FilaGrupo> filas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @param filas valor de {@code filas}
     */
    void mostrarCuotas(List<FilaCuota> filas);
}

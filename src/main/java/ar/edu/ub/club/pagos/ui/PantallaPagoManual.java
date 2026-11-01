package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import java.util.List;

/**
 * Interfaz pasiva (MVP Vista Pasiva) de la pantalla P-30 Pago manual.
 *
 * <p>Casos de uso: CU-36
 */
public interface PantallaPagoManual extends PantallaBase {

    /**
     * Carga el combo de medios habilitados; vacío deshabilita el formulario (FA3).
     *
     * <p>Casos de uso: CU-36
     * @param opciones medios habilitados
     */
    void mostrarMedios(List<OpcionSeleccion> opciones);

    /**
     * Muestra los grupos vigentes encontrados (paso 2).
     *
     * <p>Casos de uso: CU-36
     * @param filas grupos
     */
    void mostrarGrupos(List<FilaGrupo> filas);

    /**
     * Muestra las cuotas impagas del grupo elegido (paso 4).
     *
     * <p>Casos de uso: CU-36
     * @param filas cuotas pendientes y vencidas, más antigua primero
     */
    void mostrarCuotas(List<FilaCuota> filas);
}

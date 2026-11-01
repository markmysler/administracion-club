package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.pagos.aplicacion.DatosConfiguracionPagos;

/**
 * Interfaz pasiva de la pantalla P-31: Configuración de medios manuales y de la pasarela (modo y habilitación).
 *
 * <p>Casos de uso: todos
 */
public interface PantallaMediosPago extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-34
     * @param datos valor de {@code datos}
     * @param pasarela valor de {@code pasarela}
     */
    void mostrarConfiguracion(DatosConfiguracionPagos datos, ResumenPasarela pasarela);
}

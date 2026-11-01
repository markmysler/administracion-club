package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.pagos.aplicacion.DatosConfiguracionPagos;

/**
 * Interfaz pasiva (MVP Vista Pasiva) de la pantalla P-31 Medios de pago y pasarela.
 *
 * <p>Casos de uso: CU-34
 */
public interface PantallaMediosPago extends PantallaBase {

    /**
     * Carga el formulario con los medios y la pasarela, y muestra el estado de la pasarela (paso 2 y paso 11).
     *
     * <p>Casos de uso: CU-34
     * @param datos datos editables de la configuración
     * @param pasarela estado de la pasarela (solo lectura)
     */
    void mostrarConfiguracion(DatosConfiguracionPagos datos, ResumenPasarela pasarela);
}

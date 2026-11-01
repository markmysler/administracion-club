package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva (MVP Vista Pasiva) de la pantalla imprimible P-29 Comprobante.
 *
 * <p>Casos de uso: CU-05, CU-36, CU-37
 */
public interface PantallaComprobante extends PantallaBase {

    /**
     * Muestra el comprobante o la constancia de acreditación (paso 11 de CU-37).
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     * @param detalle detalle del comprobante
     */
    void mostrarComprobante(DetalleComprobante detalle);

    /**
     * Abre el diálogo de impresión del navegador.
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     */
    void imprimir();
}

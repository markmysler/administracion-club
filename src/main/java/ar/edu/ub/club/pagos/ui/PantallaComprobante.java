package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-29: Vista imprimible de un comprobante.
 *
 * <p>Casos de uso: todos
 */
public interface PantallaComprobante extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param detalle valor de {@code detalle}
     */
    void mostrarComprobante(DetalleComprobante detalle);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     */
    void imprimir();
}

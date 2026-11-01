package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-26: Página de retorno desde la pasarela.
 *
 * <p>Casos de uso: CU-35
 */
public interface PantallaResultadoPago extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param resumen valor de {@code resumen}
     */
    void mostrarEstado(ResumenPagoOnline resumen);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     */
    void mostrarEsperaConfirmacion();
}

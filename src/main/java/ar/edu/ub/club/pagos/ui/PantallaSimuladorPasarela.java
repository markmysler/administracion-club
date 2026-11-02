package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva (MVP Vista Pasiva) de la pantalla P-27 Simulador de pasarela (solo modo SIMULADA).
 *
 * <p>Casos de uso: CU-35
 */
public interface PantallaSimuladorPasarela extends PantallaBase {

    /**
     * Muestra concepto, importe y vencimiento de la intención y ofrece Aprobar y Rechazar.
     *
     * <p>Casos de uso: CU-35
     * @param resumen intención de pago
     */
    void mostrarIntencion(ResumenIntencionSimulada resumen);

    /**
     * Lleva el navegador a una dirección (la {@code urlRetorno} de la pasarela, paso 23).
     *
     * <p>Casos de uso: CU-35
     * @param url dirección destino
     */
    void redirigirA(String url);
}

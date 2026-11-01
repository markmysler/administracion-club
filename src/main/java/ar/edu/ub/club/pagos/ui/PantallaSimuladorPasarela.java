package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-27: Página del simulador de pasarela (solo modo SIMULADA): muestra concepto e
 * importe y ofrece aprobar o rechazar.
 *
 * <p>Casos de uso: CU-35
 */
public interface PantallaSimuladorPasarela extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param resumen valor de {@code resumen}
     */
    void mostrarIntencion(ResumenIntencionSimulada resumen);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param url valor de {@code url}
     */
    void redirigirA(String url);
}

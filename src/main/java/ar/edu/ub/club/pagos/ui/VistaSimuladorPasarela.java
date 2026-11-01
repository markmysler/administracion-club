package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.VistaBase;

/**
 * Vista de la pantalla P-27: Página del simulador de pasarela (solo modo SIMULADA): muestra concepto e importe y
 * ofrece aprobar o rechazar.
 *
 * <p>Ruta: {@code simulador-pasarela/:idPreferencia}.
 *
 * <p>Casos de uso: CU-35
 */
public class VistaSimuladorPasarela extends VistaBase implements PantallaSimuladorPasarela {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param resumen valor de {@code resumen}
     */
    @Override
    public void mostrarIntencion(ResumenIntencionSimulada resumen) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param url valor de {@code url}
     */
    @Override
    public void redirigirA(String url) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

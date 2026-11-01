package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-34
 */
public record ResumenPasarela(String proveedor, String modo, boolean habilitada, boolean credencialesConfiguradas) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-34
     * @param configuracion valor de {@code configuracion}
     * @return el resultado
     */
    public static ResumenPasarela desde(ConfiguracionPasarela configuracion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

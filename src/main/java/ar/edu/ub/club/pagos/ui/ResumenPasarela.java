package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;

/**
 * Modelo de vista (record inmutable) del estado de la pasarela que muestra P-31: proveedor, modo, si está habilitada
 * y si las credenciales están configuradas en el servidor; las credenciales nunca se muestran (RNF-05, MODELO 11.6.1).
 *
 * <p>Casos de uso: CU-34
 * @param proveedor proveedor de la pasarela
 * @param modo nombre del modo ({@code SIMULADA} o {@code MERCADO_PAGO})
 * @param habilitada si se aceptan pagos online
 * @param credencialesConfiguradas si el servidor tiene credenciales
 */
public record ResumenPasarela(String proveedor, String modo, boolean habilitada, boolean credencialesConfiguradas) {

    /**
     * Fábrica a partir de la configuración de la pasarela.
     *
     * <p>Casos de uso: CU-34
     * @param configuracion configuración de la pasarela
     * @return el resumen
     */
    public static ResumenPasarela desde(ConfiguracionPasarela configuracion) {
        return new ResumenPasarela(configuracion.getProveedor(),
                configuracion.getModo() == null ? null : configuracion.getModo().name(),
                Boolean.TRUE.equals(configuracion.getHabilitada()),
                Boolean.TRUE.equals(configuracion.getCredencialesConfiguradas()));
    }
}

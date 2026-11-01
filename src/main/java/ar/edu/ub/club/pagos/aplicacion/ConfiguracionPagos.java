package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-34
 */
public class ConfiguracionPagos {

    /** Atributo medios. */
    private List<MedioPago> medios;

    /** Atributo pasarela. */
    private ConfiguracionPasarela pasarela;

    /**
     * Crea una instancia de {@code ConfiguracionPagos}.
     *
     * <p>Casos de uso: CU-34
     * @param medios valor de {@code medios}
     * @param pasarela valor de {@code pasarela}
     */
    public ConfiguracionPagos(List<MedioPago> medios, ConfiguracionPasarela pasarela) {
        // PENDIENTE
    }
}

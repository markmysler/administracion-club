package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.pagos.dominio.ModoPasarela;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-34
 */
public class DatosConfiguracionPagos {

    /** Atributo medios. */
    private List<DatosMedioPago> medios;

    /** Atributo modo. */
    private ModoPasarela modo;

    /** Atributo habilitada. */
    private boolean habilitada;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-34
     * @param configuracion valor de {@code configuracion}
     * @return el resultado
     */
    public static DatosConfiguracionPagos desde(ConfiguracionPagos configuracion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

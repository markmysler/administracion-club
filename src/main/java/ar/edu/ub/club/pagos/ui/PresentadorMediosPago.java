package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.pagos.aplicacion.DatosConfiguracionPagos;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;

/**
 * Presentador de la pantalla P-31: Configuración de medios manuales y de la pasarela (modo y habilitación).
 *
 * <p>Casos de uso: todos
 */
public class PresentadorMediosPago extends PresentadorBase<PantallaMediosPago> {

    /** Atributo servicioPagos. */
    private ServicioPagos servicioPagos;

    /**
     * {@code ServicioPagos.consultarMediosYPasarela(): ConfiguracionPagos} → {@code
     * DatosConfiguracionPagos.desde(config)} y {@code ResumenPasarela.desde(config.pasarela)}
     *
     * <p>Casos de uso: CU-34
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioPagos.configurarMediosYPasarela(datos)}
     *
     * <p>Casos de uso: CU-34
     * @param datos valor de {@code datos}
     */
    public void alGuardar(DatosConfiguracionPagos datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

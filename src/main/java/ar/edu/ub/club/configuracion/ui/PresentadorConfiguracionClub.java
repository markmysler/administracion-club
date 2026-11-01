package ar.edu.ub.club.configuracion.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.configuracion.aplicacion.DatosConfiguracionClub;
import ar.edu.ub.club.configuracion.aplicacion.ServicioConfiguracionClub;

/**
 * Presentador de la pantalla P-34: Formulario de datos institucionales del club.
 *
 * <p>Casos de uso: todos
 */
public class PresentadorConfiguracionClub extends PresentadorBase<PantallaConfiguracionClub> {

    /** Atributo servicioConfiguracionClub. */
    private ServicioConfiguracionClub servicioConfiguracionClub;

    /**
     * {@code ServicioConfiguracionClub.consultar(): ConfiguracionClub} → {@code DatosConfiguracionClub.desde(c)}
     *
     * <p>Casos de uso: CU-41
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioConfiguracionClub.modificar(datos)}
     *
     * <p>Casos de uso: CU-41
     * @param datos valor de {@code datos}
     */
    public void alGuardar(DatosConfiguracionClub datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

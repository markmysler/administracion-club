package ar.edu.ub.club.configuracion.ui;

import ar.edu.ub.club.compartido.ui.InformacionClub;
import ar.edu.ub.club.configuracion.aplicacion.ServicioConfiguracionClub;

/**
 * Implementa {@code InformacionClub} de {@code compartido.ui}: entrega el nombre del club al encabezado de
 * {@code DisposicionPrincipal} sin que el paquete común dependa del módulo configuracion.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class InformacionClubConfigurada implements InformacionClub {

    /** Fachada de los datos del club. */
    private ServicioConfiguracionClub servicioConfiguracion;

    /**
     * {@code ServicioConfiguracionClub.consultar().nombre}.
     *
     * <p>Casos de uso: PENDIENTE
     * @return Nombre del club.
     */
    public String nombreClub() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

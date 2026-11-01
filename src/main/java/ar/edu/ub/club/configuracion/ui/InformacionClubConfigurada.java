package ar.edu.ub.club.configuracion.ui;

import ar.edu.ub.club.compartido.ui.InformacionClub;
import ar.edu.ub.club.configuracion.aplicacion.ServicioConfiguracionClub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementa {@code InformacionClub} de {@code compartido.ui}: entrega el nombre del club al encabezado de
 * {@code DisposicionPrincipal} sin que el paquete común dependa del módulo configuracion (inversión de
 * dependencias; Adapter).
 *
 * <p>Casos de uso: CU-41
 */
@Component
public class InformacionClubConfigurada implements InformacionClub {

    /** Bitácora técnica. */
    private static final Logger BITACORA = LoggerFactory.getLogger(InformacionClubConfigurada.class);

    /** Nombre que se muestra si la configuración no se puede leer. */
    private static final String NOMBRE_POR_OMISION = "Club";

    /** Fachada de los datos del club. */
    private final ServicioConfiguracionClub servicioConfiguracion;

    /**
     * Crea el componente.
     *
     * <p>Casos de uso: CU-41
     * @param servicioConfiguracion fachada de configuración
     */
    public InformacionClubConfigurada(ServicioConfiguracionClub servicioConfiguracion) {
        this.servicioConfiguracion = servicioConfiguracion;
    }

    /**
     * {@code ServicioConfiguracionClub.consultar().nombre}; si la lectura falla devuelve "Club" para no impedir que
     * se dibuje la pantalla.
     *
     * <p>Casos de uso: CU-41
     * @return Nombre del club.
     */
    @Override
    public String nombreClub() {
        try {
            return servicioConfiguracion.consultar().getNombre();
        } catch (RuntimeException e) {
            BITACORA.warn("No se pudo leer el nombre del club", e);
            return NOMBRE_POR_OMISION;
        }
    }
}

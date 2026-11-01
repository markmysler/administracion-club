package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.accesos.aplicacion.SesionUsuario;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.ui.AccionCierreSesion;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Presentador sin pantalla de la opción «Cerrar sesión» del menú (P-35; CU-12). Implementa {@code
 * AccionCierreSesion} de {@code compartido.ui}, de modo que el menú común no depende del módulo accesos. No
 * extiende {@code PresentadorBase} porque no tiene pantalla.
 *
 * <p>Casos de uso: CU-12
 */
@SpringComponent
@UIScope
public class PresentadorCierreSesion implements AccionCierreSesion {

    /** Bitácora técnica. */
    private static final Logger BITACORA = LoggerFactory.getLogger(PresentadorCierreSesion.class);

    /** Fachada del módulo accesos. */
    private final ServicioAccesos servicioAccesos;

    /** Cierre de la sesión HTTP y de la cookie persistente. */
    private final RegistroSesionWeb registroSesion;

    /**
     * Crea el presentador.
     *
     * <p>Casos de uso: CU-12
     * @param servicioAccesos fachada de accesos
     * @param registroSesion registro de la sesión web
     */
    public PresentadorCierreSesion(ServicioAccesos servicioAccesos, RegistroSesionWeb registroSesion) {
        this.servicioAccesos = servicioAccesos;
        this.registroSesion = registroSesion;
    }

    /**
     * {@code ServicioAccesos.consultarSesionActual()} y {@code ServicioAccesos.cerrarSesion(sesion)} (revoca los
     * tokens persistentes y registra el cierre); luego {@code registroSesion.cerrar()}. {@code MenuNavegacion} lleva
     * el navegador a {@code inicio-sesion}. Si la sesión ya expiró (FA1) no llama a {@code cerrarSesion}: solo
     * limpia el navegador ({@code sec_cu12.puml}).
     *
     * <p>Casos de uso: CU-12
     */
    @Override
    public void cerrarSesion() {
        try {
            SesionUsuario sesion = servicioAccesos.consultarSesionActual();
            servicioAccesos.cerrarSesion(sesion);
        } catch (AccesoDenegadoException e) {
            BITACORA.debug("Cierre de sesión sin cuenta identificada (sesión ya expirada)");
        } catch (RuntimeException e) {
            BITACORA.warn("No se pudieron revocar los tokens al cerrar sesión", e);
        } finally {
            registroSesion.cerrar();
        }
    }
}

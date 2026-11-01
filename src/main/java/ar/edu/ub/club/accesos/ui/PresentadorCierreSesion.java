package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.ui.AccionCierreSesion;

/**
 * Presentador sin pantalla de la opción «Cerrar sesión» del menú (P-35; CU-12). Implementa {@code
 * AccionCierreSesion} de {@code compartido.ui}, de modo que el menú común no depende del módulo accesos. No
 * extiende {@code PresentadorBase} porque no tiene pantalla.
 *
 * <p>Casos de uso: CU-12
 */
public class PresentadorCierreSesion implements AccionCierreSesion {

    /** Fachada del módulo accesos. */
    private ServicioAccesos servicioAccesos;

    /** Cierre de la sesión HTTP y de la cookie persistente. */
    private RegistroSesionWeb registroSesion;

    /**
     * {@code ServicioAccesos.consultarSesionActual()} y {@code ServicioAccesos.cerrarSesion(sesion)} (revoca los
     * tokens persistentes y registra el cierre); luego {@code registroSesion.cerrar()}. {@code MenuNavegacion} lleva
     * el navegador a {@code inicio-sesion}.
     *
     * <p>Casos de uso: CU-12
     */
    public void cerrarSesion() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

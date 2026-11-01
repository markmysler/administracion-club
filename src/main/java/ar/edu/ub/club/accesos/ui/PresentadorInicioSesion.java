package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.ui.PresentadorBase;

/**
 * Presentador de la pantalla P-01: Formulario de usuario y contraseña, casilla «Mantener la sesión iniciada» y
 * enlace «Olvidé mi contraseña» (punto de extensión hacia CU-13).
 *
 * <p>Casos de uso: CU-11, CU-13
 */
public class PresentadorInicioSesion extends PresentadorBase<PantallaInicioSesion> {

    /** Atributo servicioAccesos. */
    private ServicioAccesos servicioAccesos;

    /** Atributo registroSesion. */
    private RegistroSesionWeb registroSesion;

    /**
     * {@code ServicioAccesos.iniciarSesion(username, clave, mantenerSesion): SesionUsuario}; luego {@code
     * registroSesion.iniciar(sesion)} y {@code navegarA("panel")} si {@code sesion.permisos} contiene
     * CONSULTAR_DASHBOARD, si no {@code navegarA("inicio")}
     *
     * <p>Casos de uso: CU-11
     * @param username valor de {@code username}
     * @param clave valor de {@code clave}
     * @param mantenerSesion valor de {@code mantenerSesion}
     */
    public void alIngresar(String username, String clave, boolean mantenerSesion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("recuperar-clave")}
     *
     * <p>Casos de uso: CU-11, CU-13
     */
    public void alOlvidarClave() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

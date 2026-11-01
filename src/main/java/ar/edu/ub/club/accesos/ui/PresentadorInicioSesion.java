package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.accesos.aplicacion.SesionUsuario;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.Optional;

/**
 * Presentador de la pantalla P-01: Formulario de usuario y contraseña, casilla «Mantener la sesión iniciada» y
 * enlace «Olvidé mi contraseña» (punto de extensión hacia CU-13). MVP (Vista Pasiva): no usa tipos de Vaadin;
 * llama a la fachada dentro de {@code consultar(...)}, que convierte un {@code ErrorNegocio} en
 * {@code pantalla.mostrarError(motivo)} (FA4 a FA8).
 *
 * <p>Casos de uso: CU-11, CU-13
 */
@SpringComponent
@UIScope
public class PresentadorInicioSesion extends PresentadorBase<PantallaInicioSesion> {

    /** Fachada del módulo accesos. */
    private final ServicioAccesos servicioAccesos;

    /** Inicio de la sesión del servidor y de la cookie persistente. */
    private final RegistroSesionWeb registroSesion;

    /**
     * Crea el presentador.
     *
     * <p>Casos de uso: CU-11
     * @param servicioAccesos fachada de accesos
     * @param registroSesion registro de la sesión web
     */
    public PresentadorInicioSesion(ServicioAccesos servicioAccesos, RegistroSesionWeb registroSesion) {
        this.servicioAccesos = servicioAccesos;
        this.registroSesion = registroSesion;
    }

    /**
     * {@code ServicioAccesos.iniciarSesion(username, clave, mantenerSesion): SesionUsuario}; luego {@code
     * registroSesion.iniciar(sesion)} y {@code navegarA("panel")} si {@code sesion.permisos} contiene
     * CONSULTAR_DASHBOARD, si no {@code navegarA("inicio")} ({@code sec_cu11_p1.puml}). Si se rechaza, la pantalla
     * muestra el motivo, se limpia la clave y se vuelve a habilitar el envío.
     *
     * <p>Casos de uso: CU-11
     * @param username nombre de usuario ingresado
     * @param clave contraseña ingresada
     * @param mantenerSesion casilla «Mantener la sesión iniciada»
     */
    public void alIngresar(String username, String clave, boolean mantenerSesion) {
        Optional<SesionUsuario> sesion = consultar(() -> servicioAccesos.iniciarSesion(username, clave, mantenerSesion));
        pantalla.limpiarClave();
        if (sesion.isEmpty() || !ejecutar(() -> registroSesion.iniciar(sesion.get()))) {
            pantalla.habilitarEnvio(true);
            return;
        }
        pantalla.navegarA(sesion.get().getPermisos().contains(Permiso.CONSULTAR_DASHBOARD) ? "panel" : "inicio");
    }

    /**
     * {@code navegarA("recuperar-clave")} (CU-11 FA3, extensión hacia CU-13).
     *
     * <p>Casos de uso: CU-11, CU-13
     */
    public void alOlvidarClave() {
        pantalla.navegarA("recuperar-clave");
    }
}

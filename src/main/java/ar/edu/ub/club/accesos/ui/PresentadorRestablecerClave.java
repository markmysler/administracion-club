package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-03: Página de destino del enlace de recuperación: lee el token de la URL y pide
 * la nueva contraseña dos veces. MVP (Vista Pasiva): guarda el token como estado de la pantalla y no lo muestra.
 *
 * <p>Casos de uso: CU-13
 */
@SpringComponent
@UIScope
public class PresentadorRestablecerClave extends PresentadorBase<PantallaRestablecerClave> {

    /** Fachada del módulo accesos. */
    private final ServicioAccesos servicioAccesos;

    /** Token recibido en el enlace (no se muestra). */
    private String token;

    /**
     * Crea el presentador.
     *
     * <p>Casos de uso: CU-13
     * @param servicioAccesos fachada de accesos
     */
    public PresentadorRestablecerClave(ServicioAccesos servicioAccesos) {
        this.servicioAccesos = servicioAccesos;
    }

    /**
     * Guarda el token del enlace (CU-13 paso 10, {@code sec_cu13_p3.puml}); no lo muestra.
     *
     * <p>Casos de uso: CU-13
     * @param token token del parámetro {@code token} de la URL (puede ser nulo)
     */
    public void alEntrar(String token) {
        this.token = token;
    }

    /**
     * Si difieren {@code marcarCampo("repeticion", …)}; si no {@code ServicioAccesos.restablecerClave(token,
     * nuevaClave)} y {@code navegarA("inicio-sesion")}. Un {@code ErrorNegocio} con campo marca el requisito (FA5)
     * y uno sin campo informa el enlace no válido (FA4).
     *
     * <p>Casos de uso: CU-13
     * @param nuevaClave nueva contraseña
     * @param repeticion repetición de la nueva contraseña
     */
    public void alRestablecer(String nuevaClave, String repeticion) {
        if (nuevaClave == null || nuevaClave.isEmpty()) {
            pantalla.marcarCampo("nuevaClave", "Ingrese la nueva contraseña.");
            return;
        }
        if (!nuevaClave.equals(repeticion)) {
            pantalla.marcarCampo("repeticion", "Las contraseñas no coinciden.");
            return;
        }
        boolean hecho = ejecutar(() -> servicioAccesos.restablecerClave(token, nuevaClave));
        pantalla.limpiarClaves();
        if (hecho) {
            pantalla.mostrarMensaje("La contraseña se cambió. Ya puede iniciar sesión con la nueva contraseña.");
            pantalla.navegarA("inicio-sesion");
        }
    }
}

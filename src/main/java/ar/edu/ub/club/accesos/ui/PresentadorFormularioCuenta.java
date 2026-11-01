package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.DatosAcceso;
import ar.edu.ub.club.accesos.aplicacion.DatosAdministrador;
import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-14: Formulario de cuenta de acceso: alta de administrador o modificación de datos
 * de acceso de cualquier cuenta. MVP (Vista Pasiva). No ofrece cambiar el tipo de cuenta (CU-16 FA1, RN-04).
 *
 * <p>Casos de uso: CU-14, CU-15, CU-16
 */
@SpringComponent
@UIScope
public class PresentadorFormularioCuenta extends PresentadorBase<PantallaFormularioCuenta> {

    /** Fachada del módulo accesos. */
    private final ServicioAccesos servicioAccesos;

    /** Cuenta que se modifica; {@code null} en el alta. */
    private Long idUsuario;

    /**
     * Crea el presentador.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param servicioAccesos fachada de accesos
     */
    public PresentadorFormularioCuenta(ServicioAccesos servicioAccesos) {
        this.servicioAccesos = servicioAccesos;
    }

    /**
     * con id: {@code ServicioAccesos.consultarCuenta(idUsuario): Usuario} → {@code DatosAcceso.desde(usuario)} y
     * {@code DetalleCuenta.desde(usuario)}; sin id: {@code new DatosAdministrador()}
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param idUsuario cuenta a modificar o {@code null} para el alta
     */
    public void cargar(Long idUsuario) {
        this.idUsuario = idUsuario;
        if (idUsuario == null) {
            pantalla.mostrarAlta(new DatosAdministrador());
            return;
        }
        consultar(() -> servicioAccesos.consultarCuenta(idUsuario))
                .ifPresent(u -> pantalla.mostrarModificacion(DatosAcceso.desde(u), DetalleCuenta.desde(u)));
    }

    /**
     * {@code ServicioAccesos.crearAdministrador(datos): Administrador}; {@code navegarA("cuentas")}
     * ({@code sec_cu14_p1.puml}).
     *
     * <p>Casos de uso: CU-14, CU-16
     * @param datos datos del alta leídos del formulario
     */
    public void alCrear(DatosAdministrador datos) {
        if (ejecutar(() -> servicioAccesos.crearAdministrador(datos))) {
            pantalla.mostrarMensaje("Cuenta de administrador creada.");
            pantalla.navegarA("cuentas");
        }
    }

    /**
     * {@code ServicioAccesos.modificarDatosAcceso(idUsuario, datos)}; vuelve a {@code cuentas}
     * ({@code sec_cu15_p2.puml}).
     *
     * <p>Casos de uso: CU-15
     * @param datos datos de acceso leídos del formulario
     */
    public void alModificar(DatosAcceso datos) {
        if (ejecutar(() -> servicioAccesos.modificarDatosAcceso(idUsuario, datos))) {
            pantalla.mostrarMensaje("Datos de acceso modificados.");
            pantalla.navegarA("cuentas");
        }
    }
}

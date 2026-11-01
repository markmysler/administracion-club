package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.DatosAcceso;
import ar.edu.ub.club.accesos.aplicacion.DatosAdministrador;
import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.ui.PresentadorBase;

/**
 * Presentador de la pantalla P-14: Formulario de cuenta de acceso: alta de administrador o modificación de datos
 * de acceso de cualquier cuenta.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-16
 */
public class PresentadorFormularioCuenta extends PresentadorBase<PantallaFormularioCuenta> {

    /** Atributo servicioAccesos. */
    private ServicioAccesos servicioAccesos;

    /** Atributo idUsuario. */
    private Long idUsuario;

    /**
     * con id: {@code ServicioAccesos.consultarCuenta(idUsuario): Usuario} → {@code DatosAcceso.desde(usuario)} y
     * {@code DetalleCuenta.desde(usuario)}; sin id: {@code new DatosAdministrador()}
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param idUsuario valor de {@code idUsuario}
     */
    public void cargar(Long idUsuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAccesos.crearAdministrador(datos): Administrador}; {@code navegarA("cuentas")}
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param datos valor de {@code datos}
     */
    public void alCrear(DatosAdministrador datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAccesos.modificarDatosAcceso(idUsuario, datos)}; vuelve a {@code cuentas} o a la ficha
     *
     * <p>Casos de uso: CU-14, CU-15, CU-16
     * @param datos valor de {@code datos}
     */
    public void alModificar(DatosAcceso datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

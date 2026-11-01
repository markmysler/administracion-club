package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.ui.PresentadorBase;

/**
 * Presentador de la pantalla P-13: Listado de cuentas de administrador con alta y desactivación, y búsqueda de
 * cuentas de cualquier tipo (columna Tipo de cuenta) para modificar sus datos de acceso (CU-15).
 *
 * <p>Casos de uso: CU-14, CU-15, CU-17
 */
public class PresentadorCuentasAdministrador extends PresentadorBase<PantallaCuentasAdministrador> {

    /** Atributo servicioAccesos. */
    private ServicioAccesos servicioAccesos;

    /** Atributo reloj. */
    private Reloj reloj;

    /**
     * {@code ServicioAccesos.listarAdministradores(): List&lt;Administrador&gt;} → {@code FilaCuenta.desde(admin,
     * reloj.ahora())}
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAccesos.buscarCuentas(new CriterioBusquedaCuenta(texto)): List&lt;ResumenCuenta&gt;} → {@code
     * FilaCuenta.desde(resumen)}
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param texto valor de {@code texto}
     */
    public void alBuscar(String texto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("cuentas/formulario")}
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     */
    public void alNueva() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("cuentas/formulario/" + idUsuario)}
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param idUsuario valor de {@code idUsuario}
     */
    public void alModificarAcceso(Long idUsuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAccesos.desactivarCuenta(idUsuario)}; luego {@code cargar()}
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param idUsuario valor de {@code idUsuario}
     */
    public void alDesactivar(Long idUsuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

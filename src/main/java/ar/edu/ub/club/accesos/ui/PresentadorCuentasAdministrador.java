package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.CriterioBusquedaCuenta;
import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.LocalDateTime;

/**
 * Presentador de la pantalla P-13: Listado de cuentas de administrador con alta y desactivación, y búsqueda de
 * cuentas de cualquier tipo (columna Tipo de cuenta) para modificar sus datos de acceso (CU-15). MVP (Vista
 * Pasiva): convierte entidades y resúmenes en {@code FilaCuenta}.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-17
 */
@SpringComponent
@UIScope
public class PresentadorCuentasAdministrador extends PresentadorBase<PantallaCuentasAdministrador> {

    /** Fachada del módulo accesos. */
    private final ServicioAccesos servicioAccesos;

    /** Momento actual (para mostrar el bloqueo temporal). */
    private final Reloj reloj;

    /**
     * Crea el presentador.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param servicioAccesos fachada de accesos
     * @param reloj reloj del sistema
     */
    public PresentadorCuentasAdministrador(ServicioAccesos servicioAccesos, Reloj reloj) {
        this.servicioAccesos = servicioAccesos;
        this.reloj = reloj;
    }

    /**
     * {@code ServicioAccesos.listarAdministradores(): List&lt;Administrador&gt;} → {@code FilaCuenta.desde(admin,
     * reloj.ahora())}
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     */
    public void cargar() {
        LocalDateTime ahora = reloj.ahora();
        consultar(servicioAccesos::listarAdministradores)
                .map(lista -> lista.stream().map(a -> FilaCuenta.desde(a, ahora)).toList())
                .ifPresent(pantalla::mostrarCuentas);
    }

    /**
     * {@code ServicioAccesos.buscarCuentas(new CriterioBusquedaCuenta(texto)): List&lt;ResumenCuenta&gt;} → {@code
     * FilaCuenta.desde(resumen)}
     *
     * <p>Casos de uso: CU-15, CU-17
     * @param texto texto de búsqueda
     */
    public void alBuscar(String texto) {
        consultar(() -> servicioAccesos.buscarCuentas(new CriterioBusquedaCuenta(texto)))
                .map(lista -> lista.stream().map(FilaCuenta::desde).toList())
                .ifPresent(pantalla::mostrarCuentas);
    }

    /**
     * {@code navegarA("cuentas/formulario")} (CU-14 paso 1).
     *
     * <p>Casos de uso: CU-14
     */
    public void alNueva() {
        pantalla.navegarA("cuentas/formulario");
    }

    /**
     * {@code navegarA("cuentas/formulario/" + idUsuario)} (CU-15 paso 3).
     *
     * <p>Casos de uso: CU-15
     * @param idUsuario cuenta elegida
     */
    public void alModificarAcceso(Long idUsuario) {
        pantalla.navegarA("cuentas/formulario/" + idUsuario);
    }

    /**
     * {@code ServicioAccesos.desactivarCuenta(idUsuario)}; luego {@code cargar()} (CU-17; la confirmación la pide
     * la vista con {@code DialogoConfirmacion}).
     *
     * <p>Casos de uso: CU-17
     * @param idUsuario cuenta a desactivar
     */
    public void alDesactivar(Long idUsuario) {
        if (ejecutar(() -> servicioAccesos.desactivarCuenta(idUsuario))) {
            pantalla.mostrarMensaje("La cuenta quedó desactivada.");
            cargar();
        }
    }
}

package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoConfirmacion;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-13: Listado de cuentas de administrador con alta y desactivación, y búsqueda de cuentas
 * de cualquier tipo (columna Tipo de cuenta) para modificar sus datos de acceso (CU-15). MVP (Vista Pasiva): los
 * oyentes solo llaman al presentador; la desactivación pasa antes por {@code DialogoConfirmacion}.
 *
 * <p>Ruta: {@code cuentas}. Permiso: {@code GESTIONAR_CUENTAS}.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-17
 */
@Route(value = "cuentas", layout = DisposicionPrincipal.class)
@PageTitle("Cuentas de acceso")
public class VistaCuentasAdministrador extends VistaProtegida implements PantallaCuentasAdministrador {

    /** Presentador de P-13. */
    private final PresentadorCuentasAdministrador presentador;

    /** Tabla de cuentas. */
    private final TablaDatos<FilaCuenta> tabla = new TablaDatos<>(FilaCuenta.class);

    /**
     * Arma la barra de búsqueda, los botones y la tabla con sus acciones.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param presentador presentador de P-13
     */
    public VistaCuentasAdministrador(PresentadorCuentasAdministrador presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        TextField busqueda = new TextField();
        busqueda.setPlaceholder("Usuario, correo o nombre");
        Button buscar = new Button("Buscar cuentas", e -> presentador.alBuscar(busqueda.getValue()));
        Button administradores = new Button("Ver administradores", e -> presentador.cargar());
        Button nueva = new Button("Nueva cuenta de administrador", e -> presentador.alNueva());
        nueva.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        tabla.agregarColumna("Usuario", FilaCuenta::username);
        tabla.agregarColumna("Apellido y nombre", FilaCuenta::nombreCompleto);
        tabla.agregarColumna("Correo", FilaCuenta::email);
        tabla.agregarColumna("Tipo de cuenta", FilaCuenta::tipoCuenta);
        tabla.agregarColumna("Estado", FilaCuenta::estado);
        tabla.agregarColumna("Bloqueada", f -> f.bloqueada() ? "Sí" : "No");
        tabla.agregarColumnaAccion("Modificar acceso", f -> presentador.alModificarAcceso(f.id()));
        tabla.agregarColumnaAccion("Desactivar", this::confirmarDesactivacion);
        getContent().add(new HorizontalLayout(busqueda, buscar, administradores, nueva), tabla);
    }

    /**
     * Pide confirmación antes de desactivar (CU-17 paso 5); al confirmar llama al presentador.
     *
     * <p>Casos de uso: CU-17
     * @param fila cuenta elegida
     */
    private void confirmarDesactivacion(FilaCuenta fila) {
        DialogoConfirmacion dialogo = new DialogoConfirmacion("Desactivar cuenta",
                "¿Desactivar la cuenta " + fila.username() + "? La cuenta no podrá volver a iniciar sesión.");
        dialogo.alConfirmar(motivo -> presentador.alDesactivar(fila.id()));
        dialogo.abrir();
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @return {@code GESTIONAR_CUENTAS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_CUENTAS;
    }

    /**
     * Al entrar carga el listado de administradores.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param parametros parámetros de la ruta (no usa)
     * @param consulta parámetros de consulta (no usa)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Muestra las filas en la tabla.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param filas filas a mostrar
     */
    @Override
    public void mostrarCuentas(List<FilaCuenta> filas) {
        tabla.mostrar(filas);
    }
}

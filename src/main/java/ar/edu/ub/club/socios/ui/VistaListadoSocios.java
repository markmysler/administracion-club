package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.socios.aplicacion.CriterioBusquedaSocio;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-06: Listado y búsqueda de socios con acceso a la ficha y al alta.
 *
 * <p>Ruta: {@code socios}. Permiso: {@code GESTIONAR_SOCIOS}.
 *
 * <p>Vista pasiva del patrón MVP: arma los componentes y sus oyentes solo llaman a {@code presentador.al…(...)}.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-04
 */
@Route(value = "socios", layout = DisposicionPrincipal.class)
@PageTitle("Socios")
public class VistaListadoSocios extends VistaProtegida implements PantallaListadoSocios {

    /** Presentador de la pantalla. */
    private final PresentadorListadoSocios presentador;

    /** Texto de búsqueda (DNI, nombre o apellido). */
    private final TextField texto = new TextField("DNI, nombre o apellido");

    /** Filtro por estado de la cuenta. */
    private final ComboBox<EstadoCuenta> estado = new ComboBox<>("Estado");

    /** Resultado de la búsqueda. */
    private final TablaDatos<FilaSocio> tabla = new TablaDatos<>(FilaSocio.class);

    /**
     * Arma la búsqueda, la tabla y el botón de alta y se vincula con el presentador.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-04
     * @param presentador presentador de P-06
     */
    public VistaListadoSocios(PresentadorListadoSocios presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        estado.setItems(EstadoCuenta.values());
        estado.setClearButtonVisible(true);
        texto.setClearButtonVisible(true);
        Button buscar = new Button("Buscar", e -> buscar());
        buscar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        texto.addKeyPressListener(com.vaadin.flow.component.Key.ENTER, e -> buscar());
        Button nuevo = new Button("Registrar socio", e -> presentador.alNuevo());
        HorizontalLayout filtros = new HorizontalLayout(texto, estado, buscar, nuevo);
        filtros.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        filtros.setWrap(true);
        tabla.agregarColumna("DNI", FilaSocio::dni);
        tabla.agregarColumna("Apellido", FilaSocio::apellido);
        tabla.agregarColumna("Nombre", FilaSocio::nombre);
        tabla.agregarColumna("Categoría", FilaSocio::categoria);
        tabla.agregarColumna("Estado", FilaSocio::estado);
        tabla.agregarColumna("Titular", f -> f.esTitular() ? "Sí" : "No");
        tabla.agregarColumnaAccion("Ver ficha", f -> presentador.alSeleccionar(f.id()));
        getContent().add(new H2("Socios"), filtros, tabla);
    }

    /**
     * Arma el criterio con los valores de los filtros y lo envía al presentador.
     *
     * <p>Casos de uso: CU-04
     */
    private void buscar() {
        CriterioBusquedaSocio criterio = new CriterioBusquedaSocio();
        criterio.setTexto(texto.getValue());
        criterio.setEstado(estado.getValue());
        presentador.alBuscar(criterio);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-04
     * @return {@code GESTIONAR_SOCIOS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_SOCIOS;
    }

    /**
     * Al entrar muestra todos los socios (búsqueda sin filtros).
     *
     * <p>Casos de uso: CU-04
     * @param parametros parámetros de la ruta (sin uso)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.alBuscar(new CriterioBusquedaSocio());
    }

    /**
     * Muestra las filas en la tabla.
     *
     * <p>Casos de uso: CU-02, CU-04
     * @param filas socios encontrados
     */
    @Override
    public void mostrarSocios(List<FilaSocio> filas) {
        tabla.mostrar(filas);
    }
}

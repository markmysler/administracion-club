package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.function.ValueProvider;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Componente común de tabla sobre {@code Grid} de Vaadin: muestra modelos de vista (records), con columnas
 * ordenables, columnas de acción y carga paginada opcional.
 *
 * @param <T> modelo de vista de cada fila
 *
 * <p>Casos de uso: CU-04, CU-05, CU-06, CU-10, CU-13, CU-15, CU-17, CU-18, CU-21, CU-24, CU-26, CU-29, CU-31,
 * CU-34, CU-37, CU-38, CU-39
 */
public class TablaDatos<T> extends Composite<Grid<T>> {

    /** Grilla de Vaadin. */
    private final Grid<T> grilla;

    /**
     * Crea la tabla para un tipo de fila, sin columnas automáticas.
     *
     * <p>Casos de uso: todos
     * @param tipo clase del modelo de vista
     */
    public TablaDatos(Class<T> tipo) {
        this.grilla = new Grid<>(tipo, false);
        grilla.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
        grilla.setAllRowsVisible(true);
    }

    /**
     * Devuelve la grilla como contenido del {@code Composite}.
     *
     * <p>Casos de uso: todos
     * @return grilla
     */
    @Override
    protected Grid<T> initContent() {
        return grilla;
    }

    /**
     * Agrega una columna ordenable.
     *
     * <p>Casos de uso: todos
     * @param titulo encabezado
     * @param valor proveedor del valor de la celda
     */
    public void agregarColumna(String titulo, ValueProvider<T, ?> valor) {
        grilla.addColumn(valor).setHeader(titulo).setSortable(true).setAutoWidth(true);
    }

    /**
     * Agrega una columna con un botón de acción por fila.
     *
     * <p>Casos de uso: todos
     * @param texto texto del botón
     * @param accion acción con la fila (llama al presentador)
     */
    public void agregarColumnaAccion(String texto, Consumer<T> accion) {
        grilla.addComponentColumn(fila -> {
            Button boton = new Button(texto, e -> accion.accept(fila));
            boton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            return boton;
        }).setAutoWidth(true);
    }

    /**
     * Muestra las filas.
     *
     * <p>Casos de uso: todos
     * @param filas modelos de vista
     */
    public void mostrar(List<T> filas) {
        grilla.setItems(filas);
    }

    /**
     * Muestra las filas con carga perezosa por páginas.
     *
     * <p>Casos de uso: CU-31, CU-37
     * @param proveedor proveedor de páginas
     */
    public void mostrarPaginado(CallbackDataProvider<T, Void> proveedor) {
        grilla.setAllRowsVisible(false);
        grilla.setItems(proveedor);
    }

    /**
     * Registra el oyente de selección de una fila.
     *
     * <p>Casos de uso: todos
     * @param oyente oyente
     */
    public void alSeleccionar(Consumer<T> oyente) {
        grilla.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                oyente.accept(e.getValue());
            }
        });
    }

    /**
     * Fila seleccionada.
     *
     * <p>Casos de uso: todos
     * @return fila o vacío
     */
    public Optional<T> filaSeleccionada() {
        return grilla.getSelectedItems().stream().findFirst();
    }
}

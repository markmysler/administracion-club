package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Componente común de formulario: vincula los campos a un objeto de comando {@code Datos*} de la capa de
 * aplicación con un {@code Binder} de Vaadin (valida el formato del lado del servidor) y muestra en el campo
 * correspondiente los {@code ErrorNegocio} con campo que traduce el presentador.
 *
 * @param <T> clase del objeto de comando
 *
 * <p>Casos de uso: CU-01, CU-02, CU-06, CU-13, CU-14, CU-15, CU-18, CU-19, CU-21, CU-22, CU-24, CU-25, CU-34,
 * CU-36, CU-41
 */
public class FormularioEntidad<T> extends Composite<FormLayout> {

    /** Vinculador de Vaadin entre los campos y las propiedades del objeto. */
    private final Binder<T> vinculador;

    /** Campos por nombre de propiedad, para marcar errores. */
    private final Map<String, HasValue<?, ?>> campos = new HashMap<>();

    /** Objeto de comando cargado. */
    private T datos;

    /**
     * Crea el formulario para una clase de objeto de comando (con propiedades get/set).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06, CU-14, CU-15, CU-18, CU-21, CU-24, CU-25, CU-34, CU-36, CU-41
     * @param tipo clase del objeto de comando
     */
    public FormularioEntidad(Class<T> tipo) {
        this.vinculador = new Binder<>(tipo);
    }

    /**
     * Vincula un campo a una propiedad con sus validadores de formato y lo agrega al formulario.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06, CU-14, CU-15, CU-18, CU-21, CU-24, CU-25, CU-34, CU-36, CU-41
     * @param campo campo de Vaadin
     * @param propiedad nombre de la propiedad del objeto
     * @param validadores validadores de formato (pueden ser vacíos)
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void vincular(HasValue<?, ?> campo, String propiedad, List<Validator<?>> validadores) {
        Binder.BindingBuilder constructor = vinculador.forField((HasValue) campo);
        for (Validator<?> validador : validadores) {
            constructor = constructor.withValidator((Validator) validador);
        }
        constructor.bind(propiedad);
        campos.put(propiedad, campo);
        if (campo instanceof Component componente) {
            getContent().add(componente);
        }
    }

    /**
     * Precarga el objeto de comando en los campos.
     *
     * <p>Casos de uso: CU-02, CU-15, CU-19, CU-22, CU-24, CU-25, CU-34, CU-41
     * @param datos objeto de comando
     */
    public void cargar(T datos) {
        this.datos = datos;
        limpiarErrores();
        vinculador.readBean(datos);
    }

    /**
     * Valida y escribe los campos en el objeto de comando.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06, CU-14, CU-15, CU-18, CU-21, CU-24, CU-25, CU-34, CU-36, CU-41
     * @return el objeto o vacío si hay errores de formato
     */
    public Optional<T> leer() {
        if (datos == null) {
            return Optional.empty();
        }
        return vinculador.writeBeanIfValid(datos) ? Optional.of(datos) : Optional.empty();
    }

    /**
     * Muestra un {@code ErrorNegocio} en el campo correspondiente.
     *
     * <p>Casos de uso: todos
     * @param campo nombre de la propiedad
     * @param mensaje texto del error
     */
    public void marcarError(String campo, String mensaje) {
        HasValue<?, ?> componente = campos.get(campo);
        if (componente instanceof HasValidation validable) {
            validable.setErrorMessage(mensaje);
            validable.setInvalid(true);
        }
    }

    /**
     * Indica si el formulario tiene un campo vinculado a esa propiedad.
     *
     * <p>Casos de uso: todos
     * @param campo nombre de la propiedad
     * @return true si existe
     */
    public boolean tieneCampo(String campo) {
        return campos.containsKey(campo);
    }

    /**
     * Quita las marcas de error de todos los campos.
     *
     * <p>Casos de uso: todos
     */
    public void limpiarErrores() {
        for (HasValue<?, ?> campo : campos.values()) {
            if (campo instanceof HasValidation validable) {
                validable.setInvalid(false);
            }
        }
    }

    /**
     * Agrega el botón «Guardar», que valida y entrega el objeto de comando al oyente (que llama al presentador).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06, CU-14, CU-15, CU-18, CU-21, CU-24, CU-25, CU-34, CU-36, CU-41
     * @param oyente oyente del envío
     */
    public void alEnviar(Consumer<T> oyente) {
        Button guardar = new Button("Guardar", e -> {
            limpiarErrores();
            leer().ifPresent(oyente);
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(guardar);
    }
}

package ar.edu.ub.club.compartido.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Componente común con dos selectores de fecha (es-AR, dd/mm/aaaa) para filtrar listados y reportes.
 *
 * <p>Casos de uso: CU-29, CU-31, CU-37, CU-38, CU-39
 */
public class FiltroFechas extends Composite<HorizontalLayout> {

    /** Fecha inicial. */
    private final DatePicker desde = new DatePicker("Desde");

    /** Fecha final. */
    private final DatePicker hasta = new DatePicker("Hasta");

    /**
     * Crea el filtro con la configuración regional es-AR.
     *
     * <p>Casos de uso: CU-29, CU-31, CU-37, CU-38, CU-39
     */
    public FiltroFechas() {
        desde.setLocale(FormatoUI.ES_AR);
        hasta.setLocale(FormatoUI.ES_AR);
        getContent().add(desde, hasta);
        getContent().setAlignItems(HorizontalLayout.Alignment.BASELINE);
    }

    /**
     * Devuelve el rango; vacío y con el error marcado si {@code desde > hasta}.
     *
     * <p>Casos de uso: CU-29, CU-31, CU-37, CU-38, CU-39
     * @return rango o vacío
     */
    public Optional<RangoFechas> valor() {
        hasta.setInvalid(false);
        LocalDate d = desde.getValue();
        LocalDate h = hasta.getValue();
        if (d != null && h != null && d.isAfter(h)) {
            hasta.setErrorMessage("La fecha final debe ser posterior a la inicial");
            hasta.setInvalid(true);
            return Optional.empty();
        }
        return Optional.of(new RangoFechas(d, h));
    }

    /**
     * Precarga el rango.
     *
     * <p>Casos de uso: CU-29, CU-31, CU-37, CU-38, CU-39
     * @param desde fecha inicial
     * @param hasta fecha final
     */
    public void fijar(LocalDate desde, LocalDate hasta) {
        this.desde.setValue(desde);
        this.hasta.setValue(hasta);
    }

    /**
     * Registra el oyente de cambio del rango.
     *
     * <p>Casos de uso: CU-29, CU-31, CU-37, CU-38, CU-39
     * @param oyente oyente
     */
    public void alCambiar(Consumer<RangoFechas> oyente) {
        desde.addValueChangeListener(e -> valor().ifPresent(oyente));
        hasta.addValueChangeListener(e -> valor().ifPresent(oyente));
    }
}

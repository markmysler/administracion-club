package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.ui.DialogoBase;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import java.util.List;

/**
 * Diálogo de la pantalla P-12: asocia el socio a otro grupo familiar. Se abre desde la ficha del socio (P-08) o
 * desde P-11 con el grupo destino ya elegido ({@code preseleccionar}); busca el grupo destino por DNI o nombre de
 * un integrante o del titular, muestra el resumen del cambio y pide confirmación (CU-07 pasos 2 a 9).
 *
 * <p>Diálogo pasivo del patrón MVP: sus oyentes solo llaman a {@code presentador.al…(...)}.
 *
 * <p>Casos de uso: CU-07
 */
public class DialogoAsociarSocio extends DialogoBase implements PantallaAsociarSocio {

    /** Presentador del diálogo. */
    private final PresentadorAsociarSocio presentador;

    /** Socio y grupo actual. */
    private final Paragraph socio = new Paragraph();

    /** Resumen del cambio (grupo actual, destino y efecto). */
    private final Paragraph resumen = new Paragraph("Elija el grupo destino.");

    /** Texto de búsqueda. */
    private final TextField texto = new TextField("DNI o nombre del titular o de un integrante");

    /** Grupos vigentes encontrados. */
    private final TablaDatos<FilaGrupo> grupos = new TablaDatos<>(FilaGrupo.class);

    /** Texto del grupo actual del socio. */
    private String grupoActual = "";

    /** Grupo destino elegido ({@code null} hasta elegir). */
    private Long idGrupoDestino;

    /**
     * Arma el diálogo y se vincula con el presentador.
     *
     * <p>Casos de uso: CU-07
     * @param presentador presentador de P-12
     */
    public DialogoAsociarSocio(PresentadorAsociarSocio presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        Dialog dialogo = getContent();
        dialogo.setHeaderTitle("Asociar socio a un grupo familiar");
        Button buscar = new Button("Buscar", e -> presentador.alBuscar(texto.getValue()));
        texto.addKeyPressListener(Key.ENTER, e -> presentador.alBuscar(texto.getValue()));
        HorizontalLayout busqueda = new HorizontalLayout(texto, buscar);
        busqueda.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        grupos.agregarColumna("Grupo", FilaGrupo::idGrupo);
        grupos.agregarColumna("Titular", FilaGrupo::titular);
        grupos.agregarColumna("DNI titular", FilaGrupo::dniTitular);
        grupos.agregarColumna("Integrantes", FilaGrupo::cantidadIntegrantes);
        grupos.alSeleccionar(f -> elegir(f.idGrupo(), f.comoTexto()));
        VerticalLayout cuerpo = new VerticalLayout(socio, busqueda, grupos, resumen);
        cuerpo.setPadding(false);
        dialogo.add(cuerpo);
        Button cancelar = new Button("Cancelar", e -> cerrar());
        Button confirmar = new Button("Confirmar", e -> presentador.alConfirmar(idGrupoDestino));
        confirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialogo.getFooter().add(cancelar, confirmar);
        dialogo.setWidth("min(720px, 95vw)");
    }

    /**
     * Abre el diálogo y le pasa al presentador los datos del socio ({@code D -> P: alAbrir(...)} en
     * {@code sec_cu07_p1}).
     *
     * <p>Agregado en la implementación: sobrecarga de {@code DialogoBase.abrir()} con los datos que la vista
     * anfitriona recibe en {@code abrirAsociacion}.
     *
     * <p>Casos de uso: CU-07
     * @param idSocio socio que se asocia
     * @param nombre apellido y nombre
     * @param dni documento
     * @param idGrupoActual grupo vigente
     * @param grupoActual texto del grupo vigente
     * @param idGrupoDestino destino preseleccionado o {@code null}
     */
    public void abrir(Long idSocio, String nombre, String dni, Long idGrupoActual, String grupoActual, Long idGrupoDestino) {
        abrir();
        presentador.alAbrir(idSocio, nombre, dni, idGrupoActual, grupoActual, idGrupoDestino);
    }

    /**
     * Registra el destino elegido y muestra el resumen del cambio (CU-07 paso 8).
     *
     * <p>Casos de uso: CU-07
     * @param idGrupo grupo destino
     * @param descripcion texto del grupo destino
     */
    private void elegir(Long idGrupo, String descripcion) {
        this.idGrupoDestino = idGrupo;
        resumen.setText("Grupo actual: " + grupoActual + " → grupo destino: " + descripcion + ". El socio quedará "
                + "como integrante no titular; si su grupo actual era unipersonal dejará de estar vigente.");
    }

    /**
     * Muestra el socio y su grupo actual.
     *
     * <p>Casos de uso: CU-07
     * @param nombre apellido y nombre
     * @param dni documento
     * @param grupoActual texto del grupo vigente
     */
    @Override
    public void mostrarSocio(String nombre, String dni, String grupoActual) {
        this.grupoActual = grupoActual;
        socio.setText("Socio: " + nombre + " (DNI " + dni + ") · " + grupoActual);
    }

    /**
     * Muestra los grupos vigentes encontrados.
     *
     * <p>Casos de uso: CU-07
     * @param filas grupos
     */
    @Override
    public void mostrarGrupos(List<FilaGrupo> filas) {
        grupos.mostrar(filas);
    }

    /**
     * Fija el grupo destino elegido en P-11 y muestra el resumen del cambio.
     *
     * <p>Casos de uso: CU-07
     * @param idGrupoDestino grupo destino
     */
    @Override
    public void preseleccionar(Long idGrupoDestino) {
        elegir(idGrupoDestino, "grupo " + idGrupoDestino);
    }

    /**
     * Cierra el diálogo y dispara {@code DialogoResueltoEvento}.
     *
     * <p>Casos de uso: CU-07
     */
    @Override
    public void cerrarConExito() {
        notificarResuelto();
    }
}

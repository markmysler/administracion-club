package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista imprimible de la pantalla P-29 Comprobante (MVP Vista Pasiva, sin lógica): encabezado del club, tipo de
 * documento (comprobante de pago o constancia de acreditación), número, fecha, grupo, cuota, origen y medio e importe,
 * con Imprimir y Volver. Ruta {@code pagos/comprobante/:id}, permiso CONSULTAR_PAGOS_PROPIOS (alternativo:
 * CONSULTAR_PAGOS_CLUB).
 *
 * <p>Casos de uso: CU-05, CU-36, CU-37
 */
@Route(value = "pagos/comprobante/:id", layout = DisposicionPrincipal.class)
@PageTitle("Comprobante")
public class VistaComprobante extends VistaProtegida implements PantallaComprobante {

    /** Presentador de la pantalla. */
    private final PresentadorComprobante presentador;

    /** Zona del comprobante (lo único que se imprime). */
    private final Div documento = new Div();

    /** Botón Imprimir (habilitado al mostrar el comprobante). */
    private final Button imprimir = new Button("Imprimir");

    /**
     * Arma los componentes y vincula el presentador.
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     * @param presentador presentador de la pantalla
     */
    public VistaComprobante(PresentadorComprobante presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        documento.addClassName("comprobante-imprimible");
        imprimir.addClickListener(e -> presentador.alImprimir());
        imprimir.setEnabled(false);
        HorizontalLayout barra = new HorizontalLayout(imprimir, new Anchor("pagos", "Volver"));
        barra.addClassName("no-imprimir");
        barra.setAlignItems(HorizontalLayout.Alignment.BASELINE);
        getContent().add(documento, barra);
    }

    /**
     * Permiso principal de la pantalla.
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     * @return CONSULTAR_PAGOS_PROPIOS
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONSULTAR_PAGOS_PROPIOS;
    }

    /**
     * Permiso alternativo de la pantalla (administrador).
     *
     * <p>Casos de uso: CU-36, CU-37
     * @return CONSULTAR_PAGOS_CLUB
     */
    @Override
    protected Permiso permisoAlternativo() {
        return Permiso.CONSULTAR_PAGOS_CLUB;
    }

    /**
     * Lee el parámetro {@code id} y llama a {@code presentador.cargar(id)}; un id no numérico se informa sin consultar.
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     * @param parametros parámetros de ruta ({@code id})
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        documento.removeAll();
        imprimir.setEnabled(false);
        parametros.getLong("id").ifPresentOrElse(presentador::cargar,
                () -> mostrarError("El elemento ya no existe."));
    }

    /**
     * Muestra el comprobante o la constancia.
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     * @param d detalle del comprobante
     */
    @Override
    public void mostrarComprobante(DetalleComprobante d) {
        documento.removeAll();
        String tipo = DetalleComprobante.CONSTANCIA_ACREDITACION.equals(d.tipo())
                ? "Constancia de acreditación" : "Comprobante de pago";
        String medio = d.origen() == null ? "" : d.origen() + (d.medio() == null ? "" : " · " + d.medio());
        documento.add(new H2(valor(d.nombreClub())),
                new Paragraph(valor(d.direccionClub()) + " · " + valor(d.telefonoClub()) + " · "
                        + valor(d.emailClub())),
                new H3(tipo + " " + valor(d.numero())),
                new Paragraph("Fecha de emisión: " + FormatoUI.fechaHora(d.fechaEmision())),
                new Paragraph("Grupo familiar: " + (d.idGrupo() == null ? "" : d.idGrupo())),
                new Paragraph("Cuota: " + valor(d.periodoCuota())),
                new Paragraph("Origen / medio: " + medio),
                new Paragraph("Importe: " + FormatoUI.dinero(d.importe())));
        imprimir.setEnabled(true);
    }

    /**
     * Abre el diálogo de impresión del navegador ({@code window.print()}).
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     */
    @Override
    public void imprimir() {
        UI.getCurrent().getPage().executeJs("window.print()");
    }

    /**
     * Texto vacío para los valores nulos.
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     * @param texto valor
     * @return el valor o texto vacío
     */
    private String valor(String texto) {
        return texto == null ? "" : texto;
    }
}

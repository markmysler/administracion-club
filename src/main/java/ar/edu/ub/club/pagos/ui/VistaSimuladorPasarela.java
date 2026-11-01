package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.VistaBase;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Vista pública de la pantalla P-27 Simulador de pasarela (MVP Vista Pasiva, sin lógica): cumple el papel de la página
 * de Mercado Pago en el entorno académico. Muestra concepto, importe y vencimiento de la intención y ofrece Aprobar y
 * Rechazar; no pide datos de tarjeta (RNF-05). No usa {@code DisposicionPrincipal} (es externa al menú) ni tiene
 * {@code @Route}: la ruta {@code simulador-pasarela/:idPreferencia} la registra {@code RegistradorRutaSimulador} solo
 * en modo SIMULADA.
 *
 * <p>Casos de uso: CU-35
 */
@PageTitle("Pasarela de pago simulada")
@AnonymousAllowed
public class VistaSimuladorPasarela extends VistaBase implements PantallaSimuladorPasarela, BeforeEnterObserver {

    /** Presentador de la pantalla. */
    private final PresentadorSimuladorPasarela presentador;

    /** Concepto de la intención. */
    private final Paragraph concepto = new Paragraph();

    /** Importe de la intención. */
    private final Paragraph importe = new Paragraph();

    /** Vencimiento de la intención. */
    private final Paragraph vence = new Paragraph();

    /** Botones Aprobar y Rechazar (ocultos hasta mostrar la intención). */
    private final HorizontalLayout botones = new HorizontalLayout();

    /**
     * Arma los componentes y vincula el presentador.
     *
     * <p>Casos de uso: CU-35
     * @param presentador presentador de la pantalla
     */
    public VistaSimuladorPasarela(PresentadorSimuladorPasarela presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        Button aprobar = new Button("Aprobar pago", e -> presentador.alResolver(true));
        aprobar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        Button rechazar = new Button("Rechazar pago", e -> presentador.alResolver(false));
        rechazar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        botones.add(aprobar, rechazar);
        botones.setVisible(false);
        getContent().add(new H2("Pasarela de pago simulada"), mensajes(), concepto, importe, vence, botones,
                new Paragraph("Entorno académico: no se mueve dinero real ni se piden datos de tarjeta."));
    }

    /**
     * Lee el parámetro de ruta {@code idPreferencia} y llama a {@code presentador.cargar(idPreferencia)}.
     *
     * <p>Agregado en la implementación: la vista pública no hereda de {@code VistaProtegida}, así que implementa
     * {@code BeforeEnterObserver} para recibir el parámetro de la ruta.
     *
     * <p>Casos de uso: CU-35
     * @param evento evento de navegación
     */
    @Override
    public void beforeEnter(BeforeEnterEvent evento) {
        presentador.cargar(evento.getRouteParameters().get("idPreferencia").orElse(""));
    }

    /**
     * Muestra concepto, importe y vencimiento y habilita los botones.
     *
     * <p>Casos de uso: CU-35
     * @param resumen intención de pago
     */
    @Override
    public void mostrarIntencion(ResumenIntencionSimulada resumen) {
        concepto.setText("Concepto: " + resumen.concepto());
        importe.setText("Importe: " + FormatoUI.dinero(resumen.importe()));
        vence.setText("Vence: " + FormatoUI.fechaHora(resumen.venceEn()));
        botones.setVisible(true);
    }

    /**
     * Lleva el navegador a la {@code urlRetorno} con {@code Page.setLocation}.
     *
     * <p>Casos de uso: CU-35
     * @param url dirección de retorno
     */
    @Override
    public void redirigirA(String url) {
        UI.getCurrent().getPage().setLocation(url);
    }
}

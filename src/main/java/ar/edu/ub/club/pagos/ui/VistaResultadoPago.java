package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;
import java.util.UUID;

/**
 * Vista de la pantalla P-26 Resultado del pago (MVP Vista Pasiva, sin lógica): lee la referencia externa del
 * parámetro {@code referencia} de la URL, se la pasa a {@code PresentadorResultadoPago.alEntrar} y muestra cada estado
 * que el presentador le entrega. Como el presentador la llama desde el hilo del planificador, cada operación de la
 * pantalla se ejecuta dentro de {@code UI.access(...)} y Vaadin la envía por server push ({@code @Push} está en
 * {@code ConfiguracionInterfaz}). Al desprenderse llama a {@code presentador.alSalir()}. Ruta {@code
 * pagos/resultado?referencia=…} (se llega desde {@code /pagos/retorno}), permiso PAGAR_CUOTA_ONLINE.
 *
 * <p>Casos de uso: CU-35
 */
@Route(value = "pagos/resultado", layout = DisposicionPrincipal.class)
@PageTitle("Resultado del pago")
public class VistaResultadoPago extends VistaProtegida implements PantallaResultadoPago {

    /** Presentador de la pantalla. */
    private final PresentadorResultadoPago presentador;

    /** Interfaz de usuario de la vista, para {@code UI.access}. */
    private UI ui;

    /** Datos del pago (cuota e importe). */
    private final Paragraph datos = new Paragraph();

    /** Recuadro de resultado (aria-live). */
    private final Div resultado = new Div();

    /** Acciones según el estado (enlaces sin lógica). */
    private final Div acciones = new Div();

    /**
     * Arma los componentes, vincula el presentador y registra la cancelación al desprenderse.
     *
     * <p>Casos de uso: CU-35
     * @param presentador presentador de la pantalla
     */
    public VistaResultadoPago(PresentadorResultadoPago presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        resultado.getElement().setAttribute("role", "status");
        resultado.getElement().setAttribute("aria-live", "polite");
        getContent().add(new H3("Resultado del pago"), datos, resultado, acciones);
        addDetachListener(this::alDesprender);
    }

    /**
     * Al entrar guarda la {@code UI} actual y, si la referencia es un UUID válido, llama a {@code
     * presentador.alEntrar(referencia)}; si no, informa el error sin consultar.
     *
     * <p>Casos de uso: CU-35
     * @param parametros parámetros de ruta (sin uso)
     * @param consulta parámetros de consulta ({@code referencia})
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        this.ui = UI.getCurrent();
        List<String> valores = consulta.getParameters().getOrDefault("referencia", List.of());
        UUID referencia;
        try {
            referencia = valores.isEmpty() ? null : UUID.fromString(valores.get(0));
        } catch (IllegalArgumentException e) {
            referencia = null;
        }
        if (referencia == null) {
            resultado.setText("La referencia del pago no es válida.");
            acciones.add(new Anchor("estado-cuenta", "Volver al estado de cuenta"));
            return;
        }
        resultado.setText("Consultando el estado del pago…");
        presentador.alEntrar(referencia);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-35
     * @return PAGAR_CUOTA_ONLINE
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.PAGAR_CUOTA_ONLINE;
    }

    /**
     * Muestra el estado del pago dentro de {@code UI.access(...)} (server push): aprobado con «Ver comprobante»,
     * rechazado o vencido con «Intentar de nuevo», acreditado con «Ver constancia» y pendiente en espera.
     *
     * <p>Casos de uso: CU-35
     * @param resumen estado del pago
     */
    @Override
    public void mostrarEstado(ResumenPagoOnline resumen) {
        acceder(() -> {
            datos.setText("Cuota " + (resumen.periodoCuota() == null ? "" : resumen.periodoCuota())
                    + " · Importe " + FormatoUI.dinero(resumen.importe()));
            acciones.removeAll();
            String estado = resumen.estado() == null ? "" : resumen.estado();
            switch (estado) {
                case "APROBADO" -> {
                    resultado.setText("Pago aprobado. Se emitió el comprobante.");
                    agregarComprobante(resumen, "Ver comprobante");
                }
                case "RECHAZADO" -> {
                    resultado.setText("El pago fue rechazado"
                            + (resumen.motivo() == null ? "." : ": " + resumen.motivo() + "."));
                    acciones.add(new Anchor("estado-cuenta", "Intentar de nuevo"));
                }
                case "VENCIDO" -> {
                    resultado.setText("La intención de pago venció sin completarse. Si la pasarela confirma el"
                            + " cobro más tarde, el pago se registrará igual.");
                    acciones.add(new Anchor("estado-cuenta", "Intentar de nuevo"));
                }
                case "ACREDITADO" -> {
                    resultado.setText("La cuota ya estaba pagada. El importe quedó como saldo a favor del grupo:"
                            + " se aplica de inmediato a la cuota impaga más antigua que cubra por completo y el"
                            + " resto, en la próxima emisión.");
                    agregarComprobante(resumen, "Ver constancia");
                }
                default -> resultado.setText("Esperando la confirmación de la pasarela…");
            }
        });
    }

    /**
     * Informa, dentro de {@code UI.access(...)}, que el pago sigue en proceso tras diez consultas (FA13).
     *
     * <p>Casos de uso: CU-35
     */
    @Override
    public void mostrarEsperaConfirmacion() {
        acceder(() -> {
            resultado.setText("El pago está en proceso. Se acreditará cuando la pasarela lo confirme; puede volver"
                    + " a consultarlo desde Mis pagos y comprobantes.");
            acciones.removeAll();
            acciones.add(new Anchor("pagos", "Mis pagos y comprobantes"), new Span(" "),
                    new Anchor("estado-cuenta", "Volver al estado de cuenta"));
        });
    }

    /**
     * Muestra un error dentro de {@code UI.access(...)}, porque puede llegar desde el hilo del planificador.
     *
     * <p>Casos de uso: CU-35
     * @param mensaje mensaje de error
     */
    @Override
    public void mostrarError(String mensaje) {
        acceder(() -> {
            resultado.setText(mensaje);
            super.mostrarError(mensaje);
        });
    }

    /**
     * Muestra un mensaje dentro de {@code UI.access(...)}.
     *
     * <p>Casos de uso: CU-35
     * @param mensaje mensaje informativo
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        acceder(() -> super.mostrarMensaje(mensaje));
    }

    /**
     * Agrega el enlace al comprobante o la constancia (P-29), si el pago lo tiene.
     *
     * <p>Casos de uso: CU-35
     * @param resumen estado del pago
     * @param texto texto del enlace
     */
    private void agregarComprobante(ResumenPagoOnline resumen, String texto) {
        if (resumen.idComprobante() != null) {
            acciones.add(new Anchor("pagos/comprobante/" + resumen.idComprobante(), texto), new Span(" "));
        }
        acciones.add(new Anchor("estado-cuenta", "Volver al estado de cuenta"));
    }

    /**
     * Ejecuta una actualización de la interfaz con {@code UI.access(...)} (server push). Si la vista todavía no tiene
     * {@code UI} la ejecuta directamente (hilo de la interfaz).
     *
     * <p>Casos de uso: CU-35
     * @param accion actualización de componentes
     */
    private void acceder(Runnable accion) {
        UI destino = ui != null ? ui : getUI().orElse(UI.getCurrent());
        if (destino == null) {
            return;
        }
        destino.access(accion::run);
    }

    /**
     * Al desprenderse la vista, {@code presentador.alSalir()} cancela la consulta programada.
     *
     * <p>Casos de uso: CU-35
     * @param evento evento de desprendimiento
     */
    private void alDesprender(DetachEvent evento) {
        presentador.alSalir();
    }
}

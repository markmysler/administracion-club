package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-25: Cuotas del grupo con su estado, crédito aplicado e importe a pagar, y saldo a favor
 * del grupo. No tiene lógica: los oyentes solo llaman al presentador. El botón «Pagar online» solo aparece en las
 * filas con {@code admitePagoOnline}; «Ver comprobantes» en las cuotas pagadas.
 *
 * <p>Ruta: {@code estado-cuenta}. Permiso: {@code CONSULTAR_PERFIL_PROPIO}.
 *
 * <p>Casos de uso: CU-05, CU-35
 */
@Route(value = "estado-cuenta", layout = DisposicionPrincipal.class)
@PageTitle("Estado de cuenta")
public class VistaEstadoCuenta extends VistaProtegida implements PantallaEstadoCuenta {

    /** Presentador de la pantalla. */
    private final PresentadorEstadoCuenta presentador;

    /** Grilla de cuotas (con detalle de ítems). */
    private final Grid<FilaCuota> cuotas = new Grid<>(FilaCuota.class, false);

    /** Aviso para quien no es titular (RN-09). */
    private final Paragraph avisoTitular = new Paragraph();

    /** Saldo a favor. */
    private final Paragraph saldo = new Paragraph();

    /** Movimientos del saldo a favor. */
    private final TablaDatos<FilaMovimientoSaldo> movimientos = new TablaDatos<>(FilaMovimientoSaldo.class);

    /**
     * Arma la pantalla y se vincula con su presentador.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param presentador presentador de P-25
     */
    public VistaEstadoCuenta(PresentadorEstadoCuenta presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        cuotas.addColumn(FilaCuota::periodo).setHeader("Período").setAutoWidth(true);
        cuotas.addColumn(f -> FormatoUI.dinero(f.importe())).setHeader("Importe").setAutoWidth(true);
        cuotas.addColumn(f -> FormatoUI.dinero(f.creditoAplicado())).setHeader("Crédito aplicado").setAutoWidth(true);
        cuotas.addColumn(f -> FormatoUI.dinero(f.importeAPagar())).setHeader("Importe a pagar").setAutoWidth(true);
        cuotas.addColumn(f -> FormatoUI.fecha(f.fechaVencimiento())).setHeader("Vencimiento").setAutoWidth(true);
        cuotas.addColumn(FilaCuota::estado).setHeader("Estado").setAutoWidth(true);
        cuotas.addComponentColumn(this::acciones).setHeader("Acciones").setAutoWidth(true);
        cuotas.setItemDetailsRenderer(new ComponentRenderer<>(this::detalleItems));
        cuotas.setAllRowsVisible(true);
        movimientos.agregarColumna("Fecha", m -> FormatoUI.fechaHora(m.fecha()));
        movimientos.agregarColumna("Tipo", FilaMovimientoSaldo::tipo);
        movimientos.agregarColumna("Importe", m -> FormatoUI.dinero(m.importe()));
        movimientos.agregarColumna("Motivo", FilaMovimientoSaldo::motivo);
        getContent().add(new H2("Estado de cuenta"), avisoTitular, cuotas, new H3("Saldo a favor"), saldo, movimientos);
    }

    /**
     * Botones de la fila: «Pagar online» (si {@code admitePagoOnline}) y «Ver comprobantes» (cuota pagada).
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param fila cuota
     * @return contenedor con los botones
     */
    private Div acciones(FilaCuota fila) {
        Div contenedor = new Div();
        if (fila.admitePagoOnline()) {
            Button pagar = new Button("Pagar online", e -> presentador.alPagar(fila.id()));
            pagar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
            contenedor.add(pagar);
        }
        if (!fila.admitePago()) {
            Button ver = new Button("Ver comprobantes", e -> presentador.alVerComprobantes(fila.periodo()));
            ver.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            contenedor.add(ver);
        }
        return contenedor;
    }

    /**
     * Detalle de la fila: aporte de cada integrante.
     *
     * <p>Casos de uso: CU-05
     * @param fila cuota
     * @return lista de ítems
     */
    private Div detalleItems(FilaCuota fila) {
        Div detalle = new Div();
        for (FilaItemCuota item : fila.items()) {
            detalle.add(new Paragraph(item.socio() + " (" + item.categoria() + "): " + FormatoUI.dinero(item.importe())));
        }
        return detalle;
    }

    /**
     * Gancho de {@code VistaProtegida}: pide al presentador la carga (paso 1).
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param parametros parámetros de la ruta (no usa)
     * @param consulta parámetros de la consulta (no usa)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @return CONSULTAR_PERFIL_PROPIO
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONSULTAR_PERFIL_PROPIO;
    }

    /**
     * Muestra las cuotas; si la cuenta no es titular avisa que solo el titular paga online (FA1, RN-09).
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param filas cuotas del grupo
     * @param esTitular true si es titular
     */
    @Override
    public void mostrarCuotas(List<FilaCuota> filas, boolean esTitular) {
        cuotas.setItems(filas);
        avisoTitular.setText(esTitular ? "" : "Solo el titular del grupo familiar puede pagar las cuotas online.");
        if (filas.stream().noneMatch(FilaCuota::admitePago)) {
            avisoTitular.setText(avisoTitular.getText() + " No hay cuotas pendientes de pago.");
        }
    }

    /**
     * Muestra el saldo a favor y sus movimientos.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param saldo saldo y movimientos
     */
    @Override
    public void mostrarSaldo(ResumenSaldo saldo) {
        this.saldo.setText(FormatoUI.dinero(saldo.saldo()));
        movimientos.mostrar(saldo.movimientos());
    }

    /**
     * Sale de la aplicación hacia la pasarela: {@code UI.getCurrent().getPage().setLocation(url)} (paso 12).
     *
     * <p>Casos de uso: CU-35
     * @param url página de pago
     */
    @Override
    public void redirigirA(String url) {
        UI.getCurrent().getPage().setLocation(url);
    }
}

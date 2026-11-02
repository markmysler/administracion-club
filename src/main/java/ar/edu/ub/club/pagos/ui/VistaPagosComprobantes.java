package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FiltroFechas;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.FilaMovimientoSaldo;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import ar.edu.ub.club.pagos.aplicacion.FiltroPagos;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista de la pantalla P-28 Pagos y comprobantes (MVP Vista Pasiva, sin lógica): filtros (período de fechas con {@code
 * FiltroFechas}, período de cuota, estado y, para el administrador, grupo o titular), línea de saldo a favor con sus
 * movimientos desplegables, grilla de pagos y acceso al comprobante o constancia. Ruta {@code pagos?periodo=…},
 * permiso CONSULTAR_PAGOS_PROPIOS (alternativo: CONSULTAR_PAGOS_CLUB).
 *
 * <p>Casos de uso: CU-05, CU-37
 */
@Route(value = "pagos", layout = DisposicionPrincipal.class)
@PageTitle("Pagos y comprobantes")
public class VistaPagosComprobantes extends VistaProtegida implements PantallaPagosComprobantes {

    /** Estados del filtro (vacío = todos). */
    private static final List<String> ESTADOS =
            List.of("PENDIENTE", "APROBADO", "RECHAZADO", "VENCIDO", "ACREDITADO");

    /** Presentador de la pantalla. */
    private final PresentadorPagosComprobantes presentador;

    /** Rango de fechas del pago. */
    private final FiltroFechas fechas = new FiltroFechas();

    /** Período de la cuota (AAAA-MM). */
    private final TextField periodo = new TextField("Período");

    /** Estado del pago. */
    private final ComboBox<String> estado = new ComboBox<>("Estado", ESTADOS);

    /** Texto para buscar grupos (administrador). */
    private final TextField buscarGrupo = new TextField("Grupo o titular");

    /** Grupo elegido para el filtro (administrador). */
    private final ComboBox<FilaGrupo> grupo = new ComboBox<>("Grupo");

    /** Zona del filtro por grupo (solo administrador). */
    private final HorizontalLayout filtroGrupo = new HorizontalLayout();

    /** Línea de saldo a favor. */
    private final Span saldo = new Span();

    /** Movimientos de saldo (desplegable). */
    private final TablaDatos<FilaMovimientoSaldo> movimientos = new TablaDatos<>(FilaMovimientoSaldo.class);

    /** Zona de saldo. */
    private final Div zonaSaldo = new Div();

    /** Grilla de pagos. */
    private final Grid<FilaPago> pagos = new Grid<>(FilaPago.class, false);

    /** Columna de grupo (solo administrador). */
    private final Grid.Column<FilaPago> columnaGrupo;

    /**
     * Arma los componentes y vincula el presentador; el rango inicial son los últimos 12 meses.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param presentador presentador de la pantalla
     */
    public VistaPagosComprobantes(PresentadorPagosComprobantes presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        LocalDate hoy = LocalDate.now(RelojSistema.ZONA_CLUB);
        fechas.fijar(hoy.minusMonths(12), hoy);
        periodo.setPattern("\\d{4}-(0[1-9]|1[0-2])");
        periodo.setPlaceholder("AAAA-MM");
        periodo.setErrorMessage("Formato AAAA-MM");
        estado.setClearButtonVisible(true);
        grupo.setItemLabelGenerator(g -> g.idGrupo() + " · " + g.titular());
        grupo.setClearButtonVisible(true);
        Button botonBuscar = new Button("Buscar grupo", e -> presentador.alBuscarGrupo(buscarGrupo.getValue()));
        filtroGrupo.add(buscarGrupo, botonBuscar, grupo, new Anchor("pagos/manual", "Registrar pago manual"));
        filtroGrupo.setAlignItems(HorizontalLayout.Alignment.BASELINE);
        Button filtrar = new Button("Filtrar", e -> alFiltrar());
        filtrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout filtros = new HorizontalLayout(fechas, periodo, estado, filtrar);
        filtros.setAlignItems(HorizontalLayout.Alignment.BASELINE);
        filtros.setWrap(true);
        movimientos.agregarColumna("Fecha", m -> FormatoUI.fechaHora(m.fecha()));
        movimientos.agregarColumna("Tipo", FilaMovimientoSaldo::tipo);
        movimientos.agregarColumna("Importe", m -> FormatoUI.dinero(m.importe()));
        movimientos.agregarColumna("Motivo", FilaMovimientoSaldo::motivo);
        zonaSaldo.add(saldo, new Details("Ver movimientos de saldo", movimientos));
        zonaSaldo.setVisible(false);
        pagos.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        pagos.setAllRowsVisible(true);
        pagos.addColumn(p -> FormatoUI.fechaHora(p.fecha())).setHeader("Fecha").setAutoWidth(true);
        columnaGrupo = pagos.addColumn(FilaPago::idGrupo).setHeader("Grupo").setAutoWidth(true);
        pagos.addColumn(FilaPago::periodoCuota).setHeader("Cuota").setAutoWidth(true);
        pagos.addColumn(p -> FormatoUI.dinero(p.importe())).setHeader("Importe").setAutoWidth(true);
        pagos.addColumn(FilaPago::origen).setHeader("Origen").setAutoWidth(true);
        pagos.addColumn(FilaPago::medio).setHeader("Medio").setAutoWidth(true);
        pagos.addColumn(this::textoEstado).setHeader("Estado").setAutoWidth(true);
        pagos.addComponentColumn(this::accion).setHeader("Comprobante").setAutoWidth(true);
        getContent().add(filtros, filtroGrupo, zonaSaldo, pagos);
    }

    /**
     * Permiso principal de la pantalla (socio).
     *
     * <p>Casos de uso: CU-05, CU-37
     * @return CONSULTAR_PAGOS_PROPIOS
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONSULTAR_PAGOS_PROPIOS;
    }

    /**
     * Permiso alternativo de la pantalla (administrador).
     *
     * <p>Casos de uso: CU-37
     * @return CONSULTAR_PAGOS_CLUB
     */
    @Override
    protected Permiso permisoAlternativo() {
        return Permiso.CONSULTAR_PAGOS_CLUB;
    }

    /**
     * Al entrar toma el parámetro {@code periodo} (si viene de P-25) y carga con el filtro inicial (paso 1).
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param parametros parámetros de ruta (sin uso)
     * @param consulta parámetros de consulta ({@code periodo})
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        List<String> p = consulta.getParameters().getOrDefault("periodo", List.of());
        if (!p.isEmpty()) {
            periodo.setValue(p.get(0));
            fechas.fijar(null, null);
        }
        alFiltrar();
    }

    /**
     * Oyente de Filtrar: valida el formato ({@code FiltroFechas} y el período AAAA-MM), arma el {@code FiltroPagos} y
     * llama a {@code presentador.cargar(filtro)} (paso 6).
     *
     * <p>Casos de uso: CU-05, CU-37
     */
    private void alFiltrar() {
        if (periodo.isInvalid()) {
            return;
        }
        fechas.valor().ifPresent(rango -> {
            FiltroPagos filtro = new FiltroPagos();
            filtro.setDesde(rango.desde());
            filtro.setHasta(rango.hasta());
            filtro.setPeriodo(periodo.getValue().isBlank() ? null : periodo.getValue());
            filtro.setEstado(estado.getValue());
            filtro.setIdGrupo(grupo.getValue() == null ? null : grupo.getValue().idGrupo());
            presentador.cargar(filtro);
        });
    }

    /**
     * Muestra los pagos.
     *
     * <p>La grilla usa carga perezosa por páginas: Vaadin pide solo las filas visibles, con el orden de la
     * columna elegida.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param filas pagos
     */
    @Override
    public void mostrarPagos(List<FilaPago> filas) {
        pagos.setItems(new CallbackDataProvider<FilaPago, Void>(
                consulta -> {
                    java.util.stream.Stream<FilaPago> datos = filas.stream();
                    if (consulta.getSortingComparator().isPresent()) {
                        datos = datos.sorted(consulta.getSortingComparator().get());
                    }
                    return datos.skip(consulta.getOffset()).limit(consulta.getLimit());
                },
                consulta -> filas.size()));
    }

    /**
     * Muestra el saldo y sus movimientos; {@code null} oculta la zona de saldo.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param resumen saldo del grupo
     */
    @Override
    public void mostrarSaldo(ResumenSaldo resumen) {
        zonaSaldo.setVisible(resumen != null);
        if (resumen != null) {
            saldo.setText("Saldo a favor del grupo: " + FormatoUI.dinero(resumen.saldo()) + " ");
            movimientos.mostrar(resumen.movimientos());
        }
    }

    /**
     * Muestra u oculta el filtro y la columna de grupo.
     *
     * <p>Casos de uso: CU-37
     * @param visible {@code true} para el administrador
     */
    @Override
    public void mostrarFiltroGrupo(boolean visible) {
        filtroGrupo.setVisible(visible);
        columnaGrupo.setVisible(visible);
    }

    /**
     * Carga los grupos sugeridos en el combo de grupo.
     *
     * <p>Casos de uso: CU-37
     * @param filas grupos
     */
    @Override
    public void mostrarGrupos(List<FilaGrupo> filas) {
        grupo.setItems(filas);
        if (filas.size() == 1) {
            grupo.setValue(filas.get(0));
        }
    }

    /**
     * Texto del estado; un pago vencido se muestra «VENCIDO (Abandonado)» con su motivo.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param p fila de pago
     * @return texto del estado
     */
    private String textoEstado(FilaPago p) {
        return p.motivo() == null ? p.estado() : p.estado() + " (" + p.motivo() + ")";
    }

    /**
     * Celda de acción: «Ver comprobante» (aprobado) o «Ver constancia» (acreditado) que llama a {@code
     * presentador.alVerComprobante(id)}; «-» si el pago no tiene comprobante (FA4).
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param p fila de pago
     * @return componente de la celda
     */
    private Component accion(FilaPago p) {
        if (p.idComprobante() == null) {
            Span sin = new Span("-");
            sin.setTitle("Sin comprobante: solo los pagos aprobados o acreditados lo tienen");
            return sin;
        }
        String texto = "ACREDITADO".equals(p.estado()) ? "Ver constancia" : "Ver comprobante";
        Button boton = new Button(texto, e -> presentador.alVerComprobante(p.idComprobante()));
        boton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return boton;
    }
}

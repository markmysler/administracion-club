package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-05: Inicio del socio con el resumen de la cuota vigente, las próximas reservas y los
 * accesos a reservar y pagar.
 *
 * <p>Ruta: {@code inicio}. Permiso: {@code CONSULTAR_PERFIL_PROPIO}.
 *
 * <p>Vista pasiva del patrón MVP: sus oyentes solo llaman a {@code presentador.al…(...)}.
 *
 * <p>Casos de uso: CU-05, CU-29
 */
@Route(value = "inicio", layout = DisposicionPrincipal.class)
@PageTitle("Inicio")
public class VistaInicioSocio extends VistaProtegida implements PantallaInicioSocio {

    /** Presentador de la pantalla. */
    private final PresentadorInicioSocio presentador;

    /** Saludo. */
    private final H2 saludo = new H2("Inicio");

    /** Estado de la cuota vigente. */
    private final Paragraph cuota = new Paragraph();

    /** Próximas reservas. */
    private final TablaDatos<FilaResumenReserva> reservas = new TablaDatos<>(FilaResumenReserva.class);

    /** Leyenda sin reservas. */
    private final Paragraph sinReservas = new Paragraph("No tenés reservas próximas.");

    /**
     * Arma el resumen y los accesos y se vincula con el presentador.
     *
     * <p>Casos de uso: CU-05, CU-29
     * @param presentador presentador de P-05
     */
    public VistaInicioSocio(PresentadorInicioSocio presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        reservas.agregarColumna("Fecha", f -> FormatoUI.fecha(f.fecha()));
        reservas.agregarColumna("Horario", f -> FormatoUI.hora(f.horaInicio()) + " a " + FormatoUI.hora(f.horaFin()));
        reservas.agregarColumna("Cancha", FilaResumenReserva::cancha);
        Button reservar = new Button("Reservar una cancha", e -> presentador.alReservar());
        reservar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button pagar = new Button("Ver estado de cuenta y pagar", e -> presentador.alPagar());
        HorizontalLayout accesos = new HorizontalLayout(reservar, pagar);
        accesos.setWrap(true);
        getContent().add(saludo, new H3("Cuota del grupo"), cuota, accesos, new H3("Próximas reservas"), sinReservas,
                reservas);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-05, CU-29
     * @return {@code CONSULTAR_PERFIL_PROPIO}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONSULTAR_PERFIL_PROPIO;
    }

    /**
     * Pide la carga del resumen.
     *
     * <p>Casos de uso: CU-05
     * @param parametros parámetros de la ruta (sin uso)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Muestra el resumen del perfil.
     *
     * <p>Casos de uso: CU-05, CU-29
     * @param resumen modelo de vista del perfil
     */
    @Override
    public void mostrarResumen(ResumenPerfil resumen) {
        saludo.setText("Hola, " + resumen.nombre());
        FilaCuota vigente = resumen.cuotaVigente();
        cuota.setText(vigente == null ? "Sin cuotas emitidas."
                : "Período " + vigente.periodo() + ": " + vigente.estado() + " · a pagar "
                        + FormatoUI.dinero(vigente.importeAPagar()) + " · vence " + FormatoUI.fecha(vigente.fechaVencimiento()));
        reservas.mostrar(resumen.proximasReservas());
        sinReservas.setVisible(resumen.proximasReservas().isEmpty());
    }
}

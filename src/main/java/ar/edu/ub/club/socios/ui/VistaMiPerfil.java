package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.cuotas.ui.FilaMovimientoSaldo;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;
import ar.edu.ub.club.grupos.ui.FilaIntegrante;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-24: datos personales del socio, integrantes de su grupo, indicación de titular, estado de
 * la cuota y saldo a favor del grupo con sus movimientos, sin opciones de edición (CU-05). Los enlaces a «Mis
 * reservas» (P-21) y «Comprobantes del grupo» (P-28) llevan a las pantallas de esos casos de uso.
 *
 * <p>Ruta: {@code mi-perfil}. Permiso: {@code CONSULTAR_PERFIL_PROPIO}.
 *
 * <p>Vista pasiva del patrón MVP: solo muestra los modelos de vista que le entrega el presentador.
 *
 * <p>Casos de uso: CU-05
 */
@Route(value = "mi-perfil", layout = DisposicionPrincipal.class)
@PageTitle("Mi perfil")
public class VistaMiPerfil extends VistaProtegida implements PantallaMiPerfil {

    /** Presentador de la pantalla. */
    private final PresentadorMiPerfil presentador;

    /** Datos personales. */
    private final Paragraph datos = new Paragraph();

    /** Grupo e indicación de titular. */
    private final Paragraph grupo = new Paragraph();

    /** Estado de la cuota vigente. */
    private final Paragraph cuota = new Paragraph();

    /** Saldo a favor. */
    private final Paragraph saldo = new Paragraph();

    /** Integrantes del grupo. */
    private final TablaDatos<FilaIntegrante> integrantes = new TablaDatos<>(FilaIntegrante.class);

    /** Movimientos del saldo a favor. */
    private final TablaDatos<FilaMovimientoSaldo> movimientos = new TablaDatos<>(FilaMovimientoSaldo.class);

    /** Leyenda sin movimientos (CU-05 FA2). */
    private final Paragraph sinMovimientos = new Paragraph("No hay movimientos de saldo.");

    /**
     * Arma las secciones y se vincula con el presentador.
     *
     * <p>Casos de uso: CU-05
     * @param presentador presentador de P-24
     */
    public VistaMiPerfil(PresentadorMiPerfil presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        integrantes.agregarColumna("Apellido", FilaIntegrante::apellido);
        integrantes.agregarColumna("Nombre", FilaIntegrante::nombre);
        integrantes.agregarColumna("Categoría", FilaIntegrante::categoria);
        integrantes.agregarColumna("Titular", f -> f.esTitular() ? "Sí" : "");
        movimientos.agregarColumna("Fecha", f -> FormatoUI.fechaHora(f.fecha()));
        movimientos.agregarColumna("Tipo", FilaMovimientoSaldo::tipo);
        movimientos.agregarColumna("Importe", f -> FormatoUI.dinero(f.importe()));
        movimientos.agregarColumna("Motivo", FilaMovimientoSaldo::motivo);
        HorizontalLayout enlaces = new HorizontalLayout(new Anchor("mis-reservas", "Mis reservas"),
                new Anchor("pagos", "Comprobantes del grupo"));
        getContent().add(new H2("Mi perfil"), datos, enlaces, new H3("Grupo familiar"), grupo, integrantes,
                new H3("Cuota del grupo"), cuota, new H3("Saldo a favor"), saldo, sinMovimientos, movimientos);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-05
     * @return {@code CONSULTAR_PERFIL_PROPIO}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONSULTAR_PERFIL_PROPIO;
    }

    /**
     * Pide la carga del perfil.
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
     * Muestra datos, grupo y cuota vigente.
     *
     * <p>Casos de uso: CU-05
     * @param resumen modelo de vista del perfil
     */
    @Override
    public void mostrarPerfil(ResumenPerfil resumen) {
        datos.setText(resumen.apellido() + ", " + resumen.nombre() + " · DNI " + resumen.dni() + " · Nacimiento "
                + FormatoUI.fecha(resumen.fechaNacimiento()) + " · Teléfono " + valor(resumen.telefono())
                + " · Dirección " + valor(resumen.direccion()) + " · Correo " + resumen.email() + " · Categoría "
                + resumen.categoria());
        grupo.setText("Grupo " + resumen.idGrupo() + (resumen.esTitular() ? " · Sos el titular del grupo." : ""));
        integrantes.mostrar(resumen.integrantes());
        FilaCuota vigente = resumen.cuotaVigente();
        cuota.setText(vigente == null ? "Sin cuotas emitidas."
                : "Período " + vigente.periodo() + ": " + vigente.estado() + " · a pagar "
                        + FormatoUI.dinero(vigente.importeAPagar()));
    }

    /**
     * Muestra el saldo a favor y sus movimientos.
     *
     * <p>Casos de uso: CU-05
     * @param saldo modelo de vista del saldo
     */
    @Override
    public void mostrarSaldo(ResumenSaldo saldo) {
        this.saldo.setText("Saldo a favor del grupo: " + FormatoUI.dinero(saldo.saldo()));
        movimientos.mostrar(saldo.movimientos());
        sinMovimientos.setVisible(saldo.movimientos().isEmpty());
    }

    /**
     * Muestra un texto opcional o un guion.
     *
     * <p>Casos de uso: CU-05
     * @param texto texto
     * @return texto o «-»
     */
    private static String valor(String texto) {
        return texto == null ? "-" : texto;
    }
}

package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.aplicacion.CriterioBusquedaGrupo;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import ar.edu.ub.club.pagos.dominio.Pago;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Presentador de la pantalla P-30 Pago manual (MVP Vista Pasiva; no usa tipos de Vaadin): medios habilitados,
 * búsqueda del grupo vigente, cuotas impagas y registro del pago con el administrador de la sesión (RN-20). El importe
 * es el importe a pagar de la cuota y no se edita (RN-08, RN-21). Diagramas sec_cu36_p1 y sec_cu36_p2.
 *
 * <p>Casos de uso: CU-36
 */
@SpringComponent
@UIScope
public class PresentadorPagoManual extends PresentadorBase<PantallaPagoManual> {

    /** Fachada de pagos. */
    private final ServicioPagos servicioPagos;

    /** Fachada de grupos (búsqueda). */
    private final ServicioGruposFamiliares servicioGruposFamiliares;

    /** Fachada de cuotas (cuotas del grupo). */
    private final ServicioCuotas servicioCuotas;

    /** Cuenta de la sesión. */
    private final UsuarioActual usuarioActual;

    /**
     * Agregado en la implementación (estado de la pantalla, MODELO 11.5.2): grupo elegido, para recargar sus cuotas
     * después de un error ({@code alElegirGrupo(idGrupo)} de sec_cu36_p2).
     */
    private Long idGrupo;

    /**
     * Crea el presentador con sus dependencias.
     *
     * <p>Casos de uso: CU-36
     * @param servicioPagos fachada de pagos
     * @param servicioGruposFamiliares fachada de grupos
     * @param servicioCuotas fachada de cuotas
     * @param usuarioActual cuenta de la sesión
     */
    public PresentadorPagoManual(ServicioPagos servicioPagos, ServicioGruposFamiliares servicioGruposFamiliares,
                                 ServicioCuotas servicioCuotas, UsuarioActual usuarioActual) {
        this.servicioPagos = servicioPagos;
        this.servicioGruposFamiliares = servicioGruposFamiliares;
        this.servicioCuotas = servicioCuotas;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Carga los medios: {@code ServicioPagos.listarMediosHabilitados()} (objeto consulta {@code MediosPagoHabilitados})
     * → {@code OpcionSeleccion}. Sin medios habilitados avisa «Debe habilitarse un medio manual (CU-34)» (FA3).
     *
     * <p>Casos de uso: CU-36
     */
    public void cargar() {
        consultar(() -> servicioPagos.listarMediosHabilitados()).ifPresent(medios -> {
            List<OpcionSeleccion> opciones = medios.stream()
                    .sorted(Comparator.comparing(MedioPago::getNombre))
                    .map(m -> new OpcionSeleccion(m.getId(), m.getNombre()))
                    .toList();
            pantalla.mostrarMedios(opciones);
            if (opciones.isEmpty()) {
                pantalla.mostrarError("Debe habilitarse un medio manual (CU-34)");
            }
        });
    }

    /**
     * Busca grupos vigentes (pasos 1 y 2): {@code ServicioGruposFamiliares.buscarGrupos(new
     * CriterioBusquedaGrupo(texto, true))} → {@code FilaGrupo}. Sin resultados: «No hay coincidencias» (FA1).
     *
     * <p>Casos de uso: CU-36
     * @param texto DNI o nombre
     */
    public void alBuscarGrupo(String texto) {
        consultar(() -> servicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, true)))
                .map(resumenes -> resumenes.stream().map(FilaGrupo::desde).toList())
                .ifPresent(filas -> {
                    pantalla.mostrarGrupos(filas);
                    if (filas.isEmpty()) {
                        pantalla.mostrarMensaje("No hay coincidencias");
                    }
                });
    }

    /**
     * Muestra las cuotas impagas del grupo (pasos 3 y 4): {@code ServicioCuotas.consultarCuotasDeGrupo(idGrupo)} →
     * solo las que admiten pago, más antigua primero → {@code FilaCuota.desde(c, false)}. Sin deuda: «El grupo no
     * tiene deuda» (FA2).
     *
     * <p>Casos de uso: CU-36
     * @param idGrupo grupo elegido
     */
    public void alElegirGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
        consultar(() -> servicioCuotas.consultarCuotasDeGrupo(idGrupo)).ifPresent(cuotas -> {
            List<FilaCuota> filas = cuotas.stream()
                    .filter(Cuota::admitePago)
                    .sorted(Comparator.comparing(Cuota::getFechaEmision,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(c -> FilaCuota.desde(c, false))
                    .toList();
            pantalla.mostrarCuotas(filas);
            if (filas.isEmpty()) {
                pantalla.mostrarMensaje("El grupo no tiene deuda");
            }
        });
    }

    /**
     * Registra el pago (pasos 5 a 14): {@code ServicioPagos.registrarPagoManual(usuarioActual.obtenerAdministrador(),
     * idCuota, idMedio, importe)}; si queda registrado navega a {@code "pagos/comprobante/" + idComprobante}. Si la
     * fachada lo rechaza (medio deshabilitado, importe distinto, cuota ya pagada o pago simultáneo: FA5 a FA8), el
     * error ya lo mostró {@code consultar(...)} y se recargan los medios y las cuotas del grupo.
     *
     * <p>Casos de uso: CU-36
     * @param idCuota cuota elegida
     * @param idMedio medio elegido
     * @param importe importe a pagar de la cuota (no editable)
     */
    public void alRegistrar(Long idCuota, Long idMedio, Dinero importe) {
        Optional<Pago> pago = consultar(() -> servicioPagos.registrarPagoManual(
                usuarioActual.obtenerAdministrador(), idCuota, idMedio, importe));
        if (pago.isPresent() && pago.get().getComprobante() != null) {
            pantalla.navegarA("pagos/comprobante/" + pago.get().getComprobante().getId());
            return;
        }
        if (pago.isPresent()) {
            pantalla.mostrarMensaje("Pago registrado");
        }
        cargar();
        if (idGrupo != null) {
            alElegirGrupo(idGrupo);
        }
    }
}

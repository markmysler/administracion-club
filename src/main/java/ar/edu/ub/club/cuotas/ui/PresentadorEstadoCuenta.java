package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import ar.edu.ub.club.pagos.integracion.IntencionPago;
import ar.edu.ub.club.socios.aplicacion.PerfilSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.aplicacion.SocioActual;
import ar.edu.ub.club.socios.dominio.Socio;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;
import java.util.Optional;

/**
 * Presentador de la pantalla P-25: Cuotas del grupo con su estado, crédito aplicado e importe a pagar, y saldo a
 * favor del grupo. Obtiene el socio con {@code SocioActual} (la vista nunca envía su identidad, RN-20) y llama a
 * las fachadas dentro de {@code consultar(...)} (Template Method de {@code PresentadorBase}).
 *
 * <p>Casos de uso: CU-05, CU-35
 */
@SpringComponent
@UIScope
public class PresentadorEstadoCuenta extends PresentadorBase<PantallaEstadoCuenta> {

    /** Fachada de socios: grupo y titularidad. */
    private final ServicioSocios servicioSocios;

    /** Fachada de cuotas. */
    private final ServicioCuotas servicioCuotas;

    /** Fachada de pagos: inicio del pago online. */
    private final ServicioPagos servicioPagos;

    /** Socio de la sesión. */
    private final SocioActual socioActual;

    /**
     * Crea el presentador.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param servicioSocios fachada de socios
     * @param servicioCuotas fachada de cuotas
     * @param servicioPagos fachada de pagos
     * @param socioActual socio de la sesión
     */
    public PresentadorEstadoCuenta(ServicioSocios servicioSocios, ServicioCuotas servicioCuotas, ServicioPagos servicioPagos,
            SocioActual socioActual) {
        this.servicioSocios = servicioSocios;
        this.servicioCuotas = servicioCuotas;
        this.servicioPagos = servicioPagos;
        this.socioActual = socioActual;
    }

    /**
     * {@code ServicioSocios.consultarPerfilPropio(socio)} (grupo y titularidad), {@code
     * ServicioCuotas.consultarCuotasDeGrupo(idGrupo): List&lt;Cuota&gt;}, {@code
     * ServicioCuotas.pagoOnlineHabilitado(): boolean} → {@code FilaCuota.desde(cuota, esTitular && habilitado)};
     * {@code ServicioCuotas.consultarSaldo(idGrupo)} → {@code ResumenSaldo} (sec_cu35_p1, pasos 1-6).
     *
     * <p>Casos de uso: CU-05, CU-35
     */
    public void cargar() {
        Optional<PerfilSocio> perfil = consultar(() -> servicioSocios.consultarPerfilPropio(socioActual.obtener()));
        if (perfil.isEmpty()) {
            return;
        }
        GrupoFamiliar grupo = perfil.get().getGrupo();
        if (grupo == null) {
            pantalla.mostrarCuotas(List.of(), false);
            pantalla.mostrarMensaje("No integra un grupo familiar vigente: no tiene cuotas emitidas.");
            return;
        }
        Socio socio = perfil.get().getSocio();
        boolean esTitular = grupo.getTitular() != null && socio != null && grupo.getTitular().getId().equals(socio.getId());
        boolean habilitado = consultar(servicioCuotas::pagoOnlineHabilitado).orElse(false);
        Optional<List<Cuota>> cuotas = consultar(() -> servicioCuotas.consultarCuotasDeGrupo(grupo.getId()));
        cuotas.ifPresent(lista -> pantalla.mostrarCuotas(
                lista.stream().map(c -> FilaCuota.desde(c, esTitular && habilitado)).toList(), esTitular));
        consultar(() -> servicioCuotas.consultarSaldo(grupo.getId()))
                .ifPresent(s -> pantalla.mostrarSaldo(ResumenSaldo.desde(s)));
    }

    /**
     * {@code ServicioPagos.iniciarPagoOnline(socioActual.obtener(), idCuota): IntencionPago} → {@code
     * pantalla.redirigirA(intencion.urlCheckout)}; si la fachada rechaza (FA1, FA2, FA4, FA5, FA6) el error ya se
     * mostró y se recarga la pantalla (sec_cu35_p2).
     *
     * <p>Casos de uso: CU-35
     * @param idCuota Cuota elegida.
     */
    public void alPagar(Long idCuota) {
        Optional<IntencionPago> intencion = consultar(() -> servicioPagos.iniciarPagoOnline(socioActual.obtener(), idCuota));
        if (intencion.isPresent()) {
            pantalla.redirigirA(intencion.get().getUrlCheckout());
        } else {
            cargar();
        }
    }

    /**
     * {@code navegarA("pagos?periodo=" + periodo)}: abre P-28 filtrada por el período.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param periodo Período de la cuota.
     */
    public void alVerComprobantes(String periodo) {
        pantalla.navegarA("pagos?periodo=" + periodo);
    }
}

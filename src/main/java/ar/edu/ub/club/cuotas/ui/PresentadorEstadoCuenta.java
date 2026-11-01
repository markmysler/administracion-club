package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.aplicacion.SocioActual;

/**
 * Presentador de la pantalla P-25: Cuotas del grupo con su estado, crédito aplicado e importe a pagar, y saldo a
 * favor del grupo.
 *
 * <p>Casos de uso: CU-05, CU-35
 */
public class PresentadorEstadoCuenta extends PresentadorBase<PantallaEstadoCuenta> {

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /** Atributo servicioCuotas. */
    private ServicioCuotas servicioCuotas;

    /** Atributo servicioPagos. */
    private ServicioPagos servicioPagos;

    /** Atributo socioActual. */
    private SocioActual socioActual;

    /**
     * {@code ServicioSocios.consultarPerfilPropio(socio)} (grupo y titularidad), {@code
     * ServicioCuotas.consultarCuotasDeGrupo(idGrupo): List&lt;Cuota&gt;}, {@code
     * ServicioCuotas.pagoOnlineHabilitado(): boolean} → {@code FilaCuota.desde(cuota, esTitular && habilitado)};
     * {@code ServicioCuotas.consultarSaldo(idGrupo)} → {@code ResumenSaldo}
     *
     * <p>Casos de uso: CU-05, CU-35
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioPagos.iniciarPagoOnline(socioActual.obtener(), idCuota): IntencionPago} → {@code
     * pantalla.redirigirA(intencion.urlCheckout)}
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param idCuota valor de {@code idCuota}
     */
    public void alPagar(Long idCuota) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("pagos?periodo=" + periodo)}
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param periodo valor de {@code periodo}
     */
    public void alVerComprobantes(String periodo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

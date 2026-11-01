package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.time.LocalDateTime;

/**
 * Pago que registra la cancelación de una cuota con el saldo a favor del grupo (RN-21). Lo crea {@code
 * ProcesadorPagoConSaldo} ya aprobado.
 *
 * <p>Entidad del dominio; la persiste su broker ({@code BrokerPagoConSaldo}: tablas {@code pago} + {@code pago_con_saldo}).
 *
 * <p>Casos de uso: CU-35, CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class PagoConSaldo extends Pago {

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     */
    private PagoConSaldo() {
    }

    /**
     * Constructor que usa {@code ProcesadorPagoConSaldo}: crea el pago APROBADO con la fecha del momento del
     * registro (zona del club).
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param cuota Cuota cubierta con saldo.
     * @param importe Importe cubierto.
     */
    public PagoConSaldo(Cuota cuota, Dinero importe) {
        super(cuota, importe, LocalDateTime.now(RelojSistema.ZONA_CLUB), EstadoPagoAprobado.INSTANCIA);
    }

    /**
     * Devuelve "CON_SALDO".
     *
     * <p>Casos de uso: CU-37
     * @return "CON_SALDO"
     */
    @Override
    public String origen() {
        return "CON_SALDO";
    }

    /**
     * Devuelve "Saldo a favor".
     *
     * <p>Casos de uso: CU-37
     * @return "Saldo a favor"
     */
    @Override
    public String descripcionMedio() {
        return "Saldo a favor";
    }

    /**
     * Reconstituye un pago con saldo leído de {@code pago} + {@code pago_con_saldo} (operación «persistencia»).
     *
     * <p>Casos de uso: CU-37
     * @param id Identificador.
     * @param fecha Momento.
     * @param importe Importe.
     * @param estado Estado.
     * @param motivoRechazo Motivo (null).
     * @return el pago sin asociaciones
     */
    public static PagoConSaldo reconstituir(Long id, LocalDateTime fecha, Dinero importe, EstadoPago estado, String motivoRechazo) {
        PagoConSaldo p = new PagoConSaldo();
        p.restaurarDatos(id, fecha, importe, estado, motivoRechazo);
        return p;
    }

    /**
     * Entrega las asociaciones resueltas por {@code BrokerPagoConSaldo}.
     *
     * <p>Casos de uso: CU-37
     * @param cuota Cuota.
     * @param comprobante Comprobante o null.
     */
    public void restaurarAsociaciones(Cuota cuota, Comprobante comprobante) {
        restaurarCuotaYComprobante(cuota, comprobante);
    }
}

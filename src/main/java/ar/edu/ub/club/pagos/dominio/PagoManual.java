package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.time.LocalDateTime;

/**
 * Pago registrado por un administrador con un medio manual (CU-36). Lo crea {@code ProcesadorPagoManual} ya
 * aprobado.
 *
 * <p>Entidad del dominio; la persiste su broker ({@code BrokerPagoManual}: tablas {@code pago} + {@code pago_manual}).
 *
 * <p>Casos de uso: CU-36, CU-37
 */
public class PagoManual extends Pago {

    /** Medio utilizado. */
    private MedioPago medio;

    /** Administrador que registró el pago. */
    private Administrador registradoPor;

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     */
    private PagoManual() {
    }

    /**
     * Agregado en la implementación: crea el pago manual ya APROBADO (lo llama {@code
     * ProcesadorPagoManual.crearPago}; el diseño dice «crea PagoManual aprobado» sin nombrar el constructor).
     *
     * <p>Casos de uso: CU-36
     * @param cuota Cuota.
     * @param importe Importe recibido (igual al importe a pagar).
     * @param medio Medio habilitado.
     * @param registradoPor Administrador.
     * @param fecha Momento del registro.
     */
    public PagoManual(Cuota cuota, Dinero importe, MedioPago medio, Administrador registradoPor, LocalDateTime fecha) {
        super(cuota, importe, fecha, EstadoPagoAprobado.INSTANCIA);
        this.medio = medio;
        this.registradoPor = registradoPor;
    }

    /**
     * Devuelve "MANUAL".
     *
     * <p>Casos de uso: CU-37
     * @return "MANUAL"
     */
    @Override
    public String origen() {
        return "MANUAL";
    }

    /**
     * Devuelve el nombre del medio utilizado.
     *
     * <p>Casos de uso: CU-37
     * @return {@code medio.nombre}
     */
    @Override
    public String descripcionMedio() {
        return medio == null ? origen() : medio.getNombre();
    }

    /**
     * Reconstituye un pago manual leído de {@code pago} + {@code pago_manual} (operación «persistencia»).
     *
     * <p>Casos de uso: CU-36, CU-37
     * @param id Identificador.
     * @param fecha Momento.
     * @param importe Importe.
     * @param estado Estado.
     * @param motivoRechazo Motivo (siempre null en la práctica).
     * @return el pago sin asociaciones
     */
    public static PagoManual reconstituir(Long id, LocalDateTime fecha, Dinero importe, EstadoPago estado, String motivoRechazo) {
        PagoManual p = new PagoManual();
        p.restaurarDatos(id, fecha, importe, estado, motivoRechazo);
        return p;
    }

    /**
     * Entrega las asociaciones resueltas por {@code BrokerPagoManual}.
     *
     * <p>Casos de uso: CU-36, CU-37
     * @param cuota Cuota.
     * @param medio Medio.
     * @param registradoPor Administrador.
     * @param comprobante Comprobante o null.
     */
    public void restaurarAsociaciones(Cuota cuota, MedioPago medio, Administrador registradoPor, Comprobante comprobante) {
        restaurarCuotaYComprobante(cuota, comprobante);
        this.medio = medio;
        this.registradoPor = registradoPor;
    }

    /**
     * Devuelve {@code medio}: Medio utilizado.
     *
     * <p>Casos de uso: CU-36, CU-37
     * @return el valor del atributo
     */
    public MedioPago getMedio() {
        return medio;
    }

    /**
     * Devuelve {@code registradoPor}: Administrador que registró el pago.
     *
     * <p>Casos de uso: CU-36, CU-37
     * @return el valor del atributo
     */
    public Administrador getRegistradoPor() {
        return registradoPor;
    }
}

package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import ar.edu.ub.club.pagos.dominio.PagoConSaldo;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code PagoConSaldo}: tablas {@code pago} + {@code pago_con_saldo} (la tabla hija solo tiene el id).
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-35, CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
@Component
public class BrokerPagoConSaldo extends BrokerPago<PagoConSaldo> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerPagoConSaldo(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Tabla hija.
     *
     * <p>Casos de uso: CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return "pago_con_saldo"
     */
    @Override
    protected String tablaHija() {
        return "pago_con_saldo";
    }

    /**
     * La tabla hija no tiene columnas propias (solo el id).
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param objeto pago con saldo
     * @return mapa vacío
     */
    @Override
    protected Map<String, Object> valoresHija(PagoConSaldo objeto) {
        return new LinkedHashMap<>();
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return PagoConSaldo.class
     */
    @Override
    public Class<PagoConSaldo> claseObjeto() {
        return PagoConSaldo.class;
    }

    /**
     * Reconstituye el pago con saldo y recuerda la cuota.
     *
     * <p>Casos de uso: CU-37
     * @param fila fila de {@code pago} + {@code pago_con_saldo}
     * @return el pago
     */
    @Override
    protected PagoConSaldo crearObjeto(FilaResultado fila) {
        PagoConSaldo p = PagoConSaldo.reconstituir(fila.largo("id"), fila.fechaHora("fecha"), fila.dinero("importe"),
                estadoDe(fila), fila.texto("motivo_rechazo"));
        return recordarClaves(p, fila);
    }

    /**
     * Entrega cuota y comprobante.
     *
     * <p>Casos de uso: CU-37
     * @param objeto pago
     * @param cuota cuota
     * @param comprobante comprobante o null
     * @param clavesHija sin claves propias
     */
    @Override
    protected void restaurar(PagoConSaldo objeto, Cuota cuota, Comprobante comprobante, Long[] clavesHija) {
        objeto.restaurarAsociaciones(cuota, comprobante);
    }
}

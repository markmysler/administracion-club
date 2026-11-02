package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import ar.edu.ub.club.pagos.dominio.PagoManual;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code PagoManual}: tablas {@code pago} + {@code pago_manual} (medio y administrador).
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-36, CU-37
 */
@Component
public class BrokerPagoManual extends BrokerPago<PagoManual> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerPagoManual(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Tabla hija.
     *
     * <p>Casos de uso: CU-36, CU-37
     * @return "pago_manual"
     */
    @Override
    protected String tablaHija() {
        return "pago_manual";
    }

    /**
     * Columnas de {@code pago_manual}.
     *
     * <p>Casos de uso: CU-36
     * @param objeto pago manual
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresHija(PagoManual objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("medio_pago_id", objeto.getMedio());
        v.put("registrado_por_id", objeto.getRegistradoPor());
        return v;
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return PagoManual.class
     */
    @Override
    public Class<PagoManual> claseObjeto() {
        return PagoManual.class;
    }

    /**
     * Reconstituye el pago manual y recuerda cuota, medio y administrador.
     *
     * <p>Casos de uso: CU-36, CU-37
     * @param fila fila de {@code pago} + {@code pago_manual}
     * @return el pago
     */
    @Override
    protected PagoManual crearObjeto(FilaResultado fila) {
        PagoManual p = PagoManual.reconstituir(fila.largo("id"), fila.fechaHora("fecha"), fila.dinero("importe"),
                estadoDe(fila), fila.texto("motivo_rechazo"));
        return recordarClaves(p, fila, fila.largo("medio_pago_id"), fila.largo("registrado_por_id"));
    }

    /**
     * Resuelve medio y administrador por {@code get} y entrega las asociaciones.
     *
     * <p>Casos de uso: CU-36, CU-37
     * @param objeto pago
     * @param cuota cuota
     * @param comprobante comprobante o null
     * @param clavesHija [medio_pago_id, registrado_por_id]
     */
    @Override
    protected void restaurar(PagoManual objeto, Cuota cuota, Comprobante comprobante, Long[] clavesHija) {
        objeto.restaurarAsociaciones(cuota, fachada().get(clavesHija[0], MedioPago.class),
                fachada().get(clavesHija[1], Administrador.class), comprobante);
    }
}

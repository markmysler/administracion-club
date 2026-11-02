package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code PagoOnline}: tablas {@code pago} + {@code pago_online} (referencia externa UNIQUE, id de la
 * pasarela UNIQUE, respuesta JSONB, titular que inició el pago).
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-35, CU-37; tareas programadas: vencimiento de pagos online pendientes
 */
@Component
public class BrokerPagoOnline extends BrokerPago<PagoOnline> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerPagoOnline(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Tabla hija.
     *
     * <p>Casos de uso: CU-35, CU-37
     * @return "pago_online"
     */
    @Override
    protected String tablaHija() {
        return "pago_online";
    }

    /**
     * Columnas de {@code pago_online} (la respuesta JSONB se pasa como texto JSON).
     *
     * <p>Casos de uso: CU-35
     * @param objeto pago online
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresHija(PagoOnline objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("referencia_externa", objeto.getReferenciaExterna());
        v.put("id_pago_pasarela", objeto.getIdPagoPasarela());
        v.put("respuesta_pasarela", objeto.getRespuestaPasarela());
        v.put("iniciado_por_id", objeto.getIniciadoPor());
        return v;
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return PagoOnline.class
     */
    @Override
    public Class<PagoOnline> claseObjeto() {
        return PagoOnline.class;
    }

    /**
     * Reconstituye el pago online desde la fila del JOIN y recuerda cuota y titular.
     *
     * <p>Casos de uso: CU-35, CU-37
     * @param fila fila de {@code pago} + {@code pago_online}
     * @return el pago
     */
    @Override
    protected PagoOnline crearObjeto(FilaResultado fila) {
        PagoOnline p = PagoOnline.reconstituir(fila.largo("id"), fila.fechaHora("fecha"), fila.dinero("importe"),
                estadoDe(fila), fila.texto("motivo_rechazo"), fila.uuid("referencia_externa"),
                fila.texto("id_pago_pasarela"), fila.texto("respuesta_pasarela"));
        return recordarClaves(p, fila, fila.largo("iniciado_por_id"));
    }

    /**
     * Resuelve el titular por {@code get} y entrega las asociaciones.
     *
     * <p>Casos de uso: CU-35, CU-37
     * @param objeto pago
     * @param cuota cuota
     * @param comprobante comprobante o null
     * @param clavesHija [iniciado_por_id]
     */
    @Override
    protected void restaurar(PagoOnline objeto, Cuota cuota, Comprobante comprobante, Long[] clavesHija) {
        objeto.restaurarAsociaciones(cuota, fachada().get(clavesHija[0], Socio.class), comprobante);
    }
}

package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import ar.edu.ub.club.cuotas.dominio.TipoMovimientoSaldo;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code MovimientoSaldo} sobre {@code movimiento_saldo}. El pago del crédito se guarda por id (cuotas
 * no depende de pagos); la cuota del débito se resuelve con {@code fachada().get}.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-05, CU-35, CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
@Component
public class BrokerMovimientoSaldo extends BrokerBaseDatos<MovimientoSaldo> {

    /** Cuota del débito leída en {@code crearObjeto} hasta {@code cargarAsociaciones}. */
    private final Map<MovimientoSaldo, Long> pendientes = Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerMovimientoSaldo(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Resuelve la cuota del débito (si tiene) y la entrega con {@code restaurarAsociaciones}.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-37
     * @param m movimiento recién creado
     */
    @Override
    protected void cargarAsociaciones(MovimientoSaldo m) {
        Long idCuota = pendientes.remove(m);
        m.restaurarAsociaciones(idCuota == null ? null : fachada().get(idCuota, Cuota.class));
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return MovimientoSaldo.class
     */
    @Override
    public Class<MovimientoSaldo> claseObjeto() {
        return MovimientoSaldo.class;
    }

    /**
     * Tabla principal.
     *
     * <p>Casos de uso: todos
     * @return "movimiento_saldo"
     */
    @Override
    protected String tabla() {
        return "movimiento_saldo";
    }

    /**
     * Reconstituye el movimiento y guarda la cuota del débito para {@code cargarAsociaciones}.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-37
     * @param fila fila de {@code movimiento_saldo}
     * @return el movimiento
     */
    @Override
    protected MovimientoSaldo crearObjeto(FilaResultado fila) {
        MovimientoSaldo m = MovimientoSaldo.reconstituir(fila.largo("id"), fila.largo("grupo_familiar_id"),
                fila.fechaHora("fecha"), TipoMovimientoSaldo.valueOf(fila.texto("tipo").trim()),
                fila.dinero("importe"), fila.texto("motivo"), fila.largo("pago_id"));
        Long idCuota = fila.largo("cuota_id");
        if (idCuota != null) {
            pendientes.put(m, idCuota);
        }
        return m;
    }

    /**
     * Columnas del INSERT (crédito con {@code pago_id}, débito con {@code cuota_id}, CHECK {@code ck_movimiento_origen}).
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param objeto movimiento nuevo
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(MovimientoSaldo objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("grupo_familiar_id", objeto.getIdGrupo());
        v.put("fecha", objeto.getFecha());
        v.put("tipo", objeto.getTipo());
        v.put("importe", objeto.getImporte());
        v.put("motivo", objeto.getMotivo());
        v.put("pago_id", objeto.getIdPago());
        v.put("cuota_id", objeto.getCuota());
        return v;
    }

    /**
     * Columnas del UPDATE: el movimiento es inmutable.
     *
     * <p>Casos de uso: ninguno (el movimiento no cambia)
     * @param objeto movimiento
     * @return mapa vacío
     */
    @Override
    protected Map<String, Object> valoresActualizacion(MovimientoSaldo objeto) {
        return new LinkedHashMap<>();
    }
}

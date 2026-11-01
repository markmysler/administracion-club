package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import ar.edu.ub.club.pagos.dominio.EstadoPago;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Broker abstracto de la jerarquía {@code Pago} con herencia en tabla por subclase (JOINED, Class Table
 * Inheritance): escribe la tabla padre {@code pago}, su {@code consultaBase()} hace el JOIN con {@code
 * tablaHija()} y {@code guardarPartes} inserta o actualiza la fila hija con el mismo id. No hay discriminador:
 * el tipo lo da la existencia de la fila hija, y una consulta sobre {@code Pago} se ejecuta en los tres brokers
 * concretos ({@code BrokerPagoOnline}, {@code BrokerPagoManual}, {@code BrokerPagoConSaldo}). {@code claseRaiz()}
 * es {@code Pago}: un pago tiene una sola entrada en el mapa de identidad.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * @param <T> subclase de pago que maneja
 *
 * <p>Casos de uso: CU-35, CU-36, CU-37; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor), vencimiento de pagos online pendientes
 */
public abstract class BrokerPago<T extends Pago> extends BrokerBaseDatos<T> {

    /** Claves foráneas leídas en {@code crearObjeto} (cuota_id y las de la tabla hija) hasta {@code cargarAsociaciones}. */
    private final Map<Pago, Long[]> pendientes = Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerPago(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase raíz de la jerarquía, clave del mapa de identidad.
     *
     * <p>Casos de uso: todos
     * @return Pago.class
     */
    @Override
    public Class<?> claseRaiz() {
        return Pago.class;
    }

    /**
     * Tabla padre.
     *
     * <p>Casos de uso: todos
     * @return "pago"
     */
    @Override
    protected String tabla() {
        return "pago";
    }

    /**
     * SELECT con el JOIN de la tabla hija: {@code SELECT pago.*, <hija>.* FROM pago JOIN <hija> ON <hija>.id = pago.id}.
     *
     * <p>Casos de uso: todos
     * @return SELECT sin WHERE
     */
    @Override
    protected String consultaBase() {
        String hija = tablaHija();
        return "SELECT pago.*, " + hija + ".* FROM pago JOIN " + hija + " ON " + hija + ".id = pago.id";
    }

    /**
     * Columnas comunes del INSERT en {@code pago}.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param objeto pago nuevo
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(T objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("cuota_id", objeto.getCuota());
        v.put("importe", objeto.getImporte());
        v.put("fecha", objeto.getFecha());
        v.put("estado", objeto.getEstado());
        v.put("motivo_rechazo", objeto.getMotivoRechazo());
        return v;
    }

    /**
     * Columnas comunes del UPDATE en {@code pago}: estado, fecha y motivo (lo que cambian las transiciones State).
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param objeto pago persistido
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(T objeto) {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("fecha", objeto.getFecha());
        v.put("estado", objeto.getEstado());
        v.put("motivo_rechazo", objeto.getMotivoRechazo());
        return v;
    }

    /**
     * Asociaciones comunes: la cuota por {@code get} y el comprobante con {@code buscarUno(ComprobanteDePago(id))};
     * luego el gancho {@code restaurar} de la subclase resuelve las propias y llama a su {@code
     * restaurarAsociaciones}.
     *
     * <p>Casos de uso: todos
     * @param objeto pago recién creado
     */
    @Override
    protected void cargarAsociaciones(T objeto) {
        Long[] claves = pendientes.remove(objeto);
        if (claves == null) {
            return;
        }
        Cuota cuota = fachada().get(claves[0], Cuota.class);
        Comprobante comprobante = fachada().buscarUno(new ComprobanteDePago(objeto.getId())).orElse(null);
        Long[] clavesHija = new Long[claves.length - 1];
        System.arraycopy(claves, 1, clavesHija, 0, clavesHija.length);
        restaurar(objeto, cuota, comprobante, clavesHija);
    }

    /**
     * Inserta (alta) o actualiza la fila de la tabla hija con el mismo id que la fila de {@code pago}.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param objeto pago guardado
     * @param alta true si se acaba de insertar
     */
    @Override
    protected void guardarPartes(T objeto, boolean alta) {
        Map<String, Object> hija = valoresHija(objeto);
        if (alta) {
            List<String> columnas = new ArrayList<>();
            columnas.add("id");
            columnas.addAll(hija.keySet());
            List<Object> valores = new ArrayList<>();
            valores.add(objeto.getId());
            valores.addAll(hija.values());
            String marcas = String.join(", ", Collections.nCopies(columnas.size(), "?"));
            ejecutarActualizacion("INSERT INTO " + tablaHija() + " (" + String.join(", ", columnas) + ") VALUES (" + marcas + ")", valores);
        } else if (!hija.isEmpty()) {
            List<String> asignaciones = new ArrayList<>();
            for (String columna : hija.keySet()) {
                asignaciones.add(columna + " = ?");
            }
            List<Object> valores = new ArrayList<>(hija.values());
            valores.add(objeto.getId());
            ejecutarActualizacion("UPDATE " + tablaHija() + " SET " + String.join(", ", asignaciones) + " WHERE id = ?", valores);
        }
    }

    /**
     * Tabla hija de la subclase.
     *
     * <p>Casos de uso: todos
     * @return nombre de la tabla hija
     */
    protected abstract String tablaHija();

    /**
     * Columnas de la tabla hija (sin el id, que agrega {@code guardarPartes}).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param objeto pago
     * @return columna → valor
     */
    protected abstract Map<String, Object> valoresHija(T objeto);

    /**
     * Agregado en la implementación: gancho de la subclase que resuelve sus asociaciones propias con las claves
     * de la tabla hija y llama a su {@code restaurarAsociaciones}.
     *
     * <p>Casos de uso: todos
     * @param objeto pago
     * @param cuota cuota resuelta
     * @param comprobante comprobante o null
     * @param clavesHija claves foráneas de la tabla hija en el orden en que las guardó {@code recordarClaves}
     */
    protected abstract void restaurar(T objeto, Cuota cuota, Comprobante comprobante, Long[] clavesHija);

    /**
     * Agregado en la implementación: lee las columnas comunes de la fila (para el {@code reconstituir} de la
     * subclase) y devuelve el estado del patrón State.
     *
     * <p>Casos de uso: todos
     * @param fila fila del JOIN
     * @return estado del pago
     */
    protected final EstadoPago estadoDe(FilaResultado fila) {
        return EstadoPago.desdeCodigo(fila.texto("estado"));
    }

    /**
     * Agregado en la implementación: guarda las claves foráneas leídas en {@code crearObjeto} (la cuota primero)
     * para resolverlas en {@code cargarAsociaciones}, después de registrar el objeto en el mapa de identidad.
     *
     * <p>Casos de uso: todos
     * @param objeto pago recién reconstituido
     * @param fila fila del JOIN
     * @param clavesHija claves foráneas de la tabla hija
     * @return el mismo pago
     */
    protected final T recordarClaves(T objeto, FilaResultado fila, Long... clavesHija) {
        Long[] claves = new Long[clavesHija.length + 1];
        claves[0] = fila.largo("cuota_id");
        System.arraycopy(clavesHija, 0, claves, 1, clavesHija.length);
        pendientes.put(objeto, claves);
        return objeto;
    }
}

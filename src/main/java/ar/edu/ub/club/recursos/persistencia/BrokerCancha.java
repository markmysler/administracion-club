package ar.edu.ub.club.recursos.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.recursos.dominio.Deporte;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Intermediario de base de datos de {@code Cancha} (tabla {@code cancha}): la materializa por {@code get} o por
 * las consultas {@code CanchasDeDeporte} y {@code CanchaPorNombre}, y la inserta
 * o actualiza con {@code put}. La asociación a uno con {@code Deporte} se resuelve en {@code cargarAsociaciones}
 * con {@code fachada().get(idDeporte, Deporte)} (07C: «deporte por get»). {@code BrokerReserva} lo usa para
 * materializar la cancha leída con JOIN ({@code materializarDesdeFila(fila.conPrefijo("c_"))}): la fila debe
 * traer también {@code c_deporte_id}.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-21, CU-22, CU-23, CU-26, CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33, CU-38
 */
@Component
public class BrokerCancha extends BrokerBaseDatos<Cancha> {

    /**
     * Identificador del deporte leído en {@code crearObjeto}, pendiente de resolver en {@code cargarAsociaciones}
     * (por identidad de objeto; el broker es un bean compartido entre hilos, por eso está sincronizado).
     */
    private final Map<Cancha, Long> deportesPendientes = Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23, CU-26
     * @param proveedor proveedor de conexiones
     */
    public BrokerCancha(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Gancho del Template Method: resuelve el deporte guardado en {@code crearObjeto} con
     * {@code fachada().get(idDeporte, Deporte)} y lo entrega con {@code restaurarAsociaciones}.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23, CU-26, CU-27, CU-28, CU-32
     * @param c cancha recién creada
     */
    @Override
    protected void cargarAsociaciones(Cancha c) {
        Long idDeporte = deportesPendientes.remove(c);
        if (idDeporte != null) {
            c.restaurarAsociaciones(fachada().get(idDeporte, Deporte.class));
        }
    }

    /**
     * Clase que maneja el broker (clave del registro en {@code FachadaPersistencia}).
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23, CU-26
     * @return {@code Cancha.class}
     */
    @Override
    public Class<Cancha> claseObjeto() {
        return Cancha.class;
    }

    /**
     * Gancho del Template Method: tabla principal.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23, CU-26
     * @return nombre de la tabla {@code cancha}
     */
    @Override
    protected String tabla() {
        return "cancha";
    }

    /**
     * Gancho del Template Method: reconstituye la cancha con {@code Cancha.reconstituir(id, nombre, ubicacion,
     * caracteristicas, activa)} y guarda {@code deporte_id} para {@code cargarAsociaciones}.
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23, CU-26, CU-27, CU-28, CU-32
     * @param fila fila de {@code cancha} (con o sin prefijo)
     * @return la cancha (sin deporte todavía)
     */
    @Override
    protected Cancha crearObjeto(FilaResultado fila) {
        Cancha cancha = Cancha.reconstituir(fila.largo("id"), fila.texto("nombre"), fila.texto("ubicacion"),
                fila.texto("caracteristicas"), fila.booleano("activa"));
        deportesPendientes.put(cancha, fila.largo("deporte_id"));
        return cancha;
    }

    /**
     * Gancho del Template Method: columnas del INSERT; el deporte se pasa como entidad y {@code aParametro} lo
     * convierte en su id (CU-21 paso 7).
     *
     * <p>Casos de uso: CU-21
     * @param objeto cancha nueva
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(Cancha objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("nombre", objeto.getNombre());
        valores.put("deporte_id", objeto.getDeporte());
        valores.put("ubicacion", objeto.getUbicacion());
        valores.put("caracteristicas", objeto.getCaracteristicas());
        valores.put("activa", objeto.getActiva());
        return valores;
    }

    /**
     * Gancho del Template Method: columnas del UPDATE (CU-22 modificación o reactivación, CU-23 baja).
     *
     * <p>Casos de uso: CU-22, CU-23
     * @param objeto cancha persistida
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Cancha objeto) {
        return valoresInsercion(objeto);
    }
}

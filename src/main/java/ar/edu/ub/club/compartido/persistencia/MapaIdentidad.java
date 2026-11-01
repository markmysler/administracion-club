package ar.edu.ub.club.compartido.persistencia;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapa de identidad (Identity Map, Fowler): una fila = un objeto dentro de cada transacción. Guarda cada objeto
 * leído o insertado con la clave (clase raíz, clave). {@code FachadaPersistencia.get} lo consulta antes de ir a
 * la base y {@code BrokerBaseDatos.materializarDesdeFila} devuelve la instancia ya mapeada sin pisar los cambios
 * en memoria; así terminan las asociaciones circulares (socio → grupo → titular). No guarda cambios por sí mismo
 * (no hay Unit of Work). Lo crea y descarta {@code ProveedorConexiones}, uno por transacción.
 *
 * <p>Casos de uso: todos
 */
public class MapaIdentidad {

    /** Objetos por clase raíz y por clave. */
    private final Map<Class<?>, Map<Object, ObjetoPersistente>> objetos = new HashMap<>();

    /**
     * Busca el objeto ya cargado en la transacción.
     *
     * <p>Casos de uso: todos
     * @param claseRaiz clase raíz de la jerarquía ({@code Usuario}, {@code Pago} o la propia clase)
     * @param clave valor de la clave primaria
     * @return el objeto o {@code null} si no está
     */
    public ObjetoPersistente buscar(Class<?> claseRaiz, Object clave) {
        Map<Object, ObjetoPersistente> deClase = objetos.get(claseRaiz);
        return deClase == null ? null : deClase.get(normalizar(clave));
    }

    /**
     * Registra un objeto leído o insertado.
     *
     * <p>Casos de uso: todos
     * @param claseRaiz clase raíz de la jerarquía
     * @param clave valor de la clave primaria
     * @param objeto objeto persistente
     */
    public void registrar(Class<?> claseRaiz, Object clave, ObjetoPersistente objeto) {
        objetos.computeIfAbsent(claseRaiz, c -> new HashMap<>()).put(normalizar(clave), objeto);
    }

    /**
     * Quita un objeto borrado.
     *
     * <p>Casos de uso: CU-09, CU-11, CU-12, CU-24
     * @param claseRaiz clase raíz de la jerarquía
     * @param clave valor de la clave primaria
     */
    public void quitar(Class<?> claseRaiz, Object clave) {
        Map<Object, ObjetoPersistente> deClase = objetos.get(claseRaiz);
        if (deClase != null) {
            deClase.remove(normalizar(clave));
        }
    }

    /**
     * Vacía el mapa al terminar la transacción.
     *
     * <p>Casos de uso: todos
     */
    public void vaciar() {
        objetos.clear();
    }

    /**
     * Normaliza las claves numéricas a {@code Long} para que una clave {@code SMALLINT} (filas únicas) coincida
     * con el {@code Long} que recibe {@code get}.
     *
     * <p>Casos de uso: todos
     * @param clave clave leída o recibida
     * @return la clave normalizada
     */
    private static Object normalizar(Object clave) {
        return clave instanceof Number n ? Long.valueOf(n.longValue()) : clave;
    }
}

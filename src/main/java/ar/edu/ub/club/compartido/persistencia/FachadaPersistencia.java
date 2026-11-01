package ar.edu.ub.club.compartido.persistencia;

import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Único punto de entrada a la persistencia (GoF Facade + GRASP Pure Fabrication; «Singleton» como bean único de
 * Spring, inyectado por constructor): {@code get}, {@code put}, {@code eliminar}, {@code buscar},
 * {@code buscarUno} y {@code contar}. Elige el broker de cada clase y resuelve los objetos consulta. Reemplaza a
 * las 21 interfaces {@code Repositorio*} de 5.0. No depende de ningún módulo: los brokers se registran solos
 * (Spring inyecta la lista de todos los {@code BrokerBaseDatos}).
 *
 * <p>Toda llamada ocurre dentro de una transacción de Spring abierta por la fachada {@code Servicio*}.
 *
 * <p>Casos de uso: todos
 */
@Component
public class FachadaPersistencia {

    /** Clase concreta → su broker. */
    private final Map<Class<?>, BrokerBaseDatos<?>> brokers = new HashMap<>();

    /** Superclase abstracta → brokers de sus subclases ({@code Usuario}, {@code Pago}). */
    private final Map<Class<?>, List<BrokerBaseDatos<?>>> jerarquias = new HashMap<>();

    /** Proveedor de conexiones y del mapa de identidad. */
    private final ProveedorConexiones proveedor;

    /**
     * Registra cada broker por {@code claseObjeto()} y, si {@code claseRaiz()} es distinta, también en
     * {@code jerarquias}; llama a {@code broker.vincular(this)}.
     *
     * <p>Casos de uso: todos
     * @param brokers todos los brokers de los módulos (inyectados por Spring)
     * @param proveedor proveedor de conexiones
     */
    public FachadaPersistencia(List<BrokerBaseDatos<?>> brokers, ProveedorConexiones proveedor) {
        this.proveedor = proveedor;
        for (BrokerBaseDatos<?> broker : brokers) {
            this.brokers.put(broker.claseObjeto(), broker);
            if (!broker.claseRaiz().equals(broker.claseObjeto())) {
                jerarquias.computeIfAbsent(broker.claseRaiz(), c -> new ArrayList<>()).add(broker);
            }
            broker.vincular(this);
        }
    }

    /**
     * Devuelve el objeto con ese id: primero el mapa de identidad (por clase raíz) y si no
     * {@code brokerDe(clase).materializar(id)}; si la clase es abstracta prueba cada broker de su jerarquía (SELECT
     * con INNER JOIN de la tabla hija, sin discriminador).
     *
     * <p>Casos de uso: todos
     * @param id identificador
     * @param clase clase pedida (concreta o abstracta)
     * @param <T> tipo pedido
     * @return el objeto
     * @throws RecursoNoEncontradoException si no hay fila
     */
    public <T> T get(Long id, Class<T> clase) {
        if (id == null) {
            throw new RecursoNoEncontradoException(clase, null);
        }
        ObjetoPersistente enMapa = mapaActual().buscar(claseRaizDe(clase), id);
        if (enMapa != null && clase.isInstance(enMapa)) {
            return clase.cast(enMapa);
        }
        for (BrokerBaseDatos<?> broker : brokersDe(clase)) {
            Object objeto = broker.materializar(id);
            if (objeto != null) {
                return clase.cast(objeto);
            }
        }
        throw new RecursoNoEncontradoException(clase, id);
    }

    /**
     * Guarda el objeto: {@code objeto.esNuevo() ? broker.insertar(objeto) : broker.actualizar(objeto)}.
     *
     * <p>Casos de uso: todos
     * @param objeto objeto persistente
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void put(ObjetoPersistente objeto) {
        BrokerBaseDatos broker = brokerDe(objeto.getClass());
        if (objeto.esNuevo()) {
            broker.insertar(objeto);
        } else {
            broker.actualizar(objeto);
        }
    }

    /**
     * Borra el objeto con su broker.
     *
     * <p>Casos de uso: CU-09, CU-11, CU-12, CU-13, CU-24
     * @param objeto objeto persistido
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void eliminar(ObjetoPersistente objeto) {
        BrokerBaseDatos broker = brokerDe(objeto.getClass());
        broker.borrar(objeto);
    }

    /**
     * Ejecuta la consulta: {@code consulta.resolver(this)} (polimórfico: broker o proyección).
     *
     * <p>Casos de uso: todos
     * @param consulta objeto consulta
     * @param <T> tipo de resultado
     * @return resultados
     */
    public <T> List<T> buscar(Consulta<T> consulta) {
        return consulta.resolver(this);
    }

    /**
     * Ejecuta la consulta y espera a lo sumo un resultado.
     *
     * <p>Casos de uso: todos
     * @param consulta objeto consulta
     * @param <T> tipo de resultado
     * @return vacío si no hubo filas, el objeto si hubo una
     * @throws IllegalStateException si hubo más de una
     */
    public <T> Optional<T> buscarUno(Consulta<T> consulta) {
        List<T> resultado = buscar(consulta);
        if (resultado.size() > 1) {
            throw new IllegalStateException(consulta.getClass().getSimpleName() + " devolvió más de un resultado");
        }
        return resultado.stream().findFirst();
    }

    /**
     * Cuenta los resultados de la consulta ({@code consulta.contarCon(this)}); reemplaza a los {@code existe...}
     * de 5.0 ({@code contar(...) > 0}).
     *
     * <p>Casos de uso: todos
     * @param consulta objeto consulta
     * @param <T> tipo de resultado
     * @return cantidad
     */
    public <T> long contar(Consulta<T> consulta) {
        return consulta.contarCon(this);
    }

    /**
     * Broker de una clase concreta. Es público porque {@code BrokerReserva} lo usa para materializar el socio y la
     * cancha leídos con JOIN ({@code fachada().brokerDe(Socio).materializarDesdeFila(fila.conPrefijo("s_"))}).
     *
     * <p>Casos de uso: todos
     * @param clase clase concreta
     * @param <T> tipo
     * @return su broker
     */
    @SuppressWarnings("unchecked")
    public <T extends ObjetoPersistente> BrokerBaseDatos<T> brokerDe(Class<T> clase) {
        BrokerBaseDatos<?> broker = brokers.get(clase);
        if (broker == null) {
            throw new IllegalArgumentException("No hay broker registrado para " + clase.getName());
        }
        return (BrokerBaseDatos<T>) broker;
    }

    /**
     * Brokers de una clase: si es concreta, su broker; si es abstracta, los de su jerarquía.
     *
     * <p>Casos de uso: todos
     * @param clase clase pedida
     * @return lista de brokers
     */
    List<BrokerBaseDatos<?>> brokersDe(Class<?> clase) {
        if (brokers.containsKey(clase)) {
            return List.of(brokers.get(clase));
        }
        List<BrokerBaseDatos<?>> subclases = jerarquias.get(clase);
        if (subclases == null) {
            throw new IllegalArgumentException("No hay broker registrado para " + clase.getName());
        }
        return subclases;
    }

    /**
     * Proveedor de conexiones (lo usa {@code ConsultaProyeccion}).
     *
     * <p>Casos de uso: todos
     * @return proveedor
     */
    ProveedorConexiones proveedor() {
        return proveedor;
    }

    /**
     * Mapa de identidad de la transacción en curso (atributo derivado {@code /mapaIdentidad}).
     *
     * <p>Casos de uso: todos
     * @return mapa de identidad
     */
    MapaIdentidad mapaActual() {
        return proveedor.mapaIdentidadActual();
    }

    /**
     * Clase raíz con la que se indexa una clase en el mapa de identidad.
     *
     * <p>Casos de uso: todos
     * @param clase clase concreta o abstracta
     * @return clase raíz
     */
    private Class<?> claseRaizDe(Class<?> clase) {
        BrokerBaseDatos<?> broker = brokers.get(clase);
        return broker != null ? broker.claseRaiz() : clase;
    }
}

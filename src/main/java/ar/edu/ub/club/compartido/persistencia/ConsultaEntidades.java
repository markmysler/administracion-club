package ar.edu.ub.club.compartido.persistencia;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Objeto consulta que devuelve objetos persistentes. Su {@code sql()} es la condición y el orden
 * ({@code "WHERE … ORDER BY …"}, opcionalmente {@code FOR UPDATE}) que se agrega a {@code consultaBase()} del
 * broker; las columnas se califican con el nombre de la tabla (por ejemplo {@code reserva.fecha}). La resuelve el
 * broker de {@code claseResultado()} y pasa por el mapa de identidad; si la clase es abstracta ({@code Usuario},
 * {@code Pago}) se ejecuta en el broker de cada subclase y se unen los resultados.
 *
 * @param <T> clase persistente de resultado
 *
 * <p>Casos de uso: todos
 */
public abstract class ConsultaEntidades<T extends ObjetoPersistente> extends Consulta<T> {

    /**
     * Para cada broker de {@code fachada.brokersDe(claseResultado())} ejecuta {@code materializarTodos(this)};
     * une los resultados y, si hay más de un broker, los ordena con {@code comparador()}.
     *
     * <p>Casos de uso: todos
     * @param fachada fachada de persistencia
     * @return objetos persistentes
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    List<T> resolver(FachadaPersistencia fachada) {
        List<BrokerBaseDatos<?>> brokers = fachada.brokersDe(claseResultado());
        List<T> resultado = new ArrayList<>();
        for (BrokerBaseDatos broker : brokers) {
            resultado.addAll(broker.materializarTodos(this));
        }
        Comparator<T> orden = comparador();
        if (brokers.size() > 1 && orden != null) {
            resultado.sort(orden);
        }
        return resultado;
    }

    /**
     * Suma {@code broker.contar(this)} de cada broker de la clase de resultado.
     *
     * <p>Casos de uso: todos
     * @param fachada fachada de persistencia
     * @return cantidad de objetos
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    long contarCon(FachadaPersistencia fachada) {
        long total = 0;
        for (BrokerBaseDatos broker : fachada.brokersDe(claseResultado())) {
            total += broker.contar(this);
        }
        return total;
    }
}

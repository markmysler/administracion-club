package ar.edu.ub.club.compartido.persistencia;

import java.util.Comparator;
import java.util.List;

/**
 * Objeto consulta (Query Object, Fowler): cada búsqueda es un objeto con sus parámetros (recibidos por
 * constructor), su SQL y su forma de resolverse. Tiene dos subclases abstractas: {@code ConsultaEntidades}
 * (objetos persistentes, la resuelve el broker de {@code claseResultado()}) y {@code ConsultaProyeccion} (filas de
 * lectura o valores, la resuelve la fachada sin broker). La resolución es polimórfica: {@code resolver(fachada)},
 * sin preguntar por el tipo de la consulta.
 *
 * @param <T> clase de resultado
 *
 * <p>Casos de uso: todos
 */
public abstract class Consulta<T> {

    /**
     * Clase de los objetos que devuelve la consulta.
     *
     * <p>Casos de uso: todos
     * @return clase de resultado
     */
    public abstract Class<T> claseResultado();

    /**
     * Texto SQL: en {@code ConsultaEntidades}, la condición y el orden que se agregan a {@code consultaBase()}
     * del broker; en {@code ConsultaProyeccion}, la sentencia SELECT completa.
     *
     * <p>Casos de uso: todos
     * @return SQL con parámetros {@code ?}
     */
    public abstract String sql();

    /**
     * Valores de los parámetros {@code ?} en orden.
     *
     * <p>Casos de uso: todos
     * @return lista de parámetros
     */
    public abstract List<Object> parametros();

    /**
     * Orden de la unión de una consulta polimórfica (sobre {@code Usuario} o {@code Pago}); por omisión ninguno.
     *
     * <p>Casos de uso: CU-15, CU-37
     * @return comparador o {@code null}
     */
    public Comparator<T> comparador() {
        return null;
    }

    /**
     * Resuelve la consulta con la fachada (polimorfismo: broker o proyección).
     *
     * <p>Casos de uso: todos
     * @param fachada fachada de persistencia
     * @return resultados
     */
    abstract List<T> resolver(FachadaPersistencia fachada);

    /**
     * Cuenta los resultados de la consulta.
     *
     * <p>Casos de uso: todos
     * @param fachada fachada de persistencia
     * @return cantidad de filas
     */
    abstract long contarCon(FachadaPersistencia fachada);
}

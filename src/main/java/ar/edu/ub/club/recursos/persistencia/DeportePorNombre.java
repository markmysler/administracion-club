package ar.edu.ub.club.recursos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.recursos.dominio.Deporte;
import java.util.ArrayList;
import java.util.List;

/**
 * Deportes con un nombre dado, sin distinguir mayúsculas de minúsculas, excluyendo opcionalmente uno (el que se
 * modifica). {@code ServicioRecursos} la usa con {@code contar(...) > 0} para rechazar un nombre en uso
 * (CU-18 paso 5 con {@code idExcluido = null}; CU-19 paso 5 con el id del deporte).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-18, CU-19
 */
public class DeportePorNombre extends ConsultaEntidades<Deporte> {

    /** Nombre buscado (se compara sin espacios sobrantes y sin distinguir mayúsculas). */
    private final String nombre;

    /** Deporte que se excluye de la búsqueda (null en el alta). */
    private final Long idExcluido;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param nombre nombre buscado
     * @param idExcluido deporte a excluir (opcional)
     */
    public DeportePorNombre(String nombre, Long idExcluido) {
        this.nombre = nombre == null ? "" : nombre.trim();
        this.idExcluido = idExcluido;
    }

    /**
     * Clase de los objetos que devuelve la consulta (la resuelve {@code BrokerDeporte}).
     *
     * <p>Casos de uso: CU-18, CU-19
     * @return {@code Deporte.class}
     */
    @Override
    public Class<Deporte> claseResultado() {
        return Deporte.class;
    }

    /**
     * Condición que se agrega a {@code SELECT * FROM deporte}; la exclusión solo se agrega si hay
     * {@code idExcluido} (WHERE dinámico, convención de la URL con {@code stringtype=unspecified}).
     *
     * <p>Casos de uso: CU-18, CU-19
     * @return SQL con uno o dos parámetros
     */
    @Override
    public String sql() {
        String sql = "WHERE LOWER(deporte.nombre) = LOWER(?)";
        if (idExcluido != null) {
            sql += " AND deporte.id <> ?";
        }
        return sql;
    }

    /**
     * Parámetros en el orden del SQL: nombre y, si corresponde, el id excluido.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        List<Object> parametros = new ArrayList<>();
        parametros.add(nombre);
        if (idExcluido != null) {
            parametros.add(idExcluido);
        }
        return parametros;
    }
}

package ar.edu.ub.club.recursos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.util.ArrayList;
import java.util.List;

/**
 * Canchas con un nombre dado, sin distinguir mayúsculas de minúsculas, excluyendo opcionalmente una (la que se
 * modifica). {@code ServicioRecursos} la usa con {@code contar(...) > 0} para rechazar un nombre en uso
 * (CU-21 paso 5 con {@code idExcluido = null}; CU-22 paso 5 con el id de la cancha).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-21, CU-22
 */
public class CanchaPorNombre extends ConsultaEntidades<Cancha> {

    /** Nombre buscado (se compara sin espacios sobrantes y sin distinguir mayúsculas). */
    private final String nombre;

    /** Cancha que se excluye de la búsqueda (null en el alta). */
    private final Long idExcluido;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param nombre nombre buscado
     * @param idExcluido cancha a excluir (opcional)
     */
    public CanchaPorNombre(String nombre, Long idExcluido) {
        this.nombre = nombre == null ? "" : nombre.trim();
        this.idExcluido = idExcluido;
    }

    /**
     * Clase de los objetos que devuelve la consulta (la resuelve {@code BrokerCancha}).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return {@code Cancha.class}
     */
    @Override
    public Class<Cancha> claseResultado() {
        return Cancha.class;
    }

    /**
     * Condición que se agrega a {@code SELECT * FROM cancha}; la exclusión solo si hay {@code idExcluido}.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return SQL con uno o dos parámetros
     */
    @Override
    public String sql() {
        String sql = "WHERE LOWER(cancha.nombre) = LOWER(?)";
        if (idExcluido != null) {
            sql += " AND cancha.id <> ?";
        }
        return sql;
    }

    /**
     * Parámetros en el orden del SQL: nombre y, si corresponde, el id excluido.
     *
     * <p>Casos de uso: CU-21, CU-22
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

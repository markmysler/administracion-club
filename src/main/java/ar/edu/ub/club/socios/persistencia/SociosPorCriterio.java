package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.ArrayList;
import java.util.List;

/**
 * Búsqueda de socios del listado P-06 y de P-11 (CU-04 paso 2): el texto se compara por prefijo con el DNI y por
 * contenido, sin distinguir mayúsculas, con el nombre, el apellido y «apellido nombre»; el estado filtra por
 * {@code usuario.estado}. El WHERE se arma solo con los filtros no nulos (la URL usa {@code stringtype=unspecified}).
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code Socio} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-04, CU-07, CU-10
 */
public class SociosPorCriterio extends ConsultaEntidades<Socio> {

    /** Texto que se busca en el DNI, el nombre o el apellido (opcional). */
    private final String texto;

    /** Estado de la cuenta (opcional). */
    private final EstadoCuenta estado;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-04, CU-07, CU-10
     * @param texto texto que se busca en el DNI, el nombre o el apellido (opcional)
     * @param estado estado de la cuenta (opcional)
     */
    public SociosPorCriterio(String texto, EstadoCuenta estado) {
        this.texto = texto;
        this.estado = estado;
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code Socio}.
     *
     * <p>Casos de uso: CU-04, CU-07, CU-10
     * @return {@code Socio.class}
     */
    @Override
    public Class<Socio> claseResultado() {
        return Socio.class;
    }

    /**
     * Condiciones dinámicas (texto y estado) y orden por apellido y nombre.
     *
     * <p>Casos de uso: CU-04, CU-07, CU-10
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        List<String> condiciones = new ArrayList<>();
        if (tieneTexto()) {
            condiciones.add("(socio.dni LIKE ? OR socio.nombre ILIKE ? OR socio.apellido ILIKE ?"
                    + " OR (socio.apellido || ' ' || socio.nombre) ILIKE ?)");
        }
        if (estado != null) {
            condiciones.add("usuario.estado = ?");
        }
        String where = condiciones.isEmpty() ? "" : "WHERE " + String.join(" AND ", condiciones) + " ";
        return where + "ORDER BY socio.apellido, socio.nombre";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-04, CU-07, CU-10
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        List<Object> valores = new ArrayList<>();
        if (tieneTexto()) {
            String contenido = "%" + texto.trim() + "%";
            valores.add(texto.trim() + "%");
            valores.add(contenido);
            valores.add(contenido);
            valores.add(contenido);
        }
        if (estado != null) {
            valores.add(estado);
        }
        return valores;
    }

    /**
     * Indica si hay texto de búsqueda (no nulo ni en blanco).
     *
     * <p>Casos de uso: CU-04, CU-07, CU-10
     * @return true si se filtra por texto
     */
    private boolean tieneTexto() {
        return texto != null && !texto.isBlank();
    }
}

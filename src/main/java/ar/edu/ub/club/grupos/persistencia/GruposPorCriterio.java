package ar.edu.ub.club.grupos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import java.util.ArrayList;
import java.util.List;

/**
 * Búsqueda de grupos por el DNI o el nombre de un integrante o del titular, con vigencia opcional (P-11, P-12,
 * pagos). El WHERE se arma solo con los filtros no nulos; sin texto devuelve todos los grupos (con la vigencia
 * pedida).
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code GrupoFamiliar} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
 */
public class GruposPorCriterio extends ConsultaEntidades<GrupoFamiliar> {

    /** DNI (por prefijo) o nombre o apellido (por contenido) de un integrante o del titular. */
    private final String texto;

    /** Vigencia requerida o {@code null} para todos. */
    private final Boolean vigente;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @param texto DNI (por prefijo) o nombre o apellido (por contenido) de un integrante o del titular
     * @param vigente vigencia requerida o {@code null} para todos
     */
    public GruposPorCriterio(String texto, Boolean vigente) {
        this.texto = texto;
        this.vigente = vigente;
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code GrupoFamiliar}.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @return {@code GrupoFamiliar.class}
     */
    @Override
    public Class<GrupoFamiliar> claseResultado() {
        return GrupoFamiliar.class;
    }

    /**
     * Condiciones dinámicas (texto con EXISTS sobre socio y vigencia).
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        List<String> condiciones = new ArrayList<>();
        if (tieneTexto()) {
            condiciones.add("EXISTS (SELECT 1 FROM socio WHERE (socio.grupo_familiar_id = grupo_familiar.id"
                    + " OR socio.id = grupo_familiar.titular_id) AND (socio.dni LIKE ? OR socio.nombre ILIKE ?"
                    + " OR socio.apellido ILIKE ? OR (socio.apellido || ' ' || socio.nombre) ILIKE ?))");
        }
        if (vigente != null) {
            condiciones.add("grupo_familiar.vigente = ?");
        }
        String where = condiciones.isEmpty() ? "" : "WHERE " + String.join(" AND ", condiciones) + " ";
        return where + "ORDER BY grupo_familiar.vigente DESC, grupo_familiar.id";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
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
        if (vigente != null) {
            valores.add(vigente);
        }
        return valores;
    }

    /**
     * Indica si hay texto de búsqueda (no nulo ni en blanco).
     *
     * <p>Casos de uso: CU-07, CU-10, CU-36, CU-37
     * @return true si se filtra por texto
     */
    private boolean tieneTexto() {
        return texto != null && !texto.isBlank();
    }
}

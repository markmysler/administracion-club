package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.util.List;

/**
 * Objeto consulta de proyección n.º 61 (Entrega 7C): cantidad de socios con la cuenta ACTIVA (indicador «Socios
 * activos» de P-04, paso 5 de CU-40). Tablas: socio, usuario (herencia JOINED).
 *
 * <p>Casos de uso: CU-40
 */
public class SociosActivos extends ConsultaProyeccion<Long> {

    /**
     * Crea la consulta (sin parámetros).
     *
     * <p>Casos de uso: CU-40
     */
    public SociosActivos() {
    }

    /**
     * Devuelve la clase del resultado.
     *
     * <p>Casos de uso: CU-40
     * @return {@code Long.class}
     */
    @Override
    public Class<Long> claseResultado() {
        return Long.class;
    }

    /**
     * Lee la cantidad.
     *
     * <p>Casos de uso: CU-40
     * @param fila fila del {@code ResultSet}
     * @return cantidad de socios activos
     */
    @Override
    public Long leerFila(FilaResultado fila) {
        return fila.largo("cantidad");
    }

    /**
     * Sentencia SQL.
     *
     * <p>Casos de uso: CU-40
     * @return SQL
     */
    @Override
    public String sql() {
        return "SELECT COUNT(*) AS cantidad FROM socio JOIN usuario ON usuario.id = socio.id"
                + " WHERE usuario.estado = 'ACTIVA'";
    }

    /**
     * Sin parámetros.
     *
     * <p>Casos de uso: CU-40
     * @return lista vacía
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}

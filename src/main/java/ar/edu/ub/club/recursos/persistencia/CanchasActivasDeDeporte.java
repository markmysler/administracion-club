package ar.edu.ub.club.recursos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.util.List;

/**
 * Canchas activas de un deporte o, con {@code idDeporte = null}, todas las activas; ordenadas por nombre (07C,
 * reemplaza a {@code RepositorioCanchas.buscarActivas}). La pueden usar otros módulos (reportes, reserva manual)
 * para ofrecer solo canchas que admiten reservas.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-26, CU-28, CU-38
 */
public class CanchasActivasDeDeporte extends ConsultaEntidades<Cancha> {

    /** Deporte de las canchas (null = todas las activas). */
    private final Long idDeporte;

    /**
     * Crea la consulta con su parámetro opcional.
     *
     * <p>Casos de uso: CU-26, CU-28, CU-38
     * @param idDeporte deporte, o null para todas las canchas activas
     */
    public CanchasActivasDeDeporte(Long idDeporte) {
        this.idDeporte = idDeporte;
    }

    /**
     * Clase de los objetos que devuelve la consulta (la resuelve {@code BrokerCancha}).
     *
     * <p>Casos de uso: CU-26, CU-28, CU-38
     * @return {@code Cancha.class}
     */
    @Override
    public Class<Cancha> claseResultado() {
        return Cancha.class;
    }

    /**
     * Condición y orden que se agregan a {@code SELECT * FROM cancha} (el filtro de deporte solo si se indicó).
     *
     * <p>Casos de uso: CU-26, CU-28, CU-38
     * @return SQL con cero o un parámetro
     */
    @Override
    public String sql() {
        String sql = "WHERE cancha.activa = true";
        if (idDeporte != null) {
            sql += " AND cancha.deporte_id = ?";
        }
        return sql + " ORDER BY cancha.nombre";
    }

    /**
     * Parámetros en el orden del SQL: el deporte, si se indicó.
     *
     * <p>Casos de uso: CU-26, CU-28, CU-38
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        return idDeporte == null ? List.of() : List.of(idDeporte);
    }
}

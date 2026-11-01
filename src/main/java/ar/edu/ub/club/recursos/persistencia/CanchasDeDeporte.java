package ar.edu.ub.club.recursos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.util.List;

/**
 * Canchas (activas e inactivas) de un deporte o, con {@code idDeporte = null}, todas; ordenadas por nombre. La
 * usan {@code ServicioRecursos.listarCanchas} (P-15, P-16, P-19) y {@code ServicioAgenda.consultarDisponibilidad}
 * (sec_cu26_p2, que luego conserva las que cumplen {@code estaDisponibleParaReservar()}).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-18, CU-21, CU-22, CU-23, CU-26, CU-32
 */
public class CanchasDeDeporte extends ConsultaEntidades<Cancha> {

    /** Deporte de las canchas (null = todas). */
    private final Long idDeporte;

    /**
     * Crea la consulta con su parámetro opcional.
     *
     * <p>Casos de uso: CU-18, CU-21, CU-22, CU-23, CU-26, CU-32
     * @param idDeporte deporte, o null para todas las canchas
     */
    public CanchasDeDeporte(Long idDeporte) {
        this.idDeporte = idDeporte;
    }

    /**
     * Clase de los objetos que devuelve la consulta (la resuelve {@code BrokerCancha}).
     *
     * <p>Casos de uso: CU-18, CU-21, CU-22, CU-23, CU-26, CU-32
     * @return {@code Cancha.class}
     */
    @Override
    public Class<Cancha> claseResultado() {
        return Cancha.class;
    }

    /**
     * Condición (solo si hay deporte) y orden que se agregan a {@code SELECT * FROM cancha}.
     *
     * <p>Casos de uso: CU-18, CU-21, CU-22, CU-23, CU-26, CU-32
     * @return SQL con cero o un parámetro
     */
    @Override
    public String sql() {
        return idDeporte == null ? "ORDER BY cancha.nombre" : "WHERE cancha.deporte_id = ? ORDER BY cancha.nombre";
    }

    /**
     * Parámetros en el orden del SQL: el deporte, si se indicó.
     *
     * <p>Casos de uso: CU-18, CU-21, CU-22, CU-23, CU-26, CU-32
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        return idDeporte == null ? List.of() : List.of(idDeporte);
    }
}

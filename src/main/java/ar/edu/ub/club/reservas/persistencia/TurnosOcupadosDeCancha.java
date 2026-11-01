package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.time.LocalDate;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker.
 *
 * <p>Casos de uso: CU-26
 */
public class TurnosOcupadosDeCancha extends ConsultaProyeccion<Turno> {

    /** Atributo idCancha. */
    private Long idCancha;

    /** Atributo fecha. */
    private LocalDate fecha;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @return el resultado
     */
    public Class<Turno> claseResultado() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @param fila valor de {@code fila}
     * @return el resultado
     */
    @Override
    public Turno leerFila(FilaResultado fila) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @return el resultado
     */
    @Override
    public String sql() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

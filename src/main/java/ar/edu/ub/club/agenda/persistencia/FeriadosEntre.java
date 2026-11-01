package ar.edu.ub.club.agenda.persistencia;

import ar.edu.ub.club.agenda.dominio.Feriado;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.time.LocalDate;
import java.util.List;

/**
 * Feriados entre dos fechas, ambas inclusive. {@code ServicioAgenda.registrarFeriado} la usa con
 * {@code contar(FeriadosEntre(fecha, fecha)) > 0} para rechazar una fecha repetida (CU-24 FA1) y
 * {@code FabricaCadenaValidacion} para el validador de horario del club (RN-13).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-24, CU-27, CU-28, CU-32
 */
public class FeriadosEntre extends ConsultaEntidades<Feriado> {

    /** Primera fecha del rango (inclusive). */
    private final LocalDate desde;

    /** Última fecha del rango (inclusive). */
    private final LocalDate hasta;

    /**
     * Crea la consulta con su rango.
     *
     * <p>Casos de uso: CU-24, CU-27, CU-28, CU-32
     * @param desde primera fecha (inclusive)
     * @param hasta última fecha (inclusive)
     */
    public FeriadosEntre(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    /**
     * Clase de los objetos que devuelve la consulta (la resuelve {@code BrokerFeriado}).
     *
     * <p>Casos de uso: CU-24, CU-27, CU-28, CU-32
     * @return {@code Feriado.class}
     */
    @Override
    public Class<Feriado> claseResultado() {
        return Feriado.class;
    }

    /**
     * Condición y orden que se agregan a {@code SELECT * FROM feriado}.
     *
     * <p>Casos de uso: CU-24, CU-27, CU-28, CU-32
     * @return SQL con dos parámetros
     */
    @Override
    public String sql() {
        return "WHERE feriado.fecha BETWEEN ? AND ? ORDER BY feriado.fecha";
    }

    /**
     * Parámetros en orden: desde, hasta.
     *
     * <p>Casos de uso: CU-24, CU-27, CU-28, CU-32
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(desde, hasta);
    }
}

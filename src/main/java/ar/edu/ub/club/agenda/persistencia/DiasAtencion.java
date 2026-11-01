package ar.edu.ub.club.agenda.persistencia;

import ar.edu.ub.club.agenda.dominio.DiaSemana;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.util.List;

/**
 * Días de atención de la disponibilidad del club (tabla {@code dia_atencion}, día como número ISO 8601). La usa
 * {@code BrokerDisponibilidadClub.cargarAsociaciones}.
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker.
 *
 * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
 */
public class DiasAtencion extends ConsultaProyeccion<DiaSemana> {

    /** Identificador de la disponibilidad (siempre {@code DisponibilidadClub.ID_UNICO}). */
    private final Long idDisponibilidad;

    /**
     * Crea la consulta con su parámetro.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @param idDisponibilidad identificador de la disponibilidad
     */
    public DiasAtencion(Long idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }

    /**
     * Clase de los valores que devuelve la consulta.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return {@code DiaSemana.class}
     */
    @Override
    public Class<DiaSemana> claseResultado() {
        return DiaSemana.class;
    }

    /**
     * Convierte el número ISO de la columna {@code dia} en {@code DiaSemana}.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @param fila fila del resultado
     * @return día de la semana
     */
    @Override
    public DiaSemana leerFila(FilaResultado fila) {
        return DiaSemana.desdeNumeroIso(fila.entero("dia"));
    }

    /**
     * SELECT completo de los días de la disponibilidad, ordenados de lunes a domingo.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return SQL con un parámetro
     */
    @Override
    public String sql() {
        return "SELECT dia_atencion.dia FROM dia_atencion WHERE dia_atencion.disponibilidad_id = ? ORDER BY dia_atencion.dia";
    }

    /**
     * Parámetros en orden: identificador de la disponibilidad.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(idDisponibilidad);
    }
}

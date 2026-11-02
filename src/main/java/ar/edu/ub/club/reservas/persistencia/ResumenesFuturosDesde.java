package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Resumen de todas las reservas futuras confirmadas, que se revisan antes de cambiar la disponibilidad del
 * club.
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker y
 * devuelve filas de lectura {@code ResumenReserva} (consulta invertida {@code ConsultaReservasFuturas}, D-10).
 *
 * <p>Casos de uso: CU-24
 */
public class ResumenesFuturosDesde extends ConsultaProyeccion<ResumenReserva> {

    /** Momento a partir del cual una reserva es futura. */
    private final LocalDateTime ahora;

    /**
     * Crea la consulta; los parámetros se reciben por constructor y no cambian (Query Object).
     *
     * <p>Casos de uso: CU-24
     * @param ahora momento a partir del cual una reserva es futura
     */
    public ResumenesFuturosDesde(LocalDateTime ahora) {
        this.ahora = ahora;
    }

    /**
     * Tipo de cada fila.
     *
     * <p>Casos de uso: CU-24
     * @return {@code ResumenReserva}
     */
    @Override
    public Class<ResumenReserva> claseResultado() {
        return ResumenReserva.class;
    }

    /**
     * Convierte una fila en {@code ResumenReserva} (id, socio «Apellido, Nombre», cancha, fecha y horario).
     *
     * <p>Casos de uso: CU-24
     * @param fila fila del resultado
     * @return resumen de la reserva
     */
    @Override
    public ResumenReserva leerFila(FilaResultado fila) {
        return new ResumenReserva(fila.largo("id"), fila.texto("nombre_socio"), fila.texto("nombre_cancha"),
                fila.fecha("fecha"), fila.hora("hora_inicio"), fila.hora("hora_fin"));
    }

    /**
     * Sentencia SELECT completa sobre {@code reserva}, {@code socio} y {@code cancha} (solo reservas
     * CONFIRMADAS).
     *
     * <p>Casos de uso: CU-24
     * @return SQL con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "SELECT reserva.id, socio.apellido || ', ' || socio.nombre AS nombre_socio, cancha.nombre AS nombre_cancha,"
                + " reserva.fecha, reserva.hora_inicio, reserva.hora_fin"
                + " FROM reserva JOIN socio ON socio.id = reserva.socio_id JOIN cancha ON cancha.id = reserva.cancha_id"
                + " WHERE reserva.estado = 'CONFIRMADA' AND (reserva.fecha > ? OR (reserva.fecha = ? AND reserva.hora_inicio > ?))"
                + " ORDER BY reserva.fecha, reserva.hora_inicio";
    }

    /**
     * Valores de los parámetros en el orden de los {@code ?}.
     *
     * <p>Casos de uso: CU-24
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(ahora.toLocalDate(), ahora.toLocalDate(), ahora.toLocalTime());
    }
}

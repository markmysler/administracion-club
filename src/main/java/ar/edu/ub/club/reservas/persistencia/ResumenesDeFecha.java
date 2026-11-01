package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.time.LocalDate;
import java.util.List;

/**
 * Resumen de las reservas confirmadas de una fecha (registro de un feriado).
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker y
 * devuelve filas de lectura {@code ResumenReserva} (consulta invertida {@code ConsultaReservasFuturas}, D-10).
 *
 * <p>Casos de uso: CU-24
 */
public class ResumenesDeFecha extends ConsultaProyeccion<ResumenReserva> {

    /** Fecha consultada. */
    private final LocalDate fecha;

    /**
     * Crea la consulta; los parámetros se reciben por constructor y no cambian (Query Object).
     *
     * <p>Casos de uso: CU-24
     * @param fecha fecha consultada
     */
    public ResumenesDeFecha(LocalDate fecha) {
        this.fecha = fecha;
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
                + " WHERE reserva.estado = 'CONFIRMADA' AND reserva.fecha = ?"
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
        return List.of(fecha);
    }
}

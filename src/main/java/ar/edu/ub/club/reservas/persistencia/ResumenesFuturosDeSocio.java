package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Resumen de las reservas futuras confirmadas del socio: ficha, perfil y aviso previo a la baja del socio
 * (RN-16).
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker y
 * devuelve filas de lectura {@code ResumenReserva} (consulta invertida {@code ConsultaReservasFuturas}, D-10).
 *
 * <p>Casos de uso: CU-03, CU-04, CU-05
 */
public class ResumenesFuturosDeSocio extends ConsultaProyeccion<ResumenReserva> {

    /** Socio. */
    private final Long idSocio;

    /** Momento a partir del cual una reserva es futura. */
    private final LocalDateTime ahora;

    /**
     * Crea la consulta; los parámetros se reciben por constructor y no cambian (Query Object).
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05
     * @param idSocio socio
     * @param ahora momento a partir del cual una reserva es futura
     */
    public ResumenesFuturosDeSocio(Long idSocio, LocalDateTime ahora) {
        this.idSocio = idSocio;
        this.ahora = ahora;
    }

    /**
     * Tipo de cada fila.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05
     * @return {@code ResumenReserva}
     */
    @Override
    public Class<ResumenReserva> claseResultado() {
        return ResumenReserva.class;
    }

    /**
     * Convierte una fila en {@code ResumenReserva} (id, socio «Apellido, Nombre», cancha, fecha y horario).
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05
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
     * <p>Casos de uso: CU-03, CU-04, CU-05
     * @return SQL con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "SELECT reserva.id, socio.apellido || ', ' || socio.nombre AS nombre_socio, cancha.nombre AS nombre_cancha,"
                + " reserva.fecha, reserva.hora_inicio, reserva.hora_fin"
                + " FROM reserva JOIN socio ON socio.id = reserva.socio_id JOIN cancha ON cancha.id = reserva.cancha_id"
                + " WHERE reserva.estado = 'CONFIRMADA' AND reserva.socio_id = ? AND (reserva.fecha > ? OR (reserva.fecha = ? AND reserva.hora_inicio > ?))"
                + " ORDER BY reserva.fecha, reserva.hora_inicio";
    }

    /**
     * Valores de los parámetros en el orden de los {@code ?}.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05
     * @return lista de parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(idSocio, ahora.toLocalDate(), ahora.toLocalDate(), ahora.toLocalTime());
    }
}

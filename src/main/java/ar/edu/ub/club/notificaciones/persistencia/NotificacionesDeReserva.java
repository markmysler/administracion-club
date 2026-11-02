package ar.edu.ub.club.notificaciones.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import java.util.List;

/**
 * Notificaciones de una reserva, en orden de creación (detalle de la reserva en P-21 y P-22).
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve {@code
 * BrokerNotificacion}.
 *
 * <p>Casos de uso: CU-29, CU-31
 */
public class NotificacionesDeReserva extends ConsultaEntidades<Notificacion> {

    /** Reserva. */
    private final Long idReserva;

    /**
     * Crea la consulta; el parámetro se recibe por constructor (Query Object).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param idReserva reserva
     */
    public NotificacionesDeReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    /**
     * Clase que resuelve {@code BrokerNotificacion}.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return {@code Notificacion}
     */
    @Override
    public Class<Notificacion> claseResultado() {
        return Notificacion.class;
    }

    /**
     * Condición y orden.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return WHERE y ORDER BY
     */
    @Override
    public String sql() {
        return "WHERE notificacion.reserva_id = ? ORDER BY notificacion.fecha, notificacion.id";
    }

    /**
     * Valores de los parámetros.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return {@code [idReserva]}
     */
    @Override
    public List<Object> parametros() {
        return List.of(idReserva);
    }
}

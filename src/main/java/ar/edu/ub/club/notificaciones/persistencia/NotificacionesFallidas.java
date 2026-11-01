package ar.edu.ub.club.notificaciones.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import java.util.List;

/**
 * Notificaciones cuyo envío falló (RN-19), de la más antigua a la más reciente. Definida en 07C (consulta 45) para
 * el seguimiento de los envíos; ningún caso de uso del diseño 6.0 la invoca todavía.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve {@code
 * BrokerNotificacion}.
 *
 * <p>Casos de uso: ninguno (consulta de apoyo de RN-19)
 */
public class NotificacionesFallidas extends ConsultaEntidades<Notificacion> {

    /**
     * Crea la consulta (sin parámetros).
     *
     * <p>Casos de uso: ninguno (consulta de apoyo de RN-19)
     */
    public NotificacionesFallidas() {
        super();
    }

    /**
     * Clase que resuelve {@code BrokerNotificacion}.
     *
     * <p>Casos de uso: ninguno (consulta de apoyo de RN-19)
     * @return {@code Notificacion}
     */
    @Override
    public Class<Notificacion> claseResultado() {
        return Notificacion.class;
    }

    /**
     * Condición y orden.
     *
     * <p>Casos de uso: ninguno (consulta de apoyo de RN-19)
     * @return WHERE y ORDER BY
     */
    @Override
    public String sql() {
        return "WHERE notificacion.estado_envio = 'FALLIDA' ORDER BY notificacion.fecha, notificacion.id";
    }

    /**
     * Sin parámetros.
     *
     * <p>Casos de uso: ninguno (consulta de apoyo de RN-19)
     * @return lista vacía
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}

package ar.edu.ub.club.recursos.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Evento de dominio ({@code recursos.dominio}) que publica {@code ServicioRecursos.desactivarCancha} con
 * {@code ApplicationEventPublisher} al confirmar la baja de la cancha (patrón Observer, pat_observer_dominio). Lo
 * atiende {@code CanceladorReservasPorBaja.alDesactivarCancha} dentro de la misma transacción: cancela las
 * reservas futuras de la cancha con el motivo «Baja de cancha» (RN-16).
 *
 * <p>Casos de uso: CU-23
 */
public class CanchaDesactivadaEvento {

    /** Identificador de la cancha dada de baja. */
    private final Long idCancha;

    /** Cuenta que realizó la baja (queda como {@code canceladaPor}). */
    private final Usuario usuario;

    /** Momento de la baja. */
    private final LocalDateTime fechaHora;

    /**
     * Agregado en la implementación: crea el evento completo (el diseño solo lista los atributos;
     * sec_cu23_p3 lo crea como {@code CanchaDesactivadaEvento(id, admin, ahora)}).
     *
     * <p>Casos de uso: CU-23
     * @param idCancha cancha dada de baja
     * @param usuario cuenta que realizó la baja
     * @param fechaHora momento de la baja
     */
    public CanchaDesactivadaEvento(Long idCancha, Usuario usuario, LocalDateTime fechaHora) {
        this.idCancha = idCancha;
        this.usuario = usuario;
        this.fechaHora = fechaHora;
    }

    /**
     * Devuelve {@code idCancha}: Identificador de la cancha dada de baja.
     *
     * <p>Casos de uso: CU-23
     * @return el valor del atributo
     */
    public Long getIdCancha() {
        return idCancha;
    }

    /**
     * Devuelve {@code usuario}: Cuenta que realizó la baja (queda como {@code canceladaPor}).
     *
     * <p>Casos de uso: CU-23
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code fechaHora}: Momento de la baja.
     *
     * <p>Casos de uso: CU-23
     * @return el valor del atributo
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}

package ar.edu.ub.club.recursos.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Evento de dominio ({@code recursos.dominio}) que publica {@code ServicioRecursos.desactivarDeporte} con
 * {@code ApplicationEventPublisher} al confirmar la baja del deporte (patrón Observer, pat_observer_dominio). Lo
 * atiende {@code CanceladorReservasPorBaja.alDesactivarDeporte} dentro de la misma transacción: cancela las
 * reservas futuras de las canchas del deporte con el motivo «Baja de deporte» (RN-16). Así recursos no depende de
 * reservas.
 *
 * <p>Casos de uso: CU-20
 */
public class DeporteDesactivadoEvento {

    /** Identificador del deporte dado de baja. */
    private final Long idDeporte;

    /** Cuenta que realizó la baja (queda como {@code canceladaPor}). */
    private final Usuario usuario;

    /** Momento de la baja. */
    private final LocalDateTime fechaHora;

    /**
     * Agregado en la implementación: crea el evento completo (el diseño solo lista los atributos;
     * sec_cu20_p3 lo crea como {@code DeporteDesactivadoEvento(id, admin, ahora)}).
     *
     * <p>Casos de uso: CU-20
     * @param idDeporte deporte dado de baja
     * @param usuario cuenta que realizó la baja
     * @param fechaHora momento de la baja
     */
    public DeporteDesactivadoEvento(Long idDeporte, Usuario usuario, LocalDateTime fechaHora) {
        this.idDeporte = idDeporte;
        this.usuario = usuario;
        this.fechaHora = fechaHora;
    }

    /**
     * Devuelve {@code idDeporte}: Identificador del deporte dado de baja.
     *
     * <p>Casos de uso: CU-20
     * @return el valor del atributo
     */
    public Long getIdDeporte() {
        return idDeporte;
    }

    /**
     * Devuelve {@code usuario}: Cuenta que realizó la baja (queda como {@code canceladaPor}).
     *
     * <p>Casos de uso: CU-20
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code fechaHora}: Momento de la baja.
     *
     * <p>Casos de uso: CU-20
     * @return el valor del atributo
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}

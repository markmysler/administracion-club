package ar.edu.ub.club.socios.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Evento de dominio ({@code socios.dominio}) que publica {@code ServicioSocios.desactivarSocio} con
 * {@code ApplicationEventPublisher} al confirmar la baja del socio. Lo atiende {@code CanceladorReservasPorBaja}
 * (módulo reservas) de forma sincrónica dentro de la misma transacción y cancela las reservas futuras con el
 * motivo «Baja de socio» (RN-16). Es el sujeto del patrón Observer de dominio: socios no depende de reservas
 * (D-10). Objeto de datos inmutable, sin comportamiento.
 *
 * <p>Casos de uso: CU-03
 */
public class SocioDesactivadoEvento {

    /** Identificador del socio dado de baja. */
    private final Long idSocio;

    /** Cuenta que realizó la baja (queda como {@code canceladaPor}). */
    private final Usuario usuario;

    /** Momento de la baja. */
    private final LocalDateTime fechaHora;

    /**
     * Crea el evento con todos sus datos ({@code SocioDesactivadoEvento(id, usuario, ahora)} en
     * {@code sec_cu03_p4}).
     *
     * <p>Agregado en la implementación: constructor (el diseño lo dibuja en el diagrama pero el modelo no lo
     * declaraba).
     *
     * <p>Casos de uso: CU-03
     * @param idSocio socio dado de baja
     * @param usuario administrador que realizó la baja
     * @param fechaHora momento de la baja
     */
    public SocioDesactivadoEvento(Long idSocio, Usuario usuario, LocalDateTime fechaHora) {
        this.idSocio = idSocio;
        this.usuario = usuario;
        this.fechaHora = fechaHora;
    }

    /**
     * Devuelve {@code idSocio}: Identificador del socio dado de baja.
     *
     * <p>Casos de uso: CU-03
     * @return el valor del atributo
     */
    public Long getIdSocio() {
        return idSocio;
    }

    /**
     * Devuelve {@code usuario}: Cuenta que realizó la baja (queda como {@code canceladaPor}).
     *
     * <p>Casos de uso: CU-03
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code fechaHora}: Momento de la baja.
     *
     * <p>Casos de uso: CU-03
     * @return el valor del atributo
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}

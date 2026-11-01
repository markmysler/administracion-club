package ar.edu.ub.club.socios.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Evento de dominio ({@code socios.dominio}) que publica la fachada al confirmar la baja del socio. Lo atiende
 * {@code CanceladorReservasPorBaja} dentro de la misma transacción.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class SocioDesactivadoEvento {

    /** Identificador del socio dado de baja. */
    private Long idSocio;

    /** Cuenta que realizó la baja (queda como {@code canceladaPor}). */
    private Usuario usuario;

    /** Momento de la baja. */
    private LocalDateTime fechaHora;

    /**
     * Devuelve {@code idSocio}: Identificador del socio dado de baja.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el valor del atributo
     */
    public Long getIdSocio() {
        return idSocio;
    }

    /**
     * Devuelve {@code usuario}: Cuenta que realizó la baja (queda como {@code canceladaPor}).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code fechaHora}: Momento de la baja.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el valor del atributo
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}

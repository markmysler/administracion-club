package ar.edu.ub.club.recursos.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Evento de dominio ({@code recursos.dominio}) que publica la fachada al confirmar la baja del deporte. Lo
 * atiende {@code CanceladorReservasPorBaja} dentro de la misma transacción.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class DeporteDesactivadoEvento {

    /** Identificador del deporte dado de baja. */
    private Long idDeporte;

    /** Cuenta que realizó la baja (queda como {@code canceladaPor}). */
    private Usuario usuario;

    /** Momento de la baja. */
    private LocalDateTime fechaHora;

    /**
     * Devuelve {@code idDeporte}: Identificador del deporte dado de baja.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el valor del atributo
     */
    public Long getIdDeporte() {
        return idDeporte;
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

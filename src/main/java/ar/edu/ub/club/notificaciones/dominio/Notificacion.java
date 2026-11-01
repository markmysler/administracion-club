package ar.edu.ub.club.notificaciones.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
 */
public class Notificacion extends EntidadPersistente {

    /** Atributo socio. */
    private Socio socio;

    /** Atributo reserva. */
    private Reserva reserva;

    /** Atributo fecha. */
    private LocalDateTime fecha;

    /** Atributo tipo. */
    private TipoNotificacion tipo;

    /** Atributo mensaje. */
    private String mensaje;

    /** Atributo estadoEnvio. */
    private EstadoEnvio estadoEnvio;

    /** Atributo detalleError. */
    private String detalleError;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @param id valor de {@code id}
     * @param fecha valor de {@code fecha}
     * @param tipo valor de {@code tipo}
     * @param mensaje valor de {@code mensaje}
     * @param estadoEnvio valor de {@code estadoEnvio}
     * @param detalleError valor de {@code detalleError}
     * @return el resultado
     */
    public static Notificacion reconstituir(Long id, LocalDateTime fecha, TipoNotificacion tipo, String mensaje, EstadoEnvio estadoEnvio, String detalleError) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @param socio valor de {@code socio}
     * @param reserva valor de {@code reserva}
     */
    public void restaurarAsociaciones(Socio socio, Reserva reserva) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Crea una instancia de {@code Notificacion}.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @param socio valor de {@code socio}
     * @param reserva valor de {@code reserva}
     * @param tipo valor de {@code tipo}
     * @param mensaje valor de {@code mensaje}
     */
    public Notificacion(Socio socio, Reserva reserva, TipoNotificacion tipo, String mensaje) {
        // PENDIENTE
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     */
    public void marcarEnviada() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @param detalle valor de {@code detalle}
     */
    public void marcarFallida(String detalle) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code socio}: socio.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public Socio getSocio() {
        return socio;
    }

    /**
     * Devuelve {@code reserva}: reserva.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public Reserva getReserva() {
        return reserva;
    }

    /**
     * Devuelve {@code fecha}: fecha.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Devuelve {@code tipo}: tipo.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public TipoNotificacion getTipo() {
        return tipo;
    }

    /**
     * Devuelve {@code mensaje}: mensaje.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Devuelve {@code estadoEnvio}: estadoEnvio.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public EstadoEnvio getEstadoEnvio() {
        return estadoEnvio;
    }

    /**
     * Devuelve {@code detalleError}: detalleError.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
     * @return el valor del atributo
     */
    public String getDetalleError() {
        return detalleError;
    }
}

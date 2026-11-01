package ar.edu.ub.club.notificaciones.dominio;

import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;

/**
 * Entidad del dominio que registra cada aviso por correo de un evento de reserva al socio titular y el resultado
 * del envío (RN-19): se crea sin enviar, y {@code marcarEnviada} o {@code marcarFallida} registran el resultado. La
 * crea {@code NotificadorReservas} (Creator) y la persiste {@code BrokerNotificacion}; la consulta {@code
 * ServicioNotificaciones} para el detalle de la reserva (P-21, P-22).
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
 */
public class Notificacion extends EntidadPersistente {

    /** Longitud máxima de {@code detalle_error} en la tabla. */
    private static final int LARGO_DETALLE = 500;

    /** Socio destinatario. */
    private Socio socio;

    /** Reserva que originó el aviso. */
    private Reserva reserva;

    /** Momento de creación. */
    private LocalDateTime fecha;

    /** Tipo (confirmación, modificación o cancelación). */
    private TipoNotificacion tipo;

    /** Texto enviado. */
    private String mensaje;

    /** Estado del envío. */
    private EstadoEnvio estadoEnvio;

    /** Motivo de la falla del envío, si la hubo. */
    private String detalleError;

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: CU-29, CU-31
     */
    private Notificacion() {
        // los valores los asigna reconstituir
    }

    /**
     * Crea la notificación con los valores de una fila existente («persistencia»); la usa {@code
     * BrokerNotificacion.crearObjeto}.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param id identificador
     * @param fecha momento de creación
     * @param tipo tipo de aviso
     * @param mensaje texto enviado
     * @param estadoEnvio estado del envío
     * @param detalleError motivo de la falla o {@code null}
     * @return la notificación reconstituida
     */
    public static Notificacion reconstituir(Long id, LocalDateTime fecha, TipoNotificacion tipo, String mensaje, EstadoEnvio estadoEnvio, String detalleError) {
        Notificacion n = new Notificacion();
        n.asignarId(id);
        n.fecha = fecha;
        n.tipo = tipo;
        n.mensaje = mensaje;
        n.estadoEnvio = estadoEnvio;
        n.detalleError = detalleError;
        return n;
    }

    /**
     * Asigna las asociaciones que resolvió el broker con {@code get} («persistencia»).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param socio socio destinatario
     * @param reserva reserva que originó el aviso
     */
    public void restaurarAsociaciones(Socio socio, Reserva reserva) {
        this.socio = socio;
        this.reserva = reserva;
    }

    /**
     * Crea la notificación sin enviar, con fecha actual en la zona del club.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param socio socio destinatario
     * @param reserva reserva que originó el aviso
     * @param tipo tipo de aviso
     * @param mensaje texto del correo
     */
    public Notificacion(Socio socio, Reserva reserva, TipoNotificacion tipo, String mensaje) {
        this.socio = socio;
        this.reserva = reserva;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now(RelojSistema.ZONA_CLUB);
        this.estadoEnvio = EstadoEnvio.PENDIENTE;
    }

    /**
     * Registra que el proveedor de correo aceptó el mensaje.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     */
    public void marcarEnviada() {
        this.estadoEnvio = EstadoEnvio.ENVIADA;
        this.detalleError = null;
    }

    /**
     * Registra la falla del envío; la reserva se mantiene (RN-19).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param detalle motivo informado por el adaptador de correo
     */
    public void marcarFallida(String detalle) {
        this.estadoEnvio = EstadoEnvio.FALLIDA;
        String texto = detalle == null || detalle.isBlank() ? "Error desconocido del servicio de correo" : detalle;
        this.detalleError = texto.length() > LARGO_DETALLE ? texto.substring(0, LARGO_DETALLE) : texto;
    }

    /**
     * Devuelve {@code socio}: socio destinatario.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public Socio getSocio() {
        return socio;
    }

    /**
     * Devuelve {@code reserva}: reserva que originó el aviso.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public Reserva getReserva() {
        return reserva;
    }

    /**
     * Devuelve {@code fecha}: momento de creación.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Devuelve {@code tipo}: tipo de aviso.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public TipoNotificacion getTipo() {
        return tipo;
    }

    /**
     * Devuelve {@code mensaje}: texto enviado.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Devuelve {@code estadoEnvio}: estado del envío.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public EstadoEnvio getEstadoEnvio() {
        return estadoEnvio;
    }

    /**
     * Devuelve {@code detalleError}: motivo de la falla del envío.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public String getDetalleError() {
        return detalleError;
    }
}

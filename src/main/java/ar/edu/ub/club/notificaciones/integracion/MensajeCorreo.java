package ar.edu.ub.club.notificaciones.integracion;

/**
 * Objeto de datos inmutable con un correo a enviar por el puerto {@code ServicioCorreo} (patrón Adapter):
 * destinatario, asunto y cuerpo en texto plano. Lo arman {@code NotificadorReservas} y {@code ServicioAccesos}
 * (enlace de recuperación de contraseña).
 *
 * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public class MensajeCorreo {

    /** Dirección de correo del destinatario. */
    private final String destinatario;

    /** Asunto. */
    private final String asunto;

    /** Cuerpo en texto plano. */
    private final String cuerpo;

    /**
     * Crea el mensaje.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param destinatario dirección de correo del destinatario
     * @param asunto asunto
     * @param cuerpo cuerpo en texto plano
     */
    public MensajeCorreo(String destinatario, String asunto, String cuerpo) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }

    /**
     * Devuelve {@code destinatario}.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return dirección de correo
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Devuelve {@code asunto}.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Devuelve {@code cuerpo}.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return cuerpo en texto plano
     */
    public String getCuerpo() {
        return cuerpo;
    }
}

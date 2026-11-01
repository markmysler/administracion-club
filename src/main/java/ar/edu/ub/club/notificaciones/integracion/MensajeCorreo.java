package ar.edu.ub.club.notificaciones.integracion;

/**
 * TEMPORAL (rama mod-accesos): firma acordada con el integrador; la implementación real es del módulo notificaciones.
 *
 * <p>Casos de uso: CU-13, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public class MensajeCorreo {

    /** Destinatario. */
    private final String destinatario;

    /** Asunto. */
    private final String asunto;

    /** Cuerpo. */
    private final String cuerpo;

    /**
     * Crea el mensaje.
     *
     * <p>Casos de uso: CU-13
     * @param destinatario correo del destinatario
     * @param asunto asunto
     * @param cuerpo cuerpo en texto plano
     */
    public MensajeCorreo(String destinatario, String asunto, String cuerpo) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }

    /**
     * Destinatario.
     *
     * <p>Casos de uso: CU-13
     * @return correo
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Asunto.
     *
     * <p>Casos de uso: CU-13
     * @return asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Cuerpo.
     *
     * <p>Casos de uso: CU-13
     * @return cuerpo
     */
    public String getCuerpo() {
        return cuerpo;
    }
}

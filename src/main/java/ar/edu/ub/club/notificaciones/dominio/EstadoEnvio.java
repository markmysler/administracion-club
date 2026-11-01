package ar.edu.ub.club.notificaciones.dominio;

/**
 * Estado del envío de una {@code Notificacion} por correo (RN-19). Se guarda por nombre en {@code
 * notificacion.estado_envio}.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
 */
public enum EstadoEnvio {

    /** Creada, todavía sin enviar. */
    PENDIENTE,
    /** El proveedor de correo aceptó el mensaje. */
    ENVIADA,
    /** El envío falló; la reserva se mantiene (RN-19). */
    FALLIDA;
}

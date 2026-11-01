package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import java.time.LocalDateTime;

/**
 * Notificación del detalle de una reserva (P-21, P-22): fecha, tipo, estado de envío y detalle del error (RN-19).
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-29, CU-31
 * @param fecha momento de creación
 * @param tipo CONFIRMACION, MODIFICACION o CANCELACION
 * @param estadoEnvio estado del envío (ENVIADA, FALLIDA o sin enviar)
 * @param detalle detalle del error o {@code null}
 */
public record FilaNotificacion(LocalDateTime fecha, String tipo, String estadoEnvio, String detalle) {

    /**
     * Construye la fila desde la notificación.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param notificacion notificación
     * @return la fila
     */
    public static FilaNotificacion desde(Notificacion notificacion) {
        return new FilaNotificacion(notificacion.getFecha(), notificacion.getTipo().name(),
                notificacion.getEstadoEnvio().name(), notificacion.getDetalleError());
    }
}

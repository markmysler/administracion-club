package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-31
 */
public record FilaNotificacion(LocalDateTime fecha, String tipo, String estadoEnvio, String detalle) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-31
     * @param notificacion valor de {@code notificacion}
     * @return el resultado
     */
    public static FilaNotificacion desde(Notificacion notificacion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

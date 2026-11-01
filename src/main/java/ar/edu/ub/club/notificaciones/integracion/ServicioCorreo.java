package ar.edu.ub.club.notificaciones.integracion;

/**
 * Puerto de envío de correo electrónico (interfaz objetivo del patrón Adapter, actor AC-04). Lo implementan {@code
 * CorreoSmtpAdapter} (proveedor SMTP real) y {@code CorreoSimuladoAdapter} (registra el mensaje en la bitácora);
 * el adaptador activo se elige con la propiedad {@code club.correo.modo} ({@code simulado} por omisión o {@code
 * smtp}). Lo usan {@code NotificadorReservas} y {@code ServicioAccesos}.
 *
 * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public interface ServicioCorreo {

    /**
     * Envía el mensaje; nunca lanza excepciones por fallas del proveedor: las informa en el resultado (RN-19).
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param m mensaje a enviar
     * @return resultado del envío
     */
    ResultadoEnvio enviar(MensajeCorreo m);
}

package ar.edu.ub.club.notificaciones.integracion;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Casos de uso: CU-13, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public interface ServicioCorreo {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-13, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param m valor de {@code m}
     * @return el resultado
     */
    ResultadoEnvio enviar(MensajeCorreo m);
}

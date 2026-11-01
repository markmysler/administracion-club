package ar.edu.ub.club.notificaciones.integracion;

/**
 * TEMPORAL (rama mod-accesos): firma acordada con el integrador; la implementación real es del módulo notificaciones.
 *
 * <p>Casos de uso: CU-13
 */
public class ResultadoEnvio {

    /**
     * Indica si el servidor aceptó el mensaje.
     *
     * <p>Casos de uso: CU-13
     * @return true si se envió
     */
    public boolean isEnviado() {
        return false;
    }

    /**
     * Detalle de la falla o del envío.
     *
     * <p>Casos de uso: CU-13
     * @return detalle
     */
    public String getDetalle() {
        return null;
    }
}

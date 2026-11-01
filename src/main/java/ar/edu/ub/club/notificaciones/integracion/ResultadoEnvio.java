package ar.edu.ub.club.notificaciones.integracion;

/**
 * Objeto de datos inmutable con el resultado de {@code ServicioCorreo.enviar}: si el proveedor aceptó el mensaje y,
 * si no, el detalle del error. Lo crean los adaptadores con las fábricas {@code exito()} y {@code fallo(detalle)}.
 *
 * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public class ResultadoEnvio {

    /** True si el proveedor aceptó el mensaje. */
    private final boolean enviado;

    /** Detalle del error ({@code null} si se envió). */
    private final String detalle;

    /**
     * Crea el resultado (las clases usan las fábricas).
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param enviado true si se envió
     * @param detalle detalle del error o {@code null}
     */
    private ResultadoEnvio(boolean enviado, String detalle) {
        this.enviado = enviado;
        this.detalle = detalle;
    }

    /**
     * Resultado de un envío aceptado.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return resultado con {@code enviado = true}
     */
    public static ResultadoEnvio exito() {
        return new ResultadoEnvio(true, null);
    }

    /**
     * Resultado de un envío fallido.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param detalle motivo de la falla
     * @return resultado con {@code enviado = false}
     */
    public static ResultadoEnvio fallo(String detalle) {
        return new ResultadoEnvio(false, detalle);
    }

    /**
     * Indica si el proveedor aceptó el mensaje.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return true si se envió
     */
    public boolean isEnviado() {
        return enviado;
    }

    /**
     * Devuelve el detalle del error.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @return detalle o {@code null} si se envió
     */
    public String getDetalle() {
        return detalle;
    }
}

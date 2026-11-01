package ar.edu.ub.club.pagos.web;

import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

/**
 * Clase anotada con {@code &#64;RestControllerAdvice} limitada a {@code WebhookPasarelaController}. Responde
 * 400, 401 o 500 sin cuerpo con detalles y registra la traza con un identificador de error (RNF-16); un error
 * transitorio responde 5xx para que la pasarela reintente.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ManejadorErroresWebhook {

    /** Registro técnico con identificador de error. */
    private Logger bitacora;

    /**
     * Firma ausente o inválida: 401.
     *
     * <p>Casos de uso: PENDIENTE
     * @param ex valor de {@code ex}
     * @return el resultado
     */
    public ResponseEntity<Void> manejarFirmaInvalida(AccesoDenegadoException ex) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Cuerpo o cabeceras mal formados: 400.
     *
     * <p>Casos de uso: PENDIENTE
     * @param ex valor de {@code ex}
     * @return el resultado
     */
    public ResponseEntity<Void> manejarDatosInvalidos(Exception ex) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Registra la traza con un identificador y responde 500.
     *
     * <p>Casos de uso: PENDIENTE
     * @param ex valor de {@code ex}
     * @return el resultado
     */
    public ResponseEntity<Void> manejarErrorInesperado(Exception ex) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}

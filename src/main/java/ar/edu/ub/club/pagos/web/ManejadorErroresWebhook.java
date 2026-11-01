package ar.edu.ub.club.pagos.web;

import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Clase anotada con {@code &#64;RestControllerAdvice} limitada a {@code WebhookPasarelaController}. Responde
 * 400, 401 o 500 sin cuerpo con detalles y registra la traza con un identificador de error (RNF-16); un error
 * transitorio responde 5xx para que la pasarela reintente.
 *
 * <p>Casos de uso: CU-35
 */
@RestControllerAdvice(assignableTypes = WebhookPasarelaController.class)
public class ManejadorErroresWebhook {

    /** Registro técnico con identificador de error. */
    private final Logger bitacora = LoggerFactory.getLogger(ManejadorErroresWebhook.class);

    /**
     * Firma ausente o inválida: 401.
     *
     * <p>Casos de uso: CU-35
     * @param ex excepción de acceso
     * @return 401 sin cuerpo
     */
    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Void> manejarFirmaInvalida(AccesoDenegadoException ex) {
        bitacora.warn("Notificación de la pasarela descartada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Cuerpo o cabeceras mal formados: 400.
     *
     * <p>Casos de uso: CU-35
     * @param ex excepción de lectura o validación
     * @return 400 sin cuerpo
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    public ResponseEntity<Void> manejarDatosInvalidos(Exception ex) {
        bitacora.warn("Notificación de la pasarela mal formada: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    /**
     * Registra la traza con un identificador y responde 500 (la pasarela reintenta).
     *
     * <p>Casos de uso: CU-35
     * @param ex excepción inesperada o transitoria
     * @return 500 sin cuerpo
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> manejarErrorInesperado(Exception ex) {
        String idError = UUID.randomUUID().toString();
        bitacora.error("Error al procesar la notificación de la pasarela {}", idError, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

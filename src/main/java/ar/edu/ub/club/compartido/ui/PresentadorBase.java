package ar.edu.ub.club.compartido.ui;

import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.excepciones.ConfirmacionRequeridaException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base abstracta de los presentadores (MVP, Vista Pasiva; GoF Template Method), sin tipos de Vaadin.
 * {@code ejecutar} y {@code consultar} son métodos plantilla finales: ejecutan la llamada a la fachada y traducen
 * cada excepción a una operación de la pantalla; las subclases redefinen los ganchos
 * {@code alRequerirConfirmacion} y {@code alConflicto}. Los presentadores concretos son beans de Spring de alcance
 * de UI ({@code @SpringComponent @UIScope}, una instancia por pestaña) que Spring inyecta en la vista.
 *
 * @param <P> interfaz de pantalla que maneja
 *
 * <p>Casos de uso: todos
 */
public abstract class PresentadorBase<P extends PantallaBase> {

    /** Pantalla vinculada (la vista que llamó a {@code vincular}). */
    protected P pantalla;

    /** Bitácora técnica de errores inesperados (RNF-16). */
    private final Logger bitacora = LoggerFactory.getLogger(getClass());

    /**
     * Vincula la pantalla; lo llama la vista en su constructor.
     *
     * <p>Casos de uso: todos
     * @param pantalla vista que implementa la interfaz de pantalla
     */
    public void vincular(P pantalla) {
        this.pantalla = pantalla;
    }

    /**
     * Ejecuta una acción sobre una fachada y traduce las excepciones (método plantilla).
     *
     * <p>Casos de uso: todos
     * @param accion llamada a la fachada
     * @return true si la acción terminó sin excepción
     */
    protected final boolean ejecutar(Runnable accion) {
        return consultar(() -> {
            accion.run();
            return Boolean.TRUE;
        }).isPresent();
    }

    /**
     * Ejecuta una consulta sobre una fachada y traduce las excepciones (método plantilla):
     * {@code ConfirmacionRequeridaException} → {@code alRequerirConfirmacion}; {@code ConcurrenciaException} →
     * {@code alConflicto}; {@code ErrorNegocio} con campo → {@code marcarCampo}, sin campo → {@code mostrarError};
     * {@code AccesoDenegadoException}, {@code RecursoNoEncontradoException} y {@code TransicionInvalidaException} →
     * mensajes fijos o el de la excepción; cualquier otra → se registra con un código y se muestra un mensaje
     * genérico con ese código (RNF-16).
     *
     * <p>Casos de uso: todos
     * @param consulta llamada a la fachada
     * @param <R> tipo del resultado
     * @return el valor o vacío si hubo una excepción
     */
    protected final <R> Optional<R> consultar(Supplier<R> consulta) {
        try {
            return Optional.ofNullable(consulta.get());
        } catch (ConfirmacionRequeridaException ex) {
            alRequerirConfirmacion(ex);
        } catch (ConcurrenciaException ex) {
            alConflicto(ex);
        } catch (ErrorNegocio ex) {
            if (ex.getCampo() != null) {
                pantalla.marcarCampo(ex.getCampo(), ex.getMensaje());
            } else {
                pantalla.mostrarError(ex.getMensaje());
            }
        } catch (AccesoDenegadoException ex) {
            pantalla.mostrarError("No tiene permiso para realizar esta operación.");
        } catch (RecursoNoEncontradoException ex) {
            pantalla.mostrarError("El elemento ya no existe.");
        } catch (TransicionInvalidaException ex) {
            pantalla.mostrarError(ex.getMessage());
        } catch (RuntimeException ex) {
            String idError = UUID.randomUUID().toString();
            bitacora.error("Error inesperado {}", idError, ex);
            pantalla.mostrarError("Ocurrió un error inesperado. Código: " + idError);
        }
        return Optional.empty();
    }

    /**
     * Gancho: la operación necesita confirmación. Por omisión muestra el mensaje como error.
     *
     * <p>Casos de uso: CU-22, CU-24
     * @param ex excepción con las reservas afectadas
     */
    protected void alRequerirConfirmacion(ConfirmacionRequeridaException ex) {
        pantalla.mostrarError(ex.getMessage());
    }

    /**
     * Gancho: conflicto del control optimista. Por omisión muestra «Los datos cambiaron mientras los editaba. Se
     * vuelven a cargar.».
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33
     * @param ex excepción de concurrencia
     */
    protected void alConflicto(ConcurrenciaException ex) {
        pantalla.mostrarError("Los datos cambiaron mientras los editaba. Se vuelven a cargar.");
    }
}

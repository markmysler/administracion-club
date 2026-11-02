package ar.edu.ub.club.compartido.seguridad;

/**
 * Interfaz de {@code compartido.seguridad} con la que {@code VistaProtegida}, {@code VistaRaiz} y {@code
 * MenuNavegacion} conocen la cuenta de la sesión sin depender del módulo accesos. La implementa {@code
 * UsuarioActualSesion} (accesos.aplicacion).
 *
 * <p>Casos de uso: todos
 */
public interface SesionActual {

    /**
     * Indica si hay una sesión autenticada.
     *
     * <p>Casos de uso: todos
     * @return true si hay sesión
     */
    boolean autenticada();

    /**
     * Devuelve la cuenta de la sesión; lanza {@code AccesoDenegadoException} si no hay sesión.
     *
     * <p>Casos de uso: todos
     * @return cuenta autenticada
     */
    Autorizable cuenta();
}
